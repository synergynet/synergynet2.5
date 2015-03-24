/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.services.net.landiscovery;

import java.net.InetAddress;

/**
 * The Class ServiceDescriptor.
 */
public class ServiceDescriptor implements Comparable<ServiceDescriptor> {
	
	/**
	 * Gets the service descriptor from string representation.
	 *
	 * @param s
	 *            the s
	 * @return the service descriptor from string representation
	 */
	public static ServiceDescriptor getServiceDescriptorFromStringRepresentation(
			String s) {
		try {
			ServiceDescriptor sd = new ServiceDescriptor();
			String[] tokens = s.split("[|@]");
			sd.setServiceType(tokens[0]);
			sd.setServiceName(tokens[1]);
			sd.setServiceAddress(InetAddress.getByName(tokens[2]));
			sd.setServicePort(Integer.parseInt(tokens[3]));
			sd.setUserData(tokens[4]);
			return sd;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/** The service address. */
	private InetAddress serviceAddress;

	/** The service name. */
	private String serviceName;

	/** The service port. */
	private int servicePort;

	/** The service type. */
	private String serviceType;

	/** The user data. */
	private String userData;
	
	/**
	 * Instantiates a new service descriptor.
	 */
	public ServiceDescriptor() {
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(ServiceDescriptor o) {
		return o.getStringRepresentation().compareTo(getStringRepresentation());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof ServiceDescriptor) {
			return ((ServiceDescriptor) obj).getStringRepresentation().equals(
					getStringRepresentation());
		}
		return false;
	}

	/**
	 * Gets the service address.
	 *
	 * @return the service address
	 */
	public InetAddress getServiceAddress() {
		return serviceAddress;
	}

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Gets the service port.
	 *
	 * @return the service port
	 */
	public int getServicePort() {
		return servicePort;
	}

	/**
	 * Gets the service type.
	 *
	 * @return the service type
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * Gets the string representation.
	 *
	 * @return the string representation
	 */
	public String getStringRepresentation() {
		return getServiceType() + "|" + getServiceName() + "@"
				+ getServiceAddress().getHostAddress() + "|" + getServicePort()
				+ "|" + getUserData();
	}
	
	/**
	 * Gets the user data.
	 *
	 * @return the user data
	 */
	public String getUserData() {
		return userData;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getStringRepresentation().hashCode();
	}
	
	/**
	 * Sets the service address.
	 *
	 * @param serviceAddress
	 *            the new service address
	 */
	public void setServiceAddress(InetAddress serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	
	/**
	 * Sets the service name.
	 *
	 * @param serviceName
	 *            the new service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	/**
	 * Sets the service port.
	 *
	 * @param servicePort
	 *            the new service port
	 */
	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}
	
	/**
	 * Sets the service type.
	 *
	 * @param serviceType
	 *            the new service type
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	/**
	 * Sets the user data.
	 *
	 * @param userData
	 *            the new user data
	 */
	public void setUserData(String userData) {
		this.userData = userData;
	}
}
