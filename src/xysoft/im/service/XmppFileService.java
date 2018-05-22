package xysoft.im.service;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import xysoft.im.app.Launcher;
import xysoft.im.cache.UserCache;
import xysoft.im.db.model.FileAttachment;
import xysoft.im.db.model.ImageAttachment;
import xysoft.im.db.model.Message;
import xysoft.im.db.model.Room;
import xysoft.im.extension.OfflineFile;
import xysoft.im.extension.OfflineFileRobot;
import xysoft.im.extension.Receipt;
import xysoft.im.panels.ChatPanel;
import xysoft.im.panels.RoomsPanel;
import xysoft.im.utils.DebugUtil;
import xysoft.im.utils.JID;
import xysoft.im.utils.MimeTypeUtil;

public class XmppFileService {
	
	//用于存储离线文件发送者和文件名的关系
	static ConcurrentHashMap<String,String> offlineFileJidMap = new ConcurrentHashMap<String,String>();

	public XmppFileService() {
		// TODO Auto-generated constructor stub
	}

	//发送文件
	public static void sendFile(String fileFullPath,String fulljid,String description) {
		// TODO: 通知服务器要开始在线上传文件
		FileTransferManager manager = FileTransferManager.getInstanceFor(Launcher.connection);
		OutgoingFileTransfer transfer;
		try {
			transfer = manager.createOutgoingFileTransfer(JidCreate.entityFullFrom(fulljid));
			transfer.sendFile(new File(fileFullPath), description);
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
				String offlineSenderjid = offlineFileJidMap.get(fileName);
	
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
					    uiChange(senderjid, offlineSenderjid, downloadedFile, rawID);

					    //_endtime = System.currentTimeMillis();
					    //updateonFinished(request, downloadedFile);
					}else
					{
//					    // 100 % = Filesize
//					    // x %   = Currentsize	    
//					    long p = (transfer.getAmountWritten() * 100 / transfer.getFileSize() );
//					    //progressBar.setValue(Math.round(p));        
//					    DebugUtil.debug("updateProgessBar: " +fileName+"- "+ p);
					}
					
				    }

					public void uiChange(String senderjid, final String offlineSenderjid, final File downloadedFile,
							String rawID) {
						if (senderjid.equals(Launcher.OFFLINEFILEROBOTBAREJID)){//机器人发来的文件
					    	reciveFile(downloadedFile.getPath(),rawID,offlineSenderjid);
					    }
					    else{//在线接收的文件
						    reciveFile(downloadedFile.getPath(),rawID,senderjid);
					    }
					}
				};	
				timer.scheduleAtFixedRate(updateProgessBar, 2000, 200);
		}
		};
	}
	
	
	private static void reciveFile(String reciveFilename, String fileId,String senderBareJid) {
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

	public static void sendOfflineFile(String fileFullPath, String reciveBareJid) {
		// TODO 和离线文件机器人交互
		String uuid = UUID.randomUUID().toString().replace("-", "");
		
		sendOfflineFileMsg(fileFullPath, reciveBareJid,uuid,false);
		sendOfflineFileMsg(fileFullPath, Launcher.OFFLINEFILEROBOTJID,uuid,true); //给机器人也发送一条，让其根据uuid创建文件目录
		//发送文件给机器人
		sendFile(fileFullPath,Launcher.OFFLINEFILEROBOTJID,"");
	}

	public static void sendOfflineFileMsg(String fileFullPath, String reciveBareJid,String uuid,boolean toRobot) {
		String fileType;
		String fileName = fileFullPath.substring(fileFullPath.lastIndexOf(System.getProperty("file.separator"))+1);

		boolean isImage;
		if (fileFullPath.lastIndexOf(".")<0){
			isImage = false;
		}else{
			String type = MimeTypeUtil.getMime(fileFullPath.substring(fileFullPath.lastIndexOf(".")));
			isImage = type.startsWith("image/");
		}
		
		if (isImage){
			fileType = "image";
		}
		else{
			fileType = "file";
		}
		
		OfflineFile of = null;
		OfflineFileRobot ofr = null; 
		
		if (toRobot){
			ofr = new OfflineFileRobot(UserCache.CurrentBareJid,reciveBareJid,fileType,uuid,fileName);
		}else{
			of = new OfflineFile(UserCache.CurrentBareJid,reciveBareJid,fileType,uuid,fileName);
		}
			
		
		Chat chat;
		try {
			chat = ChatManager.getInstanceFor(Launcher.connection).chatWith(JidCreate.entityBareFrom(reciveBareJid));
	        org.jivesoftware.smack.packet.Message message = new org.jivesoftware.smack.packet.Message();
	        message.setType(Type.chat);
	        
	        if (toRobot){
		        message.addExtension(ofr);	        	
	        }else{
		        message.addExtension(of);	        	
	        }

	        message.setBody(UserCache.CurrentUserRealName + " 发给您离线文件，即将接收："+fileName);
			chat.send(message);
			DebugUtil.debug( "send offlinefile msg:"+ message.toXML());	
			
		} catch (XmppStringprepException |NotConnectedException | InterruptedException e) {
			
		}
	}

	public static void requestOfflineFile(org.jivesoftware.smack.packet.Message message) {
		// TODO 向离线文件机器人发送请求
		// 将接收到的离线文件消息，原封不动的发给离线文件机器人 ，
		//	离线机器人接收后，会立即将已存储的文件发过来，同时会发送一条消息回执，发送成功后，删除文件
		//	用户收到回执后，需要对聊天UI进行处理，注意：此时的fromJid是机器人，应处理为实际发送者，即senderFullJid
		Chat chat;
		try {
			chat = ChatManager.getInstanceFor(Launcher.connection).chatWith(JidCreate.entityBareFrom(Launcher.OFFLINEFILEROBOTJID));
	        
			chat.send(message); //发送请求到机器人,然后机器人会把文件发给我
			OfflineFile of = (OfflineFile)message.getExtension(OfflineFile.NAMESPACE);
			offlineFileJidMap.put(of.getFileName(), of.getSenderFullJid());
			DebugUtil.debug( "send offlinefile request:"+ message.toXML());	
			
		} catch (XmppStringprepException |NotConnectedException | InterruptedException e) {
			
		}
	}
	
	
}
