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

package synergynetframework.appsystem.services.net.networkedcontentmanager.messagehandler;

import java.util.concurrent.Callable;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.BlockInteraction;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.BroadcastData;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.ClearTable;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.EnableBiSynchronisation;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.EnableMenu;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.LoadData;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.RequireDataFrom;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.SendDataTo;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.SwapTable;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.SwapTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.SynchroniseData;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.AnnounceTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.EnableFlickMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.RegisterTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.TransferableContentItem;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.UnregisterTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.ClearProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.ProjectorResponse;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.ReleaseProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.SearchProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.SendDataToProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.SynchroniseProjectorData;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.BroadcastEnableRemoteDesktop;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.SendClientDataTo;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.SynchroniseRemoteDesktopData;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.UnicastEnableRemoteDesktop;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick.TransferController;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;

import com.jme.util.GameTaskQueueManager;

import core.SynergyNetDesktop;

/**
 * The Class DefaultMessageHandler.
 */
public class DefaultMessageHandler implements TableCommsApplicationListener {

	/** The contentsys. */
	@SuppressWarnings("unused")
	private ContentSystem contentsys;

	/** The networked content manager. */
	private NetworkedContentManager networkedContentManager;

	/** The transfer controller. */
	private TransferController transferController;
	
	/**
	 * Instantiates a new default message handler.
	 *
	 * @param cs
	 *            the cs
	 * @param networkedContentManager
	 *            the networked content manager
	 */
	public DefaultMessageHandler(ContentSystem cs,
			NetworkedContentManager networkedContentManager) {
		this.contentsys = cs;
		this.networkedContentManager = networkedContentManager;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.
	 * TableCommsApplicationListener#messageReceived(java.lang.Object)
	 */
	public void messageReceived(final Object obj) {

		if (TableIdentity.getTableIdentity().hashCode() == ((TableMessage) obj)
				.getSender().hashCode()) {
			return;
		}

		if (obj instanceof LoadData) {
			LoadData smd = (LoadData) obj;
			networkedContentManager.loadContent(smd.getPath(), smd.getTitle());
		}

		else if (obj instanceof BlockInteraction) {
			SynergyNetDesktop
					.getInstance()
					.getMultiTouchInputComponent()
					.setMultiTouchInputEnabled(
							!((BlockInteraction) obj).isRemoteLocked());
		}

		else if (obj instanceof ClearTable) {
			networkedContentManager.removeAllItems();
		}

		else if (obj instanceof BroadcastData) {
			BroadcastData broadcastData = (BroadcastData) obj;
			networkedContentManager.loadContent(broadcastData.getItems());
		}

		else if (obj instanceof SendClientDataTo) {
			SendClientDataTo broadcastClientData = (SendClientDataTo) obj;
			networkedContentManager.getRemoteDesktopController()
					.loadRemoteDesktopContent(((TableMessage) obj).getSender(),
							broadcastClientData.getItems());
		}
		
		else if (obj instanceof SendDataToProjector) {
			if (networkedContentManager.getProjectorController() != null) {
				networkedContentManager.getProjectorController()
						.loadProjectorContent(((TableMessage) obj).getSender(),
								((SendDataToProjector) obj).getItems());
			}
		}

		else if (obj instanceof SendDataTo) {
			SendDataTo sendDataTo = (SendDataTo) obj;
			networkedContentManager.loadContent(sendDataTo.getItems());
		}

		else if (obj instanceof RequireDataFrom) {
			RequireDataFrom requireDataFrom = (RequireDataFrom) obj;
			networkedContentManager.sendDataTo(requireDataFrom.getSender());
		}

		else if (obj instanceof EnableMenu) {
			EnableMenu enableMenu = (EnableMenu) obj;
			networkedContentManager.enableMenu(enableMenu.isMenuEnabled());
		}

		else if (obj instanceof EnableBiSynchronisation) {
			EnableBiSynchronisation enableBiSynchronisation = (EnableBiSynchronisation) obj;
			networkedContentManager
					.enableSynchronisation(enableBiSynchronisation
							.isBiSynchronisationOn());
		}

		else if (obj instanceof SynchroniseRemoteDesktopData) {
			if (networkedContentManager.getRemoteDesktopController() != null) {
				SynchroniseRemoteDesktopData synchroniseDate = (SynchroniseRemoteDesktopData) obj;
				networkedContentManager.getRemoteDesktopController()
						.synchroniseRemoteDesktopData(
								((TableMessage) obj).getSender(),
								synchroniseDate.getItems());
			}
		}

		else if (obj instanceof SynchroniseData) {
			SynchroniseData synchroniseDate = (SynchroniseData) obj;
			networkedContentManager.synchroniseData(synchroniseDate.getItems());
		}

		else if (obj instanceof SwapTable) {
			SwapTable swapTable = (SwapTable) obj;
			networkedContentManager.sendSwapTableMessage(swapTable.getTable2());
		}

		else if (obj instanceof SwapTableMessage) {
			SwapTableMessage swapTableMessage = (SwapTableMessage) obj;
			networkedContentManager.sendDataTo(swapTableMessage.getSender());
			networkedContentManager.loadContent(swapTableMessage.getItems());
		}

		else if (obj instanceof BroadcastEnableRemoteDesktop) {
			if (networkedContentManager.getRemoteDesktopController() != null) {
				BroadcastEnableRemoteDesktop msg = (BroadcastEnableRemoteDesktop) obj;
				networkedContentManager.getRemoteDesktopController()
						.enableRemoteDesktop(msg.getSender(),
								msg.isRemoteDesktopEnabled());
			}
		}

		else if (obj instanceof UnicastEnableRemoteDesktop) {
			if (networkedContentManager.getRemoteDesktopController() != null) {
				UnicastEnableRemoteDesktop msg = (UnicastEnableRemoteDesktop) obj;
				networkedContentManager.getRemoteDesktopController()
						.enableRemoteDesktop(msg.getSender(),
								msg.isRemoteDesktopEnabled());
			}
		}

		else if (obj instanceof SearchProjector) {
			if (networkedContentManager.getProjectorController() != null) {
				networkedContentManager.getProjectorController()
						.leaseProjector(((SearchProjector) obj).getSender());
			}
		}

		else if (obj instanceof ProjectorResponse) {
			if ((networkedContentManager.getProjectorController() != null)
					&& ((ProjectorResponse) obj).isLeaseSucceed()) {
				networkedContentManager.getProjectorController()
						.constructProjector(((TableMessage) obj).getSender());
			}
		}

		else if (obj instanceof SynchroniseProjectorData) {
			if (networkedContentManager.getProjectorController() != null) {
				SynchroniseProjectorData synchroniseData = (SynchroniseProjectorData) obj;
				networkedContentManager.getProjectorController()
						.synchroniseProjectorData(
								synchroniseData.getSourceTableIdentity(),
								synchroniseData.getItems());
			}
		}

		else if (obj instanceof ClearProjector) {
			if (networkedContentManager.getProjectorController() != null) {
				networkedContentManager.getProjectorController()
						.clearProjectorScreen();
			}
		}

		else if (obj instanceof ReleaseProjector) {
			if (networkedContentManager.getProjectorController() != null) {
				networkedContentManager.getProjectorController()
						.releaseProjector();
			}
		}

		else if (transferController != null) {
			if (obj instanceof EnableFlickMessage) {
				networkedContentManager.getNetworkedFlickController()
						.enableFlick(
								((EnableFlickMessage) obj).isFlickEnabled());
			} else if (obj instanceof AnnounceTableMessage) {
				AnnounceTableMessage msg = (AnnounceTableMessage) obj;
				transferController.registerRemoteTable(msg.getTableInfo());
				transferController.sendRegistrationMessage(msg.getSender());
			} else if (obj instanceof RegisterTableMessage) {
				RegisterTableMessage msg = (RegisterTableMessage) obj;
				transferController.registerRemoteTable(msg.getTableInfo());
			} else if (obj instanceof UnregisterTableMessage) {
				transferController
						.cleanUpUnregisteredTable((UnregisterTableMessage) obj);
			} else if (obj instanceof TransferableContentItem) {
				GameTaskQueueManager.getManager().update(
						new Callable<Object>() {
							public Object call() throws Exception {
								ContentItem item = transferController
										.applyTransferableContentItem((TransferableContentItem) obj);
								networkedContentManager.loadContentItem(item);
								return null;
							}
						});
			}
		}
	}
	
	/**
	 * Sets the transfer controller.
	 *
	 * @param transferController
	 *            the new transfer controller
	 */
	public void setTransferController(TransferController transferController) {
		this.transferController = transferController;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.tablecomms.client.
	 * TableCommsApplicationListener#tableDisconnected()
	 */
	@Override
	public void tableDisconnected() {
		
	}
	
}
