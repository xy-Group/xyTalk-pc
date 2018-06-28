package xysoft.im.service;

import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.muc.provider.MUCAdminProvider;
import org.jivesoftware.smackx.muc.provider.MUCOwnerProvider;

import xysoft.im.extension.Features;
import xysoft.im.extension.MucInvitation;
import xysoft.im.extension.MucKick;
import xysoft.im.extension.MucUpdateMembers;
import xysoft.im.extension.OfflineFile;
import xysoft.im.extension.OfflineFileReceipt;
import xysoft.im.extension.OfflineFileRobot;

public class ProviderRegister {

	public ProviderRegister() {
	}
	
	public static void register(){
		//ProviderManager.addExtensionProvider("request", "urn:xmpp:receipts", new ReceiptProvider());
        ProviderManager.addExtensionProvider("event", "http://jabber.org/protocol/disco#info", new Features.Provider());
        ProviderManager.addExtensionProvider("x", MucInvitation.NAMESPACE, new MucInvitation.Provider());
        ProviderManager.addExtensionProvider("x", MucKick.NAMESPACE, new MucKick.Provider());
        ProviderManager.addExtensionProvider("x", MucUpdateMembers.NAMESPACE, new MucUpdateMembers.Provider());
        ProviderManager.addExtensionProvider("x", OfflineFile.NAMESPACE, new OfflineFile.Provider());
        ProviderManager.addExtensionProvider("x", OfflineFileReceipt.NAMESPACE, new OfflineFileReceipt.Provider());        
        ProviderManager.addExtensionProvider("x", OfflineFileRobot.NAMESPACE, new OfflineFileRobot.Provider());     
        ProviderManager.addIQProvider("query", "http://jabber.org/protocol/muc#owner",  
                new MUCOwnerProvider());
        ProviderManager.addIQProvider("query","http://jabber.org/protocol/muc#admin", new MUCAdminProvider());
	}

}
