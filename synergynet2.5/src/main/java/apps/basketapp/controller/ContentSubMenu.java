package apps.basketapp.controller;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SubAppMenuEventListener;

public class ContentSubMenu {
	
	protected ContentSystem contentSystem;
	protected List<SubAppMenuEventListener> subAppMenuEventListener = new ArrayList<SubAppMenuEventListener>();
	
	public final static String CONTENT_VIDEO = "Video";
	public final static String CONTENT_TEXT = "Text";
	public final static String CONTENT_MAP = "Map";
	public final static String TABLE_RED = "Red Table";
	public final static String TABLE_BLUE = "Blue Table";
	public final static String TABLE_GREEN = "Green Table";
	public final static String TABLE_YELLOW = "Yellow Table";
	public final static String TABLE = "Table";
	
	public ContentSubMenu(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
	}
	
	public ListContainer getSubAppMenu(){

		final ListContainer menu = (ListContainer)contentSystem.createContentItem(ListContainer.class);
		menu.setWidth(200);
		menu.setItemHeight(30);
		menu.getBackgroundFrame().setBackgroundColour(Color.gray);			
		
		generateButton(CONTENT_VIDEO, menu);
		generateButton(CONTENT_TEXT, menu);
		generateButton(CONTENT_MAP, menu);
		generateButton(TABLE_RED, menu);
		generateButton(TABLE_BLUE, menu);
		generateButton(TABLE_GREEN, menu);
		generateButton(TABLE_YELLOW, menu);
		
		return menu;
	}
	
	private void generateButton(final String buttonValue, final ListContainer menu){
		SimpleButton content = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);	
		content.setAutoFitSize(false);
		content.setText(buttonValue);
		content.setBackgroundColour(Color.lightGray);
		content.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {					
				for (SubAppMenuEventListener l:subAppMenuEventListener)					
					l.menuSelected(buttonValue, buttonValue);
				menu.setVisible(false);
			}			
		});
		
		menu.addSubItem(content);
	}
	

	public void addSubAppMenuEventListener(SubAppMenuEventListener l){
		if (this.subAppMenuEventListener==null)
			this.subAppMenuEventListener = new ArrayList<SubAppMenuEventListener>();
		
		if(!this.subAppMenuEventListener.contains(l))
			this.subAppMenuEventListener.add(l);
	}
	
	public void removeSubAppMenuEventListeners(){
		subAppMenuEventListener.clear();
	}
	
	public void removeSubAppMenuEventListener(SubAppMenuEventListener l){
		subAppMenuEventListener.remove(l);
	}
}

