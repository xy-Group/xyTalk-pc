package xysoft.im.service;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.packet.Message.Type;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jivesoftware.smack.chat2.*;
//import org.jivesoftware.smack.packet.Message;
import xysoft.im.db.model.Message;
import xysoft.im.db.model.Room;
import xysoft.im.extension.OfflineFile;
import xysoft.im.extension.Receipt;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.RoomsPanel;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.JID;
import xysoft.im.app.Launcher;

public class ChatService {

	public static void messageProcess(Stanza stanza) {
		// 消息包
		org.jivesoftware.smack.packet.Message message = (org.jivesoftware.smack.packet.Message) stanza;
		if (message.getType() == org.jivesoftware.smack.packet.Message.Type.chat) {// 单聊
			if (message.getBody() == null) {
				DebugUtil.debug("(抛弃的)processPacket-Message.Type.chat:" + message.toXML());
			} else {
				boolean mucInvition = message.getExtension("x", "xytalk:muc:invitation") != null;
				boolean offlineFile = message.getExtension("x", OfflineFile.NAMESPACE) != null;
				boolean processed = false;
				if (mucInvition) {// 群组邀请消息-离线,加入群
					MucChatService.join(message);
					DebugUtil.debug("(离线群邀请)processPacket-Message.Type.chat:" + message.toXML());
					processed = true;
				}

				if (offlineFile) {// 离线文件消息抵达，需要向离线机器人发送请求
					XmppFileService.requestOfflineFile(message);
					DebugUtil.debug("(离线文件)processPacket-Message.Type.chat:" + message.toXML());
					processed = true;
				}

				if (!processed){
					ChatService.recivePacket(message);
					DebugUtil.debug("(单聊)chat:" + message.toString());
				}

			}

		}
		if (message.getType() == org.jivesoftware.smack.packet.Message.Type.headline) {// 重要消息
			HeadlineChatService.recivePacket(message);
			DebugUtil.debug("processPacket-Message.Type.headline:" + message.toXML());
		}
		if (message.getType() == org.jivesoftware.smack.packet.Message.Type.normal) {
			// 可能是普通消息，也可能是回执消息，也可能是扩展消息
			if (message.hasExtension("urn:xmpp:receipts")) {
				// 回执消息
				ChatService.receiptArrived(message);
				DebugUtil.debug("urn:xmpp:receipts:" + message.toXML());
			} else if (message.hasExtension("urn:xmpp:attention:0")) {
				// 震动提醒消息
				DebugUtil.debug("urn:xmpp:attention:0:" + message.toXML());
			} else if (message.hasExtension("http://jabber.org/protocol/muc#user")) {
				// 被邀请加入群聊
				DebugUtil.debug("被邀请加入群聊:" + message.getFrom().toString());
			} else if (message.hasExtension("jabber:x:conference")) {
				// 被邀请加入群聊
				DebugUtil.debug("被邀请加入群聊:" + message.getFrom().toString());
			} else {
				if (message.getBody() == null) {
					DebugUtil.debug("(抛弃的)processPacket-Message.Type.normal:" + message.toXML());
				} else {
					ChatService.recivePacket(message);
					DebugUtil.debug("processPacket-Message.Type.normal:" + message.toXML());
				}
			}
		}
		if (message.getType() == org.jivesoftware.smack.packet.Message.Type.groupchat) {// 表示群聊
			DebugUtil.debug("processPacket-Message.Type.groupchat:" + message.toXML());
			MucChatService.recivePacket(message);
		}
		if (message.getType() == org.jivesoftware.smack.packet.Message.Type.error) {// 表示错误信息
			DebugUtil.debug("processPacket-Message.Type.error:" + message.toXML());
			ErrorMsgService.recivePacket(message);
		}

	}

	public static void recivePacket(org.jivesoftware.smack.packet.Message message) {
		// 消息抵达后，客户端处在4种状况
		// 1、当前聊天者即为新消息发送者
		// 2、当前聊天者不为新消息发送者，联系人列表中存在消息发送者
		// 3、当前聊天者不为新消息发送者，联系人列表中不存在新消息发送者
		// 4、用户离线，但上线后同样在1-3之间处理

		Jid fromJID = message.getFrom();
		String body = message.getBody();
		String barejid = fromJID.asBareJid().toString();

		if (barejid != null && body != null) {
			if (Launcher.currRoomId.equals(barejid)) {
				// 1、当前聊天者即为新消息发送者
				updateChatPanel(message);
			} else {
				if (Launcher.roomService.exist(barejid)) {
					// 2、联系人列表中存在消息发送者,更新未读信息
					updateRoom(message);

				} else {
					// 3、联系人中不存在新消息发送者,则新建一个联系人
					createNewRoom(message);
				}
				dbMessagePersistence(message);
			}
		}
	}

	private static void updateChatPanel(org.jivesoftware.smack.packet.Message message) {
		// TODO 更新当前聊天窗
		DebugUtil.debug("新消息改变当前ChatPanel");
		ChatPanel.getContext().reciveChatMessage(message);
	}

	private static void updateRoom(org.jivesoftware.smack.packet.Message message) {
		Room room = Launcher.roomService.findById(message.getFrom().asBareJid().toString());
		room.setLastMessage(message.getBody());
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(room.getMsgSum() + 1);
		room.setUnreadCount(room.getUnreadCount() + 1);
		Launcher.roomService.insertOrUpdate(room);

		updateLeftItemUI(message.getFrom().asBareJid().toString());
	}

	// 联系人中不存在新消息发送者,则新建一个联系人
	private static void createNewRoom(org.jivesoftware.smack.packet.Message message) {

		String barejid = message.getFrom().asBareJid().toString();
		String fromUsername = JID.usernameByJid(barejid);

		Room room = new Room();
		room.setLastMessage(message.getBody());
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(1);
		room.setName(Launcher.contactsUserService.findByUsername(fromUsername).getName());
		room.setRoomId(message.getFrom().asBareJid().toString());
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("s");
		room.setUnreadCount(1);
		Launcher.roomService.insertOrUpdate(room);

		updateLeftAllUI();
	}
	
	//通过通讯录或搜索发起单聊
	public static void createNewRoom(String barejid) {

		String fromUsername = JID.usernameByJid(barejid);

		Room room = new Room();
		room.setLastMessage("发起会话");
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(1);
		room.setName(Launcher.contactsUserService.findByUsername(fromUsername).getName());
		room.setRoomId(barejid);
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("s");
		room.setUnreadCount(0);
		Launcher.roomService.insertOrUpdate(room);

		updateLeftAllUI();
	}

	private static void updateLeftAllUI() {

		DebugUtil.debug("新消息导致联系人UI-All改变");
		RoomsPanel.getContext().notifyDataSetChanged(false);
	}

	private static void updateLeftItemUI(String bareJid) {

		DebugUtil.debug("新消息导致联系人UI-Item改变");
		RoomsPanel.getContext().updateRoomItem(bareJid);
	}

	private static void dbMessagePersistence(org.jivesoftware.smack.packet.Message message) {

		Jid fromJID = message.getFrom();
		String body = message.getBody();
		String id = message.getStanzaId(); // 使用xmpp message消息ID
		String barejid = fromJID.asBareJid().toString();
		String fromUsername = JID.usernameByJid(barejid);

		Message dbMessage = null;

		if (id == null) {
			DebugUtil.debug("新消息无编号");
		} else {
			if (body == null || body.equals("")) {
				return;
			}

			dbMessage = new Message();
			dbMessage.setId(id);
			dbMessage.setMessageContent(body);
			dbMessage.setRoomId(barejid);
			dbMessage.setSenderId(barejid);
			dbMessage.setSenderUsername(fromUsername);
			dbMessage.setTimestamp(System.currentTimeMillis());
			dbMessage.setNeedToResend(false);

			Launcher.messageService.insert(dbMessage);
		}
	}

	public static void receiptArrived(org.jivesoftware.smack.packet.Message message) {
		// TODO 回执消息抵达时处理
		DebugUtil.debug("收到回执:" + message.getStanzaId() + "--" + message.getFrom());
	}

	public static void sendMessage(String roomId, String content) {
		EntityBareJid jid = null;
		try {
			jid = JidCreate.entityBareFrom(roomId);
		} catch (XmppStringprepException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Chat chat = ChatManager.getInstanceFor(Launcher.connection).chatWith(jid);

		org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
		message.setType(Type.chat);
		message.addExtension(new Receipt());
		message.setBody(content);
		try {
			chat.send(message);
			DebugUtil.debug("chat.sendMessage:：" + message.toString());
		} catch (NotConnectedException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void chatstatesArrived(org.jivesoftware.smack.packet.Message message) {
		// TODO Auto-generated method stub
		DebugUtil.debug("收到消息状态:" + message.getStanzaId() + "--" + message.getFrom());
	}

}
