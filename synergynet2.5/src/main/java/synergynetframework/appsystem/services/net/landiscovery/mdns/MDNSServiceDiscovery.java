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

package synergynetframework.appsystem.services.net.landiscovery.mdns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;

import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;


/**
 * The Class MDNSServiceDiscovery.
 */
public class MDNSServiceDiscovery implements javax.jmdns.ServiceListener, ServiceDiscoverySystem, ServiceAnnounceSystem {

	/** The jmdns. */
	protected JmDNS jmdns;
	
	/** The listeners. */
	protected List<ServiceDiscoveryListener> listeners = new ArrayList<ServiceDiscoveryListener>();

	/**
	 * Instantiates a new MDNS service discovery.
	 *
	 * @param jmdns the jmdns
	 */
	public MDNSServiceDiscovery(final JmDNS jmdns) {
		this.jmdns = jmdns;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem#registerListener(synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener)
	 */
	public void registerListener(ServiceDiscoveryListener l) {
		synchronized(listeners) {
			if(!listeners.contains(l)) listeners.add(l);
		}		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem#removeListener(synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener)
	 */
	public void removeListener(ServiceDiscoveryListener l) {
		synchronized(listeners) {
			listeners.remove(l);
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem#registerServiceForListening(java.lang.String, java.lang.String)
	 */
	public void registerServiceForListening(final String type, final String name) {
		jmdns.addServiceListener(type, this);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem#registerService(synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor)
	 */
	public void registerService(ServiceDescriptor sd) {
		try {
			jmdns.registerService(MDNSServiceHelper.getInfoForDescriptor(sd));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem#unregisterService(synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor)
	 */
	public void unregisterService(ServiceDescriptor sd) {
		jmdns.unregisterService(MDNSServiceHelper.getInfoForDescriptor(sd));
	}
	
	/* (non-Javadoc)
	 * @see javax.jmdns.ServiceListener#serviceAdded(javax.jmdns.ServiceEvent)
	 */
	public void serviceAdded(final ServiceEvent event) {
		final Runnable runnable = new Runnable() {
			public void run() {
				jmdns.requestServiceInfo(event
						.getType(), event.getName());
			}
		};
		final Thread thread = new Thread(runnable, "ResolutionThread");
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();		
	}

	/* (non-Javadoc)
	 * @see javax.jmdns.ServiceListener#serviceRemoved(javax.jmdns.ServiceEvent)
	 */
	public void serviceRemoved(final ServiceEvent event) {
		synchronized(this) {
			ServiceInfo si = jmdns.getServiceInfo(event.getType(), event.getName());		
			if(si != null) {
				for(ServiceDiscoveryListener l : listeners) {
					l.serviceRemoved(MDNSServiceHelper.getDescriptorForInfo(si));
				}				
			}
		}		
	}

	/* (non-Javadoc)
	 * @see javax.jmdns.ServiceListener#serviceResolved(javax.jmdns.ServiceEvent)
	 */
	public void serviceResolved(final ServiceEvent event) {
		synchronized(this) {
			ServiceInfo si = event.getInfo();		
			if(si != null) {
				for(ServiceDiscoveryListener l : listeners) {
					l.serviceAvailable(MDNSServiceHelper.getDescriptorForInfo(si));
				}				
			}
		}		
	}	



	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem#start()
	 */
	public void start() {
		 
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem#stop()
	 */
	public void stop() {
		jmdns.unregisterAllServices();
		jmdns.close();	
		jmdns = null;		
	}
	


}
