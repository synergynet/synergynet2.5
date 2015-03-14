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

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;

public class TableSwapDialogue {

	protected Window window;
	protected MultiLineTextLabel label;
	protected SimpleButton okButton;
	protected SimpleButton cancelButton;
	protected ContentSystem contentSystem;
	protected DropDownList table1Selection;
	protected DropDownList table2Selection;
	protected NetworkedContentManager networkedContentManager;

	public TableSwapDialogue(ContentSystem contentSystem, NetworkedContentManager networkedContentManager){
		this.contentSystem = contentSystem;
		this.networkedContentManager = networkedContentManager;
		loadContent();
	}
	
	protected void loadContent(){
		window = (Window)contentSystem.createContentItem(Window.class);
		
		label = (MultiLineTextLabel)contentSystem.createContentItem(MultiLineTextLabel.class);
		label.setBorderSize(0);
		label.setLocalLocation(0, 60);
		label.setTextColour(Color.black);
		label.setLines("Please select the tables for swap", 100);
		
		table1Selection = (DropDownList)contentSystem.createContentItem(DropDownList.class);
		table1Selection.setLocalLocation(0, 10);
		
		table2Selection = (DropDownList)contentSystem.createContentItem(DropDownList.class);
		table2Selection.setLocalLocation(0, -30);	
		
		okButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		okButton.setLocalLocation(-100, -70);
		okButton.setText("Ok");
		okButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				if (table1Selection.getSelectedItem()!=null && table2Selection.getSelectedItem()!=null){
					TableIdentity table1 = getTableIdentity(table1Selection.getSelectedItem().getValue());
					TableIdentity table2 = getTableIdentity(table2Selection.getSelectedItem().getValue());
					if (table1==null || table2 ==null) return;
					
					if (table1Selection.getSelectedItem().getValue().equals(TableIdentity.getTableIdentity().toString())){
						networkedContentManager.swapTable(table2, table1);
					}else{
						networkedContentManager.swapTable(table1, table2);
					}
					setVisible(false);
				}			
			}
		});
		
		cancelButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		cancelButton.setLocalLocation(100, -70);
		cancelButton.setText("Cancel");
		cancelButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				setVisible(false);
			}
		});
				
		window.addSubItem(label);
		window.addSubItem(table1Selection);
		window.addSubItem(table2Selection);
		window.addSubItem(okButton);
		window.addSubItem(cancelButton);	
		window.setBringToTopable(true);
		window.setRotateTranslateScalable(true);
		
		window.setLocalLocation(512, 384);
		window.setWidth(400);
		window.setHeight(200);	
		window.setOrder(Integer.MAX_VALUE);	
	
	}
	
	public void clearContent(){
		contentSystem.removeContentItem(window);
		contentSystem.removeContentItem(label);
		contentSystem.removeContentItem(okButton);
		contentSystem.removeContentItem(cancelButton);
		contentSystem.removeContentItem(table1Selection);
		contentSystem.removeContentItem(table2Selection);
		
		window = null;
		label = null;
		okButton = null;
		cancelButton = null;
		table1Selection = null;
		table2Selection = null;
	}
	
	public void setVisible(boolean isVisible){
		window.setVisible(isVisible);
		label.setVisible(isVisible);
		okButton.setVisible(isVisible);
		cancelButton.setVisible(isVisible);
		table1Selection.setVisible(isVisible);
		table2Selection.setVisible(isVisible);
		
		if (isVisible){
			for (TableIdentity table:this.networkedContentManager.getTableCommsClientService().getCurrentlyOnline()){
				table1Selection.addListItem(table.toString(), table.toString());
			}
			
			for (TableIdentity table:this.networkedContentManager.getTableCommsClientService().getCurrentlyOnline()){
				table2Selection.addListItem(table.toString(), table.toString());
			}
		}	
		else{
			table1Selection.setSelectedItem(null);
			table2Selection.setSelectedItem(null);
			table1Selection.clear();
			table2Selection.clear();
		}
	}
	
	private TableIdentity getTableIdentity(String uid){
		for (TableIdentity table: this.networkedContentManager.getTableCommsClientService().getCurrentlyOnline()){
			if (table.toString().equals(uid))
				return table;
		}
		return null;
	}
}
