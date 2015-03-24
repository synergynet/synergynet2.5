package apps.mtdesktop.fileutility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * The Class Downloader.
 */
public class Downloader {
	
	/** The asset registry. */
	private AssetRegistry assetRegistry;
	
	/**
	 * Instantiates a new downloader.
	 *
	 * @param assetRegistry
	 *            the asset registry
	 */
	public Downloader(AssetRegistry assetRegistry) {
		this.assetRegistry = assetRegistry;
	}
	
	/**
	 * Download file.
	 *
	 * @param fileUrl
	 *            the file url
	 * @param destinationDir
	 *            the destination dir
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void downloadFile(String fileUrl, String destinationDir)
			throws MalformedURLException, IOException {
		boolean downloadfFailed = false;
		String responseMessage = null;
		if ((fileUrl != null) && (destinationDir != null)) {
			String filePath = destinationDir + "/"
					+ fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			for (FileTransferListener listener : assetRegistry.listeners) {
				listener.fileDownloadStarted(new File(filePath));
			}
			
			BufferedInputStream bis = null;
			BufferedOutputStream bos = null;
			try {
				URL url = new URL(fileUrl.toString());
				URLConnection urlc = url.openConnection();
				urlc.connect();
				bis = new BufferedInputStream(urlc.getInputStream());
				bos = new BufferedOutputStream(new FileOutputStream(filePath));
				int i;
				while ((i = bis.read()) != -1) {
					bos.write(i);
				}
				bos.flush();
			} catch (Exception exp) {
				downloadfFailed = true;
				responseMessage = exp.getMessage();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
						downloadfFailed = true;
						responseMessage = ioe.getMessage();
					}
				}
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
						downloadfFailed = true;
						responseMessage = ioe.getMessage();
					}
				}
				if (downloadfFailed) {
					for (FileTransferListener listener : assetRegistry.listeners) {
						listener.fileDownloadFailed(new File(filePath),
								responseMessage);
					}
				} else {
					for (FileTransferListener listener : assetRegistry.listeners) {
						listener.fileDownloadCompleted(new File(filePath));
					}
				}
			}
		} else {
			System.out.println("Input not available");
		}
	}
}
