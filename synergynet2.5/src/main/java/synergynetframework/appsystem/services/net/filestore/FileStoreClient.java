package synergynetframework.appsystem.services.net.filestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.filestore.messages.EndFileTransfer;
import synergynetframework.appsystem.services.net.filestore.messages.FilePart;
import synergynetframework.appsystem.services.net.filestore.messages.FileTransferComplete;
import synergynetframework.appsystem.services.net.filestore.messages.StartFileTransfer;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.peer.ServerStatusMonitor;
import synergynetframework.utils.crypto.CryptoUtils;

/**
 * The Class FileStoreClient.
 */
public class FileStoreClient extends SynergyNetService implements Runnable {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(FileStoreClient.class
			.getName());

	/** The address. */
	private InetAddress address;
	
	/** The socket. */
	private Socket socket;
	
	/**
	 * Instantiates a new file store client.
	 */
	public FileStoreClient() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.SynergyNetService#hasStarted()
	 */
	@Override
	public boolean hasStarted() {
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
	}
	
	/**
	 * Send file.
	 *
	 * @param file
	 *            the file
	 * @return the string
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public String sendFile(File file) throws IOException {
		socket = new Socket(address, FileStoreServer.TCP_PORT);
		ObjectOutputStream oos = new ObjectOutputStream(
				socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		try {
			StartFileTransfer sft = new StartFileTransfer();
			sft.setFileName(file.getName());
			String md5 = CryptoUtils.md5(file);
			sft.setMD5(md5);
			oos.writeObject(sft);

			byte[] buf = new byte[8 * 1024];
			FileInputStream fis = new FileInputStream(file);
			int read;
			while ((read = fis.read(buf)) != -1) {
				FilePart fp = new FilePart();
				fp.setBytes(buf, read);
				oos.writeObject(fp);
			}
			oos.writeObject(new EndFileTransfer());

			try {
				FileTransferComplete ftc = (FileTransferComplete) ois
						.readObject();
				if (ftc != null) {
					log.info("File transfer completed.");
				}
				socket.close();
				log.info("Disconnected from file store server");
			} catch (ClassNotFoundException e) {
				log.warning(e.toString());
			}
			fis.close();

			return md5;
		} catch (NoSuchAlgorithmException e) {
			log.warning(e.toString());
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#shutdown()
	 */
	@Override
	public void shutdown() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#start()
	 */
	public void start() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager
				.getInstance().get(NetworkServiceDiscoveryService.class);
		ServiceDiscoverySystem serviceDiscovery = nsds.getServiceDiscovery();
		ServerStatusMonitor smon = new ServerStatusMonitor(
				FileStoreServer.SERVICE_TYPE, FileStoreServer.SERVICE_NAME,
				3000);
		serviceDiscovery.registerListener(smon);
		serviceDiscovery.registerServiceForListening(
				FileStoreServer.SERVICE_TYPE, FileStoreServer.SERVICE_NAME);
		
		try {
			smon.connect();
			boolean serverFound = smon.serverFound();
			if (!serverFound) {
				log.severe("Could not find file store server!");
			} else {
				ServiceDescriptor found = smon.getServerServiceDescriptor();
				address = found.getServiceAddress();

				Thread t = new Thread(this);
				t.start();
				log.info("Ready.");
			}
		} catch (InterruptedException e) {
			log.warning(e.toString());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#stop()
	 */
	@Override
	public void stop() throws ServiceNotRunningException {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#update()
	 */
	@Override
	public void update() {
		
	}
	
}
