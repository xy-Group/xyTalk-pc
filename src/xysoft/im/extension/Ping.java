package xysoft.im.extension;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.SimpleIQ;

import org.jxmpp.jid.Jid;

public class Ping extends SimpleIQ {

    public static final String ELEMENT = "ping";
    public static final String NAMESPACE = "urn:xmpp:ping";

    public Ping() {
        super(ELEMENT, NAMESPACE);
    }

    public Ping(Jid to) {
        this();
        setTo(to);
        setType(IQ.Type.get);
    }

    public IQ getPong() {
        return createResultIQ(this); //返回空子节点
    }
}