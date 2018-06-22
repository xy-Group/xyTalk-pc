package xysoft.im.service;

import java.util.Collection;

import javax.swing.SwingUtilities;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.Roster.SubscriptionMode;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import xysoft.im.app.Launcher;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.JID;

/**
 * 根据smack的文档 Automatically accept all presence subscription requests. 自动接受全部
 * Automatically reject all presence subscription requests. 自动拒绝全部 Process
 * presence subscription requests manually. 手动模式
 */
public class RosterService {

	// 自动模式，自动添加好友
	public static void addRosterListenerAuto() {

		Roster roster = Roster.getInstanceFor(Launcher.connection);
		// 自动接受全部请求
		Roster.setDefaultSubscriptionMode(SubscriptionMode.accept_all);
		Roster.setRosterLoadedAtLoginDefault(true);
		
		roster.addSubscribeListener(new SubscribeListener(){

			@Override
			public SubscribeAnswer processSubscribe(Jid arg0, Presence arg1) {
				// 自动同意
				return SubscribeAnswer.Approve;
			}
			
		});
		
		roster.addRosterListener(new RosterListener() {  
			@Override
			public void entriesAdded(Collection<Jid> arg0) {
				// TODO Auto-generated method stub
				DebugUtil.debug("好友请求entriesAdded" );
			}

			@Override
			public void entriesDeleted(Collection<Jid> arg0) {
				// TODO Auto-generated method stub
				DebugUtil.debug("好友请求entriesDeleted" );
			}

			@Override
			public void entriesUpdated(Collection<Jid> arg0) {
				// TODO Auto-generated method stub
				DebugUtil.debug("好友请求entriesUpdated" );
				
			}

			@Override
			public void presenceChanged(Presence arg0) {
				// TODO Auto-generated method stub
				DebugUtil.debug("好友请求presenceChanged" );
				
			}     
              
        });
	}

	// 手动模式添加好友，弃用
	public static void addRosterListener() {

		// 条件过滤
		StanzaFilter filter = new StanzaTypeFilter(Presence.class);
		StanzaListener listener = new StanzaListener() {

			@Override
			public void processStanza(Stanza packet)
					throws NotConnectedException, InterruptedException, NotLoggedInException {
				if (packet instanceof Presence) {

					final Presence presence = (Presence) packet;
					DebugUtil.debug("好友请求监听：" + presence.toXML());
					final Roster roster = Roster.getInstanceFor(Launcher.connection);
					final RosterEntry entry = roster.getEntry(presence.getFrom().asEntityBareJidIfPossible());

					switch (presence.getType()) {
					case subscribe:
						// 其他人提交订阅请求
						DebugUtil.debug("好友请求监听：提交订阅请求"+presence.getFrom());
						SwingUtilities.invokeLater(() -> {
							try {
								// 通过订阅
								subscribeRequest(presence.getFrom());
							} catch (Exception e) {

							}
						});
						break;
					case unsubscribe:
						 // 其他人不把我作为花名册成员
						 if ( entry != null )
						 {
						 try
						 {
						 removeContactItem( presence.getFrom() );
						 roster.removeEntry( entry );// 他删除我，我也删除他
						 }
						 catch ( Exception e )
						 {
						 Presence unsub = new Presence(
						 Presence.Type.unsubscribed );
						 unsub.setTo( presence.getFrom() );
						 try
						 {
						 Launcher.connection.sendStanza( unsub );
						 }
						 catch ( SmackException.NotConnectedException e1 )
						 {
						
						 }
						
						 }
						 }
						break;

					case subscribed:
						// 其他人同意了我提交的好友请求
						DebugUtil.debug("好友请求监听：同意了我提交的好友请求");
						SwingUtilities.invokeLater(() -> {
							try {
								// 通过订阅
								subscribedRequest(presence.getFrom());
							} catch (Exception e) {

							}
						});
						break;

					case unsubscribed:
						DebugUtil.debug("好友请求监听：对方删除了我");
						 //其他人已成功删除花名册
						 SwingUtilities.invokeLater( () ->
						 {
						 if ( entry != null )
						 {
						 try
						 {
						 removeContactItem( presence.getFrom() );
						 roster.removeEntry( entry );
						 }
						 catch ( Exception e )
						 {
						
						 }
						 }
						
						 });
						break;

					default:
						DebugUtil.debug("好友请求监听：状态变化");
						// Any other presence updates. These are likely regular
						// presence changes
						// 即席更新处理
						// presenceBuffer.update( presence );

						break;
					}
				}
			}

			private void removeContactItem(Jid from) {
				// TODO Auto-generated method stub

			}

			private void subscribeRequest(Jid from) {
				Presence presence = new Presence(Presence.Type.subscribed);
				presence.setTo(from);

				try {
					try {
						Launcher.connection.sendStanza(presence);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SmackException.NotConnectedException e) {
					e.printStackTrace();
				}
				
				try {
					Roster.getInstanceFor(Launcher.connection).createEntry(JidCreate.bareFrom(from), JID.usernameByJid(from.toString()), null);
				} catch (NotLoggedInException | NoResponseException | XMPPErrorException | NotConnectedException
						| XmppStringprepException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			private void subscribedRequest(Jid from) {
				Presence pres1 = new Presence(Presence.Type.available);

				pres1.setTo(from);

				try {
					try {
						Launcher.connection.sendStanza(pres1);

					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (SmackException.NotConnectedException e) {
					e.printStackTrace();
				}

			}

			

		};

		
		Launcher.connection.addAsyncStanzaListener(listener, filter);
	}
}
