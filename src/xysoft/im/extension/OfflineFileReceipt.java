package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * 离线机器人发送的离线文件回执
 * 消息由离线文件机器人回发，所以需要聊天UI针对senderFullJid做特殊处理，即不要显示离线文件机器人的信息，让消息看似是senderFullJid发送的
 * 未来需要考虑的：回执收到后，向原始发送方发送一条收到的回执
 *
 */
public class OfflineFileReceipt implements ExtensionElement{

	public static final String ELEMENT_NAME = "x";
	public static final String NAMESPACE = "xytalk:file:offlinereceipt";
	private String senderFullJid; //离线文件的实际发送者
	private String fileType;
	private String fileName;
	
	public OfflineFileReceipt() {
		super();
	}
	
	public OfflineFileReceipt(String senderFullJid,String fileType,String fileName) {
		super();
		this.senderFullJid = senderFullJid;
		this.fileType = fileType;
		this.fileName = fileName;
	}

	public String getSenderFullJid() {
		return senderFullJid;
	}

	public void setSenderFullJid(String senderFullJid) {
		this.senderFullJid = senderFullJid;
	}
	
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
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
    	sb.append("<fileType>" );
    	sb.append(fileType );
    	sb.append( "</fileType>");
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
	
	public static class Provider extends ExtensionElementProvider<OfflineFileReceipt> {

        public Provider() {
        }

		@Override
		public OfflineFileReceipt parse(org.xmlpull.v1.XmlPullParser parser, int initialDepth)
				throws org.xmlpull.v1.XmlPullParserException, IOException, SmackException {


            final OfflineFileReceipt result = new OfflineFileReceipt();

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

                        if ( "fileType".equals(elementName ) )
                        {
                            result.setFileType(parser.nextText() );
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
