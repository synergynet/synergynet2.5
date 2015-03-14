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

import java.util.ArrayList;

import apps.remotecontrol.TablePortalApp;
import apps.remotecontrol.tableportal.TablePortal.OperationMode;
import apps.remotecontrol.tableportal.TablePortal.RemoteTableMode;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;

public class TablePortalControlPanel{
	
	protected ContentSystem contentSystem;
	
	private LightImageLabel backImage;
	private ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
	private final int NO_BUTTONS_PER_ROW = 2;
	private OrthoContainer panel;
	protected PortalNavigator navigator;
	
	
	protected transient ArrayList<ControlPanelListener> listeners = new ArrayList<ControlPanelListener>();
	
	public TablePortalControlPanel(final ContentSystem contentSystem, final TablePortal portal){
		this.contentSystem = contentSystem;
		panel = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		
		backImage = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		backImage.drawImage(TablePortalApp.class.getResource("LeftPanelBackground.jpg"));
		backImage.setAutoFitSize(false);
		backImage.setWidth(NO_BUTTONS_PER_ROW * GraphConfig.CONTROL_PANEL_BUTTON_WIDTH + 4*panel.getBorderSize() + (NO_BUTTONS_PER_ROW-1)* panel.getBorderSize());
		backImage.setHeight(GraphConfig.CONTROL_PANEL_HEIGHT);
		backImage.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		panel.addSubItem(backImage);
		backImage.setOrder(-1);
		/*
		SimpleButton connectBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		connectBtn.setAutoFitSize(false);
		connectBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		connectBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		connectBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		connectBtn.drawImage(TablePortalUtils.class.getResource("images/buttons/connect.jpg"));
		connectBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				TableConnectDialog connectDialog = new TableConnectDialog(portal,contentSystem);
				connectDialog.getWindow().setVisible(true);
				connectDialog.getWindow().setLocalLocation(portal.getWindow().getLocalLocation());
				connectDialog.getWindow().setAngle(portal.getWindow().getAngle());
				connectDialog.getWindow().setScale(portal.getWindow().getScale());
				connectDialog.getWindow().setAsTopObject();
			}
		});
		buttons.add(connectBtn);
		*/
		final SimpleButton switchModeBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		switchModeBtn.setAutoFitSize(false);
		switchModeBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		switchModeBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		switchModeBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		switchModeBtn.drawImage(TablePortalApp.class.getResource("buttons/write_mode.jpg"));
		switchModeBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				switchModeBtn.removeAllImages();
				if(portal.currentOperationMode == OperationMode.WRITE){
					portal.setOperationMode(OperationMode.DISPLAY);
					switchModeBtn.drawImage(TablePortalApp.class.getResource("buttons/display_mode.jpg"));
				}else{
					portal.setOperationMode(OperationMode.WRITE);
					switchModeBtn.drawImage(TablePortalApp.class.getResource("buttons/write_mode.jpg"));
				}
			}
		});
		buttons.add(switchModeBtn);
		
		SimpleButton optionsBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		optionsBtn.setAutoFitSize(false);
		optionsBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		optionsBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		optionsBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		optionsBtn.drawImage(TablePortalApp.class.getResource("buttons/options.jpg"));
		optionsBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				CustomiseViewDialog configDialog = new CustomiseViewDialog(portal,contentSystem);
				configDialog.getWindow().setVisible(true);
				configDialog.getWindow().setLocalLocation(portal.getWindow().getLocalLocation());
				configDialog.getWindow().setAngle(portal.getWindow().getAngle());
				configDialog.getWindow().setScale(portal.getWindow().getScale());
				configDialog.getWindow().setAsTopObject();
			}
		});
		buttons.add(optionsBtn);

		SimpleButton refreshBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		refreshBtn.setAutoFitSize(false);
		refreshBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		refreshBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		refreshBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		refreshBtn.drawImage(TablePortalApp.class.getResource("buttons/refresh.jpg"));
		refreshBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				portal.connect(portal.remoteTableId);
			}
		});
		buttons.add(refreshBtn);
		
		SimpleButton radarBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		radarBtn.setAutoFitSize(false);
		radarBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		radarBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		radarBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		radarBtn.drawImage(TablePortalApp.class.getResource("buttons/radar.jpg"));
		radarBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				portal.radar.getRadarWindow().setVisible(!portal.getRadar().getRadarWindow().isVisible());
				portal.getWindow().setTopItem(portal.radar.getRadarWindow());
				portal.radar.getRadarWindow().setOrder(9999);
			}
		});
		buttons.add(radarBtn);
		
		
		final SimpleButton blockBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		blockBtn.setAutoFitSize(false);
		blockBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		blockBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		blockBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		blockBtn.drawImage(TablePortalApp.class.getResource("buttons/unlock.png"));
		blockBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(portal.currentTableMode.equals(RemoteTableMode.LOCKED)){
					portal.setTableMode(RemoteTableMode.UNLOCKED);
					blockBtn.removeAllImages();
					blockBtn.drawImage(TablePortalApp.class.getResource("buttons/unlock.png"));
				}else{
					portal.setTableMode(RemoteTableMode.LOCKED);
					blockBtn.removeAllImages();
					blockBtn.drawImage(TablePortalApp.class.getResource("buttons/lock.png"));
				}
			}
		});
		buttons.add(blockBtn);
		
		final SimpleButton projectorBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		projectorBtn.setAutoFitSize(false);
		projectorBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		projectorBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		projectorBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		projectorBtn.drawImage(TablePortalApp.class.getResource("buttons/LM_projector_icon.jpg"));
		projectorBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				
			}
		});
		buttons.add(projectorBtn);
		
		/*
		final SimpleButton traceBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		traceBtn.setAutoFitSize(false);
		traceBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		traceBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		traceBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		traceBtn.drawImage(TablePortalUtils.class.getResource("images/buttons/track.png"));
		traceBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(portal.getTraceMode().equals(TraceMode.ITEMS_ONLY)){
					portal.setTraceMode(TraceMode.TRACES_AND_ITEMS);
					traceBtn.removeAllImages();
					traceBtn.drawImage(TablePortalUtils.class.getResource("images/buttons/track_on.png"));
				}else{
					portal.setTraceMode(TraceMode.ITEMS_ONLY);
					traceBtn.removeAllImages();
					traceBtn.drawImage(TablePortalUtils.class.getResource("images/buttons/track.png"));
				}
			}
		});
		buttons.add(traceBtn);
		*/
		
		/*
		final SimpleButton inspectBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		inspectBtn.setAutoFitSize(false);
		inspectBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		inspectBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		inspectBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		inspectBtn.drawImage(TablePortalUtils.class.getResource("images/buttons/inspect.png"));
		inspectBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(!WorkspaceManager.getInstance().isInspectionEnabledForPortal(portal)){
					WorkspaceManager.getInstance().enableInspectionForPortal(portal);
					inspectBtn.removeAllImages();
					inspectBtn.drawImage(TablePortalUtils.class.getResource("images/buttons/inspect_on.png"));
				}else{
					WorkspaceManager.getInstance().disableInspectionForPortal(portal);
					inspectBtn.removeAllImages();
					inspectBtn.drawImage(TablePortalUtils.class.getResource("images/buttons/inspect.png"));
				}
			}
		});
		buttons.add(inspectBtn);
		*/
		
		navigator = new PortalNavigator(contentSystem, portal);
		panel.addSubItem(navigator.getWindow());
		navigator.getWindow().setLocation(0, -30);
		
		setLayout();
	}
	
	public void setLayout(){
		int shiftX=0, shiftY=0;
		int i=0;
		for(SimpleButton button: buttons){
			if(!button.isVisible()) continue;
			button.setBorderColour(GraphConfig.CONTROL_PANEL_BORDER_COLOR);
			panel.addSubItem(button);
			if(i==0){
				// place first button on the top left corder
				shiftX = -backImage.getWidth()/2 + 2*panel.getBorderSize()+ GraphConfig.CONTROL_PANEL_BUTTON_WIDTH/2;
				shiftY = backImage.getHeight()/2 - 2*panel.getBorderSize()- GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT/2;
			}
			else if(i%NO_BUTTONS_PER_ROW==0){
				shiftX = -backImage.getWidth()/2 + 2*panel.getBorderSize()+ GraphConfig.CONTROL_PANEL_BUTTON_WIDTH/2;
				shiftY-= panel.getBorderSize()+ GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT;
			}else{
				shiftX+= GraphConfig.CONTROL_PANEL_BUTTON_WIDTH + panel.getBorderSize();
			}
			button.setLocalLocation(shiftX, shiftY);
			i++;
		}
		
	}
	
	public OrthoContainer getContentPanel(){
		return panel;
	}
	
	public int getWidth(){
		return backImage.getWidth();
	}
	
	public int getHeight(){
		return backImage.getHeight();
	}
	
	public void addControlPanelListener(ControlPanelListener listener){
		if(!listeners.contains(listener)) listeners.add(listener); 
	}
	
	public void removeControlPanelListeners(){
		listeners.clear();
	}
	
	public interface ControlPanelListener{
	}

}
