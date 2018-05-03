package xysoft.im.listener;

import org.jivesoftware.smack.packet.Presence;

public interface PresenceListener {

    /**
     * Called when the user of Sparks presence has changed.
     *
     * @param presence the presence.
     */
    void presenceChanged(Presence presence);

}