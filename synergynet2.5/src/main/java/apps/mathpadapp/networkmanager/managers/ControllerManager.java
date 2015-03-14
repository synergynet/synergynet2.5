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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentManager;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentSession;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.NetworkedContentManager;
import apps.mathpadapp.networkmanager.managers.remotedesktopmanager.RemoteDesktopManager;
import apps.mathpadapp.networkmanager.managers.syncmanager.SyncManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.ResponseMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;

public class ControllerManager extends NetworkedContentManager{
	

	protected GraphManager graphManager;
	protected RemoteDesktopManager remoteDesktopManager;
	
	protected HashMap<TableIdentity, List<UserIdentity>> tableUsers = new HashMap<TableIdentity, List<UserIdentity>>();

	public ControllerManager(ContentSystem contentSystem , TableCommsClientService comms, ArrayList<Class<?>> receiverClasses){
		super(contentSystem, comms, receiverClasses);
		super.setSyncManager(new SyncManager(this));
	}

	public void createRemoteDesktopManager(ArrayList<Class<?>> controllerClasses, ArrayList<Class<?>> targetClasses){
		remoteDesktopManager = new RemoteDesktopManager(this, controllerClasses, targetClasses);
	}
	
	public void mathPadItemReceivedFromUser(TableIdentity senderTable, UserIdentity senderUser, MathToolInitSettings padSettings) {
		for(NetworkListener listener: listeners) ((ControllerNetworkListener)listener).userMathPadReceived(senderTable, senderUser, padSettings);
	}
	
	public void mathPadItemsReceivedFromTable(TableIdentity tableId, HashMap<UserIdentity, MathToolInitSettings> items) {
		for(UserIdentity userId: items.keySet()){
				for(NetworkListener listener: listeners) ((ControllerNetworkListener)listener).userMathPadReceived(tableId, userId, items.get(userId));
		}
	}

	public void registerTableUser(TableIdentity tableId, UserIdentity userId) {
		if(tableUsers.containsKey(tableId)){
			if(!tableUsers.get(tableId).contains(userId)){
				tableUsers.get(tableId).add(userId);
			}else{
				ResponseMessage msg = new ResponseMessage(MathPadClient.class, tableId, userId);
				msg.setMessage("A user with the same\n name is already registered.");
				this.sendMessage(msg);
			}
		}else{
			List<UserIdentity> users = new ArrayList<UserIdentity>();
			users.add(userId);
			tableUsers.put(tableId, users);
		}
		
		for(NetworkListener listener: listeners){
			((ControllerNetworkListener)listener).userRegistrationReceived(tableId, userId);
		}
	}
	
	public void unregisterTableUser(TableIdentity tableId, UserIdentity userId) {
		if(tableUsers.containsKey(tableId) && tableUsers.get(tableId) != null){
			tableUsers.get(tableId).remove(userId);
		}
		if(this.mathPads.containsKey(userId)){
			this.mathPads.get(userId).terminate();
			this.unregisterMathPad(userId);
		}
		
		for(NetworkListener listener: listeners){
			((ControllerNetworkListener)listener).userUnregistrationReceived(tableId, userId);
		}
	}

	public void userIdsReceivedFromTable(TableIdentity tableId,	List<UserIdentity> userIds) {
		tableUsers.remove(tableId);
		tableUsers.put(tableId, userIds);
		
		for(NetworkListener listener: listeners){
			((ControllerNetworkListener)listener).userIdsReceived(tableId, userIds);
		}
	}
	
	public interface ControllerNetworkListener extends NetworkListener{
		public void userRegistrationReceived(TableIdentity tableId, UserIdentity userId);
		public void userMathPadReceived(TableIdentity tableId, UserIdentity userId, MathToolInitSettings mathToolSettings);
		public void userUnregistrationReceived(TableIdentity tableId, UserIdentity userId);
		public void userIdsReceived(TableIdentity tableId, List<UserIdentity> userIds);
		public void tableIdReceived(TableIdentity tableId);
		public void resultsReceivedFromUser(TableIdentity tableId, UserIdentity userId, AssignmentInfo assignInfo);
		public void projectorFound(TableIdentity tableId, boolean isLeaseSuccessful);
		public void remoteDesktopContentReceived(TableIdentity tableId, HashMap<UserIdentity, MathToolInitSettings> items);
		public void syncDataReceived(TableIdentity sender,	HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData);
	}
	
	public HashMap<TableIdentity, List<UserIdentity>> getTableUsers(){
		return tableUsers;
	}
	
	public void setGraphManager(GraphManager graphManager){
		this.graphManager = graphManager;
	}
	
	public GraphManager getGraphManager(){
		return graphManager;
	}
	
	public RemoteDesktopManager getRemoteDesktopManager(){
		return remoteDesktopManager;
	}

	public void remoteDesktopReceived(TableIdentity sender,	HashMap<UserIdentity, MathToolInitSettings> items) {
		if(remoteDesktopManager != null && remoteDesktopManager.getMathPadRemoteDesktops().containsKey(sender)){
			for(NetworkListener listener: listeners){
				((ControllerNetworkListener)listener).remoteDesktopContentReceived(sender, items);
			}
		}
	}

	public void assignmentInfoReceivedFromUser(TableIdentity senderTableIdentity, UserIdentity senderUserIdentity, AssignmentInfo assignmentInfo) {
		for(AssignmentSession session: AssignmentManager.getManager().getAssignmentSessions().values()){
			if(session.getAssignment().getAssignmentId().equals(assignmentInfo.getAssignmentId())){
				UserInfo userInfo = new UserInfo(senderUserIdentity);
				userInfo.setTableIdentity(senderTableIdentity);
				session.getReceivedData().put(userInfo, assignmentInfo);
			}
		}
		for(NetworkListener listener: listeners){
			((ControllerNetworkListener)listener).resultsReceivedFromUser(senderTableIdentity, senderUserIdentity, assignmentInfo);
		}
	}

	public void projectorFound(TableIdentity projectorId, boolean isLeaseSuccessful) {
		for(NetworkListener listener: listeners){	
			((ControllerNetworkListener)listener).projectorFound(projectorId, isLeaseSuccessful);
		}
	}

	public void fireSyncDataReceived(TableIdentity sender,	HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {
		for(NetworkListener listener: listeners){	
			((ControllerNetworkListener)listener).syncDataReceived(sender, mathPadSyncData);
		}		
	}

	public void tableIdReceived(TableIdentity tableId) {
		for(NetworkListener listener: listeners){	
			((ControllerNetworkListener)listener).tableIdReceived(tableId);
		}
	}
}

