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

import core.SynergyNetDesktop;

import apps.mathpadapp.controllerapp.MathPadController;
import apps.mathpadapp.controllerapp.assignmentbuilder.Assignment;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.mathtool.AssignmentHandler;
import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.NetworkedContentManager;
import apps.mathpadapp.networkmanager.managers.syncmanager.SyncManager;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostMathPadItemsFromTableMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostRemoteDesktopMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostTableIdMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromtable.PostUserIdsFromTableMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.PostAssignmentInfoFromUserMessage;
import apps.mathpadapp.networkmanager.messages.fromclient.fromuser.PostMathPadItemFromUserMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.util.MTMessageBox;
import apps.mathpadapp.util.MTMessageBox.MessageListener;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;


/**
 * The Class ClientManager.
 */
public class ClientManager extends NetworkedContentManager{
	
	
	/**
	 * Instantiates a new client manager.
	 *
	 * @param contentSystem the content system
	 * @param comms the comms
	 * @param receiverClasses the receiver classes
	 */
	public ClientManager(ContentSystem contentSystem , TableCommsClientService comms, ArrayList<Class<?>> receiverClasses){
		super(contentSystem, comms, receiverClasses);
		super.setSyncManager(new SyncManager(this));
	}
	
	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.NetworkedContentManager#registerMathPad(apps.mathpadapp.networkmanager.utils.UserIdentity, apps.mathpadapp.mathtool.MathTool)
	 */
	public void registerMathPad(UserIdentity userId, MathTool tool){
		super.registerMathPad(userId, tool);
	}
	
	/**
	 * Block table.
	 *
	 * @param tableBlockEnabled the table block enabled
	 */
	public void blockTable(boolean tableBlockEnabled) {
		SynergyNetDesktop.getInstance().getMultiTouchInputComponent().setMultiTouchInputEnabled(!tableBlockEnabled);
	}

	/**
	 * Hide math pads.
	 *
	 * @param hideMathPadsEnabled the hide math pads enabled
	 */
	public void hideMathPads(boolean hideMathPadsEnabled) {
		for(MathTool tool: this.mathPads.values()){
			tool.getWindow().setVisible(!hideMathPadsEnabled);
		}
	}
	
	/**
	 * Post math pad item to table.
	 *
	 * @param sender the sender
	 * @param recipientUserIdentity the recipient user identity
	 */
	public void postMathPadItemToTable(TableIdentity sender, UserIdentity recipientUserIdentity) {
		if(mathPads.containsKey(recipientUserIdentity)){
			MathToolInitSettings padSettings = mathPads.get(recipientUserIdentity).getInitSettings();
			PostMathPadItemFromUserMessage msg = new PostMathPadItemFromUserMessage(MathPadController.class, padSettings, sender, recipientUserIdentity);
			this.sendMessage(msg);
		}
	}
	
	/**
	 * Post all math pads to table.
	 *
	 * @param requesterTable the requester table
	 */
	public void postAllMathPadsToTable(TableIdentity requesterTable) {
		HashMap<UserIdentity, MathToolInitSettings> padsInfo = new HashMap<UserIdentity, MathToolInitSettings>();
		for(UserIdentity userId: mathPads.keySet()){
			MathToolInitSettings padSettings = mathPads.get(userId).getInitSettings();
			padsInfo.put(userId, padSettings);
		}
		PostMathPadItemsFromTableMessage msg = new PostMathPadItemsFromTableMessage(MathPadController.class, padsInfo,requesterTable);
		this.sendMessage(msg);
	}

	/**
	 * Post all user ids to table.
	 *
	 * @param requesterTable the requester table
	 */
	public void postAllUserIdsToTable(TableIdentity requesterTable) {
		PostUserIdsFromTableMessage msg = new PostUserIdsFromTableMessage(MathPadController.class, new ArrayList<UserIdentity>(mathPads.keySet()),requesterTable);
		this.sendMessage(msg);
	}

	/**
	 * Block math pad for user.
	 *
	 * @param recipientUserIdentity the recipient user identity
	 * @param blockPad the block pad
	 */
	public void blockMathPadForUser(UserIdentity recipientUserIdentity,	boolean blockPad) {
		if(mathPads.containsKey(recipientUserIdentity)){
			mathPads.get(recipientUserIdentity).getWindow().setRotateTranslateScalable(!blockPad);
		}
	}

	/**
	 * Hide math pad for user.
	 *
	 * @param recipientUserIdentity the recipient user identity
	 * @param hidePad the hide pad
	 */
	public void hideMathPadForUser(UserIdentity recipientUserIdentity,	boolean hidePad) {
		if(mathPads.containsKey(recipientUserIdentity)){
			mathPads.get(recipientUserIdentity).getWindow().setVisible(!hidePad);
		}
	}

	/**
	 * Sets the remote desktop with table enabled.
	 *
	 * @param requesterTable the requester table
	 * @param remoteDesktopEnabled the remote desktop enabled
	 */
	public void setRemoteDesktopWithTableEnabled(TableIdentity requesterTable,	boolean remoteDesktopEnabled) {
		if(remoteDesktopEnabled){
			HashMap<UserIdentity, MathToolInitSettings> padsInfo = new HashMap<UserIdentity, MathToolInitSettings>();
			for(UserIdentity userId: mathPads.keySet()){
				MathToolInitSettings padSettings = mathPads.get(userId).getInitSettings();
				padsInfo.put(userId, padSettings);
			}
			PostRemoteDesktopMessage msg = new PostRemoteDesktopMessage(MathPadController.class, padsInfo, requesterTable);
			this.sendMessage(msg);
		}
		this.getSyncManager().enableUnicastTableSync(requesterTable,remoteDesktopEnabled);
	}

	/**
	 * Assignment received to table.
	 *
	 * @param sender the sender
	 * @param assignment the assignment
	 */
	public void assignmentReceivedToTable(TableIdentity sender, Assignment assignment) {
		for(UserIdentity userId: this.mathPads.keySet()) assignmentReceivedToUser(sender, userId, assignment);
	}

	/**
	 * Assignment received to user.
	 *
	 * @param sender the sender
	 * @param userId the user id
	 * @param assignment the assignment
	 */
	public void assignmentReceivedToUser(TableIdentity sender,	UserIdentity userId, Assignment assignment) {
		if(mathPads.containsKey(userId)){
			for(MathPad pad:mathPads.get(userId).getAllPads()) pad.clearAll();
			AssignmentHandler assignmentHandler = mathPads.get(userId).getAssignmentHandler();
			assignmentHandler.setAssignment(assignment);
			assignmentHandler.setAssignmentSender(sender);
			assignmentHandler.drawAssignment();
			
			final MTMessageBox msg = new MTMessageBox(mathPads.get(userId), contentSystem);
			msg.setTitle("New Assignment");
			msg.setMessage("New Assignment Received");
			msg.getCancelButton().setVisible(false);
			msg.getOkButton().setLocalLocation(0, msg.getOkButton().getLocalLocation().y);
			msg.addMessageListener(new MessageListener(){
				@Override
				public void buttonClicked(String buttonId) {
				}

				@Override
				public void buttonReleased(String buttonId) {
					msg.close();
				}
			});
		}
	}

	/**
	 * Post assignment results.
	 *
	 * @param requesterTable the requester table
	 * @param userId the user id
	 */
	public void postAssignmentResults(TableIdentity requesterTable, UserIdentity userId) {
		if(mathPads.containsKey(userId)){
			AssignmentInfo info = mathPads.get(userId).getCurrentAssignmentInfo();
			if(info == null) return;
			PostAssignmentInfoFromUserMessage msg = new PostAssignmentInfoFromUserMessage(MathPadController.class, info, requesterTable, userId);
			this.sendMessage(msg);
		}
	}

	/**
	 * Cancel assignment.
	 *
	 * @param sender the sender
	 * @param recipientUserIdentity the recipient user identity
	 */
	public void cancelAssignment(TableIdentity sender,	UserIdentity recipientUserIdentity) {
		if(mathPads.containsKey(recipientUserIdentity)){
			AssignmentHandler assignmentHandler = mathPads.get(recipientUserIdentity).getAssignmentHandler();
			assignmentHandler.deleteAssignment();
			
			final MTMessageBox msg = new MTMessageBox(mathPads.get(recipientUserIdentity), contentSystem);
			msg.setTitle("Assignment Cancelled");
			msg.setMessage("Assignment has been cancelled \nby the teacher");
			msg.getCancelButton().setVisible(false);
			msg.getOkButton().setLocalLocation(0, msg.getOkButton().getLocalLocation().y);
			msg.addMessageListener(new MessageListener(){
				@Override
				public void buttonClicked(String buttonId) {
				}

				@Override
				public void buttonReleased(String buttonId) {
					msg.close();
				}
			});
		}
	}

	/**
	 * Enabled table block.
	 *
	 * @param sender the sender
	 * @param blockEnabled the block enabled
	 */
	public void enabledTableBlock(TableIdentity sender, boolean blockEnabled) {
			SynergyNetDesktop.getInstance().getMultiTouchInputComponent().setMultiTouchInputEnabled(!blockEnabled);
	}

	/**
	 * Message received from server.
	 *
	 * @param sender the sender
	 * @param recipientUserIdentity the recipient user identity
	 * @param message the message
	 */
	public void messageReceivedFromServer(TableIdentity sender, UserIdentity recipientUserIdentity, String message) {
		if(mathPads.containsKey(recipientUserIdentity)){
			final MTMessageBox msg = new MTMessageBox(mathPads.get(recipientUserIdentity), contentSystem);
			msg.setTitle("Message from "+sender.toString());
			msg.setMessage(message);
			msg.getCancelButton().setVisible(false);
			msg.getOkButton().setLocalLocation(0, msg.getOkButton().getLocalLocation().y);
			msg.addMessageListener(new MessageListener(){
				@Override
				public void buttonClicked(String buttonId) {
				}

				@Override
				public void buttonReleased(String buttonId) {
					msg.close();
				}
			});
		}
	}

	/**
	 * Terminate pad for user.
	 *
	 * @param userId the user id
	 */
	public void terminatePadForUser(UserIdentity userId) {
		if(mathPads.containsKey(userId)){
			MathTool tool = mathPads.remove(userId);
			tool.terminate();
		}
	}

	/**
	 * Post table id.
	 *
	 * @param requester the requester
	 */
	public void postTableId(TableIdentity requester) {
		PostTableIdMessage msg = new PostTableIdMessage(MathPadController.class, requester);
		this.sendMessage(msg);
	}
}
