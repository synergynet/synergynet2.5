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

import apps.remotecontrol.TablePortalApp;

import com.jme.math.FastMath;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;

public class PortalNavigator {
	
	private Window navigatorWindow;
	private enum NavigationCommand{Up,Down, Left, Right, ZoomIn, ZoomOut, RotateRight, RotateLeft, DefaultScale, Null};
	private NavigationCommand command = NavigationCommand.Null;
	private TablePortal portal;
	private float shiftFactor = 0.35f;
	private float scaleFactor = 0.001f;
	private float rotateFactor = 0.1f * FastMath.DEG_TO_RAD;

	
	public PortalNavigator(ContentSystem contentSystem, final TablePortal portal){
		this.portal = portal;
		navigatorWindow = (Window) contentSystem.createContentItem(Window.class, "navigatorwindow");
		navigatorWindow.setWidth(80);
		navigatorWindow.setHeight(80);
		navigatorWindow.setScaleLimit(1f, 1f);
		navigatorWindow.setZRotateLimit(0f, 0f);
		navigatorWindow.getBackgroundFrame().setBackgroundColour(new Color(0, 0, 0, 0));
		navigatorWindow.getBackgroundFrame().drawImage(TablePortalApp.class.getResource("controls/3dnavigator.png"), 0, 0, 80, 80);
		navigatorWindow.getBackgroundFrame().addItemListener(new ItemListener() {

			@Override
			public void cursorChanged(ContentItem item, long id, float x,
					float y, float pressure) {}

			@Override
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {}

			@Override
			public void cursorDoubleClicked(ContentItem item, long id, float x,
					float y, float pressure) {}

			@Override
			public void cursorLongHeld(ContentItem item, long id, float x,
					float y, float pressure) {}

			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				if(x>=28 && x<= 52 && y >= 5 && y <= 30)
					command = NavigationCommand.Up;
				else if(x>=28 && x<= 52 && y >= 50 && y <= 76)
					command = NavigationCommand.Down;
				else if(x>=5 && x<= 31 && y >= 30 && y <= 52)
					command = NavigationCommand.Left;
				else if(x>=50 && x<= 76 && y >= 30 && y <= 52)
					command = NavigationCommand.Right;
				else if(x>=50 && x<= 76 && y >= 7 && y <= 30)
					command = NavigationCommand.RotateLeft;
				else if(x>=5 && x<= 30 && y >= 7 && y <= 30)
					command = NavigationCommand.RotateRight;
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				command = NavigationCommand.Null;
			}

			@Override
			public void cursorRightClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				 
				
			}


		});
		
		SimpleButton zoomIn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		zoomIn.setAutoFitSize(false);
		zoomIn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		zoomIn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		zoomIn.setBorderSize(1);
		zoomIn.drawImage(TablePortalApp.class.getResource("buttons/zoom_in.png"));
		zoomIn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				command = NavigationCommand.ZoomIn;

			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				command = NavigationCommand.Null;

			}
		});
		navigatorWindow.addSubItem(zoomIn);
		zoomIn.setLocalLocation(18,-57);
		
		SimpleButton zoomOut = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		zoomOut.setAutoFitSize(false);
		zoomOut.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		zoomOut.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		zoomOut.setBorderSize(1);
		zoomOut.drawImage(TablePortalApp.class.getResource("buttons/zoom_out.png"));
		zoomOut.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				command = NavigationCommand.ZoomOut;

			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				command = NavigationCommand.Null;

			}
		});
		navigatorWindow.addSubItem(zoomOut);
		zoomOut.setLocalLocation(-18,-57);
		
		SimpleButton zoomDefault = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		zoomDefault.setAutoFitSize(false);
		zoomDefault.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		zoomDefault.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		zoomDefault.setBorderSize(1);
		zoomDefault.drawImage(TablePortalApp.class.getResource("buttons/default_scale.png"));
		zoomDefault.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				command = NavigationCommand.DefaultScale;

			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				command = NavigationCommand.Null;

			}
		});
		navigatorWindow.addSubItem(zoomDefault);
		zoomDefault.setLocalLocation(0,-90);
	}
	
	public Window getWindow(){
		return navigatorWindow;
	}

	public void update() {
		if(command != NavigationCommand.Null){
			if(command == NavigationCommand.Up){
				portal.displayPanel.getWindow().setLocation(portal.displayPanel.getWindow().getLocation().x, portal.displayPanel.getWindow().getLocation().y-shiftFactor);
				portal.getRadar().getViewedArea().setLocation(portal.getRadar().getViewedArea().getLocation().x, portal.getRadar().getViewedArea().getLocation().y+shiftFactor);
			}else if(command == NavigationCommand.Down){
				portal.displayPanel.getWindow().setLocation(portal.displayPanel.getWindow().getLocation().x, portal.displayPanel.getWindow().getLocation().y+shiftFactor);
				portal.getRadar().getViewedArea().setLocation(portal.getRadar().getViewedArea().getLocation().x, portal.getRadar().getViewedArea().getLocation().y - shiftFactor);
			}else if(command == NavigationCommand.Left){
				portal.displayPanel.getWindow().setLocation(portal.displayPanel.getWindow().getLocation().x+shiftFactor, portal.displayPanel.getWindow().getLocation().y);
				portal.getRadar().getViewedArea().setLocation(portal.getRadar().getViewedArea().getLocation().x-shiftFactor, portal.getRadar().getViewedArea().getLocation().y);
			}else if(command == NavigationCommand.Right){
				portal.displayPanel.getWindow().setLocation(portal.displayPanel.getWindow().getLocation().x-shiftFactor, portal.displayPanel.getWindow().getLocation().y);
				portal.getRadar().getViewedArea().setLocation(portal.getRadar().getViewedArea().getLocation().x+shiftFactor, portal.getRadar().getViewedArea().getLocation().y);
			}else if(command == NavigationCommand.ZoomIn){
				float scale = portal.displayPanel.getWindow().getScale();
				if(scale >=0){
					portal.displayPanel.getWindow().setScale(scale+scaleFactor);
					portal.getRadar().getViewedArea().setScale(TablePortal.DEFAULT_SCALE/portal.displayPanel.getWindow().getScale());
				}
			}else if(command == NavigationCommand.ZoomOut){
				float scale = portal.displayPanel.getWindow().getScale();
				if(scale -scaleFactor>=0.1f){
					portal.displayPanel.getWindow().setScale(scale-scaleFactor);
					portal.getRadar().getViewedArea().setScale(TablePortal.DEFAULT_SCALE/portal.displayPanel.getWindow().getScale());
				}
			}else if(command == NavigationCommand.RotateLeft){
				float newAngle = portal.displayPanel.getWindow().getAngle()+ rotateFactor;
				if(newAngle > 0) newAngle = newAngle - FastMath.TWO_PI;
				if(newAngle < -FastMath.TWO_PI) newAngle = newAngle + FastMath.TWO_PI;
				portal.displayPanel.getWindow().setAngle(newAngle);
				portal.getRadar().getViewedArea().setAngle(newAngle);
			}else if(command == NavigationCommand.RotateRight){
				float newAngle = portal.displayPanel.getWindow().getAngle()- rotateFactor;
				if(newAngle > 0) newAngle = newAngle - FastMath.TWO_PI;
				if(newAngle < -FastMath.TWO_PI) newAngle = newAngle + FastMath.TWO_PI;
				portal.displayPanel.getWindow().setAngle(newAngle);
				portal.getRadar().getViewedArea().setAngle(newAngle);
			}else if(command == NavigationCommand.DefaultScale){
				portal.displayPanel.getWindow().setLocalLocation(portal.backFrame.getLocalLocation().x, portal.backFrame.getLocalLocation().y, 0);
				portal.displayPanel.getWindow().setScale(TablePortal.DEFAULT_SCALE);
				portal.displayPanel.getWindow().setAngle(0);
				
				portal.getRadar().getViewedArea().setLocalLocation(0, 0);
				portal.getRadar().getViewedArea().setScale(1);
				portal.getRadar().getViewedArea().setAngle(0);
			}
		}
	}
}
