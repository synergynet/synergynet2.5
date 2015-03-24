package apps.mtdesktop.fileutility;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import apps.mtdesktop.tabletop.fileserver.FtpServerServlet;

/**
 * The Class AssetRegistry.
 */
public class AssetRegistry {
	
	/** The registry. */
	private static AssetRegistry registry = null;

	/**
	 * Gets the single instance of AssetRegistry.
	 *
	 * @return single instance of AssetRegistry
	 */
	public static AssetRegistry getInstance() {
		if (registry == null) {
			registry = new AssetRegistry();
		}
		return registry;
	}

	/** The downloader. */
	private Downloader downloader;

	/** The listeners. */
	protected List<FileTransferListener> listeners = new ArrayList<FileTransferListener>();
	
	/** The uploader. */
	private Uploader uploader;

	/**
	 * Instantiates a new asset registry.
	 */
	private AssetRegistry() {
		uploader = new Uploader(this);
		downloader = new Downloader(this);
		
	}

	/**
	 * Adds the file transfer listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addFileTransferListener(FileTransferListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Gets the asset.
	 *
	 * @param siteUrl
	 *            the site url
	 * @param assetName
	 *            the asset name
	 * @param destinationPath
	 *            the destination path
	 * @return the asset
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean getAsset(String siteUrl, String assetName,
			String destinationPath) throws MalformedURLException, IOException {
		downloader.downloadFile(siteUrl + "/" + assetName, destinationPath);
		return true;
	}

	/**
	 * Gets the assets.
	 *
	 * @param destinationPath
	 *            the destination path
	 * @return the assets
	 */
	public boolean getAssets(String destinationPath) {
		return false;
	}

	/**
	 * Gets the uploader.
	 *
	 * @return the uploader
	 */
	public Uploader getUploader() {
		return uploader;
	}

	/**
	 * Checks if is registered.
	 *
	 * @param assetName
	 *            the asset name
	 * @return true, if is registered
	 */
	public boolean isRegistered(String assetName) {
		return false;
	}

	/**
	 * Register asset.
	 *
	 * @param ftpServletUrl
	 *            the ftp servlet url
	 * @param assetId
	 *            the asset id
	 * @param file
	 *            the file
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean registerAsset(String ftpServletUrl, String assetId, File file)
			throws IOException {
		if (file.length() > FtpServerServlet.MAX_FILE_SIZE) {
			throw new IOException("Unable to upload file larger than 5MB");
		}
		uploader.uploadFile(ftpServletUrl, assetId, file, "");
		return true;
	}

	/**
	 * Register asset.
	 *
	 * @param ftpServletUrl
	 *            the ftp servlet url
	 * @param assetId
	 *            the asset id
	 * @param filePath
	 *            the file path
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean registerAsset(String ftpServletUrl, String assetId,
			String filePath) throws IOException {
		File file = new File(filePath);
		this.registerAsset(ftpServletUrl, assetId, file);
		return true;
	}

	/**
	 * Removes the all file transfer listeners.
	 */
	public void removeAllFileTransferListeners() {
		listeners.clear();
	}

	/**
	 * Removes the file transfer listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeFileTransferListener(FileTransferListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Unregister asset.
	 *
	 * @param assetName
	 *            the asset name
	 * @return true, if successful
	 */
	public boolean unregisterAsset(String assetName) {
		return false;
	}

	/**
	 * Unregister assets.
	 */
	public void unregisterAssets() {

	}
	
}
