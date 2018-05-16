package xysoft.im.service;

import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import xysoft.im.app.Launcher;
import xysoft.im.extension.Ping;


public class StateService {
	
	static ConcurrentHashMap<String,String> fullJidMap = new ConcurrentHashMap<String,String>();
	static ConcurrentHashMap<String,Boolean> onlineMap = new ConcurrentHashMap<String,Boolean>();

	public static boolean isOnline(String fullJid){
		Boolean online = onlineMap.get(fullJid);
		if (online== null || online.equals(false)){
			return false;
		}
		else{
			return true;
		}
	}
	
	public static String getFullJid(String bareJid){
		String fullJid = fullJidMap.get(bareJid);
		if (fullJid==null || fullJid.isEmpty() ){
			return "";
		}
		else{
			if (isOnline(fullJid)){
				return fullJid;
			}
			else{
				//如果不在线，也是不能确定fulljid
				return "";
			}
		}
	}
	
	public static void sendPing(String barejid){
		try {
				sendPing(JidCreate.from(barejid + "/android"));
				sendPing(JidCreate.from(barejid + "/ios"));
				sendPing(JidCreate.from(barejid + "/pc"));
				sendPing(JidCreate.from(barejid + "/Spark"));
			} catch (XmppStringprepException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void sendPing(Jid full){
		 Ping req=new Ping(full);
		 try {	
			//iq发出后处理响应回调
			Launcher.connection.sendStanzaWithResponseCallback(req, new IQReplyFilter( req, Launcher.connection ), stanza -> {
			     IQ result = (IQ) stanza;
		    	 String bareJid = result.getFrom().asEntityBareJidIfPossible().toString();
		    	 String fullJid = result.getFrom().asEntityFullJidIfPossible().toString();

			     if ( result.getType() == IQ.Type.error ) {
			    	 // TODO error处理
			    	 onlineMap.remove(fullJid);
			     } else {
			    	 
			    	 fullJidMap.put(bareJid, fullJid);
			    	 onlineMap.put(fullJid, true);
			     }
			 });
		} catch (NotConnectedException | InterruptedException e) {
			
			e.printStackTrace();
		}
	}
		 

}
