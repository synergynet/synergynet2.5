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

package core;

import java.util.logging.Level;
import java.util.logging.Logger;

import synergynetframework.appsystem.server.ui.ServerUI;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.networkcontent.NetworkContentServerService;
import synergynetframework.appsystem.services.net.tablecomms.server.TableCommsServerService;
import synergynetframework.config.logging.LoggingConfigPrefsItem;
import synergynetframework.config.logging.LoggingConfigPrefsItem.LoggingLevel;

/**
 * The Class Server.
 */
public class Server {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws CouldNotStartServiceException
	 *             the could not start service exception
	 */
	public static void main(String[] args) throws CouldNotStartServiceException {

		// set logging level
		LoggingConfigPrefsItem logPrefs = new LoggingConfigPrefsItem();
		LoggingLevel loggingLevel = logPrefs.getLoggingLevel();
		Logger.getLogger("").setLevel(Level.parse(loggingLevel.name()));

		ServiceManager services = ServiceManager.getInstance();
		services.get(NetworkServiceDiscoveryService.class);
		services.get(NetworkContentServerService.class);
		services.get(TableCommsServerService.class);
		TableCommsServerService tableCommsServer = (TableCommsServerService) services
				.get(TableCommsServerService.class);
		ServerUI.start(tableCommsServer);
	}
	
}
