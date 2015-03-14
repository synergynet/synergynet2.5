package apps.mtdesktop.fileutility;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import apps.mtdesktop.tabletop.fileserver.FtpServerServlet;


public class AssetRegistry {

	private static AssetRegistry registry = null;
	private Uploader uploader;
	private Downloader downloader;
	protected List<FileTransferListener> listeners = new ArrayList<FileTransferListener>();

	public static AssetRegistry getInstance(){
		if(registry == null)
			registry = new AssetRegistry();
		return registry;
	}
	
	private AssetRegistry(){
		uploader = new Uploader(this);
		downloader = new Downloader(this);

	}
	
	public boolean registerAsset(String ftpServletUrl, String assetId, String filePath) throws IOException{
		File file = new File(filePath);
		this.registerAsset(ftpServletUrl, assetId, file);
		return true;
	}
	
	public boolean registerAsset(String ftpServletUrl, String assetId, File file) throws IOException{
		if(file.length() > FtpServerServlet.MAX_FILE_SIZE) throw new IOException("Unable to upload file larger than 5MB");
		uploader.uploadFile(ftpServletUrl, assetId, file, "");
		return true;
	}
	
	public boolean getAsset(String siteUrl, String assetName, String destinationPath) throws MalformedURLException, IOException{
		downloader.downloadFile(siteUrl + "/" + assetName, destinationPath);
		return true;
	}
	
	public boolean getAssets(String destinationPath){
		return false;
	}
	
	public boolean isRegistered(String assetName){
		return false;
	}
	
	public boolean unregisterAsset(String assetName){
		return false;
	}
	
	public void unregisterAssets(){
		
	}
	
	public Uploader getUploader(){
		return uploader;
	}
	
	public void addFileTransferListener(FileTransferListener listener){
		if(!listeners.contains(listener)) 
			listeners.add(listener);
	}
	
	public void removeFileTransferListener(FileTransferListener listener){
		listeners.remove(listener);
	}
	
	public void removeAllFileTransferListeners(){
		listeners.clear();
	}

}
