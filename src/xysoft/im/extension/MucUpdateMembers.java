package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;

//通知群成员保存最新的成员清单
public class MucUpdateMembers implements ExtensionElement
{	
	public static final String ELEMENT_NAME = "x";
	public static final String NAMESPACE = "xytalk:muc:updatemember";
	private String roomid;
	private String memberUsernames;
	
    public String getMemberUsernames() {
		return memberUsernames;
	}

	public void setMemberUsernames(String memberUsernames) {
		this.memberUsernames = memberUsernames;
	}

	public MucUpdateMembers() {
		super();
	}

    public MucUpdateMembers(String roomid,String memberUsernames) {
		super();
		this.roomid = roomid;
		this.setMemberUsernames(memberUsernames);
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
    	sb.append("<memberUsernames>" );
    	sb.append(memberUsernames );
    	sb.append( "</memberUsernames>");
    	sb.append( "</" + getElementName() + ">");
        
        return sb.toString();
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
                        if ( "memberUsernames".equals( elementName ) )
                        {
                            result.setMemberUsernames(parser.nextText() );
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
