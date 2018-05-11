package xysoft.im.file;

import java.io.File;

import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import xysoft.im.app.Launcher;

public class IbbFile {

	// XEP-0047: In-Band Bytestreams ibb支持

	private InBandBytestreamManager ibbmanager;

	public IbbFile() {
		// TODO Auto-generated constructor stub

		ibbmanager = InBandBytestreamManager.getByteStreamManager(Launcher.connection);
		ibbmanager.setDefaultBlockSize(61440);

		final FileTransferManager manager = FileTransferManager.getInstanceFor(Launcher.connection);
		// Create the listener
		//manager.addFileTransferListener(li);
	}

}
