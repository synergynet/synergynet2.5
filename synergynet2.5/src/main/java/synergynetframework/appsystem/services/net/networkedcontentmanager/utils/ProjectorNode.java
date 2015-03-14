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

package synergynetframework.appsystem.services.net.networkedcontentmanager.utils;

import java.util.Map;

import apps.conceptmap.ConceptMapApp;
import apps.conceptmap.graphcomponents.link.GraphLink;
import apps.conceptmap.graphcomponents.nodes.QuadNode;
import apps.conceptmap.utility.ConceptMapListener;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.controllers.ProjectorController;

public class ProjectorNode extends QuadNode{

	protected TableIdentity tableId;
	protected LightImageLabel image;
	protected RemoteDesktop desktop;
	protected ProjectorController projectorController;
	private boolean isUsed = false;
	private boolean isShowOn = false;
	
	public ProjectorNode(final TableIdentity tableId, final NetworkedContentManager networkManager) {
		super(networkManager.getContentSystem(), networkManager.getGraphManager());
		this.tableId = tableId;
		this.projectorController = networkManager.getProjectorController();
		final ProjectorNode instance = this;
		image = (LightImageLabel)this.contentSystem.createContentItem(LightImageLabel.class);
		image.drawImage(ConceptMapApp.class.getResource("images/projector.jpg"));
		super.setNodeContent(image);
		this.getLinkButton().setVisible(false);
		this.getCloseButton().setVisible(false);
		final ListContainer menu = (ListContainer) contentSystem.createContentItem(ListContainer.class);
		menu.setWidth(130);
		menu.setItemWidth(120);
		menu.setItemHeight(30);
		final SimpleButton button1 = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		button1.setAutoFitSize(false);
		button1.setText("Start");
		button1.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(desktop != null && instance.isBeingUsed()){
					if(!isShowOn){ 
						// turn show on
						projectorController.sendDataToProjector(tableId, desktop.getOnlineItems());

						instance.getIncomingLinks().get(0).setLinkMode(LineItem.ANIMATION);
						button1.setText("Stop");
					}else{
						// turn show off
						instance.getIncomingLinks().get(0).setLinkMode(LineItem.CONNECTED_LINE);
						button1.setText("Start");
					}
					isShowOn = !isShowOn;
					menu.setVisible(false);
				}
				
			}
		});
		button1.setVisible(false);
		
		final SimpleButton button2 = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		button2.setAutoFitSize(false);
		button2.setText("Detach");
		button2.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(instance.isBeingUsed()){
					if(isShowOn){
						//stop show
						instance.isShowOn = false;
					}
					projectorController.sendClearProjectorMessage(tableId);
					desktop = null;
					instance.setBeingUsed(false);
					GraphLink link = instance.getIncomingLinks().get(0);
					link.remove();
					instance.graphManager.detachGraphLink(link);
					button1.setVisible(false);
					button2.setVisible(false);
					menu.setVisible(false);
				}
			}
		});
		button2.setVisible(false);
		
		SimpleButton button3 = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		button3.setAutoFitSize(false);
		button3.setText("Disconnect");
		button3.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				projectorController.sendReleaseProjectorMessage(tableId);
				isShowOn = false;
				instance.setBeingUsed(false);
				instance.remove();
				instance.graphManager.detachGraphNode(instance);
				instance.contentSystem.removeContentItem(menu);
				button1.setVisible(false);
				button2.setVisible(false);
			}
		});
		menu.setBorderSize(2);
		menu.setSpaceToTop(15);
		menu.addSubItem(button1);
		menu.addSubItem(button2);
		menu.addSubItem(button3);
		menu.setVisible(false);
		
		this.addConceptMapListener(new ConceptMapListener(){

			
			@Override
			public void nodeConnected(GraphLink link) {
				if(link.getSourceNode() instanceof RemoteDesktop && !instance.isBeingUsed()){
					link.getLineItem().removeItemListerners();
					instance.desktop = (RemoteDesktop)link.getSourceNode();
					instance.setBeingUsed(true);
					button1.setText("Start");
					button1.setVisible(true);
					button2.setVisible(true);
			}
				else
					instance.graphManager.detachGraphLink(link);
			}
			
			

			@Override
			public void nodeDisconnected(GraphLink link) {
				System.out.println("Node disconnected");

			}
			
		});
		
		
		((OrthoContentItem)this.getNodeContent()).addScreenCursorListener(new ScreenCursorListener(){

			@Override
			public void screenCursorChanged(ContentItem item, long id,
					float x, float y, float pressure) {
					menu.setVisible(false);
			}

			@Override
			public void screenCursorClicked(ContentItem item, long id,
					float x, float y, float pressure) {
				 
				menu.setAsTopObject();
				menu.setLocalLocation(x, y - menu.getHeight());
				menu.setVisible(true);
			}

			@Override
			public void screenCursorPressed(ContentItem item, long id,
					float x, float y, float pressure) {
				 
				
			}

			@Override
			public void screenCursorReleased(ContentItem item, long id,
					float x, float y, float pressure) {
				 
				
			}

			
		});
	}
	
	
	public TableIdentity getTableId(){
		return tableId;
	}
	
	public boolean isBeingUsed(){
		return isUsed;
	}
	
	public void setBeingUsed(boolean isUsed){
		this.isUsed = isUsed; 
	}
	
	public void synchronise(Map<String, Map<String, String>> synchronisedItems){
		if(isShowOn && desktop != null)
			projectorController.sendProjectorSyncMessage(desktop.getTableId(), tableId, synchronisedItems);
	}
	
	public boolean isShowOn(){
		return isShowOn;
	}

}
