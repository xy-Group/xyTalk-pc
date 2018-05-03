package xysoft.im.service;

import org.jivesoftware.smack.provider.ProviderManager;
import xysoft.im.extension.Features;

public class ProviderRegister {

	public ProviderRegister() {
	}
	
	public static void register(){
		//ProviderManager.addExtensionProvider("request", "urn:xmpp:receipts", new ReceiptProvider());
        ProviderManager.addExtensionProvider("event", "http://jabber.org/protocol/disco#info", new Features.Provider());
	}

}
