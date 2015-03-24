package apps.mtdesktop.fileutility;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

/**
 * The Class Test.
 */
public class Test implements FileTransferListener {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[]) {
		new Test();
	}

	/**
	 * Instantiates a new test.
	 */
	public Test() {
		try {
			String ftpServlet = "http://"
					+ InetAddress.getLocalHost().getHostAddress()
					+ ":8080/ftpServlet";
			
			AssetRegistry.getInstance().addFileTransferListener(this);
			AssetRegistry.getInstance().registerAsset(ftpServlet,
					UUID.randomUUID().toString(), "C:\\123\\vvv.mpg");
			// AssetRegistry.getInstance().getAsset("http://"+InetAddress.getLocalHost().getHostAddress()+":8080/FileServer",
			// "vvv.mpg", "C:/1");
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileDownloadCompleted
	 * (java.io.File)
	 */
	@Override
	public void fileDownloadCompleted(File file) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileDownloadFailed(java
	 * .io.File, java.lang.String)
	 */
	@Override
	public void fileDownloadFailed(File file, String responseMessage) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileDownloadStarted(java
	 * .io.File)
	 */
	@Override
	public void fileDownloadStarted(File file) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileUploadCompleted(java
	 * .io.File)
	 */
	@Override
	public void fileUploadCompleted(File file) {
		System.out.println("upload complete");
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileUploadFailed(java
	 * .io.File, java.lang.String)
	 */
	@Override
	public void fileUploadFailed(File file, String responseMessage) {
		System.out.println("upload failed");

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.mtdesktop.fileutility.FileTransferListener#fileUploadStarted(java
	 * .io.File)
	 */
	@Override
	public void fileUploadStarted(File file) {
		System.out.println("upload started");
	}
}
