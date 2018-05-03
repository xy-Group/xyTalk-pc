package xysoft.im.swingDemo;

import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import xysoft.im.app.Launcher;

public class XmppDemo {

	public XmppDemo() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 对连接的配置
        XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setUsernameAndPassword("wangxin", "1");
        builder.setServiceName(Launcher.DOMAIN); 
        builder.setResource(Launcher.RESOURCE); 
        builder.setServiceName(Launcher.DOMAIN); 
        builder.setHost("127.0.0.1");
        builder.setPort(5222);
    
        // 不加这行会报错，因为没有证书
        builder.setSecurityMode(SecurityMode.disabled);
        //SASLAuthentication.supportSASLMechanism("PLAIN",0); 
        builder.setDebuggerEnabled(true);
        XMPPTCPConnectionConfiguration config = builder.build();
        
        // 建立连接并登陆
        AbstractXMPPConnection c = new XMPPTCPConnection(config);
        
        c.addAsyncStanzaListener(new StanzaListener() {

			@Override
			public void processPacket(Stanza st) throws NotConnectedException {
				// TODO Auto-generated method stub
				System.out.println("in StanzaListener:" + st.toString());
			}
        }, new StanzaFilter() {
            @Override
            public boolean accept(Stanza st) {
                System.out.println("in StanzaFilter:" + st.toXML());
                return false;
            }
        });

        try {
			c.connect();
		} catch (SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			c.login();
		} catch (XMPPException | SmackException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        ChatManager chatManager = ChatManager.getInstanceFor(c);
    
        chatManager.addChatListener(new ChatManagerListener() {
            
        	
            @Override
            public void chatCreated(Chat cc, boolean bb) {
                // 当收到来自对方的消息时触发监听函数
                cc.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat cc, Message mm) {
                        System.out.println("收到消息:" + cc + mm.getBody());
                    }
                });
            }
        });
    
        
        
        
//        Chat chat = chatManager.createChat("admin@win7-1803071731");
//		chat.getThreadID();
//		Message msg = new Message();
//		msg.setBody("kkkkkkkkkkkkkkkkkkkkk33333333333333333333333333333333333"); 
//		try {
//			chat.sendMessage(msg);
//		} catch (NotConnectedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 

}

}
