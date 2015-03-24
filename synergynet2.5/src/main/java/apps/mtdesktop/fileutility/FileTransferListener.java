package apps.mtdesktop.fileutility;

import java.io.File;


/**
 * The listener interface for receiving fileTransfer events.
 * The class that is interested in processing a fileTransfer
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addFileTransferListener<code> method. When
 * the fileTransfer event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FileTransferEvent
 */
public interface FileTransferListener {
	
	/**
	 * File upload started.
	 *
	 * @param file the file
	 */
	public void fileUploadStarted(File file);
	
	/**
	 * File upload completed.
	 *
	 * @param file the file
	 */
	public void fileUploadCompleted(File file);
	
	/**
	 * File download started.
	 *
	 * @param file the file
	 */
	public void fileDownloadStarted(File file);
	
	/**
	 * File download completed.
	 *
	 * @param file the file
	 */
	public void fileDownloadCompleted(File file);
	
	/**
	 * File upload failed.
	 *
	 * @param file the file
	 * @param responseMessage the response message
	 */
	public void fileUploadFailed(File file, String responseMessage);
	
	/**
	 * File download failed.
	 *
	 * @param file the file
	 * @param responseMessage the response message
	 */
	public void fileDownloadFailed(File file, String responseMessage);
}
