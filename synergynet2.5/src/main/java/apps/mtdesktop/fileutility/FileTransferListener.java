package apps.mtdesktop.fileutility;

import java.io.File;

public interface FileTransferListener {
	public void fileUploadStarted(File file);
	public void fileUploadCompleted(File file);
	public void fileDownloadStarted(File file);
	public void fileDownloadCompleted(File file);
	
	public void fileUploadFailed(File file, String responseMessage);
	public void fileDownloadFailed(File file, String responseMessage);
}
