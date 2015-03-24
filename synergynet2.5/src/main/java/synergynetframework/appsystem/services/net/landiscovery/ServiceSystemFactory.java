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

import java.io.IOException;
import java.net.UnknownHostException;

import synergynetframework.appsystem.services.net.landiscovery.mdns.MDNSServiceHelper;
import synergynetframework.appsystem.services.net.landiscovery.multicast.ServiceDiscoveryParams;
import synergynetframework.appsystem.services.net.landiscovery.multicast.discoverer.ServiceBrowser;
import synergynetframework.appsystem.services.net.landiscovery.multicast.responder.ServiceResponder;


/**
 * A factory for creating ServiceSystem objects.
 */
public class ServiceSystemFactory {
	
	/**
	 * The Enum ServiceProvider.
	 */
	public enum ServiceProvider {
		
		/** The multicast. */
		MULTICAST,
		
		/** The mdns. */
		MDNS
	}
	
	/** The Constant defaultProvider. */
	private static final ServiceProvider defaultProvider = ServiceProvider.MULTICAST;
	
	/** The sdp. */
	private static ServiceDiscoveryParams sdp;
	
	/** The sb. */
	private static ServiceBrowser sb;
	
	/** The sr. */
	private static ServiceResponder sr;
	
	/**
	 * Gets the service discovery system.
	 *
	 * @return the service discovery system
	 */
	public static ServiceDiscoverySystem getServiceDiscoverySystem() {
		return getServiceDiscoverySystem(defaultProvider);
	}
	
	/**
	 * Gets the service announcer system.
	 *
	 * @return the service announcer system
	 */
	public static ServiceAnnounceSystem getServiceAnnouncerSystem() {
		return getServiceAnnouncerSystem(defaultProvider);
	}
	
	/**
	 * Gets the service discovery system.
	 *
	 * @param sp the sp
	 * @return the service discovery system
	 */
	public static ServiceDiscoverySystem getServiceDiscoverySystem(ServiceProvider sp) {
		ServiceDiscoverySystem sys = null;
		switch(sp) {
		case MULTICAST: {
			try {
				ServiceBrowser sb = new ServiceBrowser(getSharedMulticastParams());
				sb.startListener();
				sys = sb;				
				sys.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		case MDNS: {
			try {
				sys = MDNSServiceHelper.getServiceDiscoverySystem();
				sys.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
		return sys;
	}
	
	/**
	 * Gets the shared multicast params.
	 *
	 * @return the shared multicast params
	 * @throws UnknownHostException the unknown host exception
	 */
	private static ServiceDiscoveryParams getSharedMulticastParams() throws UnknownHostException {
		if(sdp == null) sdp = new ServiceDiscoveryParams();
		return sdp;
	}

	/**
	 * Gets the service announcer system.
	 *
	 * @param sp the sp
	 * @return the service announcer system
	 */
	public static ServiceAnnounceSystem getServiceAnnouncerSystem(ServiceProvider sp) {
		ServiceAnnounceSystem sys = null;
		switch(sp) {
		case MULTICAST: {
			try {
				ServiceResponder sr = new ServiceResponder(getSharedMulticastParams());
				sr.startResponder();
				sys = sr;
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		case MDNS: {
			try {
				sys = MDNSServiceHelper.getServiceAnnounceSystem();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}
		return sys;
	}
	
	/**
	 * Stop.
	 */
	public static void stop(){
		if(sb != null) sb.stop();
		if(sr != null) sr.stop();
	}
}
