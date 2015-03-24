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


import java.util.HashMap;
import java.util.List;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener;
import apps.mathpadapp.networkmanager.utils.UserIdentity;
import apps.mathpadapp.util.MTFrame;
import apps.mathpadapp.util.MTList;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;


/**
 * The Class ProjectorControllerWindow.
 */
public class ProjectorControllerWindow extends MTFrame implements ControllerNetworkListener{
	
	/** The Constant windowWidth. */
	public static final int windowWidth = 350;
	
	/** The Constant windowHeight. */
	public static final int windowHeight = 440;
	
	/** The projector panel. */
	protected MTList projectorPanel;
	
	/** The control panel. */
	protected ProjectorListControlPanel controlPanel;
	
	/** The controller manager. */
	protected ControllerManager controllerManager;
	
	/**
	 * Instantiates a new projector controller window.
	 *
	 * @param contentSystem the content system
	 * @param controllerManager the controller manager
	 */
	public ProjectorControllerWindow(final ContentSystem contentSystem, final ControllerManager controllerManager){
		super(contentSystem);
		this.controllerManager = controllerManager;
		this.setWidth(windowWidth);
		this.setHeight(windowHeight);
		
		projectorPanel = new MTList(contentSystem);
		this.getWindow().addSubItem(projectorPanel.getContainer());
		projectorPanel.setHeight(350);
		projectorPanel.setWidth(330);
		projectorPanel.getContainer().setLocalLocation(0, -10);
		
		controlPanel = new ProjectorListControlPanel(contentSystem, projectorPanel, controllerManager);
		controlPanel.getContainer().setLocalLocation(controlPanel.getContainer().getLocalLocation().x, -190);
		this.getWindow().addSubItem(controlPanel.getContainer());
		
		this.getWindow().setOrder(OrthoBringToTop.bottomMost);
		
		this.setTitle("Online Projectors");
		
		this.closeButton.removeButtonListeners();
		this.closeButton.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				ProjectorControllerWindow.this.close();
			}
		});
		if(controllerManager != null) controllerManager.addNetworkListener(this);
		
		projectorPanel.getManager().addItem("temp", "temp");
		projectorPanel.getManager().deleteAllItems();
	}

	/**
	 * Gets the assignment session list.
	 *
	 * @return the assignment session list
	 */
	public MTList getAssignmentSessionList() {
		return this.projectorPanel;
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#resultsReceivedFromUser(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mathpadapp.networkmanager.utils.UserIdentity, apps.mathpadapp.controllerapp.assignmentcontroller.AssignmentInfo)
	 */
	@Override
	public void resultsReceivedFromUser(TableIdentity tableId,
			UserIdentity userId, AssignmentInfo assignInfo) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#userIdsReceived(synergynetframework.appsystem.services.net.localpresence.TableIdentity, java.util.List)
	 */
	@Override
	public void userIdsReceived(TableIdentity tableId,
			List<UserIdentity> userIds) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#userRegistrationReceived(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mathpadapp.networkmanager.utils.UserIdentity)
	 */
	@Override
	public void userRegistrationReceived(TableIdentity tableId,
			UserIdentity userId) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#userUnregistrationReceived(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mathpadapp.networkmanager.utils.UserIdentity)
	 */
	@Override
	public void userUnregistrationReceived(TableIdentity tableId,
			UserIdentity userId) {
		 
		
	}
	
	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#projectorFound(synergynetframework.appsystem.services.net.localpresence.TableIdentity, boolean)
	 */
	@Override
	public void projectorFound(final TableIdentity tableId, boolean isLeaseSuccessful) {
		if(projectorPanel.getManager().getAllItems().contains(tableId)) return;
		projectorPanel.getManager().addItem("Projector " + (projectorPanel.getManager().getAllItems().size()+1), tableId);
		projectorPanel.getManager().setIcon(tableId, MathPadResources.class.getResource("controlBar/Projectors.jpg"));
	}
	
	/* (non-Javadoc)
	 * @see apps.mathpadapp.util.MTFrame#close()
	 */
	public void close(){
		if(controllerManager != null) controllerManager.removeNetworkListener(ProjectorControllerWindow.this);
		contentSystem.removeContentItem(ProjectorControllerWindow.this.getWindow());
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#userMathPadReceived(synergynetframework.appsystem.services.net.localpresence.TableIdentity, apps.mathpadapp.networkmanager.utils.UserIdentity, apps.mathpadapp.mathtool.MathToolInitSettings)
	 */
	@Override
	public void userMathPadReceived(TableIdentity tableId,
			UserIdentity userId, MathToolInitSettings mathToolSettings) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#remoteDesktopContentReceived(synergynetframework.appsystem.services.net.localpresence.TableIdentity, java.util.HashMap)
	 */
	@Override
	public void remoteDesktopContentReceived(TableIdentity tableId,
			HashMap<UserIdentity, MathToolInitSettings> items) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#syncDataReceived(synergynetframework.appsystem.services.net.localpresence.TableIdentity, java.util.HashMap)
	 */
	@Override
	public void syncDataReceived(TableIdentity sender,
			HashMap<UserIdentity, HashMap<Short, Object>> mathPadSyncData) {
		 
		
	}

	/* (non-Javadoc)
	 * @see apps.mathpadapp.networkmanager.managers.ControllerManager.ControllerNetworkListener#tableIdReceived(synergynetframework.appsystem.services.net.localpresence.TableIdentity)
	 */
	@Override
	public void tableIdReceived(TableIdentity tableId) {
		 
		
	}


}
