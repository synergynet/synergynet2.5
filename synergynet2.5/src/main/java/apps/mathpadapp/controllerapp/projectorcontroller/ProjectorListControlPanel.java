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

package apps.mathpadapp.controllerapp.projectorcontroller;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.ReleaseProjectorMessage;
import apps.mathpadapp.projectorapp.MathPadProjector;
import apps.mathpadapp.util.MTList;
import apps.mathpadapp.util.MTListManager;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class ProjectorListControlPanel {
	
	protected OrthoContainer container;
	private MTList projectorList;
	private MTListManager projectorPanelManager;
	public static final int controlProjectorPanelHeight = 140;
	private ControllerManager controllerManager;
	private ContentSystem contentSystem;
	
	public ProjectorListControlPanel(ContentSystem contentSystem, final MTList projectorList, ControllerManager controllerManager){
		container  = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		this.contentSystem = contentSystem;
		this.projectorList = projectorList;
		this.projectorPanelManager = projectorList.getManager();
		this.controllerManager = controllerManager;
		List<String> buttonActions = new ArrayList<String>();
		buttonActions.add("Select All");
		buttonActions.add("Deselect All");
		buttonActions.add("Release");
		buttonActions.add("Retrieve");
		
		int panelWidth = ProjectorControllerWindow.windowWidth- 20;
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
	
	class ButtonAction implements SimpleButtonListener{

		@Override
		public void buttonClicked(SimpleButton b, long id, float x, float y,
				float pressure) {
			if(b.getText().equalsIgnoreCase("Select All")){
				projectorPanelManager.selectAllItems();
			}else if(b.getText().equalsIgnoreCase("Deselect All")){
				projectorPanelManager.deselectAllItems();
			}else if(b.getText().equalsIgnoreCase("Release")){
				for(Object item: projectorPanelManager.getSelectedItems()){
					ReleaseProjectorMessage msg = new ReleaseProjectorMessage(MathPadProjector.class, (TableIdentity)item);
					controllerManager.sendMessage(msg);
					if(ProjectorManager.getInstance().getProjectorNodes().containsKey((TableIdentity)item)){
						ProjectorNode projectorNode = ProjectorManager.getInstance().getProjectorNodes().get((TableIdentity)item);
						controllerManager.getGraphManager().detachGraphNode(projectorNode);
						controllerManager.removeNetworkListener(projectorNode);
						projectorNode.remove();
						ProjectorManager.getInstance().getProjectorNodes().remove((TableIdentity)item);
					}

				}
				projectorPanelManager.deleteSelectedItems();
			}else if(b.getText().equalsIgnoreCase("Retrieve")){
				for(Object item: projectorPanelManager.getSelectedItems()){
					if(!ProjectorManager.getInstance().getProjectorNodes().containsKey((TableIdentity)item)){
						ProjectorNode projectorNode = new ProjectorNode((TableIdentity)item, contentSystem, controllerManager);
						controllerManager.addNetworkListener(projectorNode);
						projectorNode.setText((((SimpleButton)projectorPanelManager.getListItem(item)).getText()));
						projectorNode.getNodeItem().setLocalLocation(projectorList.getContainer().getParent().getLocalLocation());
						projectorNode.getNodeItem().setAsTopObject();
						ProjectorManager.getInstance().getProjectorNodes().put((TableIdentity)item, projectorNode);
					}
				}
			}
		}

		@Override
		public void buttonDragged(SimpleButton b, long id, float x, float y, float pressure) {
		}

		@Override
		public void buttonPressed(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}

		@Override
		public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
			Color bgColor = b.getBackgroundColour();
			b.setBackgroundColour(b.getTextColour());
			b.setTextColour(bgColor);
		}
		
	}
	
	public OrthoContainer getContainer(){
		return container;
	}
}
