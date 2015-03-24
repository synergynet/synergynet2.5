/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.appsystem.services.net.peer;

import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener;

/**
 * The Class ServerStatusMonitor.
 */
public class ServerStatusMonitor implements ServiceDiscoveryListener {

	/** The server found. */
	protected boolean serverFound;

	/** The server service descriptor. */
	protected ServiceDescriptor serverServiceDescriptor;

	/** The service name. */
	private String serviceName;

	/** The service type. */
	private String serviceType;

	/** The time out. */
	protected long timeOut;
	
	/**
	 * Instantiates a new server status monitor.
	 *
	 * @param serviceType
	 *            the service type
	 * @param serviceName
	 *            the service name
	 * @param timeOut
	 *            the time out
	 */
	public ServerStatusMonitor(String serviceType, String serviceName,
			long timeOut) {
		this.serviceType = serviceType;
		this.serviceName = serviceName;
		this.timeOut = timeOut;
	}

	/**
	 * Connect.
	 *
	 * @throws InterruptedException
	 *             the interrupted exception
	 */
	public synchronized void connect() throws InterruptedException {
		wait(timeOut);
	}
	
	/**
	 * Gets the server service descriptor.
	 *
	 * @return the server service descriptor
	 */
	public ServiceDescriptor getServerServiceDescriptor() {
		return serverServiceDescriptor;
	}
	
	/**
	 * Server found.
	 *
	 * @return true, if successful
	 */
	public synchronized boolean serverFound() {
		return serverFound;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.
	 * ServiceDiscoveryListener
	 * #serviceAvailable(synergynetframework.appsystem.services
	 * .net.landiscovery.ServiceDescriptor)
	 */
	public synchronized void serviceAvailable(ServiceDescriptor sd) {
		if (sd.getServiceType().equals(serviceType)
				&& sd.getServiceName().equals(serviceName)) {
			serverFound = true;
			this.serverServiceDescriptor = sd;
			notify();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.
	 * ServiceDiscoveryListener
	 * #serviceRemoved(synergynetframework.appsystem.services
	 * .net.landiscovery.ServiceDescriptor)
	 */
	public void serviceRemoved(ServiceDescriptor sd) {

	}
}
