/*
 * Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.services.net.peer;

import java.io.IOException;

import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceSystemFactory;


/**
 * The Class LocalPeer.
 */
public abstract class LocalPeer {
	
	/** The service discovery system. */
	private ServiceDiscoverySystem serviceDiscoverySystem;
	
	/** The service announce system. */
	private ServiceAnnounceSystem serviceAnnounceSystem;
	
	/** The service type. */
	protected String serviceType;
	
	/** The service name. */
	protected String serviceName;
	
	/** The time out. */
	protected long timeOut = 5 * 1000;
	
	/**
	 * Instantiates a new local peer.
	 *
	 * @param serviceName the service name
	 * @param serviceType the service type
	 */
	public LocalPeer(String serviceName, String serviceType) {
		this.serviceName = serviceName;
		this.serviceType = serviceType;
	}
	
	/**
	 * Sets the time out.
	 *
	 * @param timeOut the new time out
	 */
	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}
	
	/**
	 * Connect.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void connect() throws IOException {
		
			setServiceDiscoverySystem(ServiceSystemFactory.getServiceDiscoverySystem());
			setServiceAnnounceSystem(ServiceSystemFactory.getServiceAnnouncerSystem());
			ServerStatusMonitor smon = new ServerStatusMonitor(serviceType, serviceName, timeOut);
			getServiceDiscoverySystem().registerListener(smon);
			getServiceDiscoverySystem().registerServiceForListening(serviceType, serviceName);
			try {
				smon.connect();
				boolean serverFound = smon.serverFound();
				if(!serverFound) {
					advertiseServer();
					startServer();
				}else{
					foundServer(smon.getServerServiceDescriptor());
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		
	}
	
	/**
	 * Sets the service discovery system.
	 *
	 * @param serviceDiscoverySystem the new service discovery system
	 */
	public void setServiceDiscoverySystem(ServiceDiscoverySystem serviceDiscoverySystem) {
		this.serviceDiscoverySystem = serviceDiscoverySystem;
	}

	/**
	 * Gets the service discovery system.
	 *
	 * @return the service discovery system
	 */
	public ServiceDiscoverySystem getServiceDiscoverySystem() {
		return serviceDiscoverySystem;
	}
	
	/**
	 * Sets the service announce system.
	 *
	 * @param serviceAnnounceSystem the new service announce system
	 */
	public void setServiceAnnounceSystem(ServiceAnnounceSystem serviceAnnounceSystem) {
		this.serviceAnnounceSystem = serviceAnnounceSystem;
	}

	/**
	 * Gets the service announce system.
	 *
	 * @return the service announce system
	 */
	public ServiceAnnounceSystem getServiceAnnounceSystem() {
		return serviceAnnounceSystem;
	}
	
	/**
	 * Advertise server.
	 */
	public abstract void advertiseServer();
	
	/**
	 * Start server.
	 */
	public abstract void startServer();
	
	/**
	 * Found server.
	 *
	 * @param serviceDescriptor the service descriptor
	 */
	public abstract void foundServer(ServiceDescriptor serviceDescriptor);
}
