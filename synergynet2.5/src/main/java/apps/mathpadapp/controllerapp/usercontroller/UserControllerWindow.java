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

package apps.mathpadapp.controllerapp.usercontroller;

import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo.UserStatus;
import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.mathtool.MathTool.MathToolListener;
import apps.mathpadapp.mathtool.MathTool.SeparatorState;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.RequestAllUserIdsMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.UnicastSyncWithUserMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.util.MTFrame;
import apps.mathpadapp.util.MTList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

public class UserControllerWindow extends MTFrame implements ControllerNetworkListener{
	
	public static final int windowWidth = 600;
	public static final int windowHeight = 440;
	
	private UserListControlPanel controlPanel;
	private ControllerManager controllerManager;
	public MTList userListPanel;
	
	public UserControllerWindow(final ContentSystem contentSystem, final ControllerManager controllerManager){
		super(contentSystem);
		this.controllerManager = controllerManager;
		this.setWidth(windowWidth);
		this.setHeight(windowHeight);

		userListPanel = new MTList(contentSystem);
		this.getWindow().addSubItem(userListPanel.getContainer());
		userListPanel.getContainer().setLocalLocation(0, 20);

		controlPanel = new UserListControlPanel(contentSystem, userListPanel, controllerManager);
		controlPanel.getContainer().setLocalLocation(controlPanel.getContainer().getLocalLocation().x, -windowHeight/2 + 2*window.getBorderSize() + UserListControlPanel.controlUserPanelHeight/2);

		this.getWindow().addSubItem(controlPanel.getContainer());
		this.getWindow().setOrder(OrthoBringToTop.bottomMost);
		this.setTitle("Online Users");
		userListPanel.getManager().addItem("temp", "temp");
		userListPanel.getManager().deleteAllItems();
		if(controllerManager != null){
			controllerManager.addNetworkListener(this);
			RequestAllUserIdsMessage msg = new RequestAllUserIdsMessage(MathPadClient.class);
			controllerManager.sendMessage(msg);
		}
	}

	public MTList getUserList() {
		return userListPanel;
	}

	@Override
	public void close(){
		if(controllerManager != null) controllerManager.removeNetworkListener(this);
		super.close();
	}
	
	@Override
	public void projectorFound(TableIdentity tableId, boolean isLeaseSuccessful) {	}

	@Override
	public void remoteDesktopContentReceived(TableIdentity tableId,	HashMap<UserIdentity, MathToolInitSettings> items) {}

	@Override
	public void resultsReceivedFromUser(TableIdentity tableId,	UserIdentity userId, AssignmentInfo assignInfo) {}

	@Override
	public void syncDataReceived(TableIdentity sender,	HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {	}

	@Override
	public void userIdsReceived(TableIdentity tableId, List<UserIdentity> userIds) {

		for(UserIdentity userId: userIds){
			if(!this.containsUser(userId)){
				UserInfo uInfo = new UserInfo(userId);
				uInfo.setTableIdentity(tableId);
				uInfo.setUserStatus(UserStatus.ONLINE);
				userListPanel.getManager().addItem(userId.getUserIdentity(), uInfo);
				userListPanel.getManager().setIcon(uInfo, MathPadResources.class.getResource("userstatus/online.jpg"));
			}
		}
	}

	@Override
	public void tableIdReceived(TableIdentity tableId) {}
	
	@Override
	public void userMathPadReceived(final TableIdentity tableId, final UserIdentity userId,	MathToolInitSettings mathToolSettings) {
		MathTool mathTool;
		if(!controllerManager.getRegisteredMathPads().containsKey(userId)){
			mathTool = new MathTool(contentSystem, graphManager);
			controllerManager.registerMathPad(userId, mathTool);
			mathTool.addMathToolListener(new MathToolListener(){

				@Override
				public void assignmentAnswerReady(AssignmentInfo info) {}

				@Override
				public void mathPadClosed(MathTool mathTool) {
					UnicastSyncWithUserMessage syncMessage = new UnicastSyncWithUserMessage(MathPadClient.class, tableId, userId,false);
					controllerManager.sendMessage(syncMessage);
				}

				@Override
				public void separatorChanged(SeparatorState newState) {	}

				@Override
				public void userLogin(String userName, String password) {}
				
			});
		}else{
			mathTool = controllerManager.getRegisteredMathPads().get(tableId);
		}
		mathTool.init(mathToolSettings);		
	}

	@Override
	public void userRegistrationReceived(TableIdentity tableId,	UserIdentity userId) {
		if(!this.containsUser(userId)){
			UserInfo uInfo = new UserInfo(userId);
			uInfo.setTableIdentity(tableId);
			uInfo.setUserStatus(UserStatus.ONLINE);
			userListPanel.getManager().addItem(userId.getUserIdentity(), uInfo);
			userListPanel.getManager().setIcon(uInfo, MathPadResources.class.getResource("userstatus/online.jpg"));
		}
	}

	@Override
	public void userUnregistrationReceived(TableIdentity tableId, UserIdentity userId) {
		Object toRemoveItem = null;
		for(Object item: userListPanel.getManager().getAllItems()){
			UserInfo userInfo = (UserInfo) item;
			if(userInfo.getUserIdentity().equals(userId)) 
				toRemoveItem = userInfo;
		}
		if(toRemoveItem != null) userListPanel.getManager().deleteItem(toRemoveItem);
	}
	
	private boolean containsUser(UserIdentity userId){
		for(Object item: userListPanel.getManager().getAllItems()){
			UserInfo userInfo = (UserInfo) item;
			if(userInfo.getUserIdentity().equals(userId))
				return true;
		}
		return false;
	}
}
