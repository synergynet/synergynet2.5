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

public class FileStoreServer extends SynergyNetService implements Runnable {
	
	private static final Logger log = Logger.getLogger(FileStoreServer.class.getName());
	public static final int TCP_PORT = 4343;
	public static final String SERVICE_TYPE = "SynergyNet";
	public static final String SERVICE_NAME = "filestoreserver";
	
	private ServerSocket ss;
	protected ExecutorService threadPool;
	private int threadPoolSize = 10;
	private File baseDirectory;
	private boolean running;

	public FileStoreServer() {
		
	}
	
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
	
	@Override
	public void run() {
		running = true;
		while(running) {
			try {
				Socket s = ss.accept();
				threadPool.execute(new FileReceptionHandler(baseDirectory, s));
			} catch (IOException e) {
				log.warning(e.toString());
				running = false;
			}
		}
		
	}
	
	public void setDirectory(File file) {
		this.baseDirectory = file;
	}

	@Override
	public boolean hasStarted() {
		return running;
	}

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

	@Override
	public void stop() throws ServiceNotRunningException {
		shutdown();		
	}

	@Override
	public void update() {
		
	}


	private void advertiseService() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);				
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

}
