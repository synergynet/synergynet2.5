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

public class FileReceptionHandler implements Runnable {
	private static final Logger log = Logger.getLogger(FileReceptionHandler.class.getName());
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private File targetDir;

	public FileReceptionHandler(File targetDirectory, Socket s) throws IOException {
		this.targetDir = targetDirectory;
		this.oos = new ObjectOutputStream(s.getOutputStream());
		this.ois = new ObjectInputStream(s.getInputStream());		
	}

	@Override
	public void run() {
		boolean running = true;
		File currentFile = null;
		FileOutputStream fos = null;
		while(running) {
			try {
				Object obj = ois.readObject();
				if(obj instanceof FilePart) {
					FilePart fp = (FilePart) obj;
					writeFilePart(fp, fos);
				}else if(obj instanceof StartFileTransfer) {
					log.info("File transfer started.");
					StartFileTransfer stf = (StartFileTransfer) obj;
					String filename = stf.getMD5();
					currentFile = new File(targetDir, filename);
					fos = new FileOutputStream(currentFile);
				}else if(obj instanceof EndFileTransfer) {						
					fos.close();
					oos.writeObject(new FileTransferComplete(currentFile.getName()));
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

	private void writeFilePart(FilePart fp, FileOutputStream fos) throws IOException {
		fos.write(fp.getByteBuffer(), 0, fp.getLen());
		fos.flush();
	}

}
