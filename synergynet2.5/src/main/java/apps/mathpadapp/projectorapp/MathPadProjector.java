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

package apps.mathpadapp.projectorapp;

import java.io.IOException;
import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.jme.sysutils.CameraUtility;
import apps.mathpadapp.controllerapp.MathPadController;
import apps.mathpadapp.networkmanager.handlers.projector.ProjectorMessageHandler;
import apps.mathpadapp.networkmanager.managers.ProjectorManager;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * The Class MathPadProjector.
 */
public class MathPadProjector extends DefaultSynergyNetApp {
	
	/** The comms. */
	private TableCommsClientService comms;

	/** The content system. */
	private ContentSystem contentSystem;

	/** The message handler. */
	protected ProjectorMessageHandler messageHandler;

	/** The projector manager. */
	protected ProjectorManager projectorManager;
	
	/**
	 * Instantiates a new math pad projector.
	 *
	 * @param info
	 *            the info
	 */
	public MathPadProjector(ApplicationInfo info) {
		super(info);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #getCamera()
	 */
	protected Camera getCamera() {
		if (cam == null) {
			cam = CameraUtility.getCamera();
			cam.setLocation(new Vector3f(0f, 10f, 50f));
			cam.lookAt(new Vector3f(), new Vector3f(0, 0, -1));
			cam.update();
		}
		return cam;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate
	 * ()
	 */
	@Override
	public void onActivate() {
		if (comms == null) {
			try {
				comms = (TableCommsClientService) ServiceManager.getInstance()
						.get(TableCommsClientService.class);
			} catch (CouldNotStartServiceException e1) {
				e1.printStackTrace();
			}
			ArrayList<Class<?>> receiverClasses = new ArrayList<Class<?>>();
			receiverClasses.add(MathPadController.class);
			projectorManager = new ProjectorManager(contentSystem, comms,
					receiverClasses);
			
			messageHandler = new ProjectorMessageHandler(projectorManager);
			try {
				if (comms != null) {
					comms.register(this, messageHandler);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if (comms != null) {
				comms.sendMessage(new ApplicationCommsRequest(
						MathPadProjector.class.getName()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onDeactivate
	 * ()
	 */
	@Override
	protected void onDeactivate() {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if (contentSystem != null) {
			contentSystem.update(tpf);
		}
		if (projectorManager != null) {
			projectorManager.update(tpf);
		}
	}
}
