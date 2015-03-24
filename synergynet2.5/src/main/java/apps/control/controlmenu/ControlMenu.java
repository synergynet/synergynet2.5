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

package apps.control.controlmenu;

import java.awt.Color;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.BackgroundController;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.ListEventAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.TableSwapDialogue;
import apps.mysteries.SubAppMenu;
import core.SynergyNetDesktop;

/**
 * The Class ControlMenu.
 */
public class ControlMenu {

	/** The background controller. */
	protected BackgroundController backgroundController;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The control menu. */
	protected ListContainer controlMenu;

	/** The networked content manager. */
	protected NetworkedContentManager networkedContentManager;

	/** The sub app menu. */
	protected SubAppMenu subAppMenu;

	/**
	 * Instantiates a new control menu.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param networkedContentManager
	 *            the networked content manager
	 * @param subAppMenu
	 *            the sub app menu
	 * @param backgroundController
	 *            the background controller
	 */
	public ControlMenu(ContentSystem contentSystem,
			NetworkedContentManager networkedContentManager,
			SubAppMenu subAppMenu, BackgroundController backgroundController) {
		this.contentSystem = contentSystem;
		this.subAppMenu = subAppMenu;
		this.backgroundController = backgroundController;
		this.networkedContentManager = networkedContentManager;

		LoadControlMenu();
	}
	
	/**
	 * Load control menu.
	 *
	 * @return the list container
	 */
	private ListContainer LoadControlMenu() {

		controlMenu = (ListContainer) contentSystem
				.createContentItem(ListContainer.class);
		controlMenu.setBackgroundColour(Color.BLUE);
		controlMenu.setWidth(200);
		controlMenu.setItemHeight(30);
		
		if (backgroundController != null) {
			backgroundController.addItemListener(new ItemEventAdapter() {
				public void cursorLongHeld(ContentItem b, long id, float x,
						float y, float pressure) {
					if (controlMenu.isVisible()) {
						controlMenu.setVisible(false);
						
					} else {
						controlMenu.setVisible(true);
						controlMenu.setLocalLocation(x,
								contentSystem.getScreenHeight() - y);
					}
				}
			});
		}

		final TableSwapDialogue tableSwapDialogue = new TableSwapDialogue(
				contentSystem, this.networkedContentManager);
		tableSwapDialogue.setVisible(false);

		final SimpleButton blockButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		blockButton.setAutoFitSize(false);
		blockButton.setText("Block Student Table");
		blockButton.setBackgroundColour(Color.lightGray);
		blockButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (networkedContentManager.isRemoteLocked()) {
					blockButton.setText("Block Student Table");
					networkedContentManager.setRemoteLocked(false);
				} else {
					blockButton.setText("Unblock Student Table");
					networkedContentManager.setRemoteLocked(true);
				}
			}
		});
		
		final SimpleButton disableMenuButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		disableMenuButton.setAutoFitSize(false);
		disableMenuButton.setText("Disable Students Menu");
		disableMenuButton.setBackgroundColour(Color.lightGray);
		disableMenuButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {

				if (networkedContentManager.isRemoteMenuOn()) {
					disableMenuButton.setText("Enable Students Menu");
					networkedContentManager.setRemoteMenuOn(false);
				} else {
					disableMenuButton.setText("Disable Students Menu");
					networkedContentManager.setRemoteMenuOn(true);
				}
			}
		});

		final SimpleButton clearTableButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		clearTableButton.setAutoFitSize(false);
		clearTableButton.setText("Clear Student Table");
		clearTableButton.setBackgroundColour(Color.lightGray);
		clearTableButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				networkedContentManager.clearRemoteDeskTop();
			}
		});
		
		SimpleButton broadcastButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		broadcastButton.setAutoFitSize(false);
		broadcastButton.setText("Broadcast Data");
		broadcastButton.setBackgroundColour(Color.lightGray);
		broadcastButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {

				networkedContentManager.broadcastCurrentDeskTop();
			}
		});

		final SimpleButton synchroniseButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		synchroniseButton.setAutoFitSize(false);
		synchroniseButton.setText("Synchronise");
		synchroniseButton.setBackgroundColour(Color.lightGray);
		synchroniseButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {

				if (networkedContentManager.isSynchronisationOn()) {
					synchroniseButton.setText("Synchronise");
					networkedContentManager.setSynchronisationOn(false);
				} else {
					synchroniseButton.setText("Stop Synchronise");
					networkedContentManager.setSynchronisationOn(true);
				}
				
			}
		});
		
		final SimpleButton collaborationButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		collaborationButton.setAutoFitSize(false);
		collaborationButton.setText("Collaborate");
		collaborationButton.setBackgroundColour(Color.lightGray);
		collaborationButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {

				if (networkedContentManager.isBiSynchronisationEnabled()) {
					collaborationButton.setText("Collaborate");
					networkedContentManager.setBiSynchronisationEnabled(false);

					synchroniseButton.setText("Synchronise");
					networkedContentManager.setSynchronisationOn(false);
				} else {
					collaborationButton.setText("Stop Collaborate");
					networkedContentManager.setBiSynchronisationEnabled(true);

					synchroniseButton.setText("Stop Synchronise");
					networkedContentManager.setSynchronisationOn(true);
				}
			}
		});
		
		final SimpleButton swapTableButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		swapTableButton.setAutoFitSize(false);
		swapTableButton.setText("Swap Tables");
		swapTableButton.setBackgroundColour(Color.lightGray);
		swapTableButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				tableSwapDialogue.setVisible(true);
			}
		});
		
		controlMenu.setLocalLocation(200, 200);
		controlMenu.addSubItem(blockButton);
		controlMenu.addSubItem(disableMenuButton);
		
		SimpleButton backToMainMenuButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		backToMainMenuButton.setAutoFitSize(false);
		backToMainMenuButton.setText("Back To Main Menu");
		backToMainMenuButton.setBackgroundColour(Color.lightGray);
		backToMainMenuButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				try {
					// setSafeExit();
					SynergyNetDesktop.getInstance().showMainMenu();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		
		final SimpleButton getDesktopButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		getDesktopButton.setAutoFitSize(false);
		getDesktopButton.setText("Get student desktops");
		getDesktopButton.setBackgroundColour(Color.lightGray);
		getDesktopButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (networkedContentManager.getRemoteDesktopController() != null) {
					if (getDesktopButton.getText().equals(
							"Get student desktops")) {
						networkedContentManager.getRemoteDesktopController()
								.requestRemoteDesktops(true);
						getDesktopButton.setText("Hide student desktops");
					} else {
						networkedContentManager.getRemoteDesktopController()
								.requestRemoteDesktops(false);
						getDesktopButton.setText("Get student desktops");
					}
				}
			}
		});

		final SimpleButton getProjectorButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		getProjectorButton.setAutoFitSize(false);
		getProjectorButton.setText("Search projectors");
		getProjectorButton.setBackgroundColour(Color.lightGray);
		getProjectorButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (networkedContentManager.getProjectorController() != null) {
					networkedContentManager.getProjectorController()
							.demandProjectors();
				}
			}
		});
		
		final ListContainer sendDataToMenu = (ListContainer) contentSystem
				.createContentItem(ListContainer.class);
		sendDataToMenu.setWidth(300);
		sendDataToMenu.setItemHeight(30);
		sendDataToMenu.getBackgroundFrame().setBackgroundColour(Color.gray);
		sendDataToMenu.addListEventListener(new ListEventAdapter() {
			public void listHiden() {
				sendDataToMenu.clear();
			}

			@Override
			public void listShown() {
				if (networkedContentManager.getTableCommsClientService() == null) {
					return;
				}
				List<TableIdentity> onlineTables = networkedContentManager
						.getTableCommsClientService().getCurrentlyOnline();
				for (final TableIdentity table : onlineTables) {
					if (table.hashCode() != TableIdentity.getTableIdentity()
							.hashCode()) {
						SimpleButton onlineTableButton = (SimpleButton) contentSystem
								.createContentItem(SimpleButton.class);
						onlineTableButton.setAutoFitSize(false);
						onlineTableButton.setText(table.toString());
						onlineTableButton.setBackgroundColour(Color.lightGray);
						onlineTableButton
								.addButtonListener(new SimpleButtonAdapter() {
									public void buttonClicked(SimpleButton b,
											long id, float x, float y,
											float pressure) {
										
										networkedContentManager
												.sendDataTo(table);
										sendDataToMenu.setVisible(false);
									}
								});
						
						sendDataToMenu.addSubItem(onlineTableButton);
					}
				}
			}
		});

		final ListContainer requestDataFromMenu = (ListContainer) contentSystem
				.createContentItem(ListContainer.class);
		requestDataFromMenu.setWidth(300);
		requestDataFromMenu.setItemHeight(30);
		requestDataFromMenu.getBackgroundFrame()
				.setBackgroundColour(Color.gray);
		requestDataFromMenu.addListEventListener(new ListEventAdapter() {
			public void listHiden() {
				requestDataFromMenu.clear();
			}

			@Override
			public void listShown() {
				if (networkedContentManager.getTableCommsClientService() == null) {
					return;
				}
				List<TableIdentity> onlineTables = networkedContentManager
						.getTableCommsClientService().getCurrentlyOnline();
				for (final TableIdentity table : onlineTables) {
					if (table.hashCode() != TableIdentity.getTableIdentity()
							.hashCode()) {
						SimpleButton onlineTableButton = (SimpleButton) contentSystem
								.createContentItem(SimpleButton.class);
						onlineTableButton.setAutoFitSize(false);
						onlineTableButton.setText(table.toString());
						onlineTableButton.setBackgroundColour(Color.lightGray);
						onlineTableButton
								.addButtonListener(new SimpleButtonAdapter() {
									public void buttonClicked(SimpleButton b,
											long id, float x, float y,
											float pressure) {
										
										networkedContentManager
												.requestDataFrom(table);
										requestDataFromMenu.setVisible(false);
									}
								});
						
						requestDataFromMenu.addSubItem(onlineTableButton);
					}
				}
			}
		});

		final SimpleButton flickButton = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		flickButton.setAutoFitSize(false);
		flickButton.setText("Enable Item Flick");
		flickButton.setBackgroundColour(Color.lightGray);
		flickButton.addButtonListener(new SimpleButtonAdapter() {
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {

				if (!networkedContentManager.getNetworkedFlickController()
						.isFlickEnabled()) {
					flickButton.setText("Disable Item Flick");
					networkedContentManager.getNetworkedFlickController()
							.setNetworkFlickEnabled(true);
				} else {
					flickButton.setText("Enable Item Flick");
					networkedContentManager.getNetworkedFlickController()
							.setNetworkFlickEnabled(false);
				}
			}
		});
		
		if (subAppMenu != null) {
			controlMenu.addSubMenu(subAppMenu.getSubAppMenu(), "Set Tables");
		}
		controlMenu.addSubItem(clearTableButton);
		controlMenu.addSubItem(broadcastButton);
		controlMenu.addSubItem(synchroniseButton);
		controlMenu.addSubItem(collaborationButton);
		controlMenu.addSubMenu(sendDataToMenu, "Send Data To...");
		controlMenu.addSubMenu(requestDataFromMenu, "Request Data From...");
		controlMenu.addSubItem(swapTableButton);
		controlMenu.addSubItem(flickButton);
		controlMenu.addSubItem(getDesktopButton);
		controlMenu.addSubItem(getProjectorButton);
		controlMenu.addSubItem(backToMainMenuButton);

		return controlMenu;
	}

	/**
	 * Sets the location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setLocation(float x, float y) {
		controlMenu.setLocalLocation(x, y);
	}
}
