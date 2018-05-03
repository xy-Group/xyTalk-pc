package xysoft.im.listener;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import xysoft.im.utils.DebugUtil;

public class ChatListener implements ChatMessageListener {      

	@Override
	public void processMessage(Chat chat, Message message) {
		DebugUtil.debug("processMessage:"+message.getBody());
		
	}
 
          
}  
