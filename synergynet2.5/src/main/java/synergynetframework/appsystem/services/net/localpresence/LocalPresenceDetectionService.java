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

package synergynetframework.appsystem.services.net.localpresence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoveryListener;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;

public class LocalPresenceDetectionService extends SynergyNetService implements ServiceDiscoveryListener {

	private static final Logger log = Logger.getLogger(LocalPresenceDetectionService.class.getName());
	
	private static final String SERVICE_TYPE = "_snn._tcp.local.";
	private static final String SERVICE_NAME = "presence";

	protected ServiceDiscoverySystem serviceDiscovery;
	protected ServiceAnnounceSystem serviceAnnouncer;
	protected Set<String> online = new HashSet<String>();		
	protected boolean isrunning;
	protected ServiceDescriptor sd;

	public LocalPresenceDetectionService() {
		sd = new ServiceDescriptor();
		sd.setServiceType(SERVICE_TYPE);
		sd.setServiceName(SERVICE_NAME);
		try {
			sd.setServiceAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			log.warning(e.toString());
		}
		sd.setServicePort(1268);
		sd.setUserData("path=index.html");
	}

	@Override
	public void start() {
		log.info("Local presence detection service startup...");
		final LocalPresenceDetectionService instance = this;
		new Thread() {
			public void run() {
				if(serviceDiscovery == null) {			
					try {
						NetworkServiceDiscoveryService nsds = (NetworkServiceDiscoveryService) ServiceManager.getInstance().get(NetworkServiceDiscoveryService.class);
						serviceDiscovery = nsds.getServiceDiscovery();										
						serviceDiscovery.registerListener(instance);
						serviceDiscovery.registerServiceForListening(SERVICE_TYPE, SERVICE_NAME);
						serviceAnnouncer = nsds.getServiceAnnouncer();
						isrunning = true;
					} catch (CouldNotStartServiceException e) {
						log.warning(e.toString());
					}
				}
			}
		}.start();
		log.info("Local presence detection service startup completed.");
	}

	@Override
	public void stop() {
		log.info("Local presence detection service stopping.");
		serviceAnnouncer.unregisterService(sd);
		serviceDiscovery = null;
		isrunning = false;
		log.info("Local presence detection service stopped.");
	}

	public void serviceAvailable(ServiceDescriptor sd) {
		synchronized(online) {						
			online.add(getEntry(sd));
		}
	}

	public void serviceRemoved(ServiceDescriptor sd) {
		synchronized(online) {
			online.remove(getEntry(sd));
		}
	}

	private String getEntry(ServiceDescriptor sd) {
		return sd.getStringRepresentation();
	}

	public List<String> getCurrentlyOnlineList() {
		synchronized(online) {
			List<String> tl = new ArrayList<String>();
			tl.addAll(online);
			return tl;
		}
	}

	@Override
	public void shutdown() {
		// rely on ServiceDiscovery to shutdown
	}

	@Override
	public boolean hasStarted() {
		return isrunning;
	}

	@Override
	public void update() {}
}
