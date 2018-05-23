package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;
/**
 * 用于离线存储加入群组邀请，作为Message的扩展属性，例子：
 * <message to="test2@win7-1803071731" id="wKmRm-190" from="wangxin@win7-1803071731/pc" type="chat" xmlns="jabber:client">
 * <body>请加入会议</body>
 * <x xmlns="xytalk:muc:invitation">
 * <roomid>1526285125776@muc.win7-1803071731</roomid>
 * <roomName>测试14</roomName>
 * </x>
 * <delay xmlns="urn:xmpp:delay" from="win7-1803071731" stamp="2018-05-14T08:05:26.472Z">
 * Offline Storage - win7-1803071731</delay></message>
 *
 */
public class MucInvitation implements ExtensionElement
{	
	public static final String ELEMENT_NAME = "x";
	public static final String NAMESPACE = "xytalk:muc:invitation";
	private String roomid;
	private String roomName;
	
    public MucInvitation() {
		super();
	}

    public MucInvitation(String roomid,String roomName) {
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

    public static class Provider extends ExtensionElementProvider<MucInvitation> {

        public Provider() {
        }

		@Override
		public MucInvitation parse(org.xmlpull.v1.XmlPullParser parser, int initialDepth)
				throws org.xmlpull.v1.XmlPullParserException, IOException, SmackException {


            final MucInvitation result = new MucInvitation();

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