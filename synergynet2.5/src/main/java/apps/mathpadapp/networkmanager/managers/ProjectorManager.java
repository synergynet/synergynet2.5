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

import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.syncmanager.SyncManager;
import apps.mathpadapp.networkmanager.messages.fromprojector.ProjectorResponse;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;


/**
 * The Class ProjectorManager.
 */
public class ProjectorManager extends NetworkedContentManager{

	/** The is busy. */
	protected boolean isBusy = false;
	
	/** The controller table. */
	protected TableIdentity controllerTable;
	
	/**
	 * Instantiates a new projector manager.
	 *
	 * @param contentSystem the content system
	 * @param comms the comms
	 * @param receiverClasses the receiver classes
	 */
	public ProjectorManager(ContentSystem contentSystem, TableCommsClientService comms, ArrayList<Class<?>> receiverClasses) {
		super(contentSystem, comms, receiverClasses);
		super.setSyncManager(new SyncManager(this));
	}
	
	/**
	 * Checks if is projector busy.
	 *
	 * @return true, if is projector busy
	 */
	public boolean isProjectorBusy(){
		return isBusy;
	}
	
	/**
	 * Sets the projector busy.
	 *
	 * @param isBusy the new projector busy
	 */
	public void setProjectorBusy(boolean isBusy){
		this.isBusy = isBusy;
	}

	/**
	 * Lease projector.
	 *
	 * @param requesterTable the requester table
	 */
	public void leaseProjector(TableIdentity requesterTable){
		boolean leaseSucceed = false;
		if(!isBusy){
			leaseSucceed = true;
			this.setProjectorBusy(true);
			this.setController(requesterTable);
		}
		for(Class<?> receiverClass: this.getReceiverClasses()){
			ProjectorResponse response = new ProjectorResponse(receiverClass, leaseSucceed, requesterTable);
			this.sendMessage(response);
		}
	}

	/**
	 * Release projector.
	 *
	 * @param tableId the table id
	 */
	public void releaseProjector(TableIdentity tableId) {
		if(controllerTable.hashCode() == tableId.hashCode()){
			this.setProjectorBusy(false);
			this.setController(null);
		}
	}
	
	/**
	 * Sets the controller.
	 *
	 * @param requesterTable the new controller
	 */
	private void setController(TableIdentity requesterTable) {
		this.controllerTable = requesterTable;
	}

	/**
	 * Math pad items received from table.
	 *
	 * @param senderTable the sender table
	 * @param toolSettings the tool settings
	 */
	public void mathPadItemsReceivedFromTable(TableIdentity senderTable, HashMap<UserIdentity, MathToolInitSettings> toolSettings) {
		MathTool mathTool;
		for(UserIdentity userId: toolSettings.keySet()){
			if(!mathPads.containsKey(userId)){
				mathTool = new MathTool(contentSystem);
				this.registerMathPad(userId, mathTool);
			}else{
				mathTool = mathPads.get(userId);
			}
			mathTool.init(toolSettings.get(userId));
		}
	}

	/**
	 * Clear projector.
	 */
	public void clearProjector() {
		contentSystem.removeAllContentItems();
	}
}
