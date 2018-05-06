package xysoft.im.extension;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class JabberAvatarExtension implements ExtensionElement {

    public static final String ELEMENT_NAME = "x";

    public static final String NAMESPACE = "jabber:x:avatar";

    private String photoHash;

    public void setPhotoHash(String hash) {
        photoHash = hash;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        String buf = "<" + getElementName() + " xmlns=\"" + getNamespace() + "\">" +
                "<hash>" +
                photoHash +
                "</hash>" +
                "</" + getElementName() + ">";
        return buf;
    }

    public static class Provider extends ExtensionElementProvider<JabberAvatarExtension>
    {
        public Provider() {
        }

        @Override
        public JabberAvatarExtension parse( XmlPullParser parser, int i ) throws XmlPullParserException, IOException, SmackException
        {
            final JabberAvatarExtension result = new JabberAvatarExtension();

            while ( true )
            {
                parser.next();
                String elementName = parser.getName();
                switch ( parser.getEventType() )
                {
                    case XmlPullParser.START_TAG:
                        if ( "photo".equals( elementName ) )
                        {
                            result.setPhotoHash( parser.nextText() );
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if ( ELEMENT_NAME.equals( elementName ) )
                        {
                            return result;
                        }
                        break;
                }
            }
        }
    }

}