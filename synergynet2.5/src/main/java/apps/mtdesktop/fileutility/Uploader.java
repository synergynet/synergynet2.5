package apps.mtdesktop.fileutility;
import java.io.*;
import java.net.*;

import javax.servlet.http.HttpServletResponse;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class Uploader
{
	
	public static final int BUFFER_SIZE = 1024*5;

	public static final int MAX_CHUNK_SIZE = 1024 * BUFFER_SIZE; //~4.1MB

	public static final String FILE_NAME_HEADER = "Transfer-File-Name";
	
	public static final String DESTINATION_PATH_HEADER = "Destination-Path";

	public static final String CLIENT_ID_HEADER = "Transfer-Client-ID";

	public static final String FILE_CHUNK_HEADER = "Transfer-File-Chunk";

	public static final String FILE_CHUNK_COUNT_HEADER = "Transfer-File-Chunk-Count";
	
	public static final String FILE_SIZE = "Transfer-File-Size";
	
	public static final String ASSET_ID = "Asset-Id";
	
	private AssetRegistry assetRegistry;
	
	public Uploader(AssetRegistry assetRegistry) {
		this.assetRegistry = assetRegistry;
	}

	public void uploadFile(String ftpServletUrl, String assetId, File file, String destinationPath){
		try{
			URL url = new URL(ftpServletUrl);
			
			long fileSize = file.length();
			if(fileSize> MAX_CHUNK_SIZE) throw new IOException("Unable to upload files larger than 5 MBytes");
			FileInputStream in = new FileInputStream(file);
	
			for(FileTransferListener listener: assetRegistry.listeners)
				listener.fileUploadStarted(file);
			
			int nChunks = (int)(fileSize/MAX_CHUNK_SIZE);
			if(fileSize % MAX_CHUNK_SIZE > 0){
				nChunks++;
			}
	
			byte[] buf = new byte[BUFFER_SIZE];
			long bytesRemaining = fileSize;
	
			String clientID = TableIdentity.getTableIdentity().toString();
	
			for (int i=0; i<nChunks; i++) {
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("PUT");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setUseCaches (false);
	
				int chunkSize = (int)((bytesRemaining > MAX_CHUNK_SIZE) ?  MAX_CHUNK_SIZE : bytesRemaining);
				bytesRemaining -= chunkSize;
				conn.setRequestProperty("Content-Type", "application/octet-stream");
				conn.setRequestProperty("Content-Length", String.valueOf(chunkSize));
				conn.setRequestProperty(FILE_SIZE, String.valueOf(fileSize));
				conn.setRequestProperty(CLIENT_ID_HEADER, clientID);
				conn.setRequestProperty(FILE_NAME_HEADER, file.getName());
				conn.setRequestProperty(DESTINATION_PATH_HEADER, destinationPath);
				conn.setRequestProperty(FILE_CHUNK_COUNT_HEADER, String.valueOf(nChunks));
				conn.setRequestProperty(FILE_CHUNK_HEADER, String.valueOf(i));
				conn.setRequestProperty(ASSET_ID, assetId);
				
				OutputStream out = conn.getOutputStream();
				int bytesRead = 0;
				while(bytesRead < chunkSize)
				{
					int read = in.read(buf);
					if(read == -1){
						break;
					}
					else if(read > 0){
						bytesRead += read;
						out.write(buf, 0, read);
					}
				}
				in.close();
				out.close();
				conn.disconnect();

				if(conn.getResponseCode() != HttpServletResponse.SC_OK){
	
					for(FileTransferListener listener: assetRegistry.listeners)
						listener.fileUploadFailed(file, conn.getResponseMessage());
				}
				else{
					for(FileTransferListener listener: assetRegistry.listeners)
						listener.fileUploadCompleted(file);
				}
			}
		}catch(IOException ioexp){
			for(FileTransferListener listener: assetRegistry.listeners)
				listener.fileUploadFailed(file, ioexp.getMessage());
		}
	}

	public void deleteFile(String fileUrl) throws IOException, URISyntaxException{
		
		URL url = new URL(fileUrl);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("POST");
	}

}
