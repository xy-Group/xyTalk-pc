package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;


public class BuzzPacket implements ExtensionElement
{
    public static final String ELEMENT_NAME = "attention";

    public static final String NAMESPACE = "urn:xmpp:attention:0";

    public String getElementName() {
	return ELEMENT_NAME;
    }

    public String getNamespace() {
	return NAMESPACE;
    }

    public String toXML() {
    	StringBuilder buffer = new StringBuilder();
     		buffer.append("<attention xmlns=\"urn:xmpp:attention:0\"");
     		buffer.append("/>");
     	return buffer.toString();
    }

    public static class Provider extends ExtensionElementProvider<BuzzPacket> {

        public Provider() {
        }

		@Override
		public BuzzPacket parse(org.xmlpull.v1.XmlPullParser parser, int initialDepth)
				throws org.xmlpull.v1.XmlPullParserException, IOException, SmackException {

			return new BuzzPacket();
		}
    }
}
