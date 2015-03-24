package synergynetframework.appsystem.services.net.filestore;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;

/**
 * The Class FileStoreServer.
 */
public class FileStoreServer extends SynergyNetService implements Runnable {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(FileStoreServer.class
			.getName());

	/** The Constant SERVICE_NAME. */
	public static final String SERVICE_NAME = "filestoreserver";

	/** The Constant SERVICE_TYPE. */
	public static final String SERVICE_TYPE = "SynergyNet";

	/** The Constant TCP_PORT. */
	public static final int TCP_PORT = 4343;

	/** The base directory. */
	private File baseDirectory;

	/** The running. */
	private boolean running;

	/** The ss. */
	private ServerSocket ss;

	/** The thread pool. */
	protected ExecutorService threadPool;

	/** The thread pool size. */
	private int threadPoolSize = 10;
	
	/**
	 * Instantiates a new file store server.
	 */
	public FileStoreServer() {

	}

	/**
	 * Advertise service.
	 *
	 * @throws CouldNotStartServiceException
	 *             the could not start service exception
	 */
	private void advertiseService() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager
				.getInstance().get(NetworkServiceDiscoveryService.class);
		ServiceAnnounceSystem sa = nsds.getServiceAnnouncer();
		ServiceDescriptor s = new ServiceDescriptor();
		s.setServiceType(SERVICE_TYPE);
		s.setServiceName(SERVICE_NAME);
		try {
			s.setServiceAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			log.warning(e.toString());
		}
		s.setServicePort(TCP_PORT);
		s.setUserData(TableIdentity.getTableIdentity().toString());
		sa.registerService(s);
		log.info("Advertise file store service.");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.SynergyNetService#hasStarted()
	 */
	@Override
	public boolean hasStarted() {
		return running;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				Socket s = ss.accept();
				threadPool.execute(new FileReceptionHandler(baseDirectory, s));
			} catch (IOException e) {
				log.warning(e.toString());
				running = false;
			}
		}

	}
	
	/**
	 * Sets the directory.
	 *
	 * @param file
	 *            the new directory
	 */
	public void setDirectory(File file) {
		this.baseDirectory = file;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#shutdown()
	 */
	@Override
	public void shutdown() {
		try {
			running = false;
			ss.close();
			log.warning("File store server is closed.");
		} catch (IOException e) {
			log.warning(e.toString());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#start()
	 */
	@Override
	public void start() throws CouldNotStartServiceException {
		try {
			threadPool = Executors.newFixedThreadPool(threadPoolSize);
			ss = new ServerSocket(TCP_PORT);
			Thread t = new Thread(this);
			t.start();
			log.info("File store server is started.");
			advertiseService();
		} catch (IOException e) {
			log.warning(e.toString());
			throw new CouldNotStartServiceException(this);
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#stop()
	 */
	@Override
	public void stop() throws ServiceNotRunningException {
		shutdown();
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#update()
	 */
	@Override
	public void update() {

	}
	
}
