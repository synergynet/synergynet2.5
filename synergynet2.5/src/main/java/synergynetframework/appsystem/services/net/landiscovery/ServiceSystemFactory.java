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

public class ServiceSystemFactory {
	
	public enum ServiceProvider {
		MULTICAST,
		MDNS
	}
	
	private static final ServiceProvider defaultProvider = ServiceProvider.MULTICAST;
	private static ServiceDiscoveryParams sdp;
	private static ServiceBrowser sb;
	private static ServiceResponder sr;
	
	public static ServiceDiscoverySystem getServiceDiscoverySystem() {
		return getServiceDiscoverySystem(defaultProvider);
	}
	
	public static ServiceAnnounceSystem getServiceAnnouncerSystem() {
		return getServiceAnnouncerSystem(defaultProvider);
	}
	
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
	
	private static ServiceDiscoveryParams getSharedMulticastParams() throws UnknownHostException {
		if(sdp == null) sdp = new ServiceDiscoveryParams();
		return sdp;
	}

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
	
	public static void stop(){
		if(sb != null) sb.stop();
		if(sr != null) sr.stop();
	}
}
