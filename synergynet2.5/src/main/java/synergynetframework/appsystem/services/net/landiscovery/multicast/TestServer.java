/**
 * Adapted from http://www.developer.com/java/ent/article.php/3728576
 */

package synergynetframework.appsystem.services.net.landiscovery.multicast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener;
import synergynetframework.appsystem.services.net.landiscovery.multicast.discoverer.ServiceBrowser;
import synergynetframework.appsystem.services.net.landiscovery.multicast.responder.ServiceResponder;

/**
 * The Class TestServer.
 */
public class TestServer implements ServiceDiscoveryListener {
	
	/** The responder. */
	private static ServiceResponder responder;
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		new TestServer();
	}
	
	/** The browser. */
	private ServiceBrowser browser;

	/** The descriptors. */
	List<ServiceDescriptor> descriptors = new ArrayList<ServiceDescriptor>();

	/**
	 * Instantiates a new test server.
	 *
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public TestServer() throws IOException {
		ServiceDiscoveryParams params = new ServiceDiscoveryParams();

		browser = new ServiceBrowser(params);
		browser.registerListener(this);
		browser.registerServiceForListening("SynergyNet", "Some Name");
		browser.startListener();
		browser.start();
		System.out.println("Browser started. Will search for 2 secs.");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
			Thread.interrupted();
		}
		browser.stopLookup();
		browser.stopListener();

		if (descriptors.size() > 0) {
			System.out.println("\n---SERVERS---");
			for (ServiceDescriptor descriptor : descriptors) {
				System.out.println(descriptor.getServiceName() + "> "
						+ descriptor.getServiceAddress() + ":"
						+ descriptor.getServicePort());
			}
			System.out.println("Server found, exiting.");
			System.exit(0);
		} else {
			System.out.println("Server not found, starting one.");
			
			ServiceDescriptor descriptor = new ServiceDescriptor();
			descriptor.setServiceType("SynergyNet");
			descriptor.setServiceAddress(InetAddress.getLocalHost());
			descriptor.setServicePort(1234);
			descriptor.setServiceName("Andy's SynergyNet Server");
			
			responder = new ServiceResponder(params);
			responder.registerService(descriptor);

			descriptor = new ServiceDescriptor();
			descriptor.setServiceType("SynergyNet");
			descriptor.setServiceName("random");
			responder.registerService(descriptor);

			responder.addShutdownHandler();
			responder.startResponder();

			ServerSocket ss = new ServerSocket(9489);
			ss.accept();
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.
	 * ServiceDiscoveryListener
	 * #serviceAvailable(synergynetframework.appsystem.services
	 * .net.landiscovery.ServiceDescriptor)
	 */
	public void serviceAvailable(ServiceDescriptor descriptor) {
		if (!descriptors.contains(descriptor)) {
			descriptors.add(descriptor);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.
	 * ServiceDiscoveryListener
	 * #serviceRemoved(synergynetframework.appsystem.services
	 * .net.landiscovery.ServiceDescriptor)
	 */
	public void serviceRemoved(ServiceDescriptor descriptor) {
		descriptors.remove(descriptor);
	}
	
	/**
	 * Service reply.
	 *
	 * @param descriptor
	 *            the descriptor
	 */
	public void serviceReply(ServiceDescription descriptor) {

	}
}
