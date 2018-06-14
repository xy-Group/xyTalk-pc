package xysoft.im.service.login;

import java.io.IOException;
import java.net.InetAddress;

import javax.swing.JOptionPane;

import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;

import xysoft.im.app.Launcher;
import xysoft.im.cache.UserCache;
import xysoft.im.constant.Res;
import xysoft.im.db.model.ContactsUser;
import xysoft.im.listener.SessionManager;
import xysoft.im.service.ChatService;
import xysoft.im.service.MucChatService;
import xysoft.im.service.ProviderRegister;
import xysoft.im.service.RosterService;
import xysoft.im.service.XmppFileService;
import xysoft.im.utils.DebugUtil;


/**
 * XMPP验证登陆
 *
 */
public class XmppLogin implements Login {

	String username;
	String password;
	String tag;

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public XmppLogin() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String login() {

		if (Launcher.IS_DEBUGMODE) {
			SmackConfiguration.DEBUG = true;
		}

		try {
			XMPPTCPConnectionConfiguration conf = retrieveConnectionConfiguration();
			if (conf == null) {
				return "XMPPTCPConnectionConfiguration is null";
			}

			Launcher.connection = new XMPPTCPConnection(conf);

			// 连接监听
			SessionManager sessionManager = new SessionManager();
			Launcher.connection.addConnectionListener(sessionManager);

			// 重连管理
			ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(Launcher.connection);
			reconnectionManager.setFixedDelay(Res.RE_CONNET_INTERVAL);// 重联间隔
			reconnectionManager.enableAutomaticReconnection();// 开启重联机制

			StanzaFilter filterMsg = new StanzaTypeFilter(Message.class);
//			StanzaFilter filterIQ = new StanzaTypeFilter(IQ.class);
			// PacketCollector myCollector =
			// Launcher.connection.createPacketCollector(filterMsg);
			StanzaListener listenerMsg = new StanzaListener() {
				@Override
				public void processStanza(Stanza stanza)
						throws NotConnectedException, InterruptedException, NotLoggedInException {
					DebugUtil.debug("processPacket:" + stanza.toXML());
					if (stanza instanceof org.jivesoftware.smack.packet.Message) {
						ChatService.messageProcess(stanza);
					}
				}
				
			};

//			StanzaListener listenerIQ = new StanzaListener() {
//
//				@Override
//				public void processStanza(Stanza stanza)
//						throws NotConnectedException, InterruptedException, NotLoggedInException {
//
//					if (stanza instanceof IQ) {
//						// TODO IQ包
//						
//					}
//				}
//			};

			//加入异步监听
			Launcher.connection.addAsyncStanzaListener(listenerMsg, filterMsg);
			//Launcher.connection.addSyncStanzaListener(listenerIQ, filterIQ);

			try {
				Launcher.connection.connect();
				Launcher.connection.login();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 注册扩展
			ProviderRegister.register();

			sessionManager.initializeSession(Launcher.connection);
			sessionManager.setUserBareAddress(getUsername() + "@" + Launcher.DOMAIN);
			sessionManager.setJID(sessionManager.getUserBareAddress() + "/" + Launcher.RESOURCE);
			sessionManager.setUsername(username);
			sessionManager.setPassword(password);
			
			//注册MUC邀请监听
			MucChatService.mucInvitation();
			//MucChatService.mucGetInfo("a8@muc.win7-1803071731");
			
			//自动添加好友监听,processSubscribe起了关键作用
			RosterService.addRosterListenerAuto();
			//手动模式添加好友，废弃
			//RosterService.addRosterListener();

			//接收文件listener
			final FileTransferManager manager = FileTransferManager.getInstanceFor(Launcher.connection);
			
			manager.addFileTransferListener(XmppFileService.fileListener());
			
			UserCache.CurrentUserName = sessionManager.getUsername();
			UserCache.CurrentUserPassword = sessionManager.getPassword();
			UserCache.CurrentUserToken = "";
			UserCache.CurrentBareJid = sessionManager.getUserBareAddress();
			UserCache.CurrentFullJid = sessionManager.getJID();
			ContactsUser user = Launcher.contactsUserService.findByUsername(UserCache.CurrentUserName);
			String currentRealname = "未知";
			if (user==null){
				JOptionPane.showMessageDialog(null,"本地数据表ContactUser缺失以下用户的真实姓名："+sessionManager.getUsername());
			}
			else{
				currentRealname = user.getName();
			}

			UserCache.CurrentUserRealName = currentRealname;


			DebugUtil.debug("UserCache.CurrentUserName:" + UserCache.CurrentUserName);
			DebugUtil.debug("UserCache.CurrentBareJid:" + UserCache.CurrentBareJid);
			DebugUtil.debug("UserCache.CurrentFullJid:" + UserCache.CurrentFullJid);

			DebugUtil.debug("Launcher.connection.getUser():" + Launcher.connection.getUser());
			DebugUtil.debug("Launcher.connection.getServiceName():" + Launcher.connection.getServiceName());

			// Chat chat = ChatManager.getInstanceFor(Launcher.connection)
			// .createChat("test1@win7-1803071731");
			//
			// ChatManager chatManager =
			// ChatManager.getInstanceFor(Launcher.connection);
			//
			// chatManager.addChatListener(new ChatManagerListener() {
			//
			// @Override
			// public void chatCreated(Chat cc, boolean bb) {
			// // 当收到来自对方的消息时触发监听函数
			// cc.addMessageListener(new ChatMessageListener() {
			// @Override
			// public void processMessage(Chat cc, Message mm) {
			// System.out.println("app2收到消息:" + cc + mm.getBody());
			// }
			// });
			// }
			// });
			//
			// Message message = new Message();
			// message.addExtension(new Receipt());
			// message.setBody("你好！");
			// chat.sendMessage(message);

			// 推送服务
			// PushNotificationsManager pushNotificationsManager =
			// PushNotificationsManager.getInstanceFor(Launcher.connection);
			//
			// try {
			// boolean isSupported =
			// pushNotificationsManager.isSupported();//.isSupportedByServer();
			// boolean isSupportedByServer =
			// pushNotificationsManager.isSupportedByServer();//.isSupportedByServer();
			//
			// DebugUtil.debug("PushNotificationsManager isSupported:"
			// +isSupported);
			// DebugUtil.debug("PushNotificationsManager isSupportedByServer:"
			// +isSupportedByServer);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			


			MucChatService.joinAllRooms();
			
			if (Launcher.connection.isAuthenticated())
				return "ok";
			else
				return "no authenticated";

		} catch (XMPPException e) {
			e.printStackTrace();
			return "XMPPException:" + e.getMessage();

		} catch (SmackException e) {
			e.printStackTrace();
			return "SmackException:" + e.getMessage();

		} catch (IOException e) {
			e.printStackTrace();
			return "IOException:" + e.getMessage();

		} catch (InterruptedException e) {
			e.printStackTrace();
			return "InterruptedException:" + e.getMessage();
		}

	}

	

	protected XMPPTCPConnectionConfiguration retrieveConnectionConfiguration() {

		try {

			DebugUtil.debug("login:" + getUsername() + "--" + getPassword() + "--" + Launcher.HOSTPORT + "--"
					+ Launcher.DOMAIN + "--" + InetAddress.getByName(Launcher.HOSTNAME));

			XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder()
					.setUsernameAndPassword(getUsername(), getPassword()).setResource(Launcher.RESOURCE)
					.setPort(Launcher.HOSTPORT).setConnectTimeout(5000).setXmppDomain(Launcher.DOMAIN)
					.setHost(Launcher.HOSTNAME).setHostAddress(InetAddress.getByName(Launcher.HOSTNAME)) .setSecurityMode(SecurityMode.disabled)
					.setDebuggerEnabled(true).setSendPresence(true);
			DebugUtil.debug("builder:" + builder.toString());
			return builder.build();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

}
