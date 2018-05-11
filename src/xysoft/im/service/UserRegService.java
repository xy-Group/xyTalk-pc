package xysoft.im.service;

import java.util.HashMap;
import java.util.Map;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.IQReplyFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.iqregister.packet.Registration;

public class UserRegService {

	public static void registerUser(XMPPConnection con, String gatewayDomain, String username, String password, String nickname, StanzaListener callback) throws SmackException.NotConnectedException
    {
        Map<String, String> attributes = new HashMap<>();
        if (username != null) {
            attributes.put("username", username);
        }
        if (password != null) {
            attributes.put("password", password);
        }
        if (nickname != null) {
            attributes.put("nick", nickname);
        }
        Registration registration = new Registration( attributes );
        registration.setType(IQ.Type.set);
        registration.setTo(gatewayDomain);
        registration.addExtension(new GatewayRegisterExtension());

        try {
			con.sendStanzaWithResponseCallback( registration, new IQReplyFilter( registration, con ), callback);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void unregister(XMPPConnection con, String gatewayDomain) throws SmackException.NotConnectedException
    {
        Map<String,String> map = new HashMap<>();
        map.put("remove", "");
        Registration registration = new Registration( map );
        registration.setType(IQ.Type.set);
        registration.setTo(gatewayDomain);

        try {
			con.sendStanzaWithResponseCallback( registration, new IQReplyFilter( registration, con ), stanza -> {
			    IQ response = (IQ) stanza;
			    if (response.getType() == IQ.Type.error ) {
			        //Log.warning( "Unable to unregister from gateway: " + stanza );
			    }
			} );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    static class GatewayRegisterExtension implements ExtensionElement {

        public String getElementName() {
            return "x";
        }

        public String getNamespace() {
            return "jabber:iq:gateway:register";
        }

        public String toXML() {
            String builder = "<" + getElementName() + " xmlns=\"" + getNamespace() +
                    "\"/>";
            return builder;
        }
    }


	public static String getName(String fromUsername) {
		// TODO Auto-generated method stub
		return null;
	}

}
