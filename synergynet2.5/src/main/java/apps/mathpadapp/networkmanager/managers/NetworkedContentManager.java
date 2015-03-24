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

package apps.mathpadapp.networkmanager.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.networkmanager.managers.syncmanager.SyncManager;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;


/**
 * The Class NetworkedContentManager.
 */
public abstract class NetworkedContentManager {
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The comms. */
	protected TableCommsClientService comms;
	
	/** The receiver classes. */
	protected ArrayList<Class<?>> receiverClasses;
	
	/** The allowed to send message. */
	protected boolean allowedToSendMessage=true;
	
	/** The sync manager. */
	protected SyncManager syncManager;
	
	/** The math pads. */
	protected HashMap<UserIdentity,MathTool> mathPads = new HashMap<UserIdentity,MathTool>();
	
	/** The listeners. */
	protected transient List<NetworkListener> listeners = new ArrayList<NetworkListener>();

	/**
	 * Instantiates a new networked content manager.
	 *
	 * @param contentSystem the content system
	 * @param comms the comms
	 * @param receiverClasses the receiver classes
	 */
	public NetworkedContentManager(ContentSystem contentSystem , TableCommsClientService comms, ArrayList<Class<?>> receiverClasses){
		this.contentSystem = contentSystem;
		this.comms = comms;
		this.receiverClasses = receiverClasses;
	}
	
	
	/**
	 * Gets the content system.
	 *
	 * @return the content system
	 */
	public ContentSystem getContentSystem(){
		return contentSystem;
	}
	
	/**
	 * Gets the receiver classes.
	 *
	 * @return the receiver classes
	 */
	public ArrayList<Class<?>> getReceiverClasses(){
		return receiverClasses;
	}
	
	/**
	 * Checks if is allowed to send message.
	 *
	 * @return true, if is allowed to send message
	 */
	public boolean isAllowedToSendMessage(){
		return this.allowedToSendMessage;
	}
	
	/**
	 * Allowed to sendmessage.
	 *
	 * @param allowedToSendMessage the allowed to send message
	 */
	public void allowedToSendmessage(boolean allowedToSendMessage){
		this.allowedToSendMessage = allowedToSendMessage;
	}
	
	/**
	 * Send message.
	 *
	 * @param obj the obj
	 */
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
	
	/**
	 * Register math pad.
	 *
	 * @param userId the user id
	 * @param tool the tool
	 */
	public void registerMathPad(UserIdentity userId, MathTool tool){
		mathPads.put(userId, tool);
		syncManager.addSyncListeners(userId);
	}
	
	/**
	 * Unregister math pad.
	 *
	 * @param userId the user id
	 */
	public void unregisterMathPad(UserIdentity userId){
		if(mathPads.containsKey(userId)){
			mathPads.get(userId).removeMathToolListeners();
			mathPads.get(userId).terminate();
			mathPads.remove(userId);
		}
	}
	
	/**
	 * Gets the registered math pads.
	 *
	 * @return the registered math pads
	 */
	public HashMap<UserIdentity,MathTool> getRegisteredMathPads(){
		return mathPads;
	}
	
	/**
	 * Update.
	 *
	 * @param tpf the tpf
	 */
	public void update(float tpf) {
		if(syncManager != null) syncManager.update(tpf);
	}
	
	/**
	 * Gets the sync manager.
	 *
	 * @return the sync manager
	 */
	public SyncManager getSyncManager(){
		return syncManager;
	}
	
	/**
	 * Adds the network listener.
	 *
	 * @param l the l
	 */
	public void addNetworkListener(NetworkListener l){ 
		if(!listeners.contains(l))	listeners.add(l);
		}
	
	/**
	 * Removes the network listeners.
	 */
	public void removeNetworkListeners( ){ listeners.clear();};
	
	/**
	 * Removes the network listener.
	 *
	 * @param l the l
	 */
	public void removeNetworkListener(NetworkListener  l){ listeners.remove(l);}; 
	
	/**
	 * The listener interface for receiving network events.
	 * The class that is interested in processing a network
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addNetworkListener<code> method. When
	 * the network event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see NetworkEvent
	 */
	public interface NetworkListener{
		//public void syncDataReceived(HashMap<UserIdentity,HashMap<Short,Object>> syncData);
	}

	/**
	 * Sets the sync manager.
	 *
	 * @param syncManager the new sync manager
	 */
	protected void setSyncManager(SyncManager syncManager) {
		this.syncManager = syncManager;
	}
	
	/**
	 * Gets the online tables.
	 *
	 * @return the online tables
	 */
	public List<TableIdentity> getOnlineTables(){
		return comms.getCurrentlyOnline();
	}
	
	/**
	 * Gets the user identity for math tool.
	 *
	 * @param tool the tool
	 * @return the user identity for math tool
	 */
	public UserIdentity getUserIdentityForMathTool(MathTool tool){
		for(UserIdentity userId: mathPads.keySet()){
			if(mathPads.get(userId).equals(tool)) return userId;
		}
		return null;
	}
	
}


