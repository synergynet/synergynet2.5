/**
 * Adapted from http://www.developer.com/java/ent/article.php/3728576
 */

package synergynetframework.appsystem.services.net.landiscovery.multicast;

import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * The Class ServiceDiscoveryParams.
 */
public class ServiceDiscoveryParams {

	/** The multicast group. */
	private InetAddress multicastGroup;
	
	/** The multicast port. */
	private int multicastPort;
	
	/** The datagram length. */
	private int datagramLength;
	
	/**
	 * Instantiates a new service discovery params.
	 *
	 * @throws UnknownHostException the unknown host exception
	 */
	public ServiceDiscoveryParams() throws UnknownHostException {
		setMulticastGroup(InetAddress.getByName("239.255.31.14")); // random from: 239.255.000.000-239.255.255.255 Site-Local Scope [RFC2365]
		setMulticastPort(8261);
		setDatagramLength(1024);
	}

	/**
	 * Sets the multicast group.
	 *
	 * @param multicastGroup the new multicast group
	 */
	public void setMulticastGroup(InetAddress multicastGroup) {
		this.multicastGroup = multicastGroup;
	}

	/**
	 * Gets the multicast group.
	 *
	 * @return the multicast group
	 */
	public InetAddress getMulticastGroup() {
		return multicastGroup;
	}

	/**
	 * Sets the multicast port.
	 *
	 * @param multicastPort the new multicast port
	 */
	public void setMulticastPort(int multicastPort) {
		this.multicastPort = multicastPort;
	}

	/**
	 * Gets the multicast port.
	 *
	 * @return the multicast port
	 */
	public int getMulticastPort() {
		return multicastPort;
	}

	/**
	 * Sets the datagram length.
	 *
	 * @param datagramLength the new datagram length
	 */
	public void setDatagramLength(int datagramLength) {
		this.datagramLength = datagramLength;
	}

	/**
	 * Gets the datagram length.
	 *
	 * @return the datagram length
	 */
	public int getDatagramLength() {
		return datagramLength;
	}
	
	/** The Constant RESPONDER_SOCKET_TIMEOUT. */
	public static final int RESPONDER_SOCKET_TIMEOUT = 500;
	
	/** The Constant BROWSER_SOCKET_TIMEOUT. */
	public static final int BROWSER_SOCKET_TIMEOUT = 500;
	
	/** The Constant BROWSER_QUERY_INTERVAL. */
	public static final int BROWSER_QUERY_INTERVAL = 1000;

}
