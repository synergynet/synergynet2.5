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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.ListEventAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.ListEventListener;


/**
 * The Class ListContainer.
 */
public class ListContainer extends Window {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4456150243914896154L;

	/** The list items. */
	protected List<QuadContentItem> listItems = new ArrayList<QuadContentItem>();
	
	/** The sub menus. */
	protected Map<SimpleButton, ListContainer> subMenus = new HashMap<SimpleButton, ListContainer>();
	
	/** The space to top. */
	protected int spaceToTop = 30;
	
	/** The line space. */
	protected int lineSpace = 5;
	
	/** The space to side. */
	protected int spaceToSide = 20;
	
	/** The space to bottom. */
	protected int spaceToBottom = 10;
	
	/** The item height. */
	protected int itemHeight = 30;
	
	/** The item width. */
	protected int itemWidth = 100;
	
	/** The is horizontal. */
	protected boolean isHorizontal = false;
	
	/** The is auto fit size. */
	protected boolean isAutoFitSize = true;
	
	/** The list event listeners. */
	protected transient List<ListEventListener> listEventListeners = new ArrayList<ListEventListener>();
	
	/**
	 * Instantiates a new list container.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public ListContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/**
	 * Adds the sub item.
	 *
	 * @param index the index
	 * @param item the item
	 */
	public void addSubItem(int index, QuadContentItem item){
		if (!listItems.contains(item)){
			listItems.add(index, item);
			super.addSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).addSubItem(index, item);
		}		
	}
	
	/**
	 * Adds the sub item.
	 *
	 * @param item the item
	 */
	public void addSubItem(QuadContentItem item){
		if (!listItems.contains(item)){
			listItems.add(item);
			super.addSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).addSubItem(item);
		}		
	}
	
	/**
	 * Adds the sub menu.
	 *
	 * @param subMenu the sub menu
	 * @param title the title
	 */
	public void addSubMenu(final ListContainer subMenu, String title){
		if (!subMenus.values().contains(subMenu)){
			SimpleButton button = (SimpleButton)this.contentSystem.createContentItem(SimpleButton.class);
			button.setAutoFitSize(false);
			button.text =title;
			subMenu.setVisible(false);
			subMenu.setLocalLocation(-9999, -9999);
			button.addItemListener(new ItemEventAdapter(){
				public void cursorPressed(ContentItem item, long id, float x, float y,
						float pressure) {
					if (subMenu.isVisible){
						subMenu.setVisible(false);
						subMenu.setLocalLocation(-9999, -9999);
						
					}
					else{
						subMenu.setVisible(true);
						((IListContainerImplementation)contentItemImplementation).updateVisibility();

					}
				}
			});
			listItems.add(button);
			this.subMenus.put(button, subMenu);
			super.addSubItem(button);
			super.addSubItem(subMenu);
			((IListContainerImplementation)this.contentItemImplementation).addSubMenu(button, subMenu);
		}		
	}
	
	/**
	 * Gets the sub menus.
	 *
	 * @return the sub menus
	 */
	public Map<SimpleButton, ListContainer> getSubMenus() {
		return subMenus;
	}

	/**
	 * Removes the sub item.
	 *
	 * @param item the item
	 */
	public void removeSubItem(QuadContentItem item){
		if (listItems.contains(item)){
			listItems.remove(item);
			super.removeSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).removeItem(item);
		}				
	}
	
	/**
	 * Clear.
	 */
	public void clear(){
		for (QuadContentItem item:listItems){		
			super.removeSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).removeItem(item);
		}		
		
		listItems.clear();
	}
	
	/**
	 * Gets the item width.
	 *
	 * @return the item width
	 */
	public int getItemWidth() {
		return itemWidth;
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
	 * Sets the item width.
	 *
	 * @param itemWidth the new item width
	 */
	public void setItemWidth(int itemWidth) {
		this.itemWidth = itemWidth;
	}

	/**
	 * Gets the list items.
	 *
	 * @return the list items
	 */
	public List<QuadContentItem> getListItems(){
		return listItems;
	}

	/**
	 * Gets the space to top.
	 *
	 * @return the space to top
	 */
	public int getSpaceToTop() {
		return spaceToTop;
	}

	/**
	 * Sets the space to top.
	 *
	 * @param spaceToTop the new space to top
	 */
	public void setSpaceToTop(int spaceToTop) {
		this.spaceToTop = spaceToTop;
	}

	/**
	 * Gets the line space.
	 *
	 * @return the line space
	 */
	public int getLineSpace() {
		return lineSpace;
	}

	/**
	 * Sets the line space.
	 *
	 * @param lineSpace the new line space
	 */
	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}

	/**
	 * Gets the space to side.
	 *
	 * @return the space to side
	 */
	public int getSpaceToSide() {
		return spaceToSide;
	}

	/**
	 * Sets the space to side.
	 *
	 * @param spaceToSide the new space to side
	 */
	public void setSpaceToSide(int spaceToSide) {
		this.spaceToSide = spaceToSide;
	}

	/**
	 * Gets the space to bottom.
	 *
	 * @return the space to bottom
	 */
	public int getSpaceToBottom() {
		return spaceToBottom;
	}

	/**
	 * Sets the space to bottom.
	 *
	 * @param spaceToBottom the new space to bottom
	 */
	public void setSpaceToBottom(int spaceToBottom) {
		this.spaceToBottom = spaceToBottom;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.ContentItem#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isVisible) {
		super.setVisible(isVisible);
		if (isVisible){
			((IListContainerImplementation)contentItemImplementation).updateVisibility();
		}
		
		if (isVisible){
			for (ListEventListener l: listEventListeners)
				l.listShown();
		}
		else{
			for (ListEventListener l: listEventListeners)
				l.listHiden();
		}
	}
	
	
	/**
	 * Adds the list event listener.
	 *
	 * @param l the l
	 */
	public void addListEventListener(ListEventAdapter l){
		if (this.listEventListeners==null)
			this.listEventListeners = new ArrayList<ListEventListener>();
		
		if(!this.listEventListeners.contains(l))
			this.listEventListeners.add(l);
	}
	
	/**
	 * Removes the list event listeners.
	 */
	public void removeListEventListeners(){
		listEventListeners.clear();
	}
	
	/**
	 * Removes the list event listener.
	 *
	 * @param l the l
	 */
	public void removeListEventListener(ListEventListener l){
		listEventListeners.remove(l);
	}
	
	/**
	 * Sets the horizontal.
	 *
	 * @param isHorizontal the new horizontal
	 */
	public void setHorizontal(boolean isHorizontal){
		this.isHorizontal = isHorizontal;
		((IListContainerImplementation)this.contentItemImplementation).setHorizontal(isHorizontal);
	}
	
	/**
	 * Sets the auto fit size.
	 *
	 * @param isAutoFitSize the new auto fit size
	 */
	public void setAutoFitSize(boolean isAutoFitSize){
		this.isAutoFitSize = isAutoFitSize;
		((IListContainerImplementation)this.contentItemImplementation).setAutoFitSize(isAutoFitSize);
	}
	
	/**
	 * Checks if is horizontal.
	 *
	 * @return true, if is horizontal
	 */
	public boolean isHorizontal(){
		return isHorizontal;
	}
	
	/**
	 * Checks if is auto fit size.
	 *
	 * @return true, if is auto fit size
	 */
	public boolean isAutoFitSize(){
		return isAutoFitSize;
	}
			
}
