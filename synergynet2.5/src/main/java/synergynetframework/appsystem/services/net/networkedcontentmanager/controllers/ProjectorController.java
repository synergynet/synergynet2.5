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

package synergynetframework.appsystem.services.net.networkedcontentmanager.controllers;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.ClearProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.ProjectorResponse;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.ReleaseProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.SearchProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.SendDataToProjector;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.projector.SynchroniseProjectorData;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.ProjectorNode;

import com.jme.system.DisplaySystem;

/**
 * The Class ProjectorController.
 */
public class ProjectorController {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(ProjectorController.class.getName());

	/** The controller classes. */
	private ArrayList<Class<?>> controllerClasses;

	/** The is projector leased. */
	private boolean isProjectorLeased = false;

	/** The networked content manager. */
	protected NetworkedContentManager networkedContentManager;

	/** The online projectors. */
	protected HashMap<TableIdentity, ProjectorNode> onlineProjectors = new HashMap<TableIdentity, ProjectorNode>();

	/** The projector classes. */
	private ArrayList<Class<?>> projectorClasses;

	/**
	 * Instantiates a new projector controller.
	 *
	 * @param networkedContentManager
	 *            the networked content manager
	 * @param controllerClasses
	 *            the controller classes
	 * @param projectorClasses
	 *            the projector classes
	 */
	public ProjectorController(NetworkedContentManager networkedContentManager,
			ArrayList<Class<?>> controllerClasses,
			ArrayList<Class<?>> projectorClasses) {
		this.networkedContentManager = networkedContentManager;
		this.controllerClasses = controllerClasses;
		this.projectorClasses = projectorClasses;
	}

	/**
	 * Clear projector screen.
	 */
	public void clearProjectorScreen() {
		networkedContentManager.getContentSystem().removeAllContentItems();
	}

	/**
	 * Construct projector.
	 *
	 * @param tableId
	 *            the table id
	 */
	public void constructProjector(TableIdentity tableId) {
		ProjectorNode projector = new ProjectorNode(tableId,
				networkedContentManager);
		projector.setLocation(200, 200);
		onlineProjectors.put(tableId, projector);
		log.info("Construct projector");
	}
	
	/**
	 * Demand projectors.
	 */
	public void demandProjectors() {
		for (Class<?> targetClass : projectorClasses) {
			networkedContentManager
					.sendMessage(new SearchProjector(targetClass));
		}
		log.info("Send message to search projector");
	}
	
	/**
	 * Gets the online projectors.
	 *
	 * @return the online projectors
	 */
	public List<ProjectorNode> getOnlineProjectors() {
		return new ArrayList<ProjectorNode>(onlineProjectors.values());
	}
	
	/**
	 * Lease projector.
	 *
	 * @param id
	 *            the id
	 */
	public synchronized void leaseProjector(TableIdentity id) {
		if (isProjectorLeased) {
			for (Class<?> sourceClass : controllerClasses) {
				networkedContentManager.sendMessage(new ProjectorResponse(
						sourceClass, false, id));
			}
		} else {
			for (Class<?> sourceClass : controllerClasses) {
				networkedContentManager.sendMessage(new ProjectorResponse(
						sourceClass, true, id));
			}
			isProjectorLeased = true;
			log.info("Projector is leased");

			MultiLineTextLabel info = (MultiLineTextLabel) networkedContentManager
					.getContentSystem().createContentItem(
							MultiLineTextLabel.class);
			info.setCRLFSeparatedString("Input detected from table-" + id);
			info.setFont(new Font("Arial", Font.PLAIN, 28));
			info.setTextColour(Color.white);
			info.setBackgroundColour(Color.black);
			info.setLocalLocation(
					DisplaySystem.getDisplaySystem().getWidth() / 2,
					DisplaySystem.getDisplaySystem().getHeight() / 2);
			log.info("Input detected from table-" + id);

		}
	}
	
	/**
	 * Load projector content.
	 *
	 * @param sender
	 *            the sender
	 * @param items
	 *            the items
	 */
	public void loadProjectorContent(TableIdentity sender,
			List<ContentItem> items) {
		networkedContentManager.getContentSystem().removeAllContentItems();
		for (ContentItem item : items) {
			networkedContentManager.getContentSystem().addContentItem(item);
			if (item instanceof OrthoContentItem) {
				((OrthoContentItem) item).setRotateTranslateScalable(false);
			}
			networkedContentManager.getOnlineItems().put(item.getName(), item);
		}
		
		for (NetworkedContentListener l : networkedContentManager
				.getNetworkedContentListeners()) {
			l.contentLoaded();
		}

	}
	
	/**
	 * Release projector.
	 */
	public synchronized void releaseProjector() {
		this.clearProjectorScreen();
		isProjectorLeased = false;

		log.info("Release proejctor.");
	}
	
	/**
	 * Send clear projector message.
	 *
	 * @param tableId
	 *            the table id
	 */
	public void sendClearProjectorMessage(TableIdentity tableId) {
		for (Class<?> targetClass : projectorClasses) {
			networkedContentManager.sendMessage(new ClearProjector(targetClass,
					tableId));
		}
		
		log.info("Clear projector");
	}
	
	/**
	 * Send data to projector.
	 *
	 * @param tableId
	 *            the table id
	 * @param onlineItems
	 *            the online items
	 */
	public void sendDataToProjector(TableIdentity tableId,
			Map<String, ContentItem> onlineItems) {
		for (Class<?> targetClass : projectorClasses) {
			networkedContentManager.sendMessage(new SendDataToProjector(
					targetClass, onlineItems.values(), tableId));
		}

		log.info("Send content to projector");
	}
	
	/**
	 * Send projector sync message.
	 *
	 * @param clientTableId
	 *            the client table id
	 * @param projectorTableId
	 *            the projector table id
	 * @param synchronisedItems
	 *            the synchronised items
	 */
	public void sendProjectorSyncMessage(TableIdentity clientTableId,
			TableIdentity projectorTableId,
			Map<String, Map<String, String>> synchronisedItems) {
		for (Class<?> targetClass : projectorClasses) {
			SynchroniseProjectorData msg = new SynchroniseProjectorData(
					targetClass, synchronisedItems, projectorTableId);
			msg.setSourceTableIdentity(clientTableId);
			networkedContentManager.sendMessage(msg);
		}

		log.info("Send synchronised items to projector");
	}
	
	/**
	 * Send release projector message.
	 *
	 * @param tableId
	 *            the table id
	 */
	public void sendReleaseProjectorMessage(TableIdentity tableId) {
		for (Class<?> targetClass : projectorClasses) {
			networkedContentManager.sendMessage(new ReleaseProjector(
					targetClass, tableId));
		}
		
		log.info("Send release projector message");
	}
	
	/**
	 * Synchronise projector data.
	 *
	 * @param tableId
	 *            the table id
	 * @param synchronisedItems
	 *            the synchronised items
	 */
	public void synchroniseProjectorData(TableIdentity tableId,
			Map<String, Map<String, String>> synchronisedItems) {
		if (synchronisedItems.size() == 0) {
			return;
		}
		for (String name : synchronisedItems.keySet()) {
			if (!networkedContentManager.getOnlineItems().containsKey(
					tableId + "@" + name)) {
				continue;
			}
			OrthoContentItem item = (OrthoContentItem) this.networkedContentManager
					.getOnlineItems().get(tableId + "@" + name);

			for (NetworkedContentListener l : networkedContentManager
					.getNetworkedContentListeners()) {
				l.renderSynchronisedDate(item, synchronisedItems.get(name));
			}
		}
	}

	/**
	 * Unregister projector.
	 *
	 * @param tableId
	 *            the table id
	 */
	public void unregisterProjector(TableIdentity tableId) {
		if (onlineProjectors.containsKey(tableId)) {
			ProjectorNode projector = onlineProjectors.get(tableId);
			networkedContentManager.getContentSystem().removeContentItem(
					projector.getNodeContainer());
			onlineProjectors.remove(tableId);

			log.info("Unregister projector");
		}
	}
}
