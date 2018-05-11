package xysoft.im.service;

import java.util.List;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message.Type;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jivesoftware.smack.chat2.*;
//import org.jivesoftware.smack.packet.Message;
import xysoft.im.db.model.Message;
import xysoft.im.db.model.Room;
import xysoft.im.entity.MessageItem;
import xysoft.im.extension.Receipt;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.RoomsPanel;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.JID;
import xysoft.im.app.Launcher;

public class ChatService {

	static String from;
	static Jid fromJID;
	static String datetime;
	static String subject;
	static String body;
	static String id;
	static String threadId;
	static String barejid;
	static String fromUsername;
	static List<ExtensionElement> extensions;
	static ExtensionElement currExtension;
	static String currElementName;
	static String currNamespace;

	public static void recivePacket(org.jivesoftware.smack.packet.Message message) {
		// 消息抵达后，客户端处在4种状况
		//	1、当前聊天者即为新消息发送者
		//	2、当前聊天者不为新消息发送者，联系人列表中存在消息发送者
		//	3、当前聊天者不为新消息发送者，联系人列表中不存在新消息发送者
		//	4、用户离线，但上线后同样在1-3之间处理
		
		fromJID = message.getFrom();
		body = message.getBody();
		subject = message.getSubject();
		id = message.getStanzaId();
		threadId = message.getThread();

		// 通过barejid判断是否需要新建联系人列表项
		from = fromJID.asBareJid().toString();
		barejid = JID.bare(fromJID.asBareJid().toString());
		fromUsername = JID.username(from);

		if (barejid != null && body!=null) {
			if (Launcher.currRoomId.equals(barejid)) {
				// 当前聊天者即为新消息发送者
				updateChatPanel(message);
			} else {
				if (Launcher.roomService.exist(barejid)) {
					// 联系人列表中存在消息发送者,更新未读信息，则修改
					updateRoom(message);
					
				} else {
					// 联系人中不存在新消息发送者,则新建一个联系人
					createNewRoom(message);
				}
				dbMessagePersistence();
			}
		}
	}

	private static void updateChatPanel(org.jivesoftware.smack.packet.Message message) {
		// TODO 更新当前聊天窗
		DebugUtil.debug("新消息改变当前ChatPanel");
		ChatPanel.getContext().reciveChatMessage(message);
	}

	private static void updateRoom(org.jivesoftware.smack.packet.Message message) {
		Room room = Launcher.roomService.findById(barejid);
		room.setLastMessage(message.getBody());
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(room.getMsgSum() + 1);
		room.setUnreadCount(room.getUnreadCount() + 1);
		Launcher.roomService.insertOrUpdate(room);
		
		updateLeftItemUI();
	}

	// 联系人中不存在新消息发送者,则新建一个联系人
	private static void createNewRoom(org.jivesoftware.smack.packet.Message message) {
		Room room = new Room();
		room.setLastMessage(message.getBody());
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(1);
		room.setName(Launcher.userService.getName(fromUsername));
		room.setRoomId(barejid);
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("d");
		room.setUnreadCount(1);
		Launcher.roomService.insertOrUpdate(room);
		
		updateLeftAllUI();
	}

	private static void updateLeftAllUI() {

		DebugUtil.debug("新消息导致联系人UI-All改变");
		RoomsPanel.getContext().notifyDataSetChanged(false);		
	}

	private static void updateLeftItemUI() {

		DebugUtil.debug("新消息导致联系人UI-Item改变");
		RoomsPanel.getContext().updateRoomItem(barejid);		
	}

	private static void dbMessagePersistence() {
		// 消息持久化数据操作
		Message dbMessage = null;

        if (id == null)
        {
        	DebugUtil.debug("新消息无编号");
        }
        else
        {
            if (body == null || body.equals(""))
            {
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
		DebugUtil.debug("收到回执:"+message.getStanzaId()+"--"+ message.getFrom());
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

//        ChatManager chatManager = ChatManager.getInstanceFor(Launcher.connection);       
//        chatManager.addChatListener(new ChatManagerListener() {               
//        	
//            @Override
//            public void chatCreated(Chat cc, boolean bb) {
//                // 当收到来自对方的消息时触发监听函数
//                cc.addMessageListener(new ChatMessageListener() {
//                	//消息状态监听，如开始输入\正在输入\暂停
//                	//inactive\composing\paused
//					@Override
//					public void processMessage(Chat chat, org.jivesoftware.smack.packet.Message message) {
//						// TODO Auto-generated method stub						
//						DebugUtil.debug( roomId + "processMessage:："+ message.getThread());					
//					}
//                });
//            }
//        });
    
        org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
        message.setType(Type.chat);
        message.addExtension(new Receipt());
        message.setBody(content);
        try {
			try {
				chat.send(message);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DebugUtil.debug( "chat.sendMessage:："+ message.toString());		
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void chatstatesArrived(org.jivesoftware.smack.packet.Message message) {
		// TODO Auto-generated method stub
		DebugUtil.debug( "收到消息状态:"+ message.getStanzaId()+"--"+ message.getFrom());		
	}

}
