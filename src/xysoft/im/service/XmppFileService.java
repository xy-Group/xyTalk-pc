package xysoft.im.service;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import xysoft.im.app.Launcher;
import xysoft.im.db.model.FileAttachment;
import xysoft.im.db.model.ImageAttachment;
import xysoft.im.db.model.Message;
import xysoft.im.db.model.Room;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.RoomsPanel;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.JID;
import xysoft.im.utils.MimeTypeUtil;

public class XmppFileService {

	public XmppFileService() {
		// TODO Auto-generated constructor stub
	}

	//发送文件
	public static void sendFile(String fileFullPath, String fulljid) {
		// TODO: 通知服务器要开始在线上传文件
		FileTransferManager manager = FileTransferManager.getInstanceFor(Launcher.connection);
		OutgoingFileTransfer transfer;
		try {
			transfer = manager.createOutgoingFileTransfer(JidCreate.entityFullFrom(fulljid));
			transfer.sendFile(new File(fileFullPath), "File Transfer");
		} catch (XmppStringprepException | SmackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//接收文件
	public static FileTransferListener fileListener() {
		return new FileTransferListener() {
			public void fileTransferRequest(FileTransferRequest request) {

				IncomingFileTransfer transfer = request.accept();
				String fileName = transfer.getFileName();
				String senderjid = transfer.getPeer().asEntityBareJidIfPossible().toString();
				
		        FileTransferNegotiator.getInstanceFor(Launcher.connection);
		        FileTransferNegotiator.IBB_ONLY = Launcher.ISFILETRANSFERIBBONLY;
	            String path = Launcher.appFilesBasePath
	            			+ System.getProperty("file.separator") + "files";
	            
	            File dir = new File(path);
	            if (!dir.exists()){
	                dir.mkdirs();
	            }
	            
		        final File downloadedFile = new File(path, fileName);
		        
				try {
					transfer.recieveFile(downloadedFile);
					
				} catch (SmackException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
								
				//UI在文件传输中的变化
				DebugUtil.debug("file senderjid: " + senderjid);
						
		        //progressBar.setMaximum(100); 
		        //progressBar.setStringPainted(true);
		        
				final Timer timer = new Timer();
				TimerTask updateProgessBar = new TimerTask() {
				    @Override
				    public void run() {
					if (transfer.getAmountWritten() >= request.getFileSize() 
						|| transfer.getStatus() == Status.error 
						|| transfer.getStatus() == Status.refused
						|| transfer.getStatus() == Status.cancelled
						|| transfer.getStatus() == Status.complete) 
					{
					    this.cancel();
					    timer.cancel();
					    String rawID = UUID.randomUUID().toString().replace("-", "");
					    reciveFile(downloadedFile.getPath(),rawID,senderjid);
					    //_endtime = System.currentTimeMillis();
					    //updateonFinished(request, downloadedFile);
					}else
					{
					    // 100 % = Filesize
					    // x %   = Currentsize	    
					    long p = (transfer.getAmountWritten() * 100 / transfer.getFileSize() );
					    //progressBar.setValue(Math.round(p));        
					    DebugUtil.debug("updateProgessBar: " +fileName+"- "+ p);
					}
					
				    }
				};	
				timer.scheduleAtFixedRate(updateProgessBar, 2000, 100);
		}
		};
	}
	
	
	private static void reciveFile(String reciveFilename, String fileId, String senderBareJid) {
		// TODO 接收文件时的UI变化
		boolean isImage;
		if (reciveFilename.lastIndexOf(".")<0){
			isImage = false;
		}else{
			String type = MimeTypeUtil.getMime(reciveFilename.substring(reciveFilename.lastIndexOf(".")));
			isImage = type.startsWith("image/");
		}

		// 发送的是图片
		String name = reciveFilename.substring(reciveFilename.lastIndexOf(File.separator) + 1); // 文件名

		FileAttachment fileAttachment = null;
		ImageAttachment imageAttachment = null;
		Message dbMessage = new Message();
		dbMessage.setProgress(-1);

		if (isImage) {
			imageAttachment = new ImageAttachment();
			imageAttachment.setId(fileId);
			imageAttachment.setWidth(100);
			imageAttachment.setHeight(100);
			imageAttachment.setImageUrl(reciveFilename);
			imageAttachment.setTitle(name);
			dbMessage.setImageAttachmentId(imageAttachment.getId());
			Launcher.imageAttachmentService.insertOrUpdate(imageAttachment);

		} else {

			fileAttachment = new FileAttachment();
			fileAttachment.setId(fileId);
			System.out.println(File.separator);
			fileAttachment.setLink(reciveFilename);
			fileAttachment.setTitle(name);
			dbMessage.setFileAttachmentId(fileAttachment.getId());
			Launcher.fileAttachmentService.insertOrUpdate(fileAttachment);
		}

		final String messageId = fileId;

		dbMessage.setId(messageId);
		dbMessage.setMessageContent(name);
		dbMessage.setRoomId(senderBareJid);
		dbMessage.setSenderId(senderBareJid);
		dbMessage.setSenderUsername(JID.usernameByJid(senderBareJid));
		dbMessage.setTimestamp(System.currentTimeMillis());
		dbMessage.setUpdatedAt(System.currentTimeMillis());

		Launcher.messageService.insertOrUpdate(dbMessage);

		if (ChatPanel.CHAT_ROOM_OPEN_ID.equals(senderBareJid)){//如果用户正在聊天，那也要刷新当前聊天窗
			
			ChatPanel.getContext().notifyUIStartReciveFile(reciveFilename, fileId ,senderBareJid);
		}
		else{ //不在当前聊天窗中，需要红点提醒
			
			Room room = Launcher.roomService.findById(senderBareJid);
			room.setLastMessage("接收文件"+reciveFilename);
			room.setUnreadCount(room.getUnreadCount()+1);
			Launcher.roomService.update(room);
			RoomsPanel.getContext().updateRoomItem(senderBareJid);
		}
	}
	
	
}
