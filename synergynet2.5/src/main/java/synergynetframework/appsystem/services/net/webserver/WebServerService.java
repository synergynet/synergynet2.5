package synergynetframework.appsystem.services.net.webserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.config.server.ServerConfigPrefsItem;


/**
 * The Class WebServerService.
 */
public class WebServerService extends SynergyNetService {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(WebServerService.class.getName());
	
	/** The Constant SERVICE_TYPE. */
	public static final String SERVICE_TYPE = "SynergyNet";
	
	/** The Constant SERVICE_NAME. */
	public static final String SERVICE_NAME = "webserver";
	
	/** The server. */
	private Server server;
	
	/** The directory. */
	private String directory;
	
	/** The resource_handler. */
	private ResourceHandler resource_handler = new ResourceHandler();
	
	/** The port. */
	private int port;

	/**
	 * Instantiates a new web server service.
	 */
	public WebServerService() {
		ServerConfigPrefsItem serverConfig = new ServerConfigPrefsItem();
		this.directory = serverConfig.getWebDirectory();
		this.port = serverConfig.getPort();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#hasStarted()
	 */
	@Override
	public boolean hasStarted() {
		return server.isRunning();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#shutdown()
	 */
	@Override
	public void shutdown() {
		try {
			stop();
		} catch (ServiceNotRunningException e) {
			log.warning(e.toString());
		}		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#start()
	 */
	@Override
	public void start() throws CouldNotStartServiceException {
        server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        server.addConnector(connector);
 
        resource_handler.setDirectoriesListed(true);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
 
        resource_handler.setResourceBase(this.directory);
 
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler, new DefaultHandler() });
        server.setHandler(handlers);
 
        try {
			server.start();
			log.info("WebServerService server started.");
			advertiseService();
		} catch (Exception e) {
			throw new CouldNotStartServiceException(this);
		}		
	}
	
	/**
	 * Advertise service.
	 *
	 * @throws CouldNotStartServiceException the could not start service exception
	 */
	private void advertiseService() throws CouldNotStartServiceException {
		NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);				
		ServiceAnnounceSystem sa = nsds.getServiceAnnouncer();
		ServiceDescriptor s = new ServiceDescriptor();
		s.setServiceType(SERVICE_TYPE);
		s.setServiceName(SERVICE_NAME);
		try {
			s.setServiceAddress(InetAddress.getLocalHost());
			s.setUserData("http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		s.setServicePort(port);		
		sa.registerService(s);
		log.info("WebServerService advertised.");
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#stop()
	 */
	@Override
	public void stop() throws ServiceNotRunningException {
		try {
			server.stop();
			log.info("WebServerService server stopped");
		} catch (Exception e) {
			log.warning(e.toString());
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#update()
	 */
	@Override
	public void update() {}

}
