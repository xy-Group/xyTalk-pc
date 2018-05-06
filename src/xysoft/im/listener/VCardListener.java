package xysoft.im.listener;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

public interface VCardListener {

	    void vcardChanged(VCard vcard);
}

