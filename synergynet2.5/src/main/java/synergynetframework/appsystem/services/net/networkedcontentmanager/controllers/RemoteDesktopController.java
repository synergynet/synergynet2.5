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

package synergynetframework.appsystem.services.net.networkedcontentmanager.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.BroadcastEnableRemoteDesktop;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.SendClientDataTo;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.SynchroniseRemoteDesktopData;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.remotedesktop.UnicastEnableRemoteDesktop;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.ProjectorNode;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.RemoteDesktop;

public class RemoteDesktopController {

	private static final Logger log = Logger.getLogger(RemoteDesktopController.class.getName());
	protected NetworkedContentManager networkedContentManager;
	protected HashMap<TableIdentity, RemoteDesktop> remoteDesktops = new HashMap<TableIdentity, RemoteDesktop>();
	protected Queue<TableIdentity> syncControllers = new ConcurrentLinkedQueue<TableIdentity>();
	protected ArrayList<Class<?>> controllerClasses;
	protected ArrayList<Class<?>> targetClasses;
	
	public RemoteDesktopController(NetworkedContentManager networkedContentManager, ArrayList<Class<?>> controllerClasses, ArrayList<Class<?>> targetClasses){
		this.networkedContentManager = networkedContentManager;
		this.controllerClasses = controllerClasses;
		this.targetClasses = targetClasses;
	}
	
	public void loadRemoteDesktopContent(TableIdentity tableId, List<ContentItem> collection){
		RemoteDesktop rd = null;
		if(remoteDesktops.containsKey(tableId)){
			rd = remoteDesktops.get(tableId);
			rd.removeOnlineItems();
			
		}else{
			rd = new RemoteDesktop(tableId, networkedContentManager);
			rd.setLocation(300, 300);
			rd.setScale(0.4f);
			remoteDesktops.put(tableId, rd);
		}
		for (ContentItem item:collection){
			networkedContentManager.getContentSystem().addContentItem(item);
			item.setName(tableId+"@"+item.getName());
			rd.getDesktopWindow().addSubItem(item);
			rd.getOnlineItems().put(item.getName(),item);
		}
	
		for (NetworkedContentListener l:networkedContentManager.getNetworkedContentListeners())
			l.remoteContentLoaded(rd);
		
		log.info("Load content on Table-"+tableId);
	}
	
	public void synchroniseRemoteDesktopData(TableIdentity tableId, Map<String, Map<String, String>> synchronisedItems){
		RemoteDesktop remoteDesktop = remoteDesktops.get(tableId);
		if(remoteDesktop != null){
			if (synchronisedItems.size()==0) return;
			for (String name:synchronisedItems.keySet()){
				if (!remoteDesktop.getOnlineItems().containsKey(tableId + "@" + name))
					continue;
				OrthoContentItem item = (OrthoContentItem)remoteDesktop.getOnlineItems().get(tableId + "@" + name);
				for (NetworkedContentListener l:networkedContentManager.getNetworkedContentListeners())
					l.renderRemoteDesktop(remoteDesktop, item, synchronisedItems.get(name));
	
	
			}
			if(networkedContentManager.getProjectorController() != null)	
				for(ProjectorNode projector: networkedContentManager.getProjectorController().getOnlineProjectors()) projector.synchronise(synchronisedItems);
		}
	}
	
	public void requestRemoteDesktops(boolean isRemoteDesktopEnabled) {
		for(Class<?> targetClass: targetClasses)	
			networkedContentManager.sendMessage(new BroadcastEnableRemoteDesktop(targetClass, isRemoteDesktopEnabled));
		if(!isRemoteDesktopEnabled){
			for(RemoteDesktop rd: remoteDesktops.values()){
				RemoteDesktop remoteDesktop = remoteDesktops.get(rd.getTableId());
				remoteDesktop.remove();
				networkedContentManager.getGraphManager().detachGraphNode(remoteDesktop);
			}
			remoteDesktops.clear();
			
		}
		
		if(!isRemoteDesktopEnabled)
			log.info("Enable remote tables");
		else
			log.info("Disable remote tables");
			
	}
	
	public void requestRemoteDesktop(TableIdentity remoteTableId, boolean isRemoteDesktopEnabled){
		for(Class<?> targetClass: targetClasses)	
			networkedContentManager.sendMessage(new UnicastEnableRemoteDesktop(targetClass, isRemoteDesktopEnabled, remoteTableId));
		if(!isRemoteDesktopEnabled){
			this.unregisterRemoteDesktop(remoteTableId);
		}
		
		if(!isRemoteDesktopEnabled)
			log.info("Enable remote table-"+remoteTableId.toString());
		else
			log.info("Disable remote table-"+remoteTableId.toString());
	}
	
	private void unregisterRemoteDesktop(TableIdentity tableId) {
		if(remoteDesktops.containsKey(tableId)){
			RemoteDesktop remoteDesktop = remoteDesktops.get(tableId);
			remoteDesktop.remove();
			networkedContentManager.getGraphManager().detachGraphNode(remoteDesktop);
			remoteDesktops.remove(tableId);
			
			log.info("unregister remote table-"+tableId.toString());
		}
	}

	public void enableRemoteDesktop(TableIdentity callingTableId, boolean isRemoteDesktopEnabled) {
		if(isRemoteDesktopEnabled){	
			if(!syncControllers.contains(callingTableId)) syncControllers.add(callingTableId);
			for(Class<?> sourceClass: controllerClasses)	
				networkedContentManager.sendMessage(new SendClientDataTo(sourceClass, networkedContentManager.getOnlineItems().values(), callingTableId));
		}else{ 
			syncControllers.remove(callingTableId);
		}
	}
	
	public void sendRemoteDesktopSyncMessage(Map<String, Map<String, String>> sychronisedData) {

			for (TableIdentity tableId: syncControllers){
				for(Class<?> sourceClass: controllerClasses)	
					networkedContentManager.sendMessage(new SynchroniseRemoteDesktopData(sourceClass, sychronisedData, tableId));
		}		
	}
}
