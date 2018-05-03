package xysoft.im.file;

import org.jivesoftware.smackx.filetransfer.FileTransferRequest;

public interface FileTransferListener {

    boolean handleTransfer(FileTransferRequest request);

}