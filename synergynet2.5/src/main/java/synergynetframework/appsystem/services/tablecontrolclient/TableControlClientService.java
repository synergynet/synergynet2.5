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

package synergynetframework.appsystem.services.tablecontrolclient;

import java.io.IOException;
import java.util.logging.Logger;

import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.SynergyNetService;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.tablecontrolclient.messages.ChangeApplicationMessage;
import synergynetframework.appsystem.table.appregistry.ApplicationControlError;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.ApplicationRegistry;
import synergynetframework.appsystem.table.appregistry.ApplicationTaskManager;

/**
 * The Class TableControlClientService.
 */
public class TableControlClientService extends SynergyNetService implements
		TableCommsApplicationListener {
	
	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(TableControlClientService.class.getName());

	/** The comms. */
	private TableCommsClientService comms;
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.SynergyNetService#hasStarted()
	 */
	@Override
	public boolean hasStarted() {
		return comms != null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.
	 * TableCommsApplicationListener#messageReceived(java.lang.Object)
	 */
	public void messageReceived(Object obj) {
		if (obj instanceof ChangeApplicationMessage) {
			ChangeApplicationMessage cam = (ChangeApplicationMessage) obj;
			ApplicationInfo ai = ApplicationRegistry.getInstance()
					.getInfoForName(cam.getApplicationName(),
							ApplicationInfo.APPLICATION_TYPE_CLIENT);
			try {
				ApplicationTaskManager.getInstance().setActiveApplication(ai);
			} catch (ApplicationControlError e) {
				log.warning(e.toString());
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#shutdown()
	 */
	@Override
	public void shutdown() {

	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#start()
	 */
	@Override
	public void start() throws CouldNotStartServiceException {
		comms = (TableCommsClientService) ServiceManager.getInstance().get(
				TableCommsClientService.class);
		try {
			comms.register(this, this);
		} catch (IOException e) {
			log.warning(e.toString());
			throw new CouldNotStartServiceException(this);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#stop()
	 */
	@Override
	public void stop() throws ServiceNotRunningException {
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.
	 * TableCommsApplicationListener#tableDisconnected()
	 */
	@Override
	public void tableDisconnected() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.SynergyNetService#update()
	 */
	@Override
	public void update() {
	}
}
