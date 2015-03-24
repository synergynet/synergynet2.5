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

package apps.mathpadapp.controllerapp;

import java.io.IOException;
import java.util.ArrayList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightExit;
import synergynetframework.jme.sysutils.CameraUtility;
import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mathpadapp.networkmanager.handlers.controller.ControllerMessageHandler;
import apps.mathpadapp.networkmanager.managers.ControllerManager;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * The Class MathPadController.
 */
public class MathPadController extends DefaultSynergyNetApp {

	/** The comms. */
	private TableCommsClientService comms;

	/** The content system. */
	private ContentSystem contentSystem;

	/** The control bar. */
	private ControlBar controlBar;

	/** The controller manager. */
	protected ControllerManager controllerManager;

	/** The message handler. */
	protected ControllerMessageHandler messageHandler;

	/** The nsds. */
	private NetworkServiceDiscoveryService nsds = null;
	
	/**
	 * Instantiates a new math pad controller.
	 *
	 * @param info
	 *            the info
	 */
	public MathPadController(ApplicationInfo info) {
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
		SynergyNetAppUtils.addTableOverlay(this);
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightExit());
		
		controlBar = new ControlBar(contentSystem);
	}

	/**
	 * Connect.
	 */
	public void connect() {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (comms == null) {
						comms = (TableCommsClientService) ServiceManager
								.getInstance().get(
										TableCommsClientService.class);
					}
					if (comms != null) {
						while (!comms.isClientConnected()) {
							Thread.sleep(1000);
							if (nsds == null) {
								nsds = (NetworkServiceDiscoveryService) ServiceManager
										.getInstance()
										.get(NetworkServiceDiscoveryService.class);
							} else {
								nsds.shutdown();
							}
							nsds.start();
							comms.start();
						}
						if (nsds != null) {
							nsds.shutdown();
						}
						
						ArrayList<Class<?>> clientClasses = new ArrayList<Class<?>>();
						clientClasses.add(MathPadClient.class);
						// clientClasses.add(MathPadProjector.class);
						ArrayList<Class<?>> controllerClasses = new ArrayList<Class<?>>();
						controllerClasses.add(MathPadController.class);

						controllerManager = new ControllerManager(
								contentSystem, comms, clientClasses);
						controllerManager.createRemoteDesktopManager(
								controllerClasses, clientClasses);
						controllerManager.setGraphManager(new GraphManager(
								contentSystem));
						controlBar.setControllerManager(controllerManager);
						messageHandler = new ControllerMessageHandler(
								controllerManager);
						comms.register(MathPadController.this, messageHandler);
						comms.register("connection_listener",
								new TableCommsApplicationListener() {
									@Override
									public void messageReceived(Object obj) {
										
									}
									
									@Override
									public void tableDisconnected() {
										if (comms != null) {
											try {
												comms.stop();
											} catch (ServiceNotRunningException e) {
												
												e.printStackTrace();
											}
										}
										comms = null;
										connect();
									}
									
								});
					}
				} catch (CouldNotStartServiceException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		}).start();
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
		connect();
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
		if (controllerManager != null) {
			controllerManager.update(tpf);
		}
	}
}
