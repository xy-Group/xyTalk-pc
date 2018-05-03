package xysoft.im.extension;

import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Features implements ExtensionElement {

    private List<String> availableFeatures = new ArrayList<>();


    public List<String> getAvailableFeatures() {
        return availableFeatures;
    }

    public void addFeature(String feature) {
        availableFeatures.add(feature);
    }

    public static final String ELEMENT_NAME = "event";
    public static final String NAMESPACE = "http://jabber.org/protocol/disco#info";

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        return ( "<event xmlns=\"" + NAMESPACE + "\"" ) + "</event>";
    }

    public static class Provider extends ExtensionElementProvider<Features>
    {
        public Provider() {
        }

        public Features parse( XmlPullParser parser, int initialDepth) throws XmlPullParserException, IOException {
            Features features = new Features();
            boolean done = false;
            while (!done) {
                int eventType = parser.next();
                if (eventType == XmlPullParser.START_TAG && "event".equals(parser.getName())) {
                    parser.nextText();
                }
                if (eventType == XmlPullParser.START_TAG && "feature".equals(parser.getName())) {
                    String feature = parser.getAttributeValue("", "var");
                    features.addFeature(feature);
                }
                else if (eventType == XmlPullParser.END_TAG) {
                    if ("event".equals(parser.getName())) {
                        done = true;
                    }
                }
            }

            return features;
        }
    }
}
