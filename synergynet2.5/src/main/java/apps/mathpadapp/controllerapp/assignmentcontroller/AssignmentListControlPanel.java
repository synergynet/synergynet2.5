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
import java.util.ArrayList;
import java.util.List;

import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilder;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilderListenerImpl;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentManager;
import apps.mathpadapp.controllerapp.usercontroller.UserInfo;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.CancelMathAssignmentMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.RequestResultsFromUserMessage;
import apps.mathpadapp.util.MTList;
import apps.mathpadapp.util.MTListManager;
import apps.mathpadapp.util.MTMessageBox;
import apps.mathpadapp.util.MTMessageBox.MessageListener;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;


/**
 * The Class AssignmentListControlPanel.
 */
public class AssignmentListControlPanel {
	
	/** The container. */
	protected OrthoContainer container;
	
	/** The assign list. */
	private MTList assignList;
	
	/** The assign panel manager. */
	private MTListManager assignPanelManager;
	
	/** The Constant controlAssignPanelHeight. */
	public static final int controlAssignPanelHeight = 140;
	
	/** The controller manager. */
	private ControllerManager controllerManager;
	
	/** The content system. */
	private ContentSystem contentSystem;
	
	/**
	 * Instantiates a new assignment list control panel.
	 *
	 * @param contentSystem the content system
	 * @param assignList the assign list
	 * @param controllerManager the controller manager
	 */
	public AssignmentListControlPanel(ContentSystem contentSystem, final MTList assignList, ControllerManager controllerManager){
		container  = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		this.assignList = assignList;
		this.assignPanelManager = assignList.getManager();
		this.controllerManager = controllerManager;
		this.contentSystem = contentSystem;
		List<String> buttonActions = new ArrayList<String>();
		buttonActions.add("Select All");
		buttonActions.add("Deselect All");
		buttonActions.add("Edit");
		buttonActions.add("Delete");
		buttonActions.add("Retrieve Results");
		
		int panelWidth = AssignmentControllerWindow.windowWidth- 20;
		int shiftX = -panelWidth/2;
		int shiftY = 0;
		for(int i=0; i<buttonActions.size(); i++){
			SimpleButton button = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
			button.setText(buttonActions.get(i));
			button.addButtonListener(new ButtonAction());
			button.setLocalLocation(shiftX + button.getWidth()/2, shiftY);
			shiftX+= button.getWidth()+ 5;
			if((shiftX+ button.getWidth()/2)> panelWidth/2){
				shiftX = -panelWidth/2;
				shiftY-= button.getHeight() + 5;
			}
			container.addSubItem(button);
		}
	}
	
	/**
	 * The Class ButtonAction.
	 */
	class ButtonAction implements SimpleButtonListener{

		/* (non-Javadoc)
		 * @see synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener#buttonClicked(synergynetframework.appsystem.contentsystem.items.SimpleButton, long, float, float, float)
		 */
		@Override
		public void buttonClicked(SimpleButton b, long id, float x, float y,
				float pressure) {
			if(b.getText().equalsIgnoreCase("Select All")){
				assignPanelManager.selectAllItems();
			}else if(b.getText().equalsIgnoreCase("Deselect All")){
				assignPanelManager.deselectAllItems();
			}else if(b.getText().equalsIgnoreCase("Edit")){
				for(Object item: assignPanelManager.getSelectedItems()){
					AssignmentSession session = (AssignmentSession) item;
					AssignmentBuilder ab = new AssignmentBuilder(contentSystem, controllerManager.getGraphManager());
					ab.getAssignmentHandler().setAssignment(session.getAssignment());
					ab.getAssignmentHandler().drawAssignment();
					if(session.getSolution().getHandwritingResult() != null){
						ab.getAnswerDialog().getAnswerPad().draw(session.getSolution().getHandwritingResult());
					}
					ab.addMathToolListener(new AssignmentBuilderListenerImpl(controllerManager));
				}
			}else if(b.getText().equalsIgnoreCase("Delete")){
				if(assignPanelManager.getSelectedItems().isEmpty()) return;
				final MTMessageBox msg = new MTMessageBox(null,contentSystem);
				msg.setTitle("Delete");
				msg.setMessage("Are you sure you want to delete\n the selected assignment?");
				msg.getWindow().setAsTopObject();
				msg.getWindow().setAngle(assignList.getContainer().getParent().getAngle());
				msg.getWindow().setScale(assignList.getContainer().getParent().getScale());
				msg.getWindow().setLocalLocation(assignList.getContainer().getParent().getLocalLocation());

				msg.addMessageListener(new MessageListener(){
					@Override
					public void buttonReleased(String buttonId) {
						if(buttonId.equals("OK")){
							for(Object item: assignPanelManager.getSelectedItems()){
								AssignmentSession session = (AssignmentSession) item;
								AssignmentManager.getManager().getAssignmentSessions().remove(session.getAssignment().getAssignmentId());
								for(UserInfo userInfo: session.getRecipients()){
									CancelMathAssignmentMessage msg = new CancelMathAssignmentMessage(MathPadClient.class, userInfo.getTableIdentity(), userInfo.getUserIdentity());
									controllerManager.sendMessage(msg);
								}
							}
							assignPanelManager.deleteSelectedItems();
						}
						msg.close();
					}

					@Override
					public void buttonClicked(String buttonId) {
						 
						
					}
				});
			}else if(b.getText().equalsIgnoreCase("Retrieve Results")){
				List<Object> items = assignPanelManager.getSelectedItems();
				for(Object item: items){
					AssignmentSession session = (AssignmentSession) item; 
					ResultDialog resultDialog = new ResultDialog(session.getAssignment().getAssignmentId(),contentSystem, controllerManager);
					resultDialog.close();
					resultDialog = new ResultDialog(session.getAssignment().getAssignmentId(),contentSystem, controllerManager);
					resultDialog.getWindow().setAsTopObject();
					if(controllerManager != null){
						for(UserInfo receipent: session.getRecipients()){
							RequestResultsFromUserMessage msg = new RequestResultsFromUserMessage(MathPadClient.class, receipent.getTableIdentity(), receipent.getUserIdentity());
							controllerManager.sendMessage(msg);
						}
					}
				}
			}	
		}

		/* (non-Javadoc)
		 * @see synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener#buttonDragged(synergynetframework.appsystem.contentsystem.items.SimpleButton, long, float, float, float)
		 */
		@Override
		public void buttonDragged(SimpleButton b, long id, float x, float y, float pressure) {
		}

		/* (non-Javadoc)
		 * @see synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener#buttonPressed(synergynetframework.appsystem.contentsystem.items.SimpleButton, long, float, float, float)
		 */
		@Override
		public void buttonPressed(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}

		/* (non-Javadoc)
		 * @see synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener#buttonReleased(synergynetframework.appsystem.contentsystem.items.SimpleButton, long, float, float, float)
		 */
		@Override
		public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}
		
	}
	
	/**
	 * Gets the container.
	 *
	 * @return the container
	 */
	public OrthoContainer getContainer(){
		return container;
	}
}
