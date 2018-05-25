package xysoft.im.service;

import java.util.List;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.blocking.BlockingCommandManager;
import org.jxmpp.jid.Jid;

import xysoft.im.app.Launcher;


public class BlockingService {

	public BlockingService() {		
		//初始化实例Blocking Command Manager
		BlockingCommandManager blockingCommandManager = BlockingCommandManager.getInstanceFor(Launcher.connection);
		//检查是否有阻止服务支持
		try {
			boolean isSupported = blockingCommandManager.isSupportedByServer();
			
			//获取block list
			List<Jid> blockList = blockingCommandManager.getBlockList();
			
		} catch (NoResponseException | XMPPErrorException | NotConnectedException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//阻止一个人
		//blockingCommandManager.blockContact(jid);
		//解锁Unblock 
		//blockingCommandManager.unblockContact(jid);
		//全部解锁
		//blockingCommandManager.unblockAll();
		
		
		
	}

}
