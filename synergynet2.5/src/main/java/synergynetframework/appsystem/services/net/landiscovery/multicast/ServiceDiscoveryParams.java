/**
 * Adapted from http://www.developer.com/java/ent/article.php/3728576
 */

package synergynetframework.appsystem.services.net.landiscovery.multicast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServiceDiscoveryParams {

	private InetAddress multicastGroup;
	private int multicastPort;
	private int datagramLength;
	
	public ServiceDiscoveryParams() throws UnknownHostException {
		setMulticastGroup(InetAddress.getByName("239.255.31.14")); // random from: 239.255.000.000-239.255.255.255 Site-Local Scope [RFC2365]
		setMulticastPort(8261);
		setDatagramLength(1024);
	}

	public void setMulticastGroup(InetAddress multicastGroup) {
		this.multicastGroup = multicastGroup;
	}

	public InetAddress getMulticastGroup() {
		return multicastGroup;
	}

	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	public int getMulticastPort() {
		return multicastPort;
	}

	public void setDatagramLength(int datagramLength) {
		this.datagramLength = datagramLength;
	}

	public int getDatagramLength() {
		return datagramLength;
	}
	
	public static final int RESPONDER_SOCKET_TIMEOUT = 500;
	public static final int BROWSER_SOCKET_TIMEOUT = 500;
	public static final int BROWSER_QUERY_INTERVAL = 1000;

}
