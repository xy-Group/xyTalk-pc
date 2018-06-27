package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;

public class MucUpdateMembers implements ExtensionElement
{	
	public static final String ELEMENT_NAME = "x";
	public static final String NAMESPACE = "xytalk:muc:updatemember";
	private String roomid;
	private String memberJids;
	
    public MucUpdateMembers() {
		super();
	}

    public MucUpdateMembers(String roomid,String memberJids) {
		super();
		this.roomid = roomid;
		this.setMemberJids(memberJids);
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
    	sb.append("<memberJids>" );
    	sb.append(memberJids );
    	sb.append( "</memberJids>");
    	sb.append( "</" + getElementName() + ">");
        
        return sb.toString();
    }

    public String getMemberJids() {
		return memberJids;
	}

	public void setMemberJids(String memberJids) {
		this.memberJids = memberJids;
	}

	public static class Provider extends ExtensionElementProvider<MucUpdateMembers> {

        public Provider() {
        }

		@Override
		public MucUpdateMembers parse(org.xmlpull.v1.XmlPullParser parser, int initialDepth)
				throws org.xmlpull.v1.XmlPullParserException, IOException, SmackException {


            final MucUpdateMembers result = new MucUpdateMembers();

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
                        if ( "memberJids".equals( elementName ) )
                        {
                            result.setMemberJids(parser.nextText() );
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
