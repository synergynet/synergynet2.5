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

package apps.mathpadapp.controllerapp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilder;
import apps.mathpadapp.controllerapp.assignmentbuilder.AssignmentBuilderListenerImpl;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentControllerWindow;
import apps.mathpadapp.controllerapp.projectorcontroller.ProjectorControllerWindow;
import apps.mathpadapp.controllerapp.tablecontroller.TableControllerWindow;
import apps.mathpadapp.controllerapp.usercontroller.UserControllerWindow;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.broadcast.BroadcastSearchProjectorMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.projectorapp.MathPadProjector;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;

public class ControlBar {

	public static int BUTTON_WIDTH = 80;
	public static Color BUTTON_BORDER_COLOR = Color.red;
	
	protected ContentSystem contentSystem;
	protected ControllerManager controllerManager;
	protected int MAX_NUMBER_OF_PADS = 1;
	public static int currentNoOfPads = 0; 
	
	public ControlBar(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
		ListContainer list = (ListContainer) contentSystem.createContentItem(ListContainer.class);
		list.setHorizontal(true);
		list.setAutoFitSize(false);
		list.setHeight(100);
		list.setWidth(DisplaySystem.getDisplaySystem().getWidth());
		list.setItemHeight(BUTTON_WIDTH);
		list.setItemWidth(BUTTON_WIDTH);
		list.setRotateTranslateScalable(false);
		
		List<String> buttons = new ArrayList<String>();
		buttons.add("Math Pad");
		buttons.add("Users");
		buttons.add("Tables");
		buttons.add("Tasks");
		buttons.add("Projectors");

		for(String buttonName: buttons){
			ImageTextLabel btn = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
			btn.setAutoFitSize(false);
			btn.setWidth(BUTTON_WIDTH);
			btn.setHeight(BUTTON_WIDTH);
			btn.setBorderColour(BUTTON_BORDER_COLOR);
			btn.setBackgroundColour(Color.white);
			btn.setImageInfo(MathPadResources.class.getResource("controlbar/"+buttonName+".jpg"));
			btn.setCRLFSeparatedString(buttonName);
			btn.addItemListener(new ControlBarListener());
			list.addSubItem(btn);
		}		
	}
	
	public void setControllerManager(ControllerManager controllerManager){
		this.controllerManager = controllerManager;
	}
	
	class ControlBarListener implements ItemListener{

		@Override
		public void cursorChanged(ContentItem item, long id, float x, float y,
				float pressure) {
			 
			
		}

		@Override
		public void cursorClicked(ContentItem item, long id, float x, float y,
				float pressure) {
			if(controllerManager == null || item == null || !(item instanceof ImageTextLabel)) return;
			ImageTextLabel btn = (ImageTextLabel) item;
			String btnText = btn.getFirstLine();
			if(btnText.equals("Math Pad")){
				if(currentNoOfPads == MAX_NUMBER_OF_PADS) return;
				AssignmentBuilder ab = new AssignmentBuilder(contentSystem, controllerManager.getGraphManager());
				currentNoOfPads++;
				ab.setTitle("Task Builder");
				ab.getWindow().setAsTopObject();
				ab.addMathToolListener(new AssignmentBuilderListenerImpl(controllerManager));
				UserIdentity userId = new UserIdentity();
				userId.generateUniqueUserIdentity();
				controllerManager.registerMathPad(userId, ab);
			}else if(btnText.equals("Users")){
				new UserControllerWindow(contentSystem, controllerManager).getWindow().setAsTopObject();
			}else if(btnText.equals("Tables")){
				new TableControllerWindow(contentSystem, controllerManager).getWindow().setAsTopObject();
			}else if(btnText.equals("Tasks")){
				new AssignmentControllerWindow(contentSystem, controllerManager).getWindow().setAsTopObject();
			}else if(btnText.equals("Projectors")){
				new ProjectorControllerWindow(contentSystem, controllerManager).getWindow().setAsTopObject();
				if(controllerManager != null){
					BroadcastSearchProjectorMessage msg = new BroadcastSearchProjectorMessage(MathPadProjector.class);
					controllerManager.sendMessage(msg);
				}
			}
		}

		@Override
		public void cursorDoubleClicked(ContentItem item, long id, float x,
				float y, float pressure) {
			 
			
		}

		@Override
		public void cursorLongHeld(ContentItem item, long id, float x, float y,
				float pressure) {
			 
			
		}

		@Override
		public void cursorPressed(ContentItem item, long id, float x, float y,
				float pressure) {
			 
			
		}

		@Override
		public void cursorReleased(ContentItem item, long id, float x, float y,
				float pressure) {
			 
			
		}

		@Override
		public void cursorRightClicked(ContentItem item, long id, float x,
				float y, float pressure) {
			 
			
		}
		
	}
}
