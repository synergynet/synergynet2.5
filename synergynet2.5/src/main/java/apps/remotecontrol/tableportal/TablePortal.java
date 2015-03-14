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

package apps.remotecontrol.tableportal;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mathpadapp.util.MTFrame;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager.NetworkListener;
import apps.remotecontrol.networkmanager.managers.syncmanager.SyncManager;
import apps.remotecontrol.networkmanager.messages.PostWorkspacePortalMessage;
import apps.remotecontrol.networkmanager.messages.UnicastSyncDataPortalMessage;
import apps.remotecontrol.networkmanager.messages.UpdateContentPortalMessage;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;

public class TablePortal extends MTFrame implements NetworkListener{

	protected String id;
	protected NetworkedContentManager networkManager;
	protected TablePortalControlPanel controlPanel;
	protected DisplayPanel displayPanel;
	protected TableIdentity remoteTableId = null;
	protected SyncManager syncManager;
	public static float DEFAULT_SCALE = 0.40f;
	protected Frame backFrame;
	protected CullManager cullManager;
	protected RadarCullManager rcm;
	public static enum OperationMode{WRITE, DISPLAY};
	public static enum RemoteTableMode{LOCKED, UNLOCKED};
	public static enum TraceMode{TRACES_AND_ITEMS, ITEMS_ONLY, TRACES_ONLY};

	protected OperationMode currentOperationMode = OperationMode.WRITE;
	protected RemoteTableMode currentTableMode = RemoteTableMode.UNLOCKED;
	protected TraceMode currentTraceMode = TraceMode.ITEMS_ONLY;
	protected Radar radar;
	
	public TablePortal(final ContentSystem contentSystem, NetworkedContentManager networkManager, GraphManager grapgManager) {
		super(contentSystem, grapgManager);
		id = UUID.randomUUID().toString();
		this.networkManager = networkManager;
		syncManager = new SyncManager(networkManager);
		this.setTitle("Table Portal");
		this.getWindow().setBackgroundColour(Color.red);
		this.setWidth(GraphConfig.MAIN_WINDOW_WIDTH);
		this.setHeight(GraphConfig.MAIN_WINDOW_HEIGHT);
		if(this.getLinkPoint()!= null) this.getLinkPoint().setVisible(false);
		if(networkManager != null) networkManager.addNetworkListener(this);
		closeButton.removeButtonListeners();
		closeButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				// disable the confirm message
				/*
				final MTMessageBox msg = new MTMessageBox(TablePortal.this,contentSystem);
				msg.setTitle("Close");
				msg.setMessage("Are you sure you want to close the portal?");
				msg.addMessageListener(new MessageListener(){
					@Override
					public void buttonClicked(String buttonId) {}

					@Override
					public void buttonReleased(String buttonId) {
						msg.close();
						if(buttonId.equals("OK")){
							TablePortal.this.close();
						}
					}
				});
				*/
				TablePortal.this.getWindow().setVisible(false);
				//TablePortal.this.close();
				//TablePortalManager.getInstance().unregisterTablePortal(TablePortal.this);
			}
		});
		// Add control panel
		controlPanel = new TablePortalControlPanel(contentSystem, this);
		controlPanel.getContentPanel().setLocalLocation(-this.getWindow().getWidth()/2+ controlPanel.getWidth()/2+ 2*this.getWindow().getBorderSize(), controlPanel.getHeight()/2-this.getWindow().getHeight()/2+ 2*this.getWindow().getBorderSize());
		this.getWindow().addSubItem(controlPanel.getContentPanel());
		
		// Add Display Panel
		backFrame = (Frame) contentSystem.createContentItem(Frame.class);
		backFrame.setWidth(this.getWindow().getWidth()-controlPanel.getWidth() - 5* this.getWindow().getBorderSize());
		backFrame.setHeight(controlPanel.getHeight());
		backFrame.setLocalLocation(controlPanel.getWidth()/2 + this.getWindow().getBorderSize(), backFrame.getHeight()/2-this.getWindow().getHeight()/2+ 2*this.getWindow().getBorderSize());
		backFrame.setBackgroundColour(Color.black);
		backFrame.setOrder(-1);
		this.getWindow().addSubItem(backFrame);

		displayPanel = new DisplayPanel(contentSystem, this);
		displayPanel.getWindow().setWidth(DisplaySystem.getDisplaySystem().getWidth());
		displayPanel.getWindow().setHeight(DisplaySystem.getDisplaySystem().getHeight());
		displayPanel.getWindow().setScale(TablePortal.DEFAULT_SCALE);
		
		displayPanel.getWindow().setLocalLocation(backFrame.getLocalLocation().x, backFrame.getLocalLocation().y);
		
		this.getWindow().addSubItem(displayPanel.getWindow());
		
		radar = new Radar(contentSystem, this);
		this.getWindow().addSubItem(radar.getRadarWindow());
		radar.getRadarWindow().setLocation(437,257);
		radar.getRadarWindow().setOrder(9999);
		
		cullManager = new CullManager(this);
		
		rcm = new RadarCullManager(this, radar);
		rcm.registerItemForClipping(radar.getViewedArea().getBackgroundFrame());
		radar.getRadarWindow().setVisible(false);
	}
	
	public NetworkedContentManager getNetworkManager(){
		return networkManager;
	}

	@Override

	public void close(){
		displayPanel.unregisterAllItems();
		if(this.getNetworkManager() != null){
			if(remoteTableId != null){	
				this.getNetworkManager().disconnectTable(remoteTableId);
				if(this.getNetworkManager().getSyncManager() != null) this.getNetworkManager().getSyncManager().unregisterSyncTable(remoteTableId);
			}
			this.getNetworkManager().removeNetworkListener(this);
		}
		super.close();
	}
	
	@Override
	public void messageReceived(Object obj) {
		if(obj instanceof PostWorkspacePortalMessage){
			if(this.remoteTableId != null && this.remoteTableId.equals(((TableMessage)obj).getSender())){
				PostWorkspacePortalMessage msg = (PostWorkspacePortalMessage)obj;
				displayPanel.registerContentItems(msg.getItems());
			}
		}else if(obj instanceof UpdateContentPortalMessage){
			
			if(this.remoteTableId != null && this.remoteTableId.equals(((TableMessage)obj).getSender())){
				UpdateContentPortalMessage msg = (UpdateContentPortalMessage)obj;
				HashMap<ContentItem, Short> updateData = msg.getItems();
				for(ContentItem item: updateData.keySet()){
					if(updateData.get(item) == NetworkedContentManager.ITEM_ADDED) displayPanel.registerContentItem(item);
					else if(updateData.get(item) == NetworkedContentManager.ITEM_DELETED) displayPanel.unregisterItem(id+"@"+remoteTableId+"@"+item.getName());
				}
			}
		}else if(obj instanceof UnicastSyncDataPortalMessage){
			if(this.remoteTableId != null && this.remoteTableId.equals(((TableMessage)obj).getSender())){
				displayPanel.syncContent(((UnicastSyncDataPortalMessage)obj).getItems());
			}
		}
	}

	public void update(float tpf) {
		if(this.networkManager != null) this.syncManager.update();
		if(controlPanel.navigator != null) controlPanel.navigator.update();
		if(cullManager != null) cullManager.update(tpf);
		//if(rcm != null) rcm.update();
	}
	
	public TableIdentity getTableId(){
		return remoteTableId;
	}
	
	public String getPortalId(){
		return id;
	}
	
	public DisplayPanel getDisplayPanel(){
		return displayPanel;
	}

	public void setOperationMode(OperationMode mode) {
		this.currentOperationMode = mode;
		displayPanel.setMode(mode);
	}
	
	public TraceMode getTraceMode(){
		return currentTraceMode;
	}
	
	public void setTraceMode(TraceMode currentTraceMode){
		this.currentTraceMode = currentTraceMode;
		this.getDisplayPanel().updateTraceMode(currentTraceMode);
	}
	
	public void setTableMode(RemoteTableMode mode){
		this.currentTableMode = mode;
		if(networkManager != null) networkManager.setRemoteTableMode(this.remoteTableId, mode);
	}
	
	public Radar getRadar(){
		return radar;
	}

	public void connect(TableIdentity tableIdentity) {
		displayPanel.unregisterAllItems();
		this.remoteTableId = tableIdentity;
		this.setTitle("Table Id: ("+ remoteTableId.toString() +")");
		this.getNetworkManager().connectTable(remoteTableId);
		this.syncManager.registerSyncTable(remoteTableId);
	}

	public void setNetworkedContentManager(NetworkedContentManager manager) {
		this.networkManager = manager;
		if(networkManager != null) networkManager.addNetworkListener(this);
		syncManager = new SyncManager(networkManager);
	}
}
