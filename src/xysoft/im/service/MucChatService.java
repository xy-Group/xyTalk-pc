package xysoft.im.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.disco.ServiceDiscoveryManager;
import org.jivesoftware.smackx.disco.packet.DiscoverInfo;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MucEnterConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChat.MucCreateConfigFormHandle;
import org.jivesoftware.smackx.muc.MultiUserChatException.NotAMucServiceException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCUser.Invite;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
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
import xysoft.im.extension.MucInvitation;
import xysoft.im.extension.Receipt;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.RoomsPanel;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.JID;

public class MucChatService {
	
	public static Map<String,Room> existRooms =new HashMap<String,Room>();

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
		DebugUtil.debug("muc fromFullJid:"+fromFullJid);
		DebugUtil.debug("muc barejid:"+barejid);
		DebugUtil.debug("muc fromUsername:"+fromUsername);
		DebugUtil.debug("muc body:"+body);
		if (fromJID != null && body != null) {
			if (Launcher.currRoomId.equals(barejid)) {// 当前聊天者即为新消息发送者
				// 如果有同样消息在数据库，不予处理。（MUC可能会重复推送已读消息，因为tigase离线消息持久化后不销毁，需要从服务器端解决）
				if (Launcher.messageService.findById(id) != null) {
					return;
				}

				if (fromUsername.equals(UserCache.CurrentUserName))// 如果发送者是自己
				{
					DebugUtil.debug("muc消息发送者是自己");
					return;
				}

				updateChatPanel(message);
			} else {
				if (existRooms.containsKey(barejid) || Launcher.roomService.findById(barejid)!=null) {// 联系人列表中存在消息发送者,更新未读信息，则修改左侧联系人控件
					if (!existRooms.containsKey(barejid)){
						DebugUtil.debug("muc room 加入缓存："+barejid);
						existRooms.put(barejid,Launcher.roomService.findById(barejid));//为离线消息RoomID加入缓存，预防离线消息洪水，造成数据库查询异常
					}
					DebugUtil.debug("muc消息发送者是"+barejid);
					// 如果有同样消息在数据库，不予处理。（MUC可能会重复推送已读消息，因为tigase离线消息持久化后不销毁，需要从服务器端解决）
					if (Launcher.messageService.findById(id) != null)
						return;

					if (fromUsername.equals(UserCache.CurrentUserName))// 如果发送者是自己
						return;

					updateRoom(message);

				} else {
					// 联系人中不存在新消息发送者,则新建一个联系人
					DebugUtil.debug("muc 新消息新建联系人"+barejid+message.getBody()+message.getFrom().asEntityBareJidIfPossible());
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
		room.setLastMessage("被加入新群组");
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(0);
		room.setName(mucGetInfo(message.getFrom().toString()).getName());
		room.setRoomId(message.getFrom().asEntityBareJidIfPossible().toString()); // 注意邀请消息和muc消息的from不同
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("m");
		room.setUnreadCount(0);
		room.setCreatorName(JID.usernameByJid(message.getFrom().asEntityBareJidIfPossible().toString()));
		room.setCreatorId(JID.usernameByJid(message.getFrom().asEntityBareJidIfPossible().toString()));

		Launcher.roomService.insertOrUpdate(room);

		updateLeftAllUI();
	}

	private static void createNewRoomByMe(String jid, String roomName) {
		Room room = new Room();
		room.setLastMessage("我创建新群组");
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(0);
		room.setName(roomName);
		room.setRoomId(jid);
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("m");
		room.setUnreadCount(0);
		room.setCreatorName(UserCache.CurrentUserName);
		room.setCreatorId(UserCache.CurrentUserName);

		Launcher.roomService.insertOrUpdate(room);

		updateLeftAllUI();
	}

	// 连带将成员加入db
	private static void createNewRoomByMe(String jid, String roomName, List<String> users) {
		Room room = new Room();
		room.setLastMessage("我创建的新群组");
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(0);
		room.setName(roomName);
		room.setRoomId(jid);
		room.setTotalReadCount(0);
		room.setUpdatedAt("2018-01-01T06:38:55.119Z");
		room.setType("m");
		room.setUnreadCount(0);
		room.setCreatorName(UserCache.CurrentUserName);
		room.setCreatorId(UserCache.CurrentUserName);

		String member = "";
		if (users.size() > 0) {
			for (int i = 0; i < users.size(); i++) {
				if (i == users.size() - 1) {
					member = member + JID.usernameByJid(users.get(i));
				} else {
					member = member + JID.usernameByJid(users.get(i)) + ",";
				}

			}
			room.setMember(member);
		}

		Launcher.roomService.insertOrUpdate(room);

		updateLeftAllUI();
	}

	private static void updateLeftAllUI() {
		DebugUtil.debug("新群组邀请导致联系人UI-All改变");
		RoomsPanel.getContext().notifyDataSetChanged(false);
	}

	private static void updateRoom(Message message) {
		if (message.getBody()==null || message.getBody().isEmpty())
			return;
	
		DebugUtil.debug("muc-updateRoom:" + message.getFrom().toString() + "--" + message.getBody());
				
		Room room = existRooms.get(message.getFrom().asEntityBareJidIfPossible());
		if (room == null){
			room = Launcher.roomService.findById(message.getFrom().asEntityBareJidIfPossible().toString());
		}
		else{
			DebugUtil.debug("Muc room 被缓存命中"+message.getFrom().asEntityBareJidIfPossible().toString());
		}
		room.setLastMessage(message.getBody());
		room.setLastChatAt(System.currentTimeMillis());
		room.setMsgSum(room.getMsgSum() + 1);
		room.setUnreadCount(room.getUnreadCount() + 1);
		Launcher.roomService.update(room);

		updateLeftItemUI(message);
	}

	private static void updateLeftItemUI(Message message) {
		DebugUtil.debug("新消息导致联系人UI-Item改变updateLeftItemUI："+ message.getFrom().asEntityBareJidIfPossible());
		if (RoomsPanel.getContext() == null){
			DebugUtil.debug("LeftItemUI仍未渲染完毕");
			return;
		}
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
				DebugUtil.debug("被邀请加入群聊:" + barejid + reason);
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

		// TODO 服务器需要对邀请进群的消息进行持久化，以便待邀请的用户上线后加入群组，然后再通过群离线消息，获取未读消息
		DebugUtil.debug("发送群消息：" + (System.currentTimeMillis() - time1) + "毫秒" + message.toString());
	}

	public static MultiUserChat createChatRoom(String groupName, List<String> users, String nickName) throws Exception {

		if (groupName == null || groupName.isEmpty())
			return null;

		long nowJid = System.currentTimeMillis();// 把时间戳作为jid
		String jid = nowJid + Launcher.MUCSERVICE + Launcher.DOMAIN;
		EntityBareJid groupJid = JidCreate.entityBareFrom(jid);

		MultiUserChat muc = MultiUserChatManager.getInstanceFor(Launcher.connection).getMultiUserChat(groupJid);
		muc.create(Resourcepart.from(nickName));

		// 获得聊天室的配置表单
		Form form = muc.getConfigurationForm();
		// 根据原始表单创建一个要提交的新表单。
		Form submitForm = form.createAnswerForm();

		// 因为muc成员（members）是不被持久化的，所以创建群组，需要把其他受邀的人设置为admin
		List<String> admins = new ArrayList<>();
		if (users != null && !users.isEmpty()) {
			for (int i = 0; i < users.size(); i++) {
				String userJid = users.get(i);
				admins.add(userJid);
			}
		}

		// 设置提交表单默认值
		for (FormField field : form.getFields()) {
			if (!FormField.Type.hidden.equals(field.getType()) && field.getVariable() != null) {
				submitForm.setDefaultAnswer(field.getVariable());
			}
		}

		submitForm.setAnswer("muc#roomconfig_roomname", groupName);
		submitForm.setAnswer("muc#roomconfig_roomdesc", groupName);
		submitForm.setAnswer("muc#roomconfig_persistentroom", true);
		submitForm.setAnswer("muc#roomconfig_publicroom", true);
		//submitForm.setAnswer("muc#roomconfig_moderatedroom", true);//使用默认的false即可
		submitForm.setAnswer("muc#roomconfig_allowinvites", true);
		// 设置用户成员
		submitForm.setAnswer("muc#roomconfig_roomadmins", admins);

		// 发送已完成的表单（包含默认值）到服务器来配置聊天室
		muc.sendConfigurationForm(submitForm);
		// 更新左侧panel，将群组UI新建出来
		createNewRoomByMe(jid, groupName, users);
		// 对其他人在线邀请,这一部分是对xmpp的群邀请做兼容
		for (int i = 0; i < users.size(); i++) {
			String userJid = users.get(i);
			muc.invite(JidCreate.entityBareFrom(userJid), "邀请您进入群。");
		}

		// TODO 对离线用户还应发送邀请消息，并让服务器存储离线消息，待用户上线后还应对此类消息进行处理
		sendOfflineInvitationMessage(users, jid, groupName);

		return muc;
	}
	
	public static void sendInvitationMessage(List<Jid> users, String roomid, String roomName){
		MucInvitation mi = new MucInvitation(roomid, roomName);

		for (int i = 0; i < users.size(); i++) {
			Jid userJid = users.get(i);
			Chat chat = ChatManager.getInstanceFor(Launcher.connection).chatWith(userJid.asEntityBareJidIfPossible());
			org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
			message.setType(Type.chat);
			message.addExtension(mi);
			message.setBody("请加入会议");
			try {
				chat.send(message);
				DebugUtil.debug("sendOfflineInvitationMessage:" + message.toXML());
			} catch (NotConnectedException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	private static void sendOfflineInvitationMessage(List<String> users, String roomid, String roomName)
			throws XmppStringprepException {
		// TODO 对离线用户应发送邀请消息，并让服务器存储离线消息，待用户上线后还应对此类消息进行处理
		MucInvitation mi = new MucInvitation(roomid, roomName);

		for (int i = 0; i < users.size(); i++) {
			String userJid = users.get(i);
			Chat chat = ChatManager.getInstanceFor(Launcher.connection).chatWith(JidCreate.entityBareFrom(userJid));
			org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
			message.setType(Type.chat);
			message.addExtension(mi);
			message.setBody("请加入会议");
			try {
				chat.send(message);
				DebugUtil.debug("sendOfflineInvitationMessage:" + message.toXML());
			} catch (NotConnectedException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static int getHistoryOffsize(long lastChatAt) {
		// TODO 时间戳转化为秒的请求，用于精确获取未读离线消息
		return (int) (System.currentTimeMillis() - lastChatAt) / 1000;
	}

	public static void join(Message message) {
		// TODO 收到加入群的离线消息，进入群
		MucInvitation mi = message.getExtension("x", "xytalk:muc:invitation");
		String jid = mi.getRoomid();
		String roomname = mi.getRoomName();
		MultiUserChat muc;
		try {
			muc = MultiUserChatManager.getInstanceFor(Launcher.connection)
					.getMultiUserChat(JidCreate.entityBareFrom(jid));
			muc.join(Resourcepart.from(UserCache.CurrentUserName + "-" + UserCache.CurrentUserRealName));
			// 更新左侧panel，将群组UI新建出来
			createNewRoomByMe(jid, roomname);
		} catch (NotAMucServiceException | NoResponseException | XMPPErrorException | NotConnectedException
				| XmppStringprepException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void joinAllRooms() throws XmppStringprepException, XMPPErrorException, NoResponseException,
			NotConnectedException, InterruptedException, NotAMucServiceException {
		// TODO 启动MUC房间订阅，订阅全部房间
		// 在sqlite库中查询MUC群组
		List<Room> dbMucRooms = Launcher.roomService.findByType("m");
		for (Room roomDb : dbMucRooms) {
			if (roomDb.getRoomId() != null && !roomDb.getRoomId().isEmpty() && roomDb.getRoomId().contains("@")) {
				MultiUserChat room = MultiUserChatManager.getInstanceFor(Launcher.connection)
						.getMultiUserChat(JidCreate.entityBareFrom(roomDb.getRoomId()));
				// 将room加入堆缓存，以便重复利用
				// roomCacheService.put(roomDb.getRoomId(),room);
				// 以“username-真实姓名”为nickname进入群
				// room.join(Resourcepart.from(UserCache.CurrentUserName + "-" +
				// UserCache.CurrentUserRealName));
				MucEnterConfiguration.Builder builder = room.getEnterConfigurationBuilder(
						Resourcepart.from(UserCache.CurrentUserName + "-" + UserCache.CurrentUserRealName));
				// 只获取最后10条历史记录
				// builder.requestMaxStanzasHistory(10);
				// 只获取该房间最后一条消息的时间戳到当前时间戳的离线
				int historySince = MucChatService.getHistoryOffsize(roomDb.getLastChatAt());
				builder.requestHistorySince(historySince);
				// 只获取2018-5-1以来的历史记录
				// builder.requestHistorySince(new Date(2018,5,1));
				MucEnterConfiguration mucEnterConfiguration = builder.build();
				room.join(mucEnterConfiguration);

			}
		}
	}

	public static void sendKickMessage(List<String> members, String roomId, String name) {
		// TODO Auto-generated method stub
		
	}

}
