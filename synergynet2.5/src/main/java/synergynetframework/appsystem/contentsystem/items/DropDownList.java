/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation;



/**
 * The Class DropDownList.
 */
public class DropDownList extends Window implements IDropDownListImplementation {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4456150243917776154L;

	/** The list items. */
	protected List<DropDownListItem> listItems = new ArrayList<DropDownListItem>();
	
	/** The selected item. */
	protected DropDownListItem selectedItem = null;
	
	/** The line space. */
	protected int lineSpace = 2;
	
	/** The item height. */
	protected int itemHeight = 25;
	
	/**
	 * Instantiates a new drop down list.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public DropDownList(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
		this.height = 30;
		this.width = 350;
		this.backGround.setBgColour(Color.white);
		this.border.setBorderColour(Color.gray);
	}
	
	/**
	 * Adds the list item.
	 *
	 * @param title the title
	 * @param value the value
	 */
	public void addListItem(String title, String value){
		
		DropDownListItem item = new DropDownListItem(title, value);
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
		
	}
	
	/**
	 * Adds the list item.
	 *
	 * @param imageResource the image resource
	 * @param value the value
	 */
	public void addListItem(URL imageResource, String value){
		
		DropDownListItem item = new DropDownListItem(imageResource, value);
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
		
	}
	
	/**
	 * Adds the list item.
	 *
	 * @param imageResource the image resource
	 * @param width the width
	 * @param height the height
	 * @param value the value
	 */
	public void addListItem(URL imageResource, int width, int height, String value){
		
		DropDownListItem item = new DropDownListItem(imageResource, width, height, value);
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#addListItem(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem)
	 */
	public void addListItem(DropDownList.DropDownListItem item){
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
	}
	
	/**
	 * Contains value.
	 *
	 * @param value the value
	 * @return true, if successful
	 */
	public boolean containsValue(String value){
		for(DropDownListItem item: this.listItems){
			if(item.getValue().equals(value)) return true;
		}
		return false;
	}
	
	/**
	 * Clear.
	 */
	public void clear(){
		for (DropDownListItem item:listItems){		
			((IDropDownListImplementation)this.contentItemImplementation).removeItem(item);
		}		
		
		listItems.clear();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#removeItem(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem)
	 */
	public void removeItem(DropDownList.DropDownListItem item){
		((IDropDownListImplementation)this.contentItemImplementation).removeItem(item);
		listItems.remove(item);
	}
	
	/**
	 * Gets the selected value.
	 *
	 * @return the selected value
	 */
	public String getSelectedValue(){
		if(selectedItem != null)
			return this.selectedItem.getValue();
		else
			return null;
	}
	
	/**
	 * Gets the selected title.
	 *
	 * @return the selected title
	 */
	public String getSelectedTitle(){
		return this.selectedItem.getTitle();
	}
	
	/**
	 * Gets the item height.
	 *
	 * @return the item height
	 */
	public int getItemHeight() {
		return itemHeight;
	}

	/**
	 * Sets the item height.
	 *
	 * @param itemHeight the new item height
	 */
	public void setItemHeight(int itemHeight) {
		this.itemHeight = itemHeight;
	}

	/**
	 * Gets the list items.
	 *
	 * @return the list items
	 */
	public List<DropDownListItem> getListItems(){
		return listItems;
	}

	/**
	 * Gets the line space.
	 *
	 * @return the line space
	 */
	public int getLineSpace() {
		return lineSpace;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#setLineSpace(int)
	 */
	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}
	
	/**
	 * Gets the selected item.
	 *
	 * @return the selected item
	 */
	public DropDownListItem getSelectedItem() {
		return selectedItem;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#setSelectedItem(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem)
	 */
	public void setSelectedItem(DropDownListItem selectedItem) {
		this.selectedItem = selectedItem;
		((IDropDownListImplementation)this.contentItemImplementation).setSelectedItem(selectedItem);
	}
	
	/**
	 * The Class DropDownListItem.
	 */
	public class DropDownListItem{
		
		/** The title. */
		private String title;
		
		/** The value. */
		private String value;
		
		/** The image resource. */
		private URL imageResource;
		
		/** The item button. */
		private SimpleButton itemButton;
		
		/**
		 * Instantiates a new drop down list item.
		 *
		 * @param title the title
		 * @param value the value
		 */
		public DropDownListItem(String title, String value){
			this.title = title;
			this.value = value;
			this.itemButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
			this.itemButton.setText(this.title);
		}
		
		/**
		 * Instantiates a new drop down list item.
		 *
		 * @param imageResource the image resource
		 * @param width the width
		 * @param height the height
		 * @param value the value
		 */
		public DropDownListItem(URL imageResource, int width, int height, String value){
			this.title = imageResource.toString();
			this.imageResource = imageResource;
			this.value = value;
			this.itemButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
			this.itemButton.setBorderSize(0);
			this.itemButton.setAutoFitSize(false);
			this.itemButton.setWidth(width);
			this.itemButton.setHeight(height);
			this.itemButton.drawImage(imageResource);
		}
		
		/**
		 * Instantiates a new drop down list item.
		 *
		 * @param imageResource the image resource
		 * @param value the value
		 */
		public DropDownListItem(URL imageResource, String value){
			this.title = imageResource.toString();
			this.imageResource = imageResource;
			this.value = value;
			this.itemButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
			this.itemButton.setBorderSize(0);
			this.itemButton.setAutoFitSize(false);
			this.itemButton.setWidth(DropDownList.this.width);			
			this.itemButton.setHeight(itemHeight);
			this.itemButton.drawImage(imageResource);
		}
		
		/**
		 * Gets the image resource.
		 *
		 * @return the image resource
		 */
		public URL getImageResource() {
			return imageResource;
		}
		
		/**
		 * Gets the title.
		 *
		 * @return the title
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Sets the title.
		 *
		 * @param title the new title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 *
		 * @param value the new value
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * Gets the item button.
		 *
		 * @return the item button
		 */
		public SimpleButton getItemButton() {
			return itemButton;
		}

		/**
		 * Sets the item button.
		 *
		 * @param itemButton the new item button
		 */
		public void setItemButton(SimpleButton itemButton) {
			this.itemButton = itemButton;
		}
		
	}

	/**
	 * The listener interface for receiving dropDownList events.
	 * The class that is interested in processing a dropDownList
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addDropDownListListener<code> method. When
	 * the dropDownList event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see DropDownListEvent
	 */
	public interface DropDownListListener{
		
		/**
		 * Item selected.
		 *
		 * @param item the item
		 */
		public void itemSelected(DropDownListItem item);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#addDropDownListListener(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener)
	 */
	@Override
	public void addDropDownListListener(DropDownListListener listener) {
		((IDropDownListImplementation)this.contentItemImplementation).addDropDownListListener(listener);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#removeDropDownListListeners()
	 */
	@Override
	public void removeDropDownListListeners() {
		((IDropDownListImplementation)this.contentItemImplementation).removeDropDownListListeners();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#removeDropDownListListener(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener)
	 */
	@Override
	public void removeDropDownListListener(DropDownListListener listener) {
		((IDropDownListImplementation)this.contentItemImplementation).removeDropDownListListener(listener);
	}
			
}
