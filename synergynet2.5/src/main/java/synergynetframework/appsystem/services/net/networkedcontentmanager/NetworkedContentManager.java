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

package synergynetframework.appsystem.services.net.networkedcontentmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.controllers.NetworkedFlickController;
import synergynetframework.appsystem.services.net.networkedcontentmanager.controllers.ProjectorController;
import synergynetframework.appsystem.services.net.networkedcontentmanager.controllers.RemoteDesktopController;
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
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import apps.conceptmap.utility.GraphManager;

/**
 * The Class NetworkedContentManager.
 */
public class NetworkedContentManager {
	
	/** The Constant BACKITEMSLIST. */
	public final static String BACKITEMSLIST = "Local Desktop";

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(NetworkedContentManager.class.getName());

	/** The Constant ONLINEITEMSLIST. */
	public final static String ONLINEITEMSLIST = "Online Desktop";

	/** The allowed to send message. */
	protected boolean allowedToSendMessage = true;

	/** The allow single touch free move. */
	protected boolean allowSingleTouchFreeMove = false;

	/** The back items list. */
	protected Map<String, ContentItem> backItemsList = new HashMap<String, ContentItem>();

	/** The comms. */
	protected TableCommsClientService comms;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The controlled menu. */
	protected OrthoContainer controlledMenu;

	/** The graph manager. */
	protected GraphManager graphManager;

	/** The isbackup list enabled. */
	protected boolean isbackupListEnabled = false;

	/** The is bi synchronisation enabled. */
	protected boolean isBiSynchronisationEnabled = false;

	/** The is menu controller enabled. */
	protected boolean isMenuControllerEnabled = false;

	/** The is menu enabled. */
	protected boolean isMenuEnabled = true;

	/** The is remote locked. */
	protected boolean isRemoteLocked = false;

	/** The is remote menu on. */
	protected boolean isRemoteMenuOn = true;

	/** The is synchronisation on. */
	protected boolean isSynchronisationOn = false;

	/** The list store local item. */
	protected String listStoreLocalItem = NetworkedContentManager.ONLINEITEMSLIST;

	/** The networked content listener. */
	protected List<NetworkedContentListener> networkedContentListener = new ArrayList<NetworkedContentListener>();

	/** The network flick controller. */
	protected NetworkedFlickController networkFlickController;

	/** The online items list. */
	protected Map<String, ContentItem> onlineItemsList = new HashMap<String, ContentItem>();

	/** The projector controller. */
	protected ProjectorController projectorController;
	
	/** The receiver classes. */
	protected List<Class<?>> receiverClasses;

	/** The remote desktop controller. */
	protected RemoteDesktopController remoteDesktopController;

	/** The sychronised data. */
	protected Map<String, Map<String, String>> sychronisedData = new HashMap<String, Map<String, String>>();
	
	/**
	 * Instantiates a new networked content manager.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param comms
	 *            the comms
	 * @param receiverClasses
	 *            the receiver classes
	 */
	public NetworkedContentManager(ContentSystem contentSystem,
			TableCommsClientService comms, List<Class<?>> receiverClasses) {
		this.contentSystem = contentSystem;
		this.comms = comms;
		this.receiverClasses = receiverClasses;
		networkFlickController = new NetworkedFlickController(this);
		log.info("Networked flick controller created");
	}

	/**
	 * Adds the networked content listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addNetworkedContentListener(NetworkedContentListener l) {
		if (this.networkedContentListener == null) {
			this.networkedContentListener = new ArrayList<NetworkedContentListener>();
		}

		if (!this.networkedContentListener.contains(l)) {
			this.networkedContentListener.add(l);
		}
	}

	/**
	 * Allowed to sendmessage.
	 *
	 * @param allowedToSendMessage
	 *            the allowed to send message
	 */
	public void allowedToSendmessage(boolean allowedToSendMessage) {
		this.allowedToSendMessage = allowedToSendMessage;
	}

	/**
	 * Backup local items.
	 */
	public void backupLocalItems() {
		if (!this.isbackupListEnabled) {
			return;
		}

		if (this.listStoreLocalItem
				.equals(NetworkedContentManager.BACKITEMSLIST)) {
			return;
		}

		if (this.listStoreLocalItem
				.equals(NetworkedContentManager.ONLINEITEMSLIST)) {
			this.removeAllItemsInBackList();
			for (ContentItem item : this.onlineItemsList.values()) {
				this.backItemsList.put(item.getName(), item);
				item.setVisible(false);
			}
			this.listStoreLocalItem = NetworkedContentManager.BACKITEMSLIST;

			this.onlineItemsList.clear();

			for (NetworkedContentListener l : this.networkedContentListener) {
				l.channelSwitched();
			}
		}

		log.info("Backup online items");

	}

	/**
	 * Block remote desktop.
	 *
	 * @param isRemoteLocked
	 *            the is remote locked
	 */
	public void blockRemoteDesktop(boolean isRemoteLocked) {

		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new BlockInteraction(targetClass, isRemoteLocked));
		}

		log.info("Block remote tables");
	}

	/**
	 * Broadcast current desk top.
	 */
	public void broadcastCurrentDeskTop() {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new BroadcastData(targetClass, onlineItemsList.values()));
		}

		log.info("Broadcast current desktop");
	}

	/**
	 * Clear remote desk top.
	 */
	public void clearRemoteDeskTop() {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new ClearTable(targetClass));
		}

		log.info("Clear remote desktops");
	}

	/**
	 * Creates the projector controller.
	 *
	 * @param controllerClasses
	 *            the controller classes
	 * @param projectorClasses
	 *            the projector classes
	 */
	public void createProjectorController(
			ArrayList<Class<?>> controllerClasses,
			ArrayList<Class<?>> projectorClasses) {
		projectorController = new ProjectorController(this, controllerClasses,
				projectorClasses);
		log.info("Projector controller created");
	}

	/**
	 * Creates the remote desktop controller.
	 *
	 * @param controllerClasses
	 *            the controller classes
	 * @param targetClasses
	 *            the target classes
	 */
	public void createRemoteDesktopController(
			ArrayList<Class<?>> controllerClasses,
			ArrayList<Class<?>> targetClasses) {
		remoteDesktopController = new RemoteDesktopController(this,
				controllerClasses, targetClasses);
		log.info("Remote desktop controller created");
	}

	/**
	 * Enable bi synchronisation.
	 *
	 * @param isBiSynchronisationEnabled
	 *            the is bi synchronisation enabled
	 */
	public void enableBiSynchronisation(boolean isBiSynchronisationEnabled) {
		for (Class<?> targetClass : this.receiverClasses) {
			this.sendMessage(new EnableBiSynchronisation(targetClass,
					isBiSynchronisationEnabled));
		}

		if (isBiSynchronisationEnabled) {
			log.info("Turn on synchronisation");
		} else {
			log.info("Turn off synchronisation");
		}
	}
	
	/**
	 * Enable menu.
	 *
	 * @param isMenuEnabled
	 *            the is menu enabled
	 */
	public void enableMenu(boolean isMenuEnabled) {
		if (!this.isMenuControllerEnabled) {
			return;
		}
		if (this.controlledMenu == null) {
			return;
		}

		this.isMenuEnabled = isMenuEnabled;

		if (!this.isMenuEnabled) {
			controlledMenu.setVisible(false);
		} else {
			controlledMenu.setVisible(true);
		}
	}

	/**
	 * Enable menu controller.
	 *
	 * @param isMenuControllerOn
	 *            the is menu controller on
	 * @param menu
	 *            the menu
	 */
	public void enableMenuController(boolean isMenuControllerOn,
			OrthoContainer menu) {
		if (!isMenuControllerOn) {
			this.controlledMenu = null;
			this.isMenuControllerEnabled = false;
		} else {
			if (menu == null) {
				return;
			}
			this.controlledMenu = menu;
			this.isMenuControllerEnabled = true;
		}
	}
	
	/**
	 * Enable remote menu.
	 *
	 * @param isRemoteMenuOn
	 *            the is remote menu on
	 */
	public void enableRemoteMenu(boolean isRemoteMenuOn) {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new EnableMenu(targetClass, isRemoteMenuOn));
		}

		if (isRemoteMenuOn) {
			log.info("Turn on remote menu");
		} else {
			log.info("Turn off remote menu");
		}
	}

	/**
	 * Enable synchronisation.
	 *
	 * @param isSynchronisationOn
	 *            the is synchronisation on
	 */
	public void enableSynchronisation(boolean isSynchronisationOn) {
		this.isSynchronisationOn = isSynchronisationOn;
	}

	/**
	 * Gets the content system.
	 *
	 * @return the content system
	 */
	public ContentSystem getContentSystem() {
		return contentSystem;
	}
	
	/**
	 * Gets the current screen channel.
	 *
	 * @return the current screen channel
	 */
	public String getCurrentScreenChannel() {
		if (this.listStoreLocalItem
				.equals(NetworkedContentManager.BACKITEMSLIST)) {
			return "Online Desktop";
		} else {
			return "Local Desktop";
		}

	}

	/**
	 * Gets the graph manager.
	 *
	 * @return the graph manager
	 */
	public GraphManager getGraphManager() {
		return graphManager;
	}

	/**
	 * Gets the networked content listeners.
	 *
	 * @return the networked content listeners
	 */
	public List<NetworkedContentListener> getNetworkedContentListeners() {
		return networkedContentListener;
	}

	/**
	 * Gets the networked flick controller.
	 *
	 * @return the networked flick controller
	 */
	public NetworkedFlickController getNetworkedFlickController() {
		return networkFlickController;
	}

	/**
	 * Gets the online items.
	 *
	 * @return the online items
	 */
	public Map<String, ContentItem> getOnlineItems() {
		return onlineItemsList;
	}
	
	/**
	 * Gets the projector controller.
	 *
	 * @return the projector controller
	 */
	public ProjectorController getProjectorController() {
		return projectorController;
	}

	/**
	 * Gets the receiver classes.
	 *
	 * @return the receiver classes
	 */
	public List<Class<?>> getReceiverClasses() {
		return receiverClasses;
	}

	/**
	 * Gets the remote desktop controller.
	 *
	 * @return the remote desktop controller
	 */
	public RemoteDesktopController getRemoteDesktopController() {
		return remoteDesktopController;
	}

	/**
	 * Gets the sychronised data.
	 *
	 * @return the sychronised data
	 */
	public Map<String, Map<String, String>> getSychronisedData() {
		return sychronisedData;
	}

	/**
	 * Gets the table comms client service.
	 *
	 * @return the table comms client service
	 */
	public TableCommsClientService getTableCommsClientService() {
		return comms;
	}

	/**
	 * Checks if is allowed to send message.
	 *
	 * @return true, if is allowed to send message
	 */
	public boolean isAllowedToSendMessage() {
		return this.allowedToSendMessage;
	}

	/**
	 * Checks if is backup list enabled.
	 *
	 * @return true, if is backup list enabled
	 */
	public boolean isbackupListEnabled() {
		return isbackupListEnabled;
	}

	/**
	 * Checks if is bi synchronisation enabled.
	 *
	 * @return true, if is bi synchronisation enabled
	 */
	public boolean isBiSynchronisationEnabled() {
		return isBiSynchronisationEnabled;
	}
	
	/**
	 * Checks if is menu enabled.
	 *
	 * @return true, if is menu enabled
	 */
	public boolean isMenuEnabled() {
		return this.isMenuEnabled;
	}

	/**
	 * Checks if is remote locked.
	 *
	 * @return true, if is remote locked
	 */
	public boolean isRemoteLocked() {
		return isRemoteLocked;
	}

	/**
	 * Checks if is remote menu on.
	 *
	 * @return true, if is remote menu on
	 */
	public boolean isRemoteMenuOn() {
		return isRemoteMenuOn;
	}

	/**
	 * Checks if is synchronisation on.
	 *
	 * @return true, if is synchronisation on
	 */
	public boolean isSynchronisationOn() {
		return isSynchronisationOn;
	}
	
	/**
	 * Load content.
	 *
	 * @param collection
	 *            the collection
	 */
	public void loadContent(List<ContentItem> collection) {
		if (this.isbackupListEnabled) {
			backupLocalItems();

		}

		this.removeAllItems();

		for (ContentItem item : collection) {
			contentSystem.addContentItem(item);
			if (item instanceof OrthoContentItem) {
				((OrthoContentItem) item)
						.allowSingleTouchFreeMove(allowSingleTouchFreeMove);
			}
			onlineItemsList.put(item.getName(), item);
			
		}
		
		for (NetworkedContentListener l : this.networkedContentListener) {
			l.contentLoaded();
		}
	}
	
	/**
	 * Load content.
	 *
	 * @param filePath
	 *            the file path
	 * @param name
	 *            the name
	 */
	public void loadContent(String filePath, String name) {
		if (this.isbackupListEnabled) {
			this.removeAllItemsInBackList();
			this.listStoreLocalItem = NetworkedContentManager.ONLINEITEMSLIST;
		}

		this.removeAllItems();
		this.loadLocalContent(filePath, name);

		for (NetworkedContentListener l : this.networkedContentListener) {
			l.contentLoaded();
		}
	}
	
	/**
	 * Load content in remote table.
	 *
	 * @param filePath
	 *            the file path
	 * @param name
	 *            the name
	 */
	public void loadContentInRemoteTable(String filePath, String name) {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new LoadData(targetClass, filePath, name));
		}
		log.info("Broadcast command to load data on remote tables");

	}
	
	/**
	 * Load content item.
	 *
	 * @param item
	 *            the item
	 */
	public void loadContentItem(ContentItem item) {
		if (item instanceof OrthoContentItem) {
			((OrthoContentItem) item)
					.allowSingleTouchFreeMove(allowSingleTouchFreeMove);
		}
		onlineItemsList.put(item.getName(), item);
		for (NetworkedContentListener l : this.networkedContentListener) {
			l.contentItemLoaded(item);
		}

		log.info(item.getClass().getName() + "-" + item.getName() + " loaded");
	}
	
	/**
	 * Load local content.
	 *
	 * @param filePath
	 *            the file path
	 * @param name
	 *            the name
	 */
	public void loadLocalContent(String filePath, String name) {
		
		this.removeAllItems();
		Set<ContentItem> contentItems = contentSystem
				.loadContentItems(filePath);
		for (ContentItem item : contentItems) {
			if (item instanceof OrthoContentItem) {
				((OrthoContentItem) item)
						.allowSingleTouchFreeMove(allowSingleTouchFreeMove);
			}
			onlineItemsList.put(item.getName(), item);
		}
		
		log.info("Content loaded from " + filePath);
	}
	
	/**
	 * Removes the all items.
	 */
	public void removeAllItems() {

		for (ContentItem item : onlineItemsList.values()) {
			contentSystem.removeContentItem(item);
		}

		onlineItemsList.clear();

		// temp statement only for the experiment on 19th oct, 2009
		backItemsList.clear();

		log.info("All items removed.");
	}
	
	/**
	 * Removes the all items in back list.
	 */
	public void removeAllItemsInBackList() {
		for (ContentItem item : backItemsList.values()) {
			contentSystem.removeContentItem(item);
		}

		backItemsList.clear();
		log.info("Items removed from offline list.");
	}
	
	/**
	 * Removes the content item.
	 *
	 * @param item
	 *            the item
	 */
	public void removeContentItem(ContentItem item) {
		onlineItemsList.remove(item.getName());
		contentSystem.removeContentItem(item);

		log.info(item.getClass().getName() + "-" + item.getName() + " removed.");
	}

	/**
	 * Removes the networked content listener.
	 *
	 * @param l
	 *            the l
	 */
	public void removeNetworkedContentListener(NetworkedContentListener l) {
		networkedContentListener.remove(l);
	}

	/**
	 * Removes the networked content listeners.
	 */
	public void removeNetworkedContentListeners() {
		networkedContentListener.clear();
	}

	/**
	 * Request data from.
	 *
	 * @param table
	 *            the table
	 */
	public void requestDataFrom(TableIdentity table) {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new RequireDataFrom(targetClass, table));
		}
		log.info("Request data from table-" + table.toString());
	}

	/**
	 * Send data to.
	 *
	 * @param table
	 *            the table
	 */
	public void sendDataTo(TableIdentity table) {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new SendDataTo(targetClass, onlineItemsList.values(),
					table));
		}
		log.info("Send data to table-" + table.toString());
	}

	/**
	 * Send message.
	 *
	 * @param obj
	 *            the obj
	 */
	public void sendMessage(Object obj) {
		if (!this.allowedToSendMessage) {
			return;
		}
		if (comms != null) {
			try {
				comms.sendMessage(obj);
			} catch (IOException e) {
				log.warning(e.toString());
			}
		}
	}

	/**
	 * Send swap table message.
	 *
	 * @param targetTable
	 *            the target table
	 */
	public void sendSwapTableMessage(TableIdentity targetTable) {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new SwapTableMessage(targetClass,
					onlineItemsList.values(), targetTable));
		}
		log.info("Send data for table swap to table-" + targetTable.toString());
	}

	/**
	 * Sets the backup list enabled.
	 *
	 * @param isbackupListEnabled
	 *            the new backup list enabled
	 */
	public void setbackupListEnabled(boolean isbackupListEnabled) {
		this.isbackupListEnabled = isbackupListEnabled;
	}

	/**
	 * Sets the bi synchronisation enabled.
	 *
	 * @param isBiSynchronisationEnabled
	 *            the new bi synchronisation enabled
	 */
	public void setBiSynchronisationEnabled(boolean isBiSynchronisationEnabled) {
		this.isBiSynchronisationEnabled = isBiSynchronisationEnabled;
		this.enableBiSynchronisation(isBiSynchronisationEnabled);
	}

	/**
	 * Sets the graph manager.
	 *
	 * @param graphManager
	 *            the new graph manager
	 */
	public void setGraphManager(GraphManager graphManager) {
		this.graphManager = graphManager;
	}

	/**
	 * Sets the remote locked.
	 *
	 * @param isRemoteLocked
	 *            the new remote locked
	 */
	public void setRemoteLocked(boolean isRemoteLocked) {
		this.isRemoteLocked = isRemoteLocked;
		this.blockRemoteDesktop(isRemoteLocked);
		if (isRemoteLocked) {
			log.info("Lock remote desktops");
		} else {
			log.info("Unlock remote desktops");
		}
	}
	
	/**
	 * Sets the remote menu on.
	 *
	 * @param isRemoteMenuOn
	 *            the new remote menu on
	 */
	public void setRemoteMenuOn(boolean isRemoteMenuOn) {
		this.isRemoteMenuOn = isRemoteMenuOn;
		this.enableRemoteMenu(isRemoteMenuOn);
	}

	/**
	 * Sets the synchronisation on.
	 *
	 * @param isSynchronisationOn
	 *            the new synchronisation on
	 */
	public void setSynchronisationOn(boolean isSynchronisationOn) {
		this.isSynchronisationOn = isSynchronisationOn;
		this.enableSynchronisation(isSynchronisationOn);
		if (isSynchronisationOn) {
			log.info("Turn on remote control");
		} else {
			log.info("Turn off remote control");
		}
	}

	/**
	 * Show local items.
	 */
	public void showLocalItems() {
		if (this.listStoreLocalItem
				.equals(NetworkedContentManager.BACKITEMSLIST)) {
			this.removeAllItems();
			switchItemList();
		} else {
			this.removeAllItemsInBackList();
		}
	}

	/**
	 * State update.
	 *
	 * @param tpf
	 *            the tpf
	 */
	public void stateUpdate(float tpf) {
		if (comms != null) {
			comms.update();
		}
		
		if ((this.sychronisedData.size() != 0) && this.isSynchronisationOn) {
			for (Class<?> targetClass : this.receiverClasses) {
				sendMessage(new SynchroniseData(targetClass,
						this.sychronisedData));
			}
		}
		if ((sychronisedData.size() != 0) && (remoteDesktopController != null)) {
			remoteDesktopController
					.sendRemoteDesktopSyncMessage(sychronisedData);
		}
		this.sychronisedData.clear();
	}
	
	/**
	 * Swap table.
	 *
	 * @param table1
	 *            the table1
	 * @param table2
	 *            the table2
	 */
	public void swapTable(TableIdentity table1, TableIdentity table2) {
		for (Class<?> targetClass : this.receiverClasses) {
			sendMessage(new SwapTable(targetClass, table1, table2));
		}
		log.info("Send swap table request to table-" + table1.toString());
		
	}

	/**
	 * Switch item list.
	 */
	public void switchItemList() {
		if (!this.isbackupListEnabled) {
			return;
		}

		Map<String, ContentItem> tempList = new HashMap<String, ContentItem>();

		for (ContentItem item : this.onlineItemsList.values()) {
			tempList.put(item.getName(), item);
		}

		this.onlineItemsList.clear();
		for (ContentItem item : this.backItemsList.values()) {
			onlineItemsList.put(item.getName(), item);
			item.setVisible(true);
		}

		this.backItemsList.clear();
		for (ContentItem item : tempList.values()) {
			backItemsList.put(item.getName(), item);
			item.setVisible(false);
		}

		if (this.listStoreLocalItem
				.equals(NetworkedContentManager.ONLINEITEMSLIST)) {
			this.listStoreLocalItem = NetworkedContentManager.BACKITEMSLIST;
		} else {
			this.listStoreLocalItem = NetworkedContentManager.ONLINEITEMSLIST;
		}

		for (NetworkedContentListener l : this.networkedContentListener) {
			l.channelSwitched();
		}

		log.info("Switch content channel between online item list and offline item list");

	}

	/**
	 * Synchronise data.
	 *
	 * @param synchronisedItems
	 *            the synchronised items
	 */
	public void synchroniseData(
			Map<String, Map<String, String>> synchronisedItems) {
		
		if (synchronisedItems.size() == 0) {
			return;
		}
		for (String name : synchronisedItems.keySet()) {
			if (!this.onlineItemsList.containsKey(name)) {
				continue;
			}
			OrthoContentItem item = (OrthoContentItem) this.onlineItemsList
					.get(name);

			for (NetworkedContentListener l : this.networkedContentListener) {
				l.renderSynchronisedDate(item, synchronisedItems.get(name));
			}
		}
	}
}
