/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.services.net.landiscovery;

import java.net.InetAddress;

public class ServiceDescriptor implements Comparable<ServiceDescriptor> {

	private InetAddress serviceAddress;
	private int servicePort;
	private String serviceType;
	private String serviceName;
	private String userData;
	
	public ServiceDescriptor() {}

	public void setServiceAddress(InetAddress serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	public InetAddress getServiceAddress() {
		return serviceAddress;
	}
	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}
	public int getServicePort() {
		return servicePort;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceName() {
		return serviceName;
	}

	public String getUserData() {
		return userData;
	}
	public void setUserData(String userData) {
		this.userData = userData;
	}

	public int hashCode() {
		return getStringRepresentation().hashCode();
	}

	public boolean equals(Object obj) {
		if(obj instanceof ServiceDescriptor) {
			return ((ServiceDescriptor)obj).getStringRepresentation().equals(getStringRepresentation());
		}
		return false;
	}

	public int compareTo(ServiceDescriptor o) {
		return o.getStringRepresentation().compareTo(getStringRepresentation());
	}

	public String getStringRepresentation() {
		return getServiceType() + "|" + getServiceName() + "@" + getServiceAddress().getHostAddress() + "|" + getServicePort() + "|" + getUserData();
	}

	public static ServiceDescriptor getServiceDescriptorFromStringRepresentation(String s) {
		try {
			ServiceDescriptor sd = new ServiceDescriptor();
			String[] tokens = s.split("[|@]");
			sd.setServiceType(tokens[0]);
			sd.setServiceName(tokens[1]);
			sd.setServiceAddress(InetAddress.getByName(tokens[2]));
			sd.setServicePort(Integer.parseInt(tokens[3]));	
			sd.setUserData(tokens[4]);
			return sd;
		}catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
