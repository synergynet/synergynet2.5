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

package apps.mysteriestableportal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import core.SynergyNetDesktop;

import apps.mysteries.SubAppMenu;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;

public class ControlMenu {
	protected ContentSystem contentSystem;
	protected SubAppMenu subAppMenu;
	protected ListContainer controlMenu;
	protected List<ControlMenuListener> listeners = new ArrayList<ControlMenuListener>();
	
	private boolean areTablesLocked = false;
	
	public ControlMenu(ContentSystem contentSystem, SubAppMenu subAppMenu){
		this.contentSystem = contentSystem;
		this.subAppMenu = subAppMenu;
		LoadControlMenu();
	}

	private ListContainer LoadControlMenu(){
		
		controlMenu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		controlMenu.setBackgroundColour(Color.BLUE);
		controlMenu.setWidth(200);
		controlMenu.setItemHeight(30);
		controlMenu.setVisible(true);
		
		
		SimpleButton sendDataButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		sendDataButton.setAutoFitSize(false);
		sendDataButton.setText("Send Data");
		sendDataButton.setBackgroundColour(Color.lightGray);
		sendDataButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				for(ControlMenuListener l: listeners) l.sendDesktopData();
			}			
		});

		final SimpleButton clearLocalTableButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		clearLocalTableButton.setAutoFitSize(false);
		clearLocalTableButton.setText("Clear Local Table");
		clearLocalTableButton.setBackgroundColour(Color.lightGray);
		clearLocalTableButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				for(ControlMenuListener l: listeners) l.clearLocalTable();
			}			
		});	

		
		final SimpleButton clearTableButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		clearTableButton.setAutoFitSize(false);
		clearTableButton.setText("Clear Student Tables");
		clearTableButton.setBackgroundColour(Color.lightGray);
		clearTableButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				for(ControlMenuListener l: listeners) l.clearStudentTables();
			}			
		});
		
		final SimpleButton createTablePortalButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		createTablePortalButton.setAutoFitSize(false);
		createTablePortalButton.setText("Monitor Student Tables");
		createTablePortalButton.setBackgroundColour(Color.lightGray);
		createTablePortalButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				for(ControlMenuListener l: listeners) l.createTablePortals();
			}			
		});
		
		final SimpleButton hideTablePortalButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		hideTablePortalButton.setAutoFitSize(false);
		hideTablePortalButton.setText("Hide Student Tables");
		hideTablePortalButton.setBackgroundColour(Color.lightGray);
		hideTablePortalButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {			
				for(ControlMenuListener l: listeners) l.hideTablePortals();
			}			
		});
		
		final SimpleButton lockTablesButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		lockTablesButton.setAutoFitSize(false);
		lockTablesButton.setText("Lock Student Tables");
		lockTablesButton.setBackgroundColour(Color.lightGray);
		lockTablesButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				areTablesLocked = !areTablesLocked;
				for(ControlMenuListener l: listeners) l.lockStudentTables(areTablesLocked);
				if(areTablesLocked)
					lockTablesButton.setText("Unlock Student Tables");
				else
					lockTablesButton.setText("Lock Student Tables");
			}			
		});
				
		SimpleButton backToMainMenuButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);		
		backToMainMenuButton.setAutoFitSize(false);
		backToMainMenuButton.setText("Back To Main Menu");
		backToMainMenuButton.setBackgroundColour(Color.lightGray);
		backToMainMenuButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				try {
					SynergyNetDesktop.getInstance().showMainMenu();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}			
		});	
		backToMainMenuButton.setVisible(false);	
		if (subAppMenu!=null){
			controlMenu.addSubMenu(subAppMenu.getSubAppMenu(), "Choose Mystery");
		}
		controlMenu.setLocalLocation(200, 200);
		controlMenu.addSubItem(sendDataButton);
		controlMenu.addSubItem(clearTableButton);
		controlMenu.addSubItem(clearLocalTableButton);
		controlMenu.addSubItem(createTablePortalButton);
		controlMenu.addSubItem(hideTablePortalButton);
		controlMenu.addSubItem(lockTablesButton);
		controlMenu.addSubItem(backToMainMenuButton);
		
		return controlMenu;
	}
	
	public void setLocation(float x, float y){
		controlMenu.setLocalLocation(x, y);
	}
	
	public interface ControlMenuListener{
		public void sendDesktopData();
		public void clearLocalTable();
		public void clearStudentTables();
		public void createTablePortals();
		public void hideTablePortals();
		public void lockStudentTables(boolean lock);
	}
	
	public void addControlMenuListener(ControlMenuListener l){
		listeners.add(l);
	}
	
	public void setVisible(boolean isVisible){
		controlMenu.setVisible(isVisible); 
	}
}

