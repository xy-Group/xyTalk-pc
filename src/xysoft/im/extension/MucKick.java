package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;

//废弃，和MucUpdateMembers合并处理
public class MucKick implements ExtensionElement
{	
	public static final String ELEMENT_NAME = "x";
	public static final String NAMESPACE = "xytalk:muc:kick";
	private String roomid;
	private String roomName;
	
    public MucKick() {
		super();
	}

    public MucKick(String roomid,String roomName) {
		super();
		this.roomid = roomid;
		this.roomName = roomName;
	}



    public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public void setRoomid(String roomid) {
    	this.roomid = roomid;
    }

    public String getRoomid() {
        return roomid;
    }

    public String getElementName() {
    	return ELEMENT_NAME;
    }

    public String getNamespace() {
    	return NAMESPACE;
    }

    public String toXML() {
    	StringBuffer sb =new StringBuffer();
    	sb.append("<" + getElementName() + " xmlns=\"" + getNamespace() + "\">");
    	sb.append("<roomid>" );
    	sb.append(roomid );
    	sb.append("</roomid>" );
    	sb.append("<roomName>" );
    	sb.append(roomName );
    	sb.append( "</roomName>");
    	sb.append( "</" + getElementName() + ">");
        
        return sb.toString();
    }

    public static class Provider extends ExtensionElementProvider<MucKick> {

        public Provider() {
        }

		@Override
		public MucKick parse(org.xmlpull.v1.XmlPullParser parser, int initialDepth)
				throws org.xmlpull.v1.XmlPullParserException, IOException, SmackException {


            final MucKick result = new MucKick();

            while ( true )
            {
                parser.next();
                String elementName = parser.getName();
                switch ( parser.getEventType() )
                {
                    case XmlPullParser.START_TAG:
                        if ( "roomid".equals( elementName ) )
                        {
                            result.setRoomid( parser.nextText() );
                        }
                        if ( "roomName".equals( elementName ) )
                        {
                            result.setRoomName(parser.nextText() );
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