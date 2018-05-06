package xysoft.im.extension;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class VCardUpdateExtension implements ExtensionElement {

    public static final String ELEMENT_NAME = "x";

    public static final String NAMESPACE = "vcard-temp:x:update";

    private String photoHash;

    public void setPhotoHash(String hash) {
        photoHash = hash;
    }

    public String getPhotoHash() {
        return photoHash;
    }

    public String getElementName() {
        return ELEMENT_NAME;
    }

    public String getNamespace() {
        return NAMESPACE;
    }

    public String toXML() {
        String buf = "<" + getElementName() + " xmlns=\"" + getNamespace() + "\">" +
                "<photo>" +
                photoHash +
                "</photo>" +
                "</" + getElementName() + ">";
        return buf;
    }

    public static class Provider extends ExtensionElementProvider<VCardUpdateExtension>
    {
        public Provider() {
        }

        @Override
        public VCardUpdateExtension parse( XmlPullParser parser, int i ) throws XmlPullParserException, IOException, SmackException
        {
            final VCardUpdateExtension result = new VCardUpdateExtension();

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
