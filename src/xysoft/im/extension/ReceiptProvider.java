package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ReceiptProvider extends ExtensionElementProvider<Receipt> {

	public ReceiptProvider() {

	}

	@Override
	public Receipt parse(XmlPullParser parser, int initialDepth)
			throws XmlPullParserException, IOException, SmackException {

		Receipt receipt = new Receipt();
        boolean done = false;
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG && "request".equals(parser.getName())) {
                parser.nextText();
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if ("request".equals(parser.getName())) {
                    done = true;
                }
            }
        }

        return receipt;
    
	}

}
