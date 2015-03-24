/**
 * Adapted from http://www.developer.com/java/ent/article.php/3728576
 */

package synergynetframework.appsystem.services.net.landiscovery.multicast;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;


/**
 * The Class ServiceDescription.
 */
public class ServiceDescription implements Comparable<ServiceDescription> {

	/** The instance name. */
	protected String instanceName;
	
	/** The port. */
	protected int port;
	
	/** The address. */
	protected InetAddress address;
	
	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public InetAddress getAddress() {
		return address;
	}
	
	/**
	 * Sets the address.
	 *
	 * @param serviceAddress the new address
	 */
	public void setAddress(InetAddress serviceAddress) {
		this.address = serviceAddress;
	}
	
	/**
	 * Gets the address as string.
	 *
	 * @return the address as string
	 */
	protected String getAddressAsString() {
		return getAddress().getHostAddress();
	}
	
	/**
	 * Gets the instance name.
	 *
	 * @return the instance name
	 */
	public String getInstanceName() {
		return instanceName;
	}
	
	/**
	 * Sets the instance name.
	 *
	 * @param serviceDescription the new instance name
	 */
	public void setInstanceName(String serviceDescription) {
		this.instanceName = serviceDescription;
	}

	/**
	 * Gets the encoded instance name.
	 *
	 * @return the encoded instance name
	 */
	protected String getEncodedInstanceName() {
		try {
			return URLEncoder.encode(getInstanceName(),"UTF-8");
		}
		catch (UnsupportedEncodingException uee) {
			return null;
		}
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets the port.
	 *
	 * @param servicePort the new port
	 */
	public void setPort(int servicePort) {
		this.port = servicePort;
	}

	/**
	 * Gets the port as string.
	 *
	 * @return the port as string
	 */
	protected String getPortAsString() {
		return ""+getPort();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(getEncodedInstanceName());
		buf.append(" ");
		buf.append(getAddressAsString());
		buf.append(" ");
		buf.append(getPortAsString());
		return buf.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o==this) { return true; }
		if (! (o instanceof ServiceDescription)) { return false; }
		ServiceDescription descriptor = (ServiceDescription)o;
		return descriptor.getInstanceName().equals(getInstanceName());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getInstanceName().hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ServiceDescription sd) throws ClassCastException {
		if (sd == null) { throw new NullPointerException(); }
		if (sd == this) { return 0; }

		return getInstanceName().compareTo(sd.getInstanceName());
	}
	
	/**
	 * Parses the.
	 *
	 * @param encodedInstanceName the encoded instance name
	 * @param addressAsString the address as string
	 * @param portAsString the port as string
	 * @return the service description
	 */
	public static ServiceDescription parse(String encodedInstanceName, String addressAsString, String portAsString) {
		
		ServiceDescription descriptor = new ServiceDescription();
		try {
			String name = URLDecoder.decode(encodedInstanceName,"UTF-8");
			if (name==null || name.length()==0) {
				return null;
			}
			descriptor.setInstanceName(name);
		}
		catch (UnsupportedEncodingException uee) {
			uee.printStackTrace();
			return null;
		}
		
		try {
			InetAddress addr = InetAddress.getByName(addressAsString);
			descriptor.setAddress(addr);
		}
		catch (UnknownHostException uhe) {
			System.err.println("Unexpected exception: "+uhe);
			uhe.printStackTrace();
			return null;
		}

		try {
			int p = Integer.parseInt(portAsString);
			descriptor.setPort(p);
		}
		catch (NumberFormatException nfe) {
			System.err.println("Unexpected exception: "+nfe);
			nfe.printStackTrace();
			return null;
		}
		
		return descriptor;
	}
}
