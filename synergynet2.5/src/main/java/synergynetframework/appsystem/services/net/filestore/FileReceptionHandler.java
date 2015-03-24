package synergynetframework.appsystem.services.net.filestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.net.filestore.messages.EndFileTransfer;
import synergynetframework.appsystem.services.net.filestore.messages.FilePart;
import synergynetframework.appsystem.services.net.filestore.messages.FileTransferComplete;
import synergynetframework.appsystem.services.net.filestore.messages.StartFileTransfer;

/**
 * The Class FileReceptionHandler.
 */
public class FileReceptionHandler implements Runnable {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(FileReceptionHandler.class.getName());

	/** The ois. */
	private ObjectInputStream ois;

	/** The oos. */
	private ObjectOutputStream oos;

	/** The target dir. */
	private File targetDir;
	
	/**
	 * Instantiates a new file reception handler.
	 *
	 * @param targetDirectory
	 *            the target directory
	 * @param s
	 *            the s
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public FileReceptionHandler(File targetDirectory, Socket s)
			throws IOException {
		this.targetDir = targetDirectory;
		this.oos = new ObjectOutputStream(s.getOutputStream());
		this.ois = new ObjectInputStream(s.getInputStream());
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		boolean running = true;
		File currentFile = null;
		FileOutputStream fos = null;
		while (running) {
			try {
				Object obj = ois.readObject();
				if (obj instanceof FilePart) {
					FilePart fp = (FilePart) obj;
					writeFilePart(fp, fos);
				} else if (obj instanceof StartFileTransfer) {
					log.info("File transfer started.");
					StartFileTransfer stf = (StartFileTransfer) obj;
					String filename = stf.getMD5();
					currentFile = new File(targetDir, filename);
					fos = new FileOutputStream(currentFile);
				} else if (obj instanceof EndFileTransfer) {
					fos.close();
					oos.writeObject(new FileTransferComplete(currentFile
							.getName()));
					running = false;
					log.info("File transfer ended.");
				}
			} catch (IOException e) {
				log.warning(e.toString());
			} catch (ClassNotFoundException e) {
				log.warning(e.toString());
			}
		}
		
	}
	
	/**
	 * Write file part.
	 *
	 * @param fp
	 *            the fp
	 * @param fos
	 *            the fos
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeFilePart(FilePart fp, FileOutputStream fos)
			throws IOException {
		fos.write(fp.getByteBuffer(), 0, fp.getLen());
		fos.flush();
	}
	
}
