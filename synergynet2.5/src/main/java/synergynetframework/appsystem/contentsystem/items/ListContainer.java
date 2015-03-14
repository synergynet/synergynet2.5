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

public class ListContainer extends Window {
	
	private static final long serialVersionUID = 4456150243914896154L;

	protected List<QuadContentItem> listItems = new ArrayList<QuadContentItem>();
	protected Map<SimpleButton, ListContainer> subMenus = new HashMap<SimpleButton, ListContainer>();
	protected int spaceToTop = 30;
	protected int lineSpace = 5;
	protected int spaceToSide = 20;
	protected int spaceToBottom = 10;
	protected int itemHeight = 30;
	protected int itemWidth = 100;
	
	protected boolean isHorizontal = false;
	protected boolean isAutoFitSize = true;
	
	protected transient List<ListEventListener> listEventListeners = new ArrayList<ListEventListener>();
	
	public ListContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	public void addSubItem(int index, QuadContentItem item){
		if (!listItems.contains(item)){
			listItems.add(index, item);
			super.addSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).addSubItem(index, item);
		}		
	}
	
	public void addSubItem(QuadContentItem item){
		if (!listItems.contains(item)){
			listItems.add(item);
			super.addSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).addSubItem(item);
		}		
	}
	
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
	
	public Map<SimpleButton, ListContainer> getSubMenus() {
		return subMenus;
	}

	public void removeSubItem(QuadContentItem item){
		if (listItems.contains(item)){
			listItems.remove(item);
			super.removeSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).removeItem(item);
		}				
	}
	
	public void clear(){
		for (QuadContentItem item:listItems){		
			super.removeSubItem(item);
			((IListContainerImplementation)this.contentItemImplementation).removeItem(item);
		}		
		
		listItems.clear();
	}
	
	public int getItemWidth() {
		return itemWidth;
	}
	
	public int getItemHeight() {
		return itemHeight;
	}

	public void setItemHeight(int itemHeight) {
		this.itemHeight = itemHeight;
	}
	
	public void setItemWidth(int itemWidth) {
		this.itemWidth = itemWidth;
	}

	public List<QuadContentItem> getListItems(){
		return listItems;
	}

	public int getSpaceToTop() {
		return spaceToTop;
	}

	public void setSpaceToTop(int spaceToTop) {
		this.spaceToTop = spaceToTop;
	}

	public int getLineSpace() {
		return lineSpace;
	}

	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}

	public int getSpaceToSide() {
		return spaceToSide;
	}

	public void setSpaceToSide(int spaceToSide) {
		this.spaceToSide = spaceToSide;
	}

	public int getSpaceToBottom() {
		return spaceToBottom;
	}

	public void setSpaceToBottom(int spaceToBottom) {
		this.spaceToBottom = spaceToBottom;
	}

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
	
	
	public void addListEventListener(ListEventAdapter l){
		if (this.listEventListeners==null)
			this.listEventListeners = new ArrayList<ListEventListener>();
		
		if(!this.listEventListeners.contains(l))
			this.listEventListeners.add(l);
	}
	
	public void removeListEventListeners(){
		listEventListeners.clear();
	}
	
	public void removeListEventListener(ListEventListener l){
		listEventListeners.remove(l);
	}
	
	public void setHorizontal(boolean isHorizontal){
		this.isHorizontal = isHorizontal;
		((IListContainerImplementation)this.contentItemImplementation).setHorizontal(isHorizontal);
	}
	
	public void setAutoFitSize(boolean isAutoFitSize){
		this.isAutoFitSize = isAutoFitSize;
		((IListContainerImplementation)this.contentItemImplementation).setAutoFitSize(isAutoFitSize);
	}
	
	public boolean isHorizontal(){
		return isHorizontal;
	}
	
	public boolean isAutoFitSize(){
		return isAutoFitSize;
	}
			
}
