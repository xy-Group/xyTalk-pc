package xysoft.im.extension;

import org.jivesoftware.smack.packet.ExtensionElement;


public class Receipt implements ExtensionElement{

	public Receipt() {
		
	}

	@Override
	public String getElementName() {
		return "request";
	}

	@Override
	public CharSequence toXML() {
		 StringBuilder buffer = new StringBuilder();
	        buffer.append("<request xmlns=\"urn:xmpp:receipts\"");
	        buffer.append("/>");
	     return buffer.toString();
	}

	@Override
	public String getNamespace() {
		return "urn:xmpp:receipts";
	}

	

}
