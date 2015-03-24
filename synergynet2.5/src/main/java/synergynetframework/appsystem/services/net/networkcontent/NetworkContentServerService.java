package synergynetframework.appsystem.services.net.networkcontent;

import java.io.File;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.filestore.FileStoreServer;
import synergynetframework.config.server.ServerConfigPrefsItem;

/**
 * The Class NetworkContentServerService.
 */
public class NetworkContentServerService extends SynergyNetService {
	
	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(NetworkContentServerService.class.getName());
	// private WebServerService wss;
	/** The fss. */
	private FileStoreServer fss;
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.SynergyNetService#hasStarted()
	 */
	@Override
	public boolean hasStarted() {
		return /* wss != null || */fss != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#shutdown()
	 */
	@Override
	public void shutdown() {
		try {
			stop();
		} catch (ServiceNotRunningException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#start()
	 */
	@Override
	public void start() throws CouldNotStartServiceException {
		ServiceManager services = ServiceManager.getInstance();
		ServerConfigPrefsItem srvConfig = new ServerConfigPrefsItem();

		String fileStoreDirectory = srvConfig.getWebDirectory();
		fss = (FileStoreServer) services.get(FileStoreServer.class);
		fss.setDirectory(new File(fileStoreDirectory));

		// wss = (WebServerService) services.get(WebServerService.class);
		log.info("NetworkContentServer service started");
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#stop()
	 */
	@Override
	public void stop() throws ServiceNotRunningException {
		// wss.stop();
		fss.stop();
		log.info("NetworkContentServer service stopped");
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#update()
	 */
	@Override
	public void update() {
	}
	
}
