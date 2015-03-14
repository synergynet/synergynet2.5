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
import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.conceptmapping.GraphLink;
import apps.mathpadapp.conceptmapping.GraphNode;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.controllerapp.tablecontroller.MathPadRemoteDesktop;
import apps.mathpadapp.controllerapp.tablecontroller.RemoteDesktop;
import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener;
import apps.mathpadapp.networkmanager.messages.common.UnicastMathPadSyncMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.ClearProjector;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.PostMathPadItemsFromControllerMessage;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.toprojector.UnregisterUserMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.projectorapp.MathPadProjector;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class ProjectorNode extends GraphNode implements DataSourceListener, ControllerNetworkListener{

	protected TableIdentity tableId;
	protected ControllerManager controllerManager;
	protected Window window;
	protected ImageTextLabel image;
	protected List<GraphNode> dataSources;
	private boolean isShowOn = false;
	
	public ProjectorNode(final TableIdentity tableId, final ContentSystem contentSystem, final ControllerManager controllerManager) {
		super(controllerManager.getGraphManager(), (Window) contentSystem.createContentItem(Window.class));
		this.controllerManager = controllerManager;
		final ProjectorNode instance = this;
		this.tableId = tableId;
		dataSources = new ArrayList<GraphNode>();
		window = (Window)this.getNodeItem();
		window.setBackgroundColour(Color.white);
		window.setWidth(100);
		window.setHeight(80);

		image = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		image.setWidth(100);
		image.setHeight(80);
		image.setBackgroundColour(Color.white);
		image.setTextColour(Color.black);
		image.setBorderColour(Color.black);
		image.setBorderSize(1);
		image.setCRLFSeparatedString(tableId.toString());
		image.setImageInfo(MathPadResources.class.getResource("controlbar/Projectors.jpg"));
		window.addSubItem(image);
		
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
				HashMap<UserIdentity, MathToolInitSettings> toolSettings = new HashMap<UserIdentity, MathToolInitSettings>();
				for(GraphNode dataSource: dataSources){
					toolSettings.clear();
					if(dataSource instanceof MathPadRemoteDesktop){
						MathPadRemoteDesktop rd = (MathPadRemoteDesktop) dataSource;
						for(UserIdentity userId: rd.getOnlineMathTools().keySet()){
							toolSettings.put(userId, rd.getOnlineMathTools().get(userId).getInitSettings());
						}
					}else if(dataSource instanceof MathTool){
						controllerManager.getSyncManager().enableUnicastTableSync(tableId, true);
						MathTool tool = (MathTool) dataSource;
						toolSettings.put(controllerManager.getUserIdentityForMathTool(tool), tool.getInitSettings());
					}
					PostMathPadItemsFromControllerMessage msg = new PostMathPadItemsFromControllerMessage(MathPadProjector.class, toolSettings, tableId);
					controllerManager.sendMessage(msg);
				}
				menu.setVisible(false);
				if(!isShowOn){ 
					// turn show on
					//projectorController.sendDataToProjector(tableId, desktop.getOnlineItems());

					ProjectorNode.this.getIncomingLinks().get(0).setLinkMode(LineItem.ANIMATION);
					button1.setText("Stop");
				}else{
					// turn show off
					ProjectorNode.this.getIncomingLinks().get(0).setLinkMode(LineItem.CONNECTED_LINE);
					button1.setText("Start");
				}
				isShowOn = !isShowOn;
			}
		});
		button1.setVisible(true);
		
		final SimpleButton button2 = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		button2.setAutoFitSize(false);
		button2.setText("Detach");
		button2.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonClicked(SimpleButton b, long id, float x,
					float y, float pressure) {
				//if(instance.isBeingUsed()){
					if(isShowOn){
						//stop show
						instance.isShowOn = false;
					}
					ClearProjector msg = new ClearProjector(MathPadProjector.class, tableId);
					controllerManager.sendMessage(msg);
					dataSources.clear();
					//instance.setBeingUsed(false);
					GraphLink link = ProjectorNode.this.getIncomingLinks().get(0);
					link.remove();
					instance.getGraphManager().detachGraphLink(link);
					button1.setVisible(false);
					button2.setVisible(false);
					menu.setVisible(false);
				//}
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
				//projectorController.sendReleaseProjectorMessage(tableId);
				isShowOn = false;
				//instance.setBeingUsed(false);
				ClearProjector msg = new ClearProjector(MathPadProjector.class, tableId);
				controllerManager.sendMessage(msg);
				instance.remove();
				instance.getGraphManager().detachGraphNode(ProjectorNode.this);
				contentSystem.removeContentItem(menu);
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
		
		ProjectorNode.this.addConceptMapListener(new ConceptMapListener(){

			
			@Override
			public void nodeConnected(GraphLink link) {
				
				if(link.getSourceNode() instanceof RemoteDesktop || link.getSourceNode() instanceof MathTool/* && !instance.isBeingUsed()*/){
					link.getLineItem().removeItemListerners();
					ProjectorNode.this.dataSources.add(link.getSourceNode());
					//instance.setBeingUsed(true);
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
		
		
		ProjectorNode.this.getNodeItem().addScreenCursorListener(new ScreenCursorListener(){

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
	/*
	public boolean isBeingUsed(){
		return isUsed;
	}
	
	public void setBeingUsed(boolean isUsed){
		this.isUsed = isUsed; 
	}
	*/
	private void syncContent(HashMap<UserIdentity, HashMap<Short, Object>> syncData){
		if(/*isShowOn && */ !dataSources.isEmpty()){
			UnicastMathPadSyncMessage msg = new UnicastMathPadSyncMessage(MathPadProjector.class, syncData, tableId);
			controllerManager.sendMessage(msg);
		}
	}
	
	public boolean isShowOn(){
		return isShowOn;
	}
	
	public void setText(String text){
		image.setCRLFSeparatedString(text);
	}

	@Override
	public void sourceDataUpdated(TableIdentity tableId, HashMap<UserIdentity, MathToolInitSettings> items) {
		for(GraphNode dataSource: dataSources){
			if(dataSource instanceof MathTool || (dataSource instanceof MathPadRemoteDesktop && ((MathPadRemoteDesktop)dataSource).getTableId().equals(tableId))){
				PostMathPadItemsFromControllerMessage msg = new PostMathPadItemsFromControllerMessage(MathPadProjector.class, items, this.tableId);
				controllerManager.sendMessage(msg);
			}
		}
	}

	@Override
	public void syncDataReceived(TableIdentity tableId,	HashMap<UserIdentity, HashMap<Short, Object>> syncData) {
		for(GraphNode dataSource: dataSources){
			if(dataSource instanceof MathTool ||  dataSource instanceof MathPadRemoteDesktop && ((MathPadRemoteDesktop)dataSource).getTableId().equals(tableId)){
				this.syncContent(syncData);
			}
		}
	}

	@Override
	public void projectorFound(TableIdentity tableId, boolean isLeaseSuccessful) {	}

	@Override
	public void remoteDesktopContentReceived(TableIdentity tableId,	HashMap<UserIdentity, MathToolInitSettings> items) {}

	@Override
	public void resultsReceivedFromUser(TableIdentity tableId,	UserIdentity userId, AssignmentInfo assignInfo) {}

	@Override
	public void tableIdReceived(TableIdentity tableId) {}


	@Override
	public void userIdsReceived(TableIdentity tableId,	List<UserIdentity> userIds) {}

	@Override
	public void userMathPadReceived(TableIdentity tableId, UserIdentity userId,	MathToolInitSettings mathToolSettings) {}

	@Override
	public void userRegistrationReceived(TableIdentity tableId,	UserIdentity userId) {}

	@Override
	public void userUnregistrationReceived(TableIdentity tableId,	UserIdentity userId) {
		UnregisterUserMessage msg = new UnregisterUserMessage(MathPadProjector.class, this.tableId, userId);
		controllerManager.sendMessage(msg);
	}
}
