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

package apps.mathpadapp.controllerapp.assignmentcontroller;


import java.awt.Color;
import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentManager;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.util.MTFrame;
import apps.mathpadapp.util.MTMessageBox;
import apps.mathpadapp.util.MTMessageBox.MessageListener;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class ResultDialog extends MTFrame implements ControllerNetworkListener{
	
	public static final int windowWidth = 400;
	public static final int windowHeight = 480;
	
	//private ResultListControlPanel controlPanel;
	public ResultList resultListPanel;
	private String assignmentId;
	private ControllerManager controllerManager;
	private HashMap<UserIdentity, AssignmentInfo> resultMap = new HashMap<UserIdentity, AssignmentInfo>();
	
	public ResultDialog(String assignmentId, final ContentSystem contentSystem, final ControllerManager controllerManager){
		super(contentSystem);
		this.assignmentId = assignmentId;
		this.controllerManager = controllerManager;
		this.setWidth(windowWidth);
		this.setHeight(windowHeight);
		resultListPanel = new ResultList(contentSystem);
		resultListPanel.setWidth(360);
		resultListPanel.setHeight(400);
		resultListPanel.getContainer().setLocalLocation(0,-20);
		this.getWindow().addSubItem(resultListPanel.getContainer());

		final SimpleButton autoCheckBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		autoCheckBtn.setText("Auto check");
		autoCheckBtn.setLocalLocation(0, -210);
		autoCheckBtn.setBackgroundColour(Color.LIGHT_GRAY);
		autoCheckBtn.setTextColour(Color.black);
		this.getWindow().addSubItem(autoCheckBtn);
		autoCheckBtn.addButtonListener(new SimpleButtonListener(){

			@Override
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				AssignmentInfo solution =null;
				for(AssignmentSession session: AssignmentManager.getManager().getAssignmentSessions().values()){
					if(session.getAssignment().getAssignmentId().equals(ResultDialog.this.assignmentId)){
						solution = session.getSolution();
					}
				}
				if(solution == null || solution.getExpressionResult()== null || solution.getExpressionResult().isEmpty()){
					final MTMessageBox msg = new MTMessageBox(null,contentSystem);
					msg.setTitle("Error");
					msg.setMessage("Correct Solution is not set \nfor this nassignment. Please edit the\n session to set one.");
					msg.getWindow().setAsTopObject();
					msg.getWindow().setAngle(ResultDialog.this.getWindow().getAngle());
					msg.getWindow().setScale(ResultDialog.this.getWindow().getScale());
					msg.getWindow().setLocalLocation(ResultDialog.this.getWindow().getLocalLocation());
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
					return;
				}
				for(Object item: resultListPanel.getManager().getAllItems()){
					AssignmentInfo resultEntry = (AssignmentInfo) item;
					if(resultEntry.getExpressionResult() != null && !resultEntry.getExpressionResult().isEmpty()){
						ContentItem contentItem = resultListPanel.getManager().getListItem(item);
						TextLabel resultBox = null;
						for(ContentItem subItem: ((Window)contentItem).getAllItemsIncludeSystemItems()){
							if(subItem.getNote().equals("resultBox")) resultBox = (TextLabel) subItem;
						}
						if(resultEntry.getExpressionResult().get(0).equalsIgnoreCase(solution.getExpressionResult().get(0))){
							resultBox.setBackgroundColour(Color.green);
							resultBox.setTextColour(Color.black);

						}else{
							resultBox.setBackgroundColour(Color.red);
							resultBox.setTextColour(Color.black);
						}
					}

				}
			}

			@Override
			public void buttonDragged(SimpleButton b, long id, float x,
					float y, float pressure) {
				 
				
			}

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				autoCheckBtn.setBackgroundColour(Color.DARK_GRAY);
				autoCheckBtn.setTextColour(Color.white);
			}

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				autoCheckBtn.setBackgroundColour(Color.LIGHT_GRAY);
				autoCheckBtn.setTextColour(Color.black);
			}
		});
		
		closeButton.removeButtonListeners();
		closeButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				ResultDialog.this.close();
			}
		});
		if(controllerManager != null) controllerManager.addNetworkListener(this);
	}

	public ResultList getResultList() {
		return this.resultListPanel;
	}

	@Override
	public void userIdsReceived(TableIdentity tableId,
			List<UserIdentity> userIds) {
		 
		
	}

	@Override
	public void userRegistrationReceived(TableIdentity tableId,
			UserIdentity userId) {
		 
		
	}

	@Override
	public void userUnregistrationReceived(TableIdentity tableId,
			UserIdentity userId) {
		 
		
	}

	@Override
	public void resultsReceivedFromUser(TableIdentity tableId,	UserIdentity userId, AssignmentInfo assignInfo) {
		if(resultListPanel.getContainer() != null && assignInfo.getAssignmentId().equals(this.assignmentId)){	
			if(resultMap.containsKey(userId)){
				resultListPanel.getManager().deleteItem(assignInfo);
				resultMap.remove(userId);
			}
			resultListPanel.getManager().addItem(assignInfo.getAssignmentId(), assignInfo);
			resultMap.put(userId, assignInfo);
			ContentItem contentItem = resultListPanel.getManager().getListItem(assignInfo);
			if(contentItem instanceof Window){
				for(ContentItem subItem: ((Window)contentItem).getAllItemsIncludeSystemItems()){
					if(subItem instanceof TextLabel){
						TextLabel labelItem = (TextLabel) subItem;
						if(labelItem.getNote().equals("userName")) labelItem.setText(userId.getUserIdentity().toString());
						if(!assignInfo.getExpressionResult().isEmpty())	
							if(labelItem.getNote().equals("resultBox")) labelItem.setText(assignInfo.getExpressionResult().get(0));
					}
				}
			}
		}
	}
	
	public void close(){
		if(controllerManager != null) controllerManager.removeNetworkListener(ResultDialog.this);
		contentSystem.removeContentItem(ResultDialog.this.getWindow());
	}

	@Override
	public void projectorFound(TableIdentity tableId, boolean isLeaseSuccessful) {
		 
		
	}

	@Override
	public void userMathPadReceived(TableIdentity tableId,
			UserIdentity userId, MathToolInitSettings mathToolSettings) {
		 
		
	}

	@Override
	public void remoteDesktopContentReceived(TableIdentity tableId,
			HashMap<UserIdentity, MathToolInitSettings> items) {
		 
		
	}

	@Override
	public void syncDataReceived(TableIdentity sender,
			HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {
		 
		
	}

	@Override
	public void tableIdReceived(TableIdentity tableId) {
		 
		
	}
}
