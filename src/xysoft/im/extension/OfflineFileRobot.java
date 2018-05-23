package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * 发送离线文件后，发送给离线机器人的消息。离线机器人接收消息后会根据fileGUID来创建新文件夹，待实际接收者上线后将对应文件发出
 */
public class OfflineFileRobot implements ExtensionElement{

	public static final String ELEMENT_NAME = "x";
	public static final String NAMESPACE = "xytalk:file:robotoffline";
	private String senderFullJid;
	private String reciverBareJid;
	private String fileType;
	private String fileGUID;
	private String fileName;
	
	public OfflineFileRobot() {
		super();
	}
	
	public OfflineFileRobot(String senderFullJid, String reciverBareJid, String fileType, String fileGUID, String fileName) {
		super();
		this.senderFullJid = senderFullJid;
		this.reciverBareJid = reciverBareJid;
		this.fileType = fileType;
		this.fileGUID = fileGUID;
		this.fileName = fileName;
	}

	public String getSenderFullJid() {
		return senderFullJid;
	}

	public void setSenderFullJid(String senderFullJid) {
		this.senderFullJid = senderFullJid;
	}
	
	public String getReciverBareJid() {
		return reciverBareJid;
	}

	public void setReciverBareJid(String reciverBareJid) {
		this.reciverBareJid = reciverBareJid;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileGUID() {
		return fileGUID;
	}

	public void setFileGUID(String fileGUID) {
		this.fileGUID = fileGUID;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getElementName() {
		return ELEMENT_NAME;
	}

	@Override
	public CharSequence toXML() {
		
		StringBuffer sb =new StringBuffer();
    	sb.append("<" + getElementName() + " xmlns=\"" + getNamespace() + "\">");
    	sb.append("<senderFullJid>" );
    	sb.append(senderFullJid );
    	sb.append("</senderFullJid>" );
    	sb.append("<reciverBareJid>" );
    	sb.append(reciverBareJid );
    	sb.append("</reciverBareJid>" );
    	sb.append("<fileType>" );
    	sb.append(fileType );
    	sb.append( "</fileType>");
    	sb.append("<fileGUID>" );
    	sb.append(fileGUID );
    	sb.append( "</fileGUID>");
    	sb.append("<fileName>" );
    	sb.append(fileName );
    	sb.append( "</fileName>");
    	sb.append( "</" + getElementName() + ">");
        
        return sb.toString();
	}

	@Override
	public String getNamespace() {
		return NAMESPACE;
	}
	
	public static class Provider extends ExtensionElementProvider<OfflineFileRobot> {

        public Provider() {
        }

		@Override
		public OfflineFileRobot parse(org.xmlpull.v1.XmlPullParser parser, int initialDepth)
				throws org.xmlpull.v1.XmlPullParserException, IOException, SmackException {


            final OfflineFileRobot result = new OfflineFileRobot();

            while ( true )
            {
                parser.next();
                String elementName = parser.getName();
                switch ( parser.getEventType() )
                {
                    case XmlPullParser.START_TAG:
                        if ( "senderFullJid".equals( elementName ) )
                        {
                            result.setSenderFullJid(parser.nextText() );
                        }
                        if ( "reciverBareJid".equals( elementName ) )
                        {
                            result.setReciverBareJid(parser.nextText() );
                        }
                        if ( "fileType".equals(elementName ) )
                        {
                            result.setFileType(parser.nextText() );
                        }
                        if ( "fileGUID".equals( elementName ) )
                        {
                            result.setFileGUID(parser.nextText() );
                        }
                        if ( "fileName".equals( elementName ) )
                        {
                            result.setFileName(parser.nextText() );
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
