package xysoft.im.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.SwingWorker;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException.NotAMucServiceException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCUser.Invite;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import xysoft.im.app.Launcher;
import xysoft.im.cache.UserCache;
import xysoft.im.db.model.Room;
import xysoft.im.entity.MucRoomInfo;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.RoomsPanel;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.JID;

public class MucChatService {
	static ExecutorService executor = Executors.newFixedThreadPool(5);
	public static DiscoverInfo getRoomInfo(EntityBareJid room)
			throws NoResponseException, XMPPErrorException, NotConnectedException, InterruptedException {
		DiscoverInfo info = ServiceDiscoveryManager.getInstanceFor(Launcher.connection).discoverInfo(room);
		return info;
	}

	public static void recivePacket(org.jivesoftware.smack.packet.Message message) {
		// 消息抵达后，客户端处在4种状况
		// 1、当前聊天者即为新消息发送者
		// 2、当前聊天者不为新消息发送者，联系人列表中存在消息发送者
		// 3、当前聊天者不为新消息发送者，联系人列表中不存在新消息发送者
		// 4、用户离线，但上线后同样在1-3之间处理
		String fromFullJid;
		Jid fromJID;
		String datetime;
		String subject;
		String body;
		String id;
		String threadId;
		String barejid;
		String fromUsername;

		fromJID = message.getFrom();
		body = message.getBody();
		subject = message.getSubject();
		id = message.getStanzaId();
		threadId = message.getThread();

		// 通过barejid判断是否需要新建联系人列表项
		fromFullJid = fromJID.asFullJidIfPossible().toString();
		barejid = fromJID.asBareJid().toString();
		fromUsername = JID.usernameByMuc(fromFullJid);
		DebugUtil.debug("Launcher.currRoomId:" + Launcher.currRoomId);
		DebugUtil.debug("barejid:" + barejid);
		if (barejid != null && body != null) {
			if (Launcher.currRoomId.equals(barejid)) {// 当前聊天者即为新消息发送者
				// 如果有同样消息在数据库，不予处理。（MUC可能会重复推送已读消息，因为tigase离线消息持久化后不销毁，需要从服务器端解决）
				if (Launcher.messageService.findById(id) != null)
					return;

				if (fromUsername.equals(UserCache.CurrentUserName))// 如果发送者是自己
					return;

				updateChatPanel(message);
			} else {
				if (Launcher.roomService.exist(barejid)) {
					// 如果有同样消息在数据库，不予处理。（MUC可能会重复推送已读消息，因为tigase离线消息持久化后不销毁，需要从服务器端解决）
					if (Launcher.messageService.findById(id) != null)
						return;
					// 联系人列表中存在消息发送者,更新未读信息，则修改
					updateRoom(message);

				} else {
					// 联系人中不存在新消息发送者,则新建一个联系人
					createNewRoom(message);
				}
				dbMessagePersistence(message);
			}
		}
	}

	private static void dbMessagePersistence(Message message) {
		xysoft.im.db.model.Message dbMessage = null;
		String from;
		Jid fromJID;
		String datetime;
		String subject;
		String body;
		String id;
		String threadId;
		String barejid;
		String fromUsername;

		fromJID = message.getFrom();
		body = message.getBody();
		subject = message.getSubject();
		id = message.getStanzaId();
		threadId = message.getThread();
		from = fromJID.asEntityFullJidIfPossible().toString();
		barejid = fromJID.asEntityBareJidIfPossible().toString();
		fromUsername = JID.usernameByMuc(from);

		if (id == null) {
			DebugUtil.debug("新消息无编号");
		} else {
			if (body == null || body.equals("")) {
				return;
			}

			dbMessage = new xysoft.im.db.model.Message();
			dbMessage.setId(id);
			dbMessage.setMessageContent(body);
			dbMessage.setRoomId(barejid);// 注意：roomID是barejid
			dbMessage.setSenderId(from);// SenderId是fulljid
			dbMessage.setSenderUsername(fromUsername);
			dbMessage.setTimestamp(System.currentTimeMillis());
			dbMessage.setNeedToResend(false);

			Launcher.messageService.insert(dbMessage);
		}
	}

	private static void createNewRoom(Message message) {
		Room room = new Room();
		room.setLastMessage("加入新群组");
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(0);
		room.setName(mucGetInfo(message.getFrom().toString()).getName());
		room.setRoomId(message.getFrom().asEntityBareJidIfPossible().toString()); // 注意邀请消息和muc消息的from不同
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("c");
		room.setUnreadCount(0);
		Launcher.roomService.insertOrUpdate(room);

		updateLeftAllUI();
	}

	private static void updateLeftAllUI() {
		DebugUtil.debug("新群组邀请导致联系人UI-All改变");
		RoomsPanel.getContext().notifyDataSetChanged(false);
	}

	private static void updateRoom(Message message) {
		DebugUtil.debug("muc-updateRoom:" + message.getFrom().toString() + "--" + message.getBody());
		Room room = Launcher.roomService.findById(message.getFrom().asEntityBareJidIfPossible().toString());
		room.setLastMessage(message.getBody());
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(room.getMsgSum() + 1);
		room.setUnreadCount(room.getUnreadCount() + 1);
		Launcher.roomService.insertOrUpdate(room);

		updateLeftItemUI(message);
	}

	private static void updateLeftItemUI(Message message) {
		DebugUtil.debug("新消息导致联系人UI-Item改变updateLeftItemUI");
		RoomsPanel.getContext().updateRoomItem(message.getFrom().asEntityBareJidIfPossible().toString());
	}

	private static void updateLeftItemUICurrentRoom(Message message) {
		DebugUtil.debug("新消息导致联系人UI-Item改变updateLeftItemUI");
		RoomsPanel.getContext().updateRoomItemAddUnread(message.getFrom().asEntityBareJidIfPossible().toString(),
				"新消息");
	}

	private static void updateChatPanel(Message message) {
		DebugUtil.debug("新消息改变当前updateChatPanel");
		ChatPanel.getContext().reciveChatMessage(message);
		updateLeftItemUICurrentRoom(message);
	}

	public static MucRoomInfo mucGetInfo(String jid) {
		// MultiUserChatManager manager =
		// MultiUserChatManager.getInstanceFor(Launcher.connection);
		// RoomInfo info;
		try {
			// info = manager.getRoomInfo(JidCreate.entityBareFrom(jid));
			MucRoomInfo info = new MucRoomInfo(getRoomInfo(JidCreate.entityBareFrom(jid)));
			DebugUtil.debug("Room jid:" + info.getRoom().toString());
			DebugUtil.debug("Room of occupants:" + info.getOccupantsCount());
			DebugUtil.debug("Room getName:" + info.getName());
			DebugUtil.debug("Room getOwnerJid:" + info.getOwnerJid());
			DebugUtil.debug("Room getAdminJid:" + info.getAdminJid());

			return info;

		} catch (NoResponseException | XMPPErrorException | NotConnectedException | XmppStringprepException
				| InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void mucInvitation() {
		MultiUserChatManager.getInstanceFor(Launcher.connection).addInvitationListener(new InvitationListener() {
			@Override
			public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason,
					String password, Message message, Invite invitation) {
				String barejid = message.getFrom().toString();
				DebugUtil.debug("邀请加入群聊:" + barejid + reason);
				try {

					room.join(Resourcepart.from(UserCache.CurrentUserName + "-" + UserCache.CurrentUserRealName));

					if (barejid != null) {
						if (Launcher.currRoomId.equals(barejid)) {
							// 当前聊天者即为已有群
							// updateChatPanel(message);
						} else {
							if (Launcher.roomService.exist(barejid)) {
								// 联系人列表中存在群组,更新未读信息，则修改
								// updateRoom(message);
							} else {
								// 联系人中不存在群组,则新建一个群
								createNewRoom(message);
							}
							// dbMessagePersistence(message);
						}
					}

				} catch (NotAMucServiceException | NoResponseException | XMPPErrorException | NotConnectedException
						| XmppStringprepException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	public static void sendMessage(String roomId, String content) {
		long time1 = System.currentTimeMillis();
		EntityBareJid jid = null;
		try {
			jid = JidCreate.entityBareFrom(roomId);
		} catch (XmppStringprepException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// TODO 如果没有进群，则进入
		MultiUserChat room = MultiUserChatManager.getInstanceFor(Launcher.connection).getMultiUserChat(jid);
		if (!room.isJoined())
			try {
				room.join(Resourcepart.from(UserCache.CurrentUserName + "-" + UserCache.CurrentUserRealName));
			} catch (NotAMucServiceException | NoResponseException | XMPPErrorException | NotConnectedException
					| XmppStringprepException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
		}
		
		org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
		message.setType(Type.groupchat);
		message.setBody(content);
		try {
			try {
				room.sendMessage(message);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		// TODO 群消息发送之后，需要邀请群里的其他人进群，否则其他人不能即时收到消息
//		// TODO 未来必须取消这一步，否则太耗时，应在适当时候异步让用户加入所有会议室
//		// 获取群中其他人,其他人均为admin的原因是便于发现
//		MucRoomInfo info;
//		try {
//			info = new MucRoomInfo(getRoomInfo(JidCreate.entityBareFrom(jid)));
//			List<String> ownerList = info.getOwnerJid();
//			List<String> adminList = info.getAdminJid();
//
//			if (adminList!=null && !adminList.isEmpty())
//			for (String item : adminList) {
//				if (!item.equals(UserCache.CurrentBareJid)) {
//					room.invite(JidCreate.entityBareFrom(item), "邀请您进群");
//					DebugUtil.debug("邀请其他人进群:" + JidCreate.entityBareFrom(item));
//				}
//			}
//			
//			if (ownerList!=null && !ownerList.isEmpty())
//			for (String item : ownerList) {
//				if (!item.equals(UserCache.CurrentBareJid)) {
//					room.invite(JidCreate.entityBareFrom(item), "邀请您进群");
//					DebugUtil.debug("邀请其他人进群:" + JidCreate.entityBareFrom(item));
//				}
//			}
//
//		} catch (NoResponseException | XMPPErrorException | NotConnectedException | XmppStringprepException
//				| InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

		// TODO 服务器需要对邀请进群的消息进行持久化，以便待邀请的用户上线后加入群组，然后再通过群离线消息，获取未读消息
		DebugUtil.debug("发送群消息："+(System.currentTimeMillis()-time1)+"毫秒"+message.toString());
	}

}
