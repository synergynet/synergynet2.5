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

package apps.mathpadapp.controllerapp.assignmentbuilder;

import java.util.List;
import java.util.UUID;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.mathtool.AssignmentHandler;
import apps.mathpadapp.mathtool.GraphConfig;
import apps.mathpadapp.mathtool.MathTool;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;

public class AssignmentBuilder extends MathTool{

	public AssignmentBuilder(final ContentSystem contentSystem, final GraphManager graphManager) {
		super(contentSystem, graphManager);
		
		final SimpleButton sendAssignment = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		sendAssignment.setAutoFitSize(false);
		sendAssignment.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		sendAssignment.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		sendAssignment.drawImage(MathPadResources.class.getResource("buttons/send_off.jpg"));
		sendAssignment.addButtonListener(new SimpleButtonAdapter(){

			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				sendAssignment.removeAllImages();
				sendAssignment.drawImage(MathPadResources.class.getResource("buttons/send_on.jpg"));
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				SendToDialog sendToDialog = new SendToDialog(AssignmentBuilder.this, contentSystem);
				sendToDialog.getWindow().setVisible(true);
				sendToDialog.getWindow().setLocalLocation(AssignmentBuilder.this.getWindow().getLocalLocation());
				sendToDialog.getWindow().setAngle(AssignmentBuilder.this.getWindow().getAngle());
				sendToDialog.getWindow().setScale(AssignmentBuilder.this.getWindow().getScale());
				sendToDialog.getWindow().setAsTopObject();
				
				for(MathToolListener listener: mathToolListeners){
					if(listener instanceof AssignmentBuilderListener) ((AssignmentBuilderListener)listener).sendToDialogDisplayed(sendToDialog);
				}

				sendAssignment.removeAllImages();
				sendAssignment.drawImage(MathPadResources.class.getResource("buttons/send_off.jpg"));
			}
		});
		controlPanel.getContentPanel().addSubItem(sendAssignment);
		controlPanel.registerButton(sendAssignment);		
		
		final SimpleButton options = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		options.setAutoFitSize(false);
		options.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		options.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		options.drawImage(MathPadResources.class.getResource("buttons/options.jpg"));
		options.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				SettingsDialog settingsDialog = new SettingsDialog(AssignmentBuilder.this, contentSystem);
				settingsDialog.getWindow().setLocalLocation(AssignmentBuilder.this.getWindow().getLocalLocation());
				settingsDialog.getWindow().setAngle(AssignmentBuilder.this.getWindow().getAngle());
				settingsDialog.getWindow().setScale(AssignmentBuilder.this.getWindow().getScale());
				settingsDialog.getWindow().setAsTopObject();
			}
			
		});
		
		controlPanel.getContentPanel().addSubItem(options);
		controlPanel.registerButton(options);
		
		controlPanel.getLoginButton().setVisible(false);
		controlPanel.getSolutionButton().setVisible(true);

		controlPanel.setLayout();
		controlPanel.getContentPanel().setAsTopObject();
	}
	
	public void fireSend(List<Object> receipents){
		if(assignmentHandler.getAssignment() == null){
			Assignment assignment = new Assignment(UUID.randomUUID().toString());
			assignment.setDrawData(this.getDrawData());
			assignmentHandler.setAssignment(assignment);
		}
		assignmentHandler.getAssignment().setDrawData(this.getDrawData());
		AssignmentInfo solution = this.getCurrentAssignmentInfo();
		for(MathToolListener listener: mathToolListeners){
			if(listener instanceof AssignmentBuilderListener) ((AssignmentBuilderListener)listener).assignmentSendRequest(assignmentHandler.getAssignment(), receipents, solution);
		}
	}
	
	public interface AssignmentBuilderListener extends MathToolListener{
		public void assignmentSendRequest(Assignment assignment, List<Object> receipents, AssignmentInfo solution);
		public void sendToDialogDisplayed(SendToDialog sendToDialog);
	}

	public AssignmentHandler getAssignmentHandler() {
		return assignmentHandler;
	}
}
