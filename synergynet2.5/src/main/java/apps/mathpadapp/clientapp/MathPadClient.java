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

package apps.mathpadapp.clientapp;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.sysutils.CameraUtility;
import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.controllerapp.MathPadController;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathTool.MathToolListener;
import apps.mathpadapp.mathtool.MathTool.SeparatorState;
import apps.mathpadapp.mathtool.MathTool.WritingState;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.handlers.client.ClientMessageHandler;
import apps.mathpadapp.networkmanager.managers.ClientManager;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.RegisterUserMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.UnregisterUserMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.util.MTMessageBox;
import apps.mathpadapp.util.MTMessageBox.MessageListener;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * The Class MathPadClient.
 */
public class MathPadClient extends DefaultSynergyNetApp {
	
	/** The client manager. */
	protected ClientManager clientManager;

	/** The comms. */
	private TableCommsClientService comms = null;

	/** The content system. */
	private ContentSystem contentSystem;

	/** The current no of pads. */
	protected int currentNoOfPads = 0;

	/** The max number of pads. */
	protected int MAX_NUMBER_OF_PADS = 20;

	/** The message handler. */
	protected ClientMessageHandler messageHandler;

	/** The nsds. */
	private NetworkServiceDiscoveryService nsds = null;

	/**
	 * Instantiates a new math pad client.
	 *
	 * @param info
	 *            the info
	 */
	public MathPadClient(ApplicationInfo info) {
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
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		SynergyNetAppUtils.addTableOverlay(this);
		
		ImageTextLabel btn = (ImageTextLabel) contentSystem
				.createContentItem(ImageTextLabel.class);
		btn.setAutoFitSize(false);
		btn.setWidth(80);
		btn.setHeight(80);
		btn.setBorderColour(Color.red);
		btn.setBackgroundColour(Color.white);
		btn.setImageInfo(MathPadResources.class
				.getResource("controlbar/Math Pad.jpg"));
		btn.setCRLFSeparatedString("Math Pad");
		btn.setLocalLocation(50, 50);
		btn.setRotateTranslateScalable(false);
		
		// setAllContentSteadfast();

		btn.addItemListener(new ItemListener() {
			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				if (currentNoOfPads == MAX_NUMBER_OF_PADS) {
					return;
				}
				final MathTool tool = new MathTool(contentSystem);
				currentNoOfPads++;
				MathToolInitSettings settings = new MathToolInitSettings();
				settings.setTextColor(Color.black);
				settings.setLineWidth(1.0f);
				settings.setSeparatorState(SeparatorState.COLLAPSED);
				settings.setWritingState(WritingState.FREE_DRAW);
				settings.setBackgroundColor(Color.white);
				settings.setLocationX(300);
				settings.setLocationY(300);
				settings.setOrder(4);
				settings.setScale(0.7f);
				tool.init(settings);
				tool.addMathToolListener(new MathToolListener() {
					
					@Override
					public void assignmentAnswerReady(AssignmentInfo info) {
					}
					
					@Override
					public void mathPadClosed(MathTool mathTool) {
						if (currentNoOfPads > 0) {
							currentNoOfPads--;
						}
						if (clientManager == null) {
							return;
						}
						UserIdentity userId = clientManager
								.getUserIdentityForMathTool(mathTool);
						if (userId != null) {
							UnregisterUserMessage message = new UnregisterUserMessage(
									MathPadController.class, userId);
							clientManager.sendMessage(message);
							clientManager.unregisterMathPad(userId);
						}
					}
					
					@Override
					public void separatorChanged(SeparatorState newState) {
					}
					
					@Override
					public void userLogin(String userName, String password) {
						MathPadClient.this.login(tool, userName, password);
					}

				});

				// login with a random Name
				MathPadClient.this.login(tool, UUID.randomUUID().toString(),
						" ");
			}
			
			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
			}
			
			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
			}

		});
	}

	/**
	 * Connect.
	 *
	 * @param isReconnect
	 *            the is reconnect
	 */
	public void connect(final boolean isReconnect) {

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
						
						ArrayList<Class<?>> receiverClasses = new ArrayList<Class<?>>();
						receiverClasses.add(MathPadController.class);
						HashMap<UserIdentity, MathTool> existingTools = null;
						if (isReconnect && (clientManager != null)) {
							existingTools = clientManager
									.getRegisteredMathPads();
						}
						clientManager = new ClientManager(contentSystem, comms,
								receiverClasses);
						messageHandler = new ClientMessageHandler(clientManager);
						comms.register(MathPadClient.this, messageHandler);
						if (isReconnect && (existingTools != null)) {
							for (UserIdentity userId : existingTools.keySet()) {
								RegisterUserMessage message = new RegisterUserMessage(
										MathPadController.class, userId);
								clientManager.sendMessage(message);
								clientManager.registerMathPad(userId,
										existingTools.get(userId));
							}
						}
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
										connect(true);
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

	/**
	 * Login.
	 *
	 * @param tool
	 *            the tool
	 * @param userName
	 *            the user name
	 * @param password
	 *            the password
	 */
	public void login(MathTool tool, String userName, String password) {
		UserIdentity userId = new UserIdentity();
		userId.setUserIdentity(userName);
		userId.setPassword(password);
		if (clientManager != null) {
			if (!clientManager.getRegisteredMathPads().containsValue(tool)) {
				RegisterUserMessage message = new RegisterUserMessage(
						MathPadController.class, userId);
				clientManager.sendMessage(message);
				clientManager.registerMathPad(userId, tool);
			} else {
				final MTMessageBox msg = new MTMessageBox(tool, contentSystem);
				msg.setTitle("Login Error");
				msg.setMessage("You already logged in.");
				msg.getCancelButton().setVisible(false);
				msg.getOkButton().setLocalLocation(0,
						msg.getOkButton().getLocalLocation().y);
				msg.addMessageListener(new MessageListener() {
					@Override
					public void buttonClicked(String buttonId) {
					}
					
					@Override
					public void buttonReleased(String buttonId) {
						msg.close();
					}
				});
			}
		}
		tool.setTitle(userId.getUserIdentity());
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate
	 * ()
	 */
	@Override
	public void onActivate() {
		this.connect(true);
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
		if (clientManager != null) {
			clientManager.update(tpf);
		}
	}

}
