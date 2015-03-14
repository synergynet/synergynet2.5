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

public abstract class NetworkedContentManager {
	
	protected ContentSystem contentSystem;
	protected TableCommsClientService comms;
	protected ArrayList<Class<?>> receiverClasses;
	protected boolean allowedToSendMessage=true;
	
	protected SyncManager syncManager;
	protected HashMap<UserIdentity,MathTool> mathPads = new HashMap<UserIdentity,MathTool>();
	
	protected transient List<NetworkListener> listeners = new ArrayList<NetworkListener>();

	public NetworkedContentManager(ContentSystem contentSystem , TableCommsClientService comms, ArrayList<Class<?>> receiverClasses){
		this.contentSystem = contentSystem;
		this.comms = comms;
		this.receiverClasses = receiverClasses;
	}
	
	
	public ContentSystem getContentSystem(){
		return contentSystem;
	}
	
	public ArrayList<Class<?>> getReceiverClasses(){
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
	
	public void registerMathPad(UserIdentity userId, MathTool tool){
		mathPads.put(userId, tool);
		syncManager.addSyncListeners(userId);
	}
	
	public void unregisterMathPad(UserIdentity userId){
		if(mathPads.containsKey(userId)){
			mathPads.get(userId).removeMathToolListeners();
			mathPads.get(userId).terminate();
			mathPads.remove(userId);
		}
	}
	
	public HashMap<UserIdentity,MathTool> getRegisteredMathPads(){
		return mathPads;
	}
	
	public void update(float tpf) {
		if(syncManager != null) syncManager.update(tpf);
	}
	
	public SyncManager getSyncManager(){
		return syncManager;
	}
	
	public void addNetworkListener(NetworkListener l){ 
		if(!listeners.contains(l))	listeners.add(l);
		}
	
	public void removeNetworkListeners( ){ listeners.clear();};
	
	public void removeNetworkListener(NetworkListener  l){ listeners.remove(l);}; 
	
	public interface NetworkListener{
		//public void syncDataReceived(HashMap<UserIdentity,HashMap<Short,Object>> syncData);
	}

	protected void setSyncManager(SyncManager syncManager) {
		this.syncManager = syncManager;
	}
	
	public List<TableIdentity> getOnlineTables(){
		return comms.getCurrentlyOnline();
	}
	
	public UserIdentity getUserIdentityForMathTool(MathTool tool){
		for(UserIdentity userId: mathPads.keySet()){
			if(mathPads.get(userId).equals(tool)) return userId;
		}
		return null;
	}
	
}


