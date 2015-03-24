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

package synergynetframework.appsystem.services.net.landiscovery.mdns;

import java.io.IOException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import synergynetframework.appsystem.services.net.landiscovery.ServiceAnnounceSystem;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDescriptor;
import synergynetframework.appsystem.services.net.landiscovery.ServiceDiscoverySystem;

/**
 * The Class MDNSServiceHelper.
 */
public class MDNSServiceHelper {

	/** The jmdns. */
	private static JmDNS jmdns;

	/**
	 * Gets the descriptor for info.
	 *
	 * @param info
	 *            the info
	 * @return the descriptor for info
	 */
	public static ServiceDescriptor getDescriptorForInfo(ServiceInfo info) {
		ServiceDescriptor sd = new ServiceDescriptor();
		sd.setServiceAddress(info.getAddress());
		sd.setServicePort(info.getPort());
		sd.setServiceType(info.getName());
		sd.setServiceName(info.getName());
		return sd;
	}
	
	/**
	 * Gets the info for descriptor.
	 *
	 * @param sd
	 *            the sd
	 * @return the info for descriptor
	 */
	public static ServiceInfo getInfoForDescriptor(ServiceDescriptor sd) {
		return ServiceInfo.create(sd.getServiceType(), sd.getServiceName(),
				sd.getServicePort(), 0, 0, sd.getUserData());
	}

	/**
	 * Gets the service announce system.
	 *
	 * @return the service announce system
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ServiceAnnounceSystem getServiceAnnounceSystem()
			throws IOException {
		return new MDNSServiceAnnouncer(getSharedJmDNS());
	}

	/**
	 * Gets the service discovery system.
	 *
	 * @return the service discovery system
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ServiceDiscoverySystem getServiceDiscoverySystem()
			throws IOException {
		ServiceDiscoverySystem sd = new MDNSServiceDiscovery(getSharedJmDNS());
		return sd;
	}

	/**
	 * Gets the shared jm dns.
	 *
	 * @return the shared jm dns
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private static JmDNS getSharedJmDNS() throws IOException {
		if (jmdns == null) {
			jmdns = JmDNS.create();
		}
		return jmdns;
	}
}
