package xysoft.im.extension;

import java.io.IOException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * 离线文件请求，用于收到该离线消息后向离线机器人发送文件请求
 * 例：
 * <message to="test2@win7-1803071731" id="0j66I-230" type="chat">
  <body>用户  发给您离线文件，即将接收：南粤银行动态令牌高并发高可用技术方案.docx</body>
  <x xmlns="xytalk:file:offline">
    <senderFullJid>wangxin@win7-1803071731</senderFullJid>
    <reciverBareJid>test2@win7-1803071731</reciverBareJid>
    <fileType>file</fileType>
    <fileGUID>6e51285ebf4f4e00a3070bcef8af8ab0</fileGUID>
    <fileName>南粤银行动态令牌高并发高可用技术方案.docx</fileName>
  </x>
</message>

 *
 */
public class OfflineFile implements ExtensionElement{

	public static final String ELEMENT_NAME = "x";
	public static final String NAMESPACE = "xytalk:file:offline";
	private String senderFullJid;
	private String reciverBareJid;
	private String fileType;
	private String fileGUID;
	private String fileName;
	
	public OfflineFile() {
		super();
	}
	
	public OfflineFile(String senderFullJid, String reciverBareJid, String fileType, String fileGUID, String fileName) {
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
	
	public static class Provider extends ExtensionElementProvider<OfflineFile> {

        public Provider() {
        }

		@Override
		public OfflineFile parse(org.xmlpull.v1.XmlPullParser parser, int initialDepth)
				throws org.xmlpull.v1.XmlPullParserException, IOException, SmackException {


            final OfflineFile result = new OfflineFile();

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
