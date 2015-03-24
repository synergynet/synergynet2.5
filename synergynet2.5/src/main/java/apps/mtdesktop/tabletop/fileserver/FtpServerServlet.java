package apps.mtdesktop.tabletop.fileserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mtdesktop.MTDesktopConfigurations;
import apps.mtdesktop.fileutility.Uploader;

/**
 * The Class FtpServerServlet.
 */
public class FtpServerServlet extends HttpServlet {
	
	/**
	 * The listener interface for receiving ftpServlet events. The class that is
	 * interested in processing a ftpServlet event implements this interface,
	 * and the object created with that class is registered with a component
	 * using the component's <code>addFtpServletListener<code> method. When
	 * the ftpServlet event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see FtpServletEvent
	 */
	public interface FtpServletListener {

		/**
		 * File downloaded.
		 *
		 * @param file
		 *            the file
		 */
		public void fileDownloaded(File file);

		/**
		 * File uploaded.
		 *
		 * @param peerId
		 *            the peer id
		 * @param assetId
		 *            the asset id
		 * @param file
		 *            the file
		 */
		public void fileUploaded(TableIdentity peerId, String assetId, File file);
	}
	
	/** The Constant BUFFER_SIZE. */
	public static final int BUFFER_SIZE = 5 * 1024;

	/** The Constant MAX_FILE_SIZE. */
	public static final int MAX_FILE_SIZE = 5 * 1024 * 1024;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6389374451513338270L;

	/** The listeners. */
	private List<FtpServletListener> listeners = new ArrayList<FtpServletListener>();
	
	/**
	 * Adds the ftp servlet listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addFtpServletListener(FtpServletListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPut(req, resp);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doDelete(req, resp);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String fileName = req.getHeader(Uploader.FILE_NAME_HEADER);
		if ((fileName == null) && !resp.isCommitted()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Filename not specified");
			return;
		}

		String destinationPath = req
				.getHeader(Uploader.DESTINATION_PATH_HEADER);
		if ((destinationPath == null) && !resp.isCommitted()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Destination path not specified");
			return;
		}
		
		String clientID = req.getHeader(Uploader.CLIENT_ID_HEADER);
		String assetId = req.getHeader(Uploader.ASSET_ID);
		
		if (((clientID == null) || (assetId == null)) && !resp.isCommitted()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Missing Client ID");
			return;
		}
		long fileSize = 0;
		String str_file_size = req.getHeader(Uploader.FILE_SIZE);
		if (str_file_size != null) {
			fileSize = Long.parseLong(str_file_size);
		}

		if ((fileSize > MAX_FILE_SIZE) && !resp.isCommitted()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Oversize file");
			return;
		}
		/*
		 * if(new File(WebServer.jettyDir + "/webapps/" +
		 * destinationPath).exists()) {
		 * resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
		 * "File exists"); return; }
		 */
		int nChunks = req.getIntHeader(Uploader.FILE_CHUNK_COUNT_HEADER);
		int chunk = req.getIntHeader(Uploader.FILE_CHUNK_HEADER);

		if (((nChunks == -1) || (chunk == -1)) && !resp.isCommitted()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Missing chunk information");
			return;
		}
		
		if (chunk == 0) {
			// check permission to create file here
		}
		String locDesPath = MTDesktopConfigurations.OutboxFolder + "/"
				+ destinationPath;
		File file = null;

		String tempFileName = String.valueOf((long) (Long.MIN_VALUE * Math
				.random()));

		if (new File(locDesPath).exists() || new File(locDesPath).mkdirs()) {
			OutputStream out = null;
			if (nChunks == 1) {
				String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
				file = new File(locDesPath + "/" + UUID.randomUUID().toString()
						+ "." + ext);
				out = new FileOutputStream(file);
			} else {
				file = new File(getTempFile(tempFileName));
				out = new FileOutputStream(file, (chunk > 0));
			}
			
			InputStream in = req.getInputStream();
			byte[] buf = new byte[BUFFER_SIZE];
			while (true) {
				int read = in.read(buf);
				if (read == -1) {
					break;
				} else if (read > 0) {
					out.write(buf, 0, read);
				}
			}
			in.close();
			out.close();
		}
		
		TableIdentity tableId = new TableIdentity(clientID);
		
		if ((nChunks > 1) && (chunk == (nChunks - 1))) {
			File tmpFile = new File(getTempFile(tempFileName));
			File destFile = new File(locDesPath + "/" + fileName);
			if (destFile.exists()) {
				destFile.delete();
			}
			if (!resp.isCommitted()) {
				if (!tmpFile.renameTo(destFile)) {
					resp.sendError(
							HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Unable to create file");
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					for (FtpServletListener listener : listeners) {
						listener.fileUploaded(tableId, assetId, destFile);
					}
				}
			}
		} else if (!resp.isCommitted()) {
			resp.setStatus(HttpServletResponse.SC_OK);
			for (FtpServletListener listener : listeners) {
				listener.fileUploaded(tableId, assetId, file);
			}
		}
		
	}
	
	/**
	 * Gets the temp file.
	 *
	 * @param tempFileName
	 *            the temp file name
	 * @return the temp file
	 */
	private String getTempFile(String tempFileName) {
		return "c://temp/" + tempFileName + ".tmp";
	}
}