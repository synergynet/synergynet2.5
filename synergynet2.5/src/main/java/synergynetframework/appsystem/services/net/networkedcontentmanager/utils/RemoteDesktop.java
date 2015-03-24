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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import apps.conceptmap.ConceptMapApp;
import apps.conceptmap.GraphConfig;
import apps.conceptmap.graphcomponents.link.GraphLink;
import apps.conceptmap.graphcomponents.nodes.GraphNode;
import apps.conceptmap.utility.ConceptMapListener;

import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.controllers.RemoteDesktopController;


/**
 * The Class RemoteDesktop.
 */
public class RemoteDesktop extends GraphNode{
	
	/** The remote desktop controller. */
	protected RemoteDesktopController remoteDesktopController;
	
	/** The table id. */
	protected TableIdentity tableId;
	
	/** The scale. */
	protected int scale = 1;
	
	/** The desktop window. */
	protected OrthoContainer desktopWindow;
	
	/** The back image. */
	protected LightImageLabel backImage;
	
	/** The top bar. */
	protected ListContainer topBar;
	
	/** The top bar button2. */
	protected SimpleButton topBarButton1, topBarButton2;
	
	/** The control menu. */
	protected ListContainer controlMenu;
	
	/** The control menu button4. */
	protected SimpleButton controlMenuButton1, controlMenuButton2, controlMenuButton3, controlMenuButton4;
	
	/** The online items. */
	protected Map<String, ContentItem> onlineItems = new HashMap<String, ContentItem>();
	
	/** The item notes. */
	protected Map<String, ContentItem> itemNotes = new HashMap<String, ContentItem>();
	
	/**
	 * Instantiates a new remote desktop.
	 *
	 * @param tableId the table id
	 * @param networkManager the network manager
	 */
	public RemoteDesktop(final TableIdentity tableId, final NetworkedContentManager networkManager){
		super(networkManager.getContentSystem(), networkManager.getGraphManager());
		this.tableId = tableId;
		this.remoteDesktopController = networkManager.getRemoteDesktopController();
		this.contentSystem = networkManager.getContentSystem();
		this.desktopWindow = (OrthoContainer)contentSystem.createContentItem(OrthoContainer.class);
		
		backImage = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		backImage.drawImage(ConceptMapApp.class.getResource("images/background.jpg"));
		backImage.setAutoFitSize(false);
		backImage.setWidth((int)(DisplaySystem.getDisplaySystem().getWidth() * desktopWindow.getScale()));
		backImage.setHeight((int)(DisplaySystem.getDisplaySystem().getHeight() * desktopWindow.getScale()));
		desktopWindow.addSubItem(backImage);
		
		backImage.setLocalLocation(DisplaySystem.getDisplaySystem().getWidth()/2,DisplaySystem.getDisplaySystem().getHeight()/2);
		backImage.setOrder(-1);
		
		controlMenu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		controlMenu.setHorizontal(true);
		controlMenu.setBackgroundColour(Color.BLUE);
		controlMenu.setAutoFitSize(false);
		controlMenu.setSpaceToSide(10);
		controlMenu.setHeight(50);
		controlMenu.setWidth(backImage.getWidth());
		controlMenu.setItemHeight(30);
		controlMenu.setItemWidth(100);
		
		controlMenuButton1 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		controlMenuButton1.setAutoFitSize(false);
		controlMenuButton1.setText("Button 1");
		controlMenuButton1.setBackgroundColour(Color.lightGray);
		controlMenuButton1.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			

			}			
		});	
		
		
		controlMenuButton2 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		controlMenuButton2.setAutoFitSize(false);
		controlMenuButton2.setText("Button 2");
		controlMenuButton2.setBackgroundColour(Color.lightGray);
		controlMenuButton2.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			

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
		
		// Temporarily hide the control buttons
		controlMenuButton1.setVisible(false);
		controlMenuButton2.setVisible(false);
		controlMenuButton3.setVisible(false);
		controlMenuButton4.setVisible(false);
		
		controlMenu.setLocalLocation(0,  - controlMenu.getHeight());
		
		
		desktopWindow.addSubItem(controlMenu);
		desktopWindow.setTopItem(controlMenu);
		
		topBar = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		topBar.setHorizontal(true);
		topBar.setBackgroundColour(Color.RED);
		topBar.setAutoFitSize(false);
		topBar.setSpaceToSide(10);
		topBar.setHeight(50);
		topBar.setWidth(backImage.getWidth());
		topBar.setItemHeight(30);
		topBar.setItemWidth(30);
		
		topBarButton1 = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		
		topBarButton1.setAutoFitSize(false);
		topBarButton1.setBackgroundColour(Color.lightGray);
		topBarButton1.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				remoteDesktopController.requestRemoteDesktop(tableId, false);
			}			
		});	
		
		
		topBar.addSubItem(topBarButton1);
		
		topBar.setLocalLocation(0, backImage.getHeight());
		
		
		desktopWindow.addSubItem(topBar);
		desktopWindow.setTopItem(topBar);
		desktopWindow.setLocalLocation(0,0);
		
		this.setNodeContent(desktopWindow);
		this.setLinkButtonLocation("topRightCorder");
		this.getLinkButton().setWidth(30);
		this.getLinkButton().setHeight(30);
		this.getLinkButton().drawImage(GraphConfig.nodeLinkImageResource);
		this.getCloseButton().setVisible(false);
		desktopWindow.addScreenCursorListener(new ScreenCursorListener(){

			@Override
			public void screenCursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {

				graphManager.graphNodeDragged(RemoteDesktop.this, id, x, y);
			}

			@Override
			public void screenCursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				graphManager.graphNodeClicked(RemoteDesktop.this, id, x, y);

			}

			@Override
			public void screenCursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				graphManager.graphNodePressed(RemoteDesktop.this, id, x, y);
				
			}

			@Override
			public void screenCursorReleased(ContentItem item, long id,
					float x, float y, float pressure) {
				graphManager.graphNodeReleased(RemoteDesktop.this, id, x, y);
				
			}
			
		});
		
		this.addConceptMapListener(new ConceptMapListener(){

			
			@Override
			public void nodeConnected(GraphLink link) {
				if(!(link.getTargetNode() instanceof ProjectorNode)){
					RemoteDesktop.this.graphManager.detachGraphLink(link);
					return;
				}
				link.setArrowMode(LineItem.ARROW_TO_TARGET);
			}
			
			

			@Override
			public void nodeDisconnected(GraphLink link) {

			}
			
		});
	}
	
	/**
	 * Gets the table id.
	 *
	 * @return the table id
	 */
	public TableIdentity getTableId() {
		return tableId;
	}

	/**
	 * Sets the table id.
	 *
	 * @param tableId the new table id
	 */
	public void setTableId(TableIdentity tableId) {
		this.tableId = tableId;
	}

	/**
	 * Gets the desktop window.
	 *
	 * @return the desktop window
	 */
	public OrthoContainer getDesktopWindow() {
		return desktopWindow;
	}
	
	/**
	 * Sets the desktop window.
	 *
	 * @param desktopWindow the new desktop window
	 */
	public void setDesktopWindow(OrthoContainer desktopWindow) {
		this.desktopWindow = desktopWindow;
	}
	
	/**
	 * Gets the online items.
	 *
	 * @return the online items
	 */
	public Map<String, ContentItem> getOnlineItems() {
		return onlineItems;
	}
	
	/**
	 * Sets the online items.
	 *
	 * @param onlineItems the online items
	 */
	public void setOnlineItems(Map<String, ContentItem> onlineItems) {
		this.onlineItems = onlineItems;
	}

	/**
	 * Removes the online items.
	 */
	public void removeOnlineItems(){
		for(ContentItem item: onlineItems.values())
			this.desktopWindow.removeSubItem(item);
		onlineItems.clear();
	}

	/* (non-Javadoc)
	 * @see apps.conceptmap.graphcomponents.nodes.GraphNode#setCloseButtonLocation(java.lang.String)
	 */
	@Override
	public void setCloseButtonLocation(String location) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.conceptmap.graphcomponents.nodes.GraphNode#setLinkButtonLocation(java.lang.String)
	 */
	@Override
	public void setLinkButtonLocation(String location) {
		this.getLinkButton().setLocalLocation(topBar.getWidth() - this.getLinkButton().getWidth(),topBar.getLocalLocation().y + topBar.getHeight()/2);
		
	}

	/* (non-Javadoc)
	 * @see apps.conceptmap.graphcomponents.nodes.GraphNode#setNodeContent(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	protected void setNodeContent(ContentItem contentItem) {
		container.addSubItem(contentItem);
	}
	
	/* (non-Javadoc)
	 * @see apps.conceptmap.graphcomponents.nodes.GraphNode#updateNode()
	 */
	public void updateNode(){
		this.updateConnectionPoints();
	}
	
	/* (non-Javadoc)
	 * @see apps.conceptmap.graphcomponents.nodes.GraphNode#getLocation()
	 */
	public Location getLocation(){
		Spatial s =  ((Spatial)backImage.getImplementationObject());
		return new Location((int)s.getWorldTranslation().x, (int)s.getWorldTranslation().y, 0);
	}
	
	/**
	 * Adds the note container for item.
	 *
	 * @param item the item
	 * @param noteItem the note item
	 */
	public void addNoteContainerForItem(ContentItem item,ContentItem noteItem){
		this.removeNoteContainerForItem(item);
		this.getDesktopWindow().addSubItem(noteItem);
		itemNotes.put(item.getName(), noteItem);		
	}

	/**
	 * Removes the note container for item.
	 *
	 * @param item the item
	 */
	public void removeNoteContainerForItem(ContentItem item) {
		if(itemNotes.containsKey(item.getName())){
			this.getDesktopWindow().removeSubItem(itemNotes.get(item.getName()));
			itemNotes.remove(item.getName());
		}
	}
}
