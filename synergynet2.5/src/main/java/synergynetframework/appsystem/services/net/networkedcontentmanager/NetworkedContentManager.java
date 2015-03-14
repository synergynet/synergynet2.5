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

package synergynetframework.appsystem.services.net.networkedcontentmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import apps.conceptmap.utility.GraphManager;

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

public class NetworkedContentManager {

	private static final Logger log = Logger.getLogger(NetworkedContentManager.class.getName());
	public final static String ONLINEITEMSLIST = "Online Desktop";
	public final static String BACKITEMSLIST = "Local Desktop";
	
	protected TableCommsClientService comms;
	protected ContentSystem contentSystem;
	protected GraphManager graphManager;
	protected RemoteDesktopController remoteDesktopController;
	protected ProjectorController projectorController;
	protected NetworkedFlickController networkFlickController;
	protected List<Class<?>> receiverClasses;
	
	protected Map<String, ContentItem> onlineItemsList = new HashMap<String, ContentItem>();
	protected Map<String, ContentItem> backItemsList = new HashMap<String, ContentItem>();
	protected String listStoreLocalItem = NetworkedContentManager.ONLINEITEMSLIST;
	protected boolean isbackupListEnabled = false;
	protected Map<String, Map<String, String>> sychronisedData = new HashMap<String, Map<String, String>>();
	
	protected boolean isMenuEnabled = true;
	protected boolean isMenuControllerEnabled = false;
	protected boolean isBiSynchronisationEnabled = false;
	
	protected boolean isSynchronisationOn = false;
	protected boolean isRemoteMenuOn = true;
	protected boolean isRemoteLocked = false;
	protected boolean allowSingleTouchFreeMove = false;

	protected OrthoContainer controlledMenu;
	protected boolean allowedToSendMessage = true;
	
	protected List<NetworkedContentListener> networkedContentListener = new ArrayList<NetworkedContentListener>();

	public NetworkedContentManager(ContentSystem contentSystem, TableCommsClientService comms, List<Class<?>> receiverClasses){
		this.contentSystem = contentSystem;
		this.comms = comms;
		this.receiverClasses = receiverClasses;
		networkFlickController = new NetworkedFlickController(this);
		log.info("Networked flick controller created");
	}
	
	public void setGraphManager(GraphManager graphManager){
		this.graphManager = graphManager;
	}
	
	public GraphManager getGraphManager(){
		return graphManager;
	}
	
	public void createRemoteDesktopController(ArrayList<Class<?>> controllerClasses, ArrayList<Class<?>> targetClasses){
		remoteDesktopController = new RemoteDesktopController(this, controllerClasses, targetClasses);
		log.info("Remote desktop controller created");
	}
	
	public void createProjectorController(ArrayList<Class<?>> controllerClasses, ArrayList<Class<?>> projectorClasses){
		projectorController = new ProjectorController(this, controllerClasses, projectorClasses);
		log.info("Projector controller created");
	}
	
	public ContentSystem getContentSystem(){
		return contentSystem;
	}
	
	public Map<String, ContentItem> getOnlineItems() {
		return onlineItemsList;
	}
	
	public boolean isAllowedToSendMessage(){
		return this.allowedToSendMessage;
	}
	
	public void allowedToSendmessage(boolean allowedToSendMessage){
		this.allowedToSendMessage = allowedToSendMessage;
	}
	
	public boolean isbackupListEnabled() {
		return isbackupListEnabled;
	}

	public void setbackupListEnabled(boolean isbackupListEnabled) {
		this.isbackupListEnabled = isbackupListEnabled;
	}
	
	public String getCurrentScreenChannel(){
		if (this.listStoreLocalItem.equals(NetworkedContentManager.BACKITEMSLIST)){
			return "Online Desktop";
		}
		else{
			return "Local Desktop";
		}
		
	}

	public void backupLocalItems(){
		if(!this.isbackupListEnabled) return;
		
		if (this.listStoreLocalItem.equals(NetworkedContentManager.BACKITEMSLIST)) return;
		
		if (this.listStoreLocalItem.equals(NetworkedContentManager.ONLINEITEMSLIST)){
			this.removeAllItemsInBackList();
			for (ContentItem item:this.onlineItemsList.values()){
				this.backItemsList.put(item.getName(), item);
				item.setVisible(false);
			}
			this.listStoreLocalItem = NetworkedContentManager.BACKITEMSLIST;
			
			this.onlineItemsList.clear();
			
			for (NetworkedContentListener l:this.networkedContentListener)
				l.channelSwitched();
		}
		
		log.info("Backup online items");
		
	}
	
	public void switchItemList(){
		if(!this.isbackupListEnabled) return;
		
		Map<String, ContentItem> tempList = new HashMap<String, ContentItem>();
		
		for (ContentItem item:this.onlineItemsList.values()){
			tempList.put(item.getName(), item);
		}
		
		this.onlineItemsList.clear();
		for (ContentItem item:this.backItemsList.values()){
			onlineItemsList.put(item.getName(), item);
			item.setVisible(true);
		}
		
		this.backItemsList.clear();
		for (ContentItem item:tempList.values()){
			backItemsList.put(item.getName(), item);
			item.setVisible(false);
		}
		
		if (this.listStoreLocalItem.equals(NetworkedContentManager.ONLINEITEMSLIST)){
			this.listStoreLocalItem = NetworkedContentManager.BACKITEMSLIST;
		}
		else
			this.listStoreLocalItem = NetworkedContentManager.ONLINEITEMSLIST;
		
		for (NetworkedContentListener l:this.networkedContentListener)
			l.channelSwitched();
		
		log.info("Switch content channel between online item list and offline item list");
		
	}
	
	public void showLocalItems(){
		if (this.listStoreLocalItem.equals(NetworkedContentManager.BACKITEMSLIST)){
			this.removeAllItems();
			switchItemList();
		}else{
			this.removeAllItemsInBackList();
		}
	}

	public void loadLocalContent(String filePath, String name){
			
		this.removeAllItems();
		Set<ContentItem> contentItems = contentSystem.loadContentItems(filePath);
		for (ContentItem item: contentItems){
			if(item instanceof OrthoContentItem){	
				((OrthoContentItem)item).allowSingleTouchFreeMove(allowSingleTouchFreeMove);
			}
			onlineItemsList.put(item.getName(),item);
		}			
		
		log.info("Content loaded from "+filePath);
	}
	
	public void loadContentItem(ContentItem item){
		if(item instanceof OrthoContentItem) ((OrthoContentItem)item).allowSingleTouchFreeMove(allowSingleTouchFreeMove);
		onlineItemsList.put(item.getName(),item);
		for (NetworkedContentListener l:this.networkedContentListener)
			l.contentItemLoaded(item);
		
		log.info(item.getClass().getName()+"-"+item.getName()+" loaded");
	}
	
	public void loadContent(String filePath, String name){
		if(this.isbackupListEnabled){
			this.removeAllItemsInBackList();
			this.listStoreLocalItem = NetworkedContentManager.ONLINEITEMSLIST;
		}
		
		this.removeAllItems();
		this.loadLocalContent(filePath, name);
		
		for (NetworkedContentListener l:this.networkedContentListener)
			l.contentLoaded();
	}
	
	public void loadContent(List<ContentItem> collection){
		if(this.isbackupListEnabled){
			backupLocalItems();
			
		}
		
		this.removeAllItems();
		
		for (ContentItem item:collection){
			contentSystem.addContentItem(item);
			if(item instanceof OrthoContentItem){	
				((OrthoContentItem)item).allowSingleTouchFreeMove(allowSingleTouchFreeMove);
			}
			onlineItemsList.put(item.getName(),item);

		}		
		
		for (NetworkedContentListener l:this.networkedContentListener)
			l.contentLoaded();
	}
	
	public void removeAllItemsInBackList(){
		for (ContentItem item:backItemsList.values()){
			contentSystem.removeContentItem(item);
		}
		
		backItemsList.clear();
		log.info("Items removed from offline list.");
	}

	public void removeContentItem(ContentItem item){
		onlineItemsList.remove(item.getName());
		contentSystem.removeContentItem(item);
		
		log.info(item.getClass().getName()+"-"+item.getName()+" removed.");
	}
	
	public void removeAllItems(){
		
		for (ContentItem item:onlineItemsList.values()){
			contentSystem.removeContentItem(item);
		}
		
		onlineItemsList.clear();
		
		//temp statement only for the experiment on 19th oct, 2009
		backItemsList.clear();
		
		log.info("All items removed.");
	}
	
	public void enableMenuController(boolean isMenuControllerOn, OrthoContainer menu){
		if (!isMenuControllerOn){
			this.controlledMenu = null;
			this.isMenuControllerEnabled = false;
		}
		else{
			if (menu==null) return;
			this.controlledMenu = menu;
			this.isMenuControllerEnabled = true;
		}
	}
	
	public void enableMenu(boolean isMenuEnabled){
		if (!this.isMenuControllerEnabled) return;
		if (this.controlledMenu==null) return;
		
		this.isMenuEnabled = isMenuEnabled;
		
		if (!this.isMenuEnabled)
			controlledMenu.setVisible(false);
		else
			controlledMenu.setVisible(true);
	}
	
	public boolean isMenuEnabled(){
		return this.isMenuEnabled;
	}
	
	public void enableSynchronisation(boolean isSynchronisationOn){
		this.isSynchronisationOn = isSynchronisationOn;
	}
	
	public void synchroniseData(Map<String, Map<String, String>> synchronisedItems){

		if (synchronisedItems.size()==0) return;
		for (String name:synchronisedItems.keySet()){
			if (!this.onlineItemsList.containsKey(name))
				continue;
			OrthoContentItem item = (OrthoContentItem)this.onlineItemsList.get(name);
			
			for (NetworkedContentListener l:this.networkedContentListener)
				l.renderSynchronisedDate(item, synchronisedItems.get(name));
		}
	}
	
	public Map<String, Map<String, String>> getSychronisedData() {
		return sychronisedData;
	}

	public TableCommsClientService getTableCommsClientService(){
		return comms;
	}
	
	public void stateUpdate(float tpf) {
		if(comms != null) comms.update();
	
		if (this.sychronisedData.size()!=0 && this.isSynchronisationOn){ 
			for (Class<?> targetClass:this.receiverClasses)
				sendMessage(new SynchroniseData(targetClass, this.sychronisedData));
		}
		if(sychronisedData.size()!=0 && remoteDesktopController != null) remoteDesktopController.sendRemoteDesktopSyncMessage(sychronisedData);
		this.sychronisedData.clear();
	}
	
	public void sendMessage(Object obj) {
		if (!this.allowedToSendMessage) return;
		if(comms != null) {
			try {
				comms.sendMessage(obj);					
			} catch (IOException e) {
				log.warning(e.toString());
			}	
		}
	}
	
	public boolean isRemoteMenuOn() {
		return isRemoteMenuOn;
	}

	public void setRemoteMenuOn(boolean isRemoteMenuOn) {
		this.isRemoteMenuOn = isRemoteMenuOn;
		this.enableRemoteMenu(isRemoteMenuOn);
	}

	public boolean isRemoteLocked() {
		return isRemoteLocked;
	}

	public void setRemoteLocked(boolean isRemoteLocked) {
		this.isRemoteLocked = isRemoteLocked;
		this.blockRemoteDesktop(isRemoteLocked);
		if (isRemoteLocked)
			log.info("Lock remote desktops");
		else
			log.info("Unlock remote desktops");
	}

	public boolean isBiSynchronisationEnabled() {
		return isBiSynchronisationEnabled;
	}

	public void setBiSynchronisationEnabled(boolean isBiSynchronisationEnabled) {
		this.isBiSynchronisationEnabled = isBiSynchronisationEnabled;
		this.enableBiSynchronisation(isBiSynchronisationEnabled);
	}

	public boolean isSynchronisationOn() {
		return isSynchronisationOn;
	}

	public void setSynchronisationOn(boolean isSynchronisationOn) {
		this.isSynchronisationOn = isSynchronisationOn;
		this.enableSynchronisation(isSynchronisationOn);
		if (isSynchronisationOn)
			log.info("Turn on remote control");
		else
			log.info("Turn off remote control");
	}

	public void addNetworkedContentListener(NetworkedContentListener l){
		if (this.networkedContentListener==null)
			this.networkedContentListener = new ArrayList<NetworkedContentListener>();
		
		if(!this.networkedContentListener.contains(l))
			this.networkedContentListener.add(l);
	}
	
	public void removeNetworkedContentListeners(){
		networkedContentListener.clear();
	}
	
	public void removeNetworkedContentListener(NetworkedContentListener l){
		networkedContentListener.remove(l);
	}
	
	public void blockRemoteDesktop(boolean isRemoteLocked){
		
		for (Class<?> targetClass:this.receiverClasses)		
			sendMessage(new BlockInteraction(targetClass, isRemoteLocked));
		
		log.info("Block remote tables");
	}
	
	public void enableRemoteMenu(boolean isRemoteMenuOn){
		for (Class<?> targetClass:this.receiverClasses)	
			sendMessage(new EnableMenu(targetClass, isRemoteMenuOn));
		
		if (isRemoteMenuOn)
			log.info("Turn on remote menu");
		else
			log.info("Turn off remote menu");
	}
	
	public void clearRemoteDeskTop(){
		for (Class<?> targetClass:this.receiverClasses)	
			sendMessage(new ClearTable(targetClass));
		
		log.info("Clear remote desktops");
	}
	
	public void broadcastCurrentDeskTop(){
		for (Class<?> targetClass:this.receiverClasses)	
			sendMessage(new BroadcastData(targetClass, onlineItemsList.values()));
		
		log.info("Broadcast current desktop");
	}
	
	public void enableBiSynchronisation(boolean isBiSynchronisationEnabled){
		for (Class<?> targetClass:this.receiverClasses)	
			this.sendMessage(new EnableBiSynchronisation(targetClass, isBiSynchronisationEnabled));
		
		if (isBiSynchronisationEnabled)
			log.info("Turn on synchronisation");
		else
			log.info("Turn off synchronisation");
	}
	
	public void loadContentInRemoteTable(String filePath, String name){
		for (Class<?> targetClass:this.receiverClasses)	
			sendMessage(new LoadData(targetClass, filePath, name));	
		log.info("Broadcast command to load data on remote tables");
		
	}
	
	public void sendDataTo(TableIdentity table){
		for (Class<?> targetClass:this.receiverClasses)	
			sendMessage(new SendDataTo(targetClass, onlineItemsList.values(), table));		
		log.info("Send data to table-"+table.toString());
	}
	
	public void requestDataFrom(TableIdentity table){
		for (Class<?> targetClass:this.receiverClasses)	
			sendMessage(new RequireDataFrom(targetClass, table));		
		log.info("Request data from table-"+table.toString());
	}

	public void swapTable(TableIdentity table1, TableIdentity table2){
		for (Class<?> targetClass:this.receiverClasses)		
			sendMessage(new SwapTable(targetClass, table1, table2));
		log.info("Send swap table request to table-"+table1.toString());
			
	}
	
	public void sendSwapTableMessage(TableIdentity targetTable){
		for (Class<?> targetClass:this.receiverClasses)
			sendMessage(new SwapTableMessage(targetClass, onlineItemsList.values(), targetTable));
		log.info("Send data for table swap to table-"+targetTable.toString());
	}
	
	public List<Class<?>> getReceiverClasses(){
		return receiverClasses;
	}
	
	public List<NetworkedContentListener> getNetworkedContentListeners(){
		return networkedContentListener;
	}

	public RemoteDesktopController getRemoteDesktopController(){
		return remoteDesktopController;
	}
	
	public ProjectorController getProjectorController(){
		return projectorController;
	}
	
	public NetworkedFlickController getNetworkedFlickController(){
		return networkFlickController;
	}
}
