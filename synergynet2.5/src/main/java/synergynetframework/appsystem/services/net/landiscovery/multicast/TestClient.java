/**
 * Adapted from http://www.developer.com/java/ent/article.php/3728576
 */

package synergynetframework.appsystem.services.net.landiscovery.multicast;

import java.io.IOException;
import java.util.Vector;

import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener;
import synergynetframework.appsystem.services.net.landiscovery.multicast.discoverer.ServiceBrowser;

public class TestClient implements ServiceDiscoveryListener {

	public static void main(String[] args) throws IOException {
		new TestClient();
	}

	ServiceBrowser browser;
	Vector<ServiceDescriptor> descriptors;
	
	TestClient() throws IOException {
		ServiceDiscoveryParams params = new ServiceDiscoveryParams();
		descriptors = new Vector<ServiceDescriptor>();
		browser = new ServiceBrowser(params);
		browser.registerListener(this);
		browser.registerServiceForListening("SynergyNet", "Some Name");
		browser.startListener();
		browser.start();
		System.out.println("Browser started. Will search for 2 secs.");
		try {
			Thread.sleep(2000);
		}catch (InterruptedException ie) {
			Thread.interrupted();
		}
		browser.stopLookup();
		browser.stopListener();
		
		if (descriptors.size()>0) {
			System.out.println("\n---SERVERS---");
			for (ServiceDescriptor descriptor : descriptors) {
				System.out.println(descriptor.getServiceName());
			}
		}
		else {
			System.out.println("\n---NO SERVERS FOUND---");
		}
		
		System.out.println("\nThat's all folks.");
		System.exit(0);
	}

	public void serviceAvailable(ServiceDescriptor descriptor) {
		int pos = descriptors.indexOf(descriptor);
		if (pos>-1) {
			descriptors.removeElementAt(pos);
		}
		descriptors.add(descriptor);		
	}

	public void serviceRemoved(ServiceDescriptor descriptor) {
		
		
	}

}
