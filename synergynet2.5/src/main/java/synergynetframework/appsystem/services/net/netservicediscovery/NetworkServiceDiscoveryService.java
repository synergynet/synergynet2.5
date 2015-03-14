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

package synergynetframework.appsystem.services.net.netservicediscovery;

import java.util.logging.Logger;

import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceSystemFactory;

/**
 * Service that publishes information about services, or listens
 * for services that become available or become unavailable.
 *
 */
public class NetworkServiceDiscoveryService extends SynergyNetService {

	private static final Logger log = Logger.getLogger(NetworkServiceDiscoveryService.class.getName()); 

	protected ServiceDiscoverySystem serviceDiscovery;
	protected ServiceAnnounceSystem serviceAnnouncer;

	public ServiceDiscoverySystem getServiceDiscovery() {
		return serviceDiscovery;
	}

	public ServiceAnnounceSystem getServiceAnnouncer() {
		return serviceAnnouncer;
	}

	protected boolean isrunning;

	public NetworkServiceDiscoveryService() {
		log.info(this.getClass().getName() + " instance created");
	}

	@Override
	public void shutdown() {
		log.info("Initiating shutdown.");
		stop();
		log.info("Shutdown complete.");
	}

	@Override
	public void start() throws CouldNotStartServiceException {
		log.info("Starting up...");
		serviceDiscovery = ServiceSystemFactory.getServiceDiscoverySystem();
		serviceAnnouncer = ServiceSystemFactory.getServiceAnnouncerSystem();
		isrunning = true;
		log.info("Startup complete.");
	}

	@Override
	public void stop() {
		log.info("Stopping.");
		if(hasStarted()) {
			ServiceSystemFactory.stop();
			serviceDiscovery.stop();
			serviceAnnouncer.stop();
			isrunning = false;
		}
	}

	@Override
	public boolean hasStarted() {
		return isrunning;
	}

	@Override
	public void update() {}
}
