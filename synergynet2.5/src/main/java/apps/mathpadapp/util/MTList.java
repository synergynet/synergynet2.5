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
package apps.mathpadapp.util;

import java.awt.Color;
import java.awt.Font;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;

public class MTList {

	private TextLabel listNoLabel;
	public int listItemHeight = 25;
	public int listItemWidth = 420;
	public int listHeight = 300;
	public Color listItemTextColor = Color.black;
	public Color listItemBgColor = Color.white;
	
	protected SimpleButton previousButton, nextButton;
	protected OrthoContainer container;
	protected ContentSystem contentSystem;
	protected MTListManager listManager;
	
	public MTList(ContentSystem contentSystem){
		this.contentSystem = contentSystem;
		listManager = new MTListManager(this);
		container = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		//Add page number label
		setListNoLabel((TextLabel) contentSystem.createContentItem(TextLabel.class));
		getListNoLabel().setText("Page 1 of 1");
		getListNoLabel().setFont(new Font("Times New Roman", Font.PLAIN, 10));
		getListNoLabel().setBorderSize(0);
		getListNoLabel().setTextColour(listItemTextColor);
		getListNoLabel().setBackgroundColour(listItemBgColor);
		container.addSubItem(getListNoLabel());
		
		nextButton=((SimpleButton) contentSystem.createContentItem(SimpleButton.class));
		nextButton.setText(" Next ");
		nextButton.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		nextButton.setBorderSize(0);
		nextButton.setTextColour(listItemTextColor);
		nextButton.setBackgroundColour(listItemBgColor);
		nextButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				listManager.showNext();
			}
		});
		container.addSubItem(nextButton);
		
		previousButton =((SimpleButton) contentSystem.createContentItem(SimpleButton.class));
		previousButton.setText("Previous");
		previousButton.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		previousButton.setBorderSize(0);
		previousButton.setTextColour(listItemTextColor);
		previousButton.setBackgroundColour(listItemBgColor);
		previousButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				listManager.showPrevious();
			}
		});
		container.addSubItem(previousButton);
		listManager.addNewItemList();
		
		updateLayout();
	}
	
	protected ListContainer createNewItemList(){
		ListContainer newList = (ListContainer) contentSystem.createContentItem(ListContainer.class);
		newList.setAutoFitSize(false);
		newList.setBackgroundColour(listItemBgColor);
		newList.setBorderColour(Color.white);
		newList.setWidth(listItemWidth);
		newList.setHeight(listHeight);
		newList.setItemHeight(25);
		newList.setBorderSize(1);
		newList.setBorderColour(Color.black);
		newList.setLocalLocation(-listItemWidth/2,-listHeight/2 + 30,0);
		container.addSubItem(newList);
		return newList;
	}
	
	protected ContentItem createListItem(String str, Object item, ListContainer targetList){
		SimpleButton itemButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		itemButton.setAutoFitSize(false);
		itemButton.setWidth(targetList.getWidth());
		itemButton.setHeight(listItemHeight);
		itemButton.setBorderSize(0);
		itemButton.setText("            "+ str.trim());
		itemButton.setAlignment(Alignment.LEFT);
		itemButton.addButtonListener(new ListItemAction());
		targetList.addSubItem(itemButton);
		itemButton.setBackgroundColour(targetList.getBackgroundColour());
		return itemButton;
	}
	
	class ListItemAction extends SimpleButtonAdapter{

		@Override
		public void buttonPressed(SimpleButton b, long id, float x, float y,
				float pressure) {
			for(Object item: listManager.getAllItems()){
				ContentItem contentItem = listManager.getListItem(item);
				if(contentItem != null && contentItem instanceof SimpleButton){
					if(((SimpleButton)contentItem).getName().equals(b.getName())){
						if(listManager.getSelectedItems().contains(item))
							listManager.deselectItem(item);
						else
							listManager.selectItem(item);
						return;
					}
				}
			}
		}

		@Override
		public void buttonReleased(SimpleButton b, long id, float x, float y,
				float pressure) {
		}
	}
	
	public OrthoContainer getContainer(){
		return container;
	}
	
	public MTListManager getManager(){
		return listManager;
	}
	
	public void setWidth(int width){
		listItemWidth = width;
		updateLayout();
	}
	
	public void setHeight(int height){
		listHeight = height;
		updateLayout();
	}
	
	private void updateLayout(){
		getListNoLabel().setLocalLocation(0, + listHeight/2 + 20);
		nextButton.setLocalLocation(listItemWidth/2  - nextButton.getWidth()/2-3,  + listHeight/2 + 20);
		previousButton.setLocalLocation(-listItemWidth/2  + previousButton.getWidth()/2+3, + listHeight/2 + 20);
		for(ListContainer listContainer: this.getManager().getItemLists()){
			listContainer.setWidth(listItemWidth);
			listContainer.setHeight(listHeight);
			//listContainer.setLocalLocation(-listItemWidth/2,-listHeight/2 + 30,0);
		}
	}

	protected void setListNoLabel(TextLabel listNoLabel) {
		this.listNoLabel = listNoLabel;
	}

	protected TextLabel getListNoLabel() {
		return listNoLabel;
	}
	
}

