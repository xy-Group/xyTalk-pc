package xysoft.im.listener;

import org.jivesoftware.smack.packet.Presence;

public interface PresenceListener {

    void presenceChanged(Presence presence);

}