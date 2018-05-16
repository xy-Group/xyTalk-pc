package xysoft.im.service;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import xysoft.im.app.Launcher;
import xysoft.im.utils.DebugUtil;

public class XmppFileService {

	public XmppFileService() {
		// TODO Auto-generated constructor stub
	}

	//接收文件
	public static FileTransferListener fileListener() {
		return new FileTransferListener() {
			public void fileTransferRequest(FileTransferRequest request) {

				IncomingFileTransfer transfer = request.accept();
				String fileName = transfer.getFileName();
				String senderjid = transfer.getPeer().asEntityFullJidIfPossible().toString();
				
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
	
	
}
