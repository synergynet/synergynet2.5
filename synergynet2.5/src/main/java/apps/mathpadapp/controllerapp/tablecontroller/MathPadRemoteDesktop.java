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

package apps.mathpadapp.controllerapp.tablecontroller;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.clientapp.MathPadClient;
import apps.mathpadapp.conceptmapping.GraphLink;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.controllerapp.projectorcontroller.DataSourceListener;
import apps.mathpadapp.controllerapp.projectorcontroller.ProjectorNode;
import apps.mathpadapp.mathtool.MathTool;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.NetworkedContentManager;
import apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener;
import apps.mathpadapp.networkmanager.managers.remotedesktopmanager.RemoteDesktopManager;
import apps.mathpadapp.networkmanager.managers.syncmanager.MathToolSyncRenderer;
import apps.mathpadapp.networkmanager.managers.syncmanager.SyncRenderer;
import apps.mathpadapp.networkmanager.messages.fromcontroller.unicast.touser.RequestMathPadItemFromUserMessage;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class MathPadRemoteDesktop extends RemoteDesktop implements ControllerNetworkListener{
	protected RemoteDesktopManager remoteDesktopManager;
	protected HashMap<UserIdentity, MathTool> onlineMathTools = new HashMap<UserIdentity, MathTool>();
	protected HashMap<UserIdentity, ContentItem> onlineUserTracks = new HashMap<UserIdentity, ContentItem>();
	protected ListContainer controlMenu;
	protected TextLabel title;
	protected SimpleButton controlMenuButton1, controlMenuButton2, controlMenuButton3, controlMenuButton4;
	protected ListContainer topBar;
	protected float defaultScale = 0.4f;
	protected boolean isOriented = false;
	protected boolean isTrackUserMode = false;
	protected boolean isFullScreenMode = false;
	
	protected transient List<DataSourceListener> listeners = new ArrayList<DataSourceListener>(); 
	
	public MathPadRemoteDesktop(final ContentSystem contentSystem, final NetworkedContentManager networkManager){
		super(contentSystem, networkManager);

		controlMenu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		controlMenu.setHorizontal(true);
		controlMenu.setBackgroundColour(Color.BLUE);
		controlMenu.setAutoFitSize(false);
		controlMenu.setSpaceToSide(10);
		controlMenu.setHeight(50);
		controlMenu.setWidth(((Window)this.getNodeItem()).getWidth());
		controlMenu.setItemHeight(30);
		controlMenu.setItemWidth(100);
		
		controlMenuButton1 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		controlMenuButton1.setAutoFitSize(false);
		controlMenuButton1.setText("Reorient");
		controlMenuButton1.setBackgroundColour(Color.lightGray);
		controlMenuButton1.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				// Reorient items for the teacher's view
				if(!isOriented){
					for(MathTool tool: onlineMathTools.values()) tool.getWindow().setAngle(0);
					if(syncRenderer != null) syncRenderer.disableSyncParameter(SyncRenderer.SYNC_ANGLE);
					controlMenuButton1.setText("Free move");
				}else{
					for(MathTool tool: onlineMathTools.values()) tool.getWindow().setAngle(0);
					if(syncRenderer != null) syncRenderer.enableSyncParameter(SyncRenderer.SYNC_ANGLE);
					controlMenuButton1.setText("Reorient");					
				}
				isOriented = !isOriented;
			}			
		});	
		
		
		controlMenuButton2 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		controlMenuButton2.setAutoFitSize(false);
		controlMenuButton2.setText("Track Users");
		controlMenuButton2.setBackgroundColour(Color.lightGray);
		controlMenuButton2.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				if(!isTrackUserMode){
					for(MathTool tool: onlineMathTools.values()) tool.getWindow().setVisible(false);
					for(ContentItem userTrack: onlineUserTracks.values()) userTrack.setVisible(true);
					controlMenuButton2.setText("Track Pads");
				}else{
					for(MathTool tool: onlineMathTools.values()) tool.getWindow().setVisible(true);
					for(ContentItem userTrack: onlineUserTracks.values()) userTrack.setVisible(false);
					controlMenuButton2.setText("Track Users");
				}
				isTrackUserMode = !isTrackUserMode;
			}			
		});
		
		controlMenuButton3 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		controlMenuButton3.setAutoFitSize(false);
		controlMenuButton3.setText("Button 3");
		controlMenuButton3.setBackgroundColour(Color.lightGray);
		controlMenuButton3.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			

			}			
		});
		
		controlMenuButton4 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		controlMenuButton4.setAutoFitSize(false);
		controlMenuButton4.setText("Button 4");
		controlMenuButton4.setBackgroundColour(Color.lightGray);
		controlMenuButton4.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			

			}			
		});
		
		controlMenu.addSubItem(controlMenuButton1);
		controlMenu.addSubItem(controlMenuButton2);
		controlMenu.addSubItem(controlMenuButton3);
		controlMenu.addSubItem(controlMenuButton4);
		
		controlMenuButton1.setVisible(true);
		controlMenuButton2.setVisible(true);
		controlMenuButton3.setVisible(false);
		controlMenuButton4.setVisible(false);
		
		controlMenu.setLocalLocation(-((Window)this.getNodeItem()).getWidth()/2,  - ((Window)this.getDesktopWindow()).getHeight()/2);
		
		
		this.getDesktopWindow().addSubItem(controlMenu);
		this.getDesktopWindow().setTopItem(controlMenu);
		
		topBar = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		topBar.setHorizontal(true);
		topBar.setBackgroundColour(Color.RED);
		topBar.setAutoFitSize(false);
		topBar.setSpaceToSide(10);
		topBar.setHeight(60);
		topBar.setWidth(((Window)this.getNodeItem()).getWidth());
		topBar.setItemHeight(30);
		topBar.setItemWidth(30);
		
		topBar.setLocalLocation(-((Window)this.getNodeItem()).getWidth()/2, ((Window)this.getNodeItem()).getHeight()/2);
		
		title = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		title.setBackgroundColour(topBar.getBackgroundColour());
		title.setFont(new Font("TimesRoman", Font.PLAIN,  32));
		title.setTextColour(Color.white);
		title.setBorderSize(0);
		title.setAutoFitSize(false);
		title.setAlignment(Alignment.LEFT);
		topBar.addSubItem(title);
		
		this.getDesktopWindow().setBackgroundColour(Color.black);
		this.getDesktopWindow().addSubItem(topBar);
		this.getDesktopWindow().setTopItem(topBar);

		SimpleButton closeButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		closeButton.setAutoFitSize(false);
		closeButton.setWidth(30);
		closeButton.setHeight(30);
		topBar.addSubItem(closeButton);
		closeButton.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				((ControllerManager)networkManager).getRemoteDesktopManager().unregisterRemoteDesktop(tableId);
				MathPadRemoteDesktop.this.getGraphManager().detachGraphNode(MathPadRemoteDesktop.this);
				MathPadRemoteDesktop.this.removeDataSourceListeners();
				MathPadRemoteDesktop.this.remove();
			}
		});
		
		SimpleButton linkButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		linkButton.setAutoFitSize(false);
		linkButton.setWidth(30);
		linkButton.setHeight(30);
		topBar.addSubItem(linkButton);
		
		SimpleButton fullScreenButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		fullScreenButton.setAutoFitSize(false);
		fullScreenButton.setWidth(30);
		fullScreenButton.setHeight(30);
		topBar.addSubItem(fullScreenButton);
		fullScreenButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				isFullScreenMode = !isFullScreenMode;
				if(isFullScreenMode)	
					MathPadRemoteDesktop.this.getDesktopWindow().setScale(1);
				else
					MathPadRemoteDesktop.this.getDesktopWindow().setScale(MathPadRemoteDesktop.this.defaultScale);
				MathPadRemoteDesktop.this.getDesktopWindow().centerItem();
			}
		});
		
		closeButton.setLocalLocation(topBar.getWidth()  - closeButton.getWidth()/2 - topBar.getBorderSize()-6, topBar.getHeight()/2 - 2);
		linkButton.setLocalLocation(topBar.getWidth() - closeButton.getWidth() - linkButton.getWidth()/2 - topBar.getBorderSize()-10, topBar.getHeight()/2 - 2);
		fullScreenButton.setLocalLocation(topBar.getWidth() - closeButton.getWidth() - linkButton.getWidth() - fullScreenButton.getWidth()/2 - topBar.getBorderSize()-13, topBar.getHeight()/2 - 2);

		topBar.setAsBottomObject();
		
		this.setLinkPoint(linkButton);
		this.addConceptMapListener(new ConceptMapListener(){
			@Override
			public void nodeConnected(GraphLink link) {
				if(link.getTargetNode() instanceof ProjectorNode){
					MathPadRemoteDesktop.this.addDataSourceListener((ProjectorNode)link.getTargetNode());
				}
				else{
					MathPadRemoteDesktop.this.graphManager.detachGraphLink(link);
					return;
				}
				link.setArrowMode(LineItem.ARROW_TO_TARGET);
			}
			@Override
			public void nodeDisconnected(GraphLink link) {
				if(link.getTargetNode() instanceof ProjectorNode){
					MathPadRemoteDesktop.this.removeDataSourceListener((ProjectorNode)link.getTargetNode());
				}			
			}
		});
		this.getDesktopWindow().setScale(this.defaultScale);
		this.getDesktopWindow().setOrder(1000);
	}

	private void syncContent(HashMap<UserIdentity, HashMap<Short, Object>> syncData) {
		this.getDesktopWindow().setAsTopObject();
		if(syncRenderer != null){
			for(UserIdentity userId: syncData.keySet()){
				MathTool tool = onlineMathTools.get(userId);
				if(tool != null) ((MathToolSyncRenderer)syncRenderer).renderSyncData(userId, tool, syncData.get(userId));
				ImageTextLabel userTrack = (ImageTextLabel)onlineUserTracks.get(userId);
				if(userTrack != null) syncRenderer.renderSyncData(userTrack, syncData.get(userId));
			}
		}
		
	}

	public void addRemoteMathPad(UserIdentity userId, MathTool mathTool) {
		this.addOnlineItem(mathTool.getWindow());
		onlineMathTools.put(userId,mathTool);
		ContentItem userTrack = createUserTrack(userId);
		userTrack.setVisible(false);
		this.getDesktopWindow().addSubItem(userTrack);
		((OrthoContentItem)userTrack).setLocation(mathTool.getWindow().getLocation());
		onlineUserTracks.put(userId, userTrack);
		mathTool.getWindow().setRotateTranslateScalable(false);
	}
	
	private ImageTextLabel createUserTrack(UserIdentity userId) {
		ImageTextLabel userTrack = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		userTrack.setHeight(50);
		userTrack.setWidth(50);
		userTrack.setImageInfo(MathPadResources.class.getResource("userstatus/online.jpg"));
		userTrack.setFont(new Font("TimesRoman", Font.PLAIN,  32));
		userTrack.setBackgroundColour(Color.black);
		userTrack.setTextColour(Color.white);
		userTrack.setBorderSize(0);
		userTrack.setCRLFSeparatedString(userId.getUserIdentity());
		userTrack.setRotateTranslateScalable(false);
		return userTrack;
	}

	public void addRemoteMathPads(HashMap<UserIdentity, MathTool> mathTools) {
		for(UserIdentity userId: mathTools.keySet()){
			addRemoteMathPad(userId, mathTools.get(userId));
		}
	}
	
	public void removeMathPadItem(UserIdentity userId){
		if(onlineMathTools.containsKey(userId))	{
			super.removeOnlineItem(onlineMathTools.get(userId).getWindow());
			onlineMathTools.remove(userId);
			onlineUserTracks.remove(userId);
		}
	}
	
	public void removeAllMathPads(){
		super.removeOnlineItems();
		onlineMathTools.clear();
		onlineUserTracks.clear();
	}
	
	public HashMap<UserIdentity, MathTool> getOnlineMathTools(){
		return onlineMathTools;
	}
	
	public void setTitle(String title){
		this.title.setText(" "+title);
		this.title.setWidth(topBar.getWidth()-115);
		this.title.setHeight(topBar.getHeight()-6);
		this.title.setLocalLocation(this.title.getWidth()/2 + topBar.getBorderSize()+3, this.title.getHeight()/2 + 2);
	}

	@Override
	public void createSyncRenderer() {
		syncRenderer = new MathToolSyncRenderer(this.networkManager.getSyncManager());
	}
	
	@Override
	public void userIdsReceived(TableIdentity tableId,
			List<UserIdentity> userIds) {
		 
		
	}

	@Override
	public void userRegistrationReceived(TableIdentity tableId, UserIdentity userId) {
		if(tableId.equals(MathPadRemoteDesktop.this.tableId)){
			RequestMathPadItemFromUserMessage msg = new RequestMathPadItemFromUserMessage(MathPadClient.class, tableId, userId);
			networkManager.sendMessage(msg);
		}
	}

	@Override
	public void userUnregistrationReceived(TableIdentity tableId,	UserIdentity userId) {
		if(!tableId.equals(MathPadRemoteDesktop.this.tableId)) return;
		if(MathPadRemoteDesktop.this.onlineMathTools.containsKey(userId)){
			removeMathPadItem(userId); 
		}

		HashMap<UserIdentity, MathToolInitSettings> sourceData = new HashMap<UserIdentity, MathToolInitSettings>();
		for(UserIdentity id: onlineMathTools.keySet())	sourceData.put(id, onlineMathTools.get(id).getInitSettings());
		//for(DataSourceListener listener: listeners) listener.sourceDataUpdated(tableId, sourceData);
	}

	@Override
	public void resultsReceivedFromUser(TableIdentity tableId,
			UserIdentity userId, AssignmentInfo assignInfo) {
		 
		
	}

	@Override
	public void projectorFound(TableIdentity tableId, boolean isLeaseSuccessful) {
		 
		
	}

	@Override
	public void userMathPadReceived(TableIdentity tableId,	UserIdentity userId, MathToolInitSettings mathToolSettings) {
		if(!tableId.equals(MathPadRemoteDesktop.this.tableId)) return;
		this.getDesktopWindow().setAsBottomObject();
		MathTool mathTool = null;
		if(!this.getOnlineMathTools().containsKey(userId)){
			mathTool = new MathTool(networkManager.getContentSystem());
			this.addRemoteMathPad(userId, mathTool);
		}else{
			mathTool = this.getOnlineMathTools().get(userId);
		}
		mathTool.init(mathToolSettings);
		HashMap<UserIdentity, MathToolInitSettings> remoteMathPads = new HashMap<UserIdentity, MathToolInitSettings>();
		remoteMathPads.put(userId, mathToolSettings);
		for(DataSourceListener listener: listeners) listener.sourceDataUpdated(tableId, remoteMathPads);	
	}

	@Override
	public void remoteDesktopContentReceived(TableIdentity tableId,	HashMap<UserIdentity, MathToolInitSettings> items) {
		if(!tableId.equals(MathPadRemoteDesktop.this.tableId)) return;
		this.getDesktopWindow().setAsBottomObject();
		HashMap<UserIdentity, MathTool> remoteMathPads = new HashMap<UserIdentity, MathTool>();
		for(UserIdentity userId: items.keySet()){
			MathTool mathTool = null;
			if(!this.getOnlineMathTools().containsKey(userId)){
				mathTool = new MathTool(networkManager.getContentSystem());
				remoteMathPads.put(userId, mathTool);
			}else{
				mathTool = this.getOnlineMathTools().get(userId);
			}
			mathTool.init(items.get(userId));
		}	
		this.addRemoteMathPads(remoteMathPads);
		for(DataSourceListener listener: listeners) listener.sourceDataUpdated(tableId, items);
	}
	
	public void addDataSourceListener(DataSourceListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	public void removeDataSourceListener(DataSourceListener listener){
		listeners.remove(listener);
	}
	
	public void removeDataSourceListeners(){
		listeners.clear();
	}

	@Override
	public void syncDataReceived(TableIdentity sender,	HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {
		if(sender.equals(tableId)){
			syncContent(mathPadSyncData);
			for(DataSourceListener listener: listeners) listener.syncDataReceived(sender, mathPadSyncData);
		}
	}

	@Override
	public void tableIdReceived(TableIdentity tableId) {
		 
		
	}
}
