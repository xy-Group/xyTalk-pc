package xysoft.im.file;

import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;

import xysoft.im.app.Launcher;


public class IbbFile {
	
	//XEP-0047: In-Band Bytestreams ibb支持

    private InBandBytestreamManager ibbmanager;
	public IbbFile() {
		// TODO Auto-generated constructor stub
		
        ibbmanager = InBandBytestreamManager.getByteStreamManager( Launcher.connection);
        ibbmanager.setDefaultBlockSize(61440);
	}

}
