/*
 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.remotecontrol.networkmanager.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.remotecontrol.networkmanager.managers.syncmanager.SyncManager;
import apps.remotecontrol.networkmanager.managers.syncmanager.SyncRenderer;
import apps.remotecontrol.networkmanager.messages.ConnectTablePortalMessage;
import apps.remotecontrol.networkmanager.messages.LockTableMessage;
import apps.remotecontrol.networkmanager.messages.PostItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.PostWorkspacePortalMessage;
import apps.remotecontrol.networkmanager.messages.RequestItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.UnicastAlivePortalMessage;
import apps.remotecontrol.networkmanager.messages.UpdateContentPortalMessage;
import apps.remotecontrol.tableportal.TablePortal;
import apps.remotecontrol.tableportal.WorkspaceManager;
import apps.remotecontrol.tableportal.TablePortal.RemoteTableMode;

import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import core.SynergyNetDesktop;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;

public class NetworkedContentManager {
	
	public static short ITEM_ADDED = 1;
	public static short ITEM_DELETED = 2;
	
	protected ContentSystem contentSystem;
	protected TableCommsClientService comms;
	protected List<Class<?>> receiverClasses;
	protected boolean allowedToSendMessage=true;
	protected SyncManager syncManager;
	protected SyncRenderer syncRenderer;
	protected List<TableIdentity> projectorTables = new ArrayList<TableIdentity>();
	
	public Map<String, ContentItem> onlineItemsList = new HashMap<String, ContentItem>();

	protected transient List<NetworkListener> listeners = new ArrayList<NetworkListener>();

	public NetworkedContentManager(ContentSystem contentSystem , TableCommsClientService comms, List<Class<?>> receiverClasses){
		this.contentSystem = contentSystem;
		this.comms = comms;
		this.syncManager = new SyncManager(this);
		this.receiverClasses = receiverClasses;
		syncRenderer = new SyncRenderer();
	}
	
	
	public ContentSystem getContentSystem(){
		return contentSystem;
	}
	
	public List<Class<?>> getReceiverClasses(){
		return receiverClasses;
	}
	
	public boolean isAllowedToSendMessage(){
		return this.allowedToSendMessage;
	}
	
	public void allowedToSendmessage(boolean allowedToSendMessage){
		this.allowedToSendMessage = allowedToSendMessage;
	}
	
	public void sendMessage(Object obj) {
		if (!this.allowedToSendMessage) return;
		if(comms != null) {
			try {
				comms.sendMessage(obj);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public Map<String, ContentItem> getOnlineItems() {
		return onlineItemsList;
	}
	
	private boolean registerContentItem(ContentItem item){
		boolean isRegistered = false;
		if(!contentSystem.getAllContentItems().containsKey(item.getName())){ 
			contentSystem.addContentItem(item);
		}
		if(!onlineItemsList.containsKey(item.getName())){
			onlineItemsList.put(item.getName(), item);
			syncManager.addSyncListeners(item);
			WorkspaceManager.getInstance().addWorkspaceListener(item);
			isRegistered = true;
		}
		return isRegistered;
	}
	

	public void registerContentItems(List<ContentItem> contentItems) {
		HashMap<ContentItem, Short> updatedItems = new HashMap<ContentItem, Short>();
		for (ContentItem item: contentItems){
			if(registerContentItem(item)){
				updatedItems.put(item, NetworkedContentManager.ITEM_ADDED);
			}
		}
		if(!updatedItems.isEmpty()){
			for(Class<?> targetClass: this.getReceiverClasses())
				this.sendMessage(new UpdateContentPortalMessage(targetClass,  updatedItems));
		}
	}
	
	
	public void registerContentItems(HashMap<ContentItem, Location> contentItems) {
		HashMap<ContentItem, Short> updatedItems = new HashMap<ContentItem, Short>();
		for (ContentItem item: contentItems.keySet()){
			if(registerContentItem(item)){
				item.setLocation(contentItems.get(item));
				updatedItems.put(item, NetworkedContentManager.ITEM_ADDED);
			}
		}
		if(!updatedItems.isEmpty()){
			for(Class<?> targetClass: this.getReceiverClasses())
				this.sendMessage(new UpdateContentPortalMessage(targetClass,  updatedItems));
		}
	}
	
	
	public void unregisterContentItems(List<ContentItem> contentItems) {
		HashMap<ContentItem, Short> updatedItems = new HashMap<ContentItem, Short>();
		for(ContentItem item: contentItems){
			if(this.onlineItemsList.containsKey(item.getName())){
				updatedItems.put(item, NetworkedContentManager.ITEM_DELETED);
				onlineItemsList.remove(item.getName());
			}
			if(contentSystem.getAllContentItems().containsKey(item.getName())){
				contentSystem.removeContentItem(item);
			}
		}
		if(!updatedItems.isEmpty()){
			for(Class<?> targetClass: this.getReceiverClasses())
				this.sendMessage(new UpdateContentPortalMessage(targetClass,  updatedItems));
		}
		updatedItems.clear();
	}
	
	public void addNetworkListener(NetworkListener l){ 
		if(!listeners.contains(l))	listeners.add(l);
		}
	
	public void removeNetworkListeners( ){ listeners.clear();};
	
	public void removeNetworkListener(NetworkListener  l){ listeners.remove(l);}; 
	
	public interface NetworkListener{
		public void messageReceived(Object obj);
	}
	
	public List<TableIdentity> getOnlineTables(){
		return comms.getCurrentlyOnline();
	}

	public void postAliveMessage(TableIdentity tableIdentity) {
		for(Class<?> targetClass: this.getReceiverClasses())
			this.sendMessage(new UnicastAlivePortalMessage(targetClass, tableIdentity));
	}


	public void fireMessageReceived(Object obj) {
		for(NetworkListener listener: listeners) listener.messageReceived(obj);
	}


	public void handleConnectionRequest(TableIdentity targetTable, boolean isConnect) {
		if(isConnect){	
			for(Class<?> targetClass: this.getReceiverClasses())
				this.sendMessage(new PostWorkspacePortalMessage(targetClass, targetTable, onlineItemsList.values()));
			this.syncManager.registerSyncTable(targetTable);
		}else{
			syncManager.unregisterSyncTable(targetTable);
		}
	}

	public void update(float tpf) {
		if(syncManager != null) syncManager.update();
	}
	
	public SyncManager getSyncManager(){
		return this.syncManager;
	}


	public void disconnectTable(TableIdentity remoteTableId) {
		for(Class<?> targetClass: this.getReceiverClasses())
			this.sendMessage(new ConnectTablePortalMessage(targetClass, remoteTableId, false));
	}


	public void connectTable(TableIdentity remoteTableId) {
		for(Class<?> targetClass: this.getReceiverClasses())	
			this.sendMessage(new ConnectTablePortalMessage(targetClass, remoteTableId, true));
	}


	public void syncContent(Map<String, Map<Short, Object>> itemSyncDataMap) {
		if (onlineItemsList.isEmpty()) return;
		for (String name : itemSyncDataMap.keySet()){
			String[] arr = name.split("@");
			if(arr.length <3) return;
			String portalId = arr[0];
			String tableId = arr[1];
			String itemName = arr[2];

			if(portalId == null || tableId == null || itemName == null) continue;
			if(!tableId.equals(TableIdentity.getTableIdentity().toString())) continue;
			if (!onlineItemsList.containsKey(itemName))	continue;
			OrthoContentItem item = (OrthoContentItem)onlineItemsList.get(itemName);
			syncRenderer.renderSyncData(item, itemSyncDataMap.get(name));
		}
	}


	public void transferContentItem(ContentItem item, TableIdentity sourceTableId,	TableIdentity targetTableId, Location newLoc) {
		if(sourceTableId == null || targetTableId == null) return;
		if(sourceTableId.equals(targetTableId)) return;
		if(!sourceTableId.equals(TableIdentity.getTableIdentity()) && targetTableId.equals(TableIdentity.getTableIdentity())){
			// transfer item from a remote table to the local workspace
			String[] strs = item.getName().split("@");
			if(strs == null || strs.length < 3) return;
			HashMap<String, Location> itemNames = new HashMap<String, Location>();
			itemNames.put(strs[2], newLoc);
			for(Class<?> targetClass: this.getReceiverClasses())	
				this.sendMessage(new RequestItemsPortalMessage(targetClass, sourceTableId, itemNames,TableIdentity.getTableIdentity(), true));
			// register new location, waiting for the item to arrive
			//WorkspaceManager.getInstance().itemLoc.put(strs[2], newLoc);
			// remote items from the portal
			TablePortal portal = TablePortalManager.getInstance().getTablePortalForTable(sourceTableId);
			if(portal != null)	portal.getDisplayPanel().unregisterItem(item.getName());
		
		}else if(sourceTableId.equals(TableIdentity.getTableIdentity()) && !targetTableId.equals(TableIdentity.getTableIdentity())){
			// transfer item from local workspace to a remote table
			HashMap<ContentItem, Location> items = new HashMap<ContentItem, Location>();
			
			TablePortal portal = TablePortalManager.getInstance().getTablePortalForTable(targetTableId);
			if(portal != null){
				Vector3f newV = new Vector3f();
				((Spatial)(portal.getDisplayPanel().getWindow().getImplementationObject())).worldToLocal(new Vector3f(newLoc.x, newLoc.y,0), newV);
				newLoc.x = newV.x + DisplaySystem.getDisplaySystem().getWidth()/2;
				newLoc.y = newV.y + DisplaySystem.getDisplaySystem().getHeight()/2;
				newLoc.z = 0;
				item.setLocation(newLoc);
			}
			
			items.put(item, newLoc);
			for(Class<?> targetClass: this.getReceiverClasses()){	
				this.sendMessage(new PostItemsPortalMessage(targetClass, targetTableId, items, targetTableId));
			}
	
			this.unregisterContentItems(new ArrayList<ContentItem>(items.keySet()));
		}else{
			// transfer item from a remote table to another remote table
			String[] strs = item.getName().split("@");
			if(strs == null || strs.length < 3) return;
			
			TablePortal portal = TablePortalManager.getInstance().getTablePortalForTable(targetTableId);
			if(portal != null){
				Vector3f newV = new Vector3f();
				((Spatial)(portal.getDisplayPanel().getWindow().getImplementationObject())).worldToLocal(new Vector3f(newLoc.x, newLoc.y,0), newV);
				newLoc.x = newV.x + DisplaySystem.getDisplaySystem().getWidth()/2;
				newLoc.y = newV.y + DisplaySystem.getDisplaySystem().getHeight()/2;
				newLoc.z = 0;
			}
			
			HashMap<String, Location> itemNames = new HashMap<String, Location>();
			itemNames.put(strs[2], newLoc);
			for(Class<?> targetClass: this.getReceiverClasses())	
				this.sendMessage(new RequestItemsPortalMessage(targetClass, sourceTableId, itemNames, targetTableId, true));

		}
	}

	public void postItemsToTable(HashMap<String, Location> itemNames, TableIdentity tableId,	TableIdentity targetTableId, boolean deleteItems) {
		HashMap<ContentItem, Location> requestedItems = new HashMap<ContentItem, Location>();
		for(String itemName: itemNames.keySet()){
			if(this.onlineItemsList.containsKey(itemName)) requestedItems.put(onlineItemsList.get(itemName), itemNames.get(itemName));
		}
		for(Class<?> targetClass: this.getReceiverClasses())	
			this.sendMessage(new PostItemsPortalMessage(targetClass, tableId, requestedItems, targetTableId));
		
		if(deleteItems)	this.unregisterContentItems(new ArrayList<ContentItem>(requestedItems.keySet()));

	}


	public void processReceivedItems(PostItemsPortalMessage msg) {
		if(msg.getTargetTableId().equals(TableIdentity.getTableIdentity())){
			this.registerContentItems(msg.getItems());	
		}else{
			msg.setRecipient(msg.getTargetTableId());
			for(Class<?> targetClass: this.getReceiverClasses()){	
				msg.setTargetClassName(targetClass.getName());
				this.sendMessage(msg);
			}
		}
	}


	public void updateSyncData() {
		this.syncManager.updateSyncData();
	}


	public void setRemoteTableMode(TableIdentity tableId, RemoteTableMode mode) {
		if(tableId == null) return;
		boolean isLocked = false;
		if(mode.equals(TablePortal.RemoteTableMode.LOCKED)) 
			isLocked = true;
		for(Class<?> targetClass: this.getReceiverClasses()){	
			LockTableMessage lock = new LockTableMessage(targetClass, tableId);
			lock.enableLock(isLocked);
			this.sendMessage(lock);
		}
	}


	public void processTableMode(boolean locked) {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().setMultiTouchInputEnabled(!locked);
	}

	public void registerProjector(TableIdentity tableId){
		if(!projectorTables.contains(tableId)) projectorTables.add(tableId);
	}
	
	public List<TableIdentity> getProjectors(){
		return projectorTables;
	}

}


