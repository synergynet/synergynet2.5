package synergynetframework.appsystem.services.net.networkcontent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;


import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.filestore.FileStoreClient;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.peer.ServerStatusMonitor;
import synergynetframework.appsystem.services.net.webserver.WebServerService;


/**
 * The Class NetworkContentClientService.
 */
public class NetworkContentClientService extends SynergyNetService {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(NetworkContentClientService.class.getName());
	
	/** The fsc. */
	private FileStoreClient fsc;

	/** The base url. */
	private String baseURL;

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#hasStarted()
	 */
	@Override
	public boolean hasStarted() {
		return false;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#shutdown()
	 */
	@Override
	public void shutdown() {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#start()
	 */
	@Override
	public void start() throws CouldNotStartServiceException {
		ServiceManager services = ServiceManager.getInstance();
		
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
		if(!nsds.hasStarted()) nsds.start();
		ServiceDiscoverySystem serviceDiscovery = nsds.getServiceDiscovery();
		ServerStatusMonitor smon = new ServerStatusMonitor(WebServerService.SERVICE_TYPE, WebServerService.SERVICE_NAME, 3000);
		serviceDiscovery.registerListener(smon);
		serviceDiscovery.registerServiceForListening(WebServerService.SERVICE_TYPE, WebServerService.SERVICE_NAME);

		try {
			smon.connect();
			boolean serverFound = smon.serverFound();
			if(!serverFound) {
				log.severe("Could not find web server!");
			}else{
				ServiceDescriptor found = smon.getServerServiceDescriptor();
				baseURL = found.getUserData();				
				log.info("Found web server at: " + baseURL);
				fsc = (FileStoreClient) services.get(FileStoreClient.class);				
				fsc.start();
				log.info("NetworkContentClient service started");
			}
		} catch (InterruptedException e) {
			log.warning(e.toString());
		}
		
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#stop()
	 */
	@Override
	public void stop() throws ServiceNotRunningException {
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#update()
	 */
	@Override
	public void update() {
		
	}
	
	/**
	 * Publish file.
	 *
	 * @param f the f
	 * @return the url
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public URL publishFile(File f) throws IOException {
		String path = fsc.sendFile(f);
		return new URL(baseURL + "/" + path);
	}
}
