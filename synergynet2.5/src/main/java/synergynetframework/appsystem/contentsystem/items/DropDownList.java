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


public class DropDownList extends Window implements IDropDownListImplementation {
	
	private static final long serialVersionUID = 4456150243917776154L;

	protected List<DropDownListItem> listItems = new ArrayList<DropDownListItem>();
	protected DropDownListItem selectedItem = null;
	protected int lineSpace = 2;
	protected int itemHeight = 25;
	
	public DropDownList(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
		this.height = 30;
		this.width = 350;
		this.backGround.setBgColour(Color.white);
		this.border.setBorderColour(Color.gray);
	}
	
	public void addListItem(String title, String value){
		
		DropDownListItem item = new DropDownListItem(title, value);
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
		
	}
	
	public void addListItem(URL imageResource, String value){
		
		DropDownListItem item = new DropDownListItem(imageResource, value);
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
		
	}
	
	public void addListItem(URL imageResource, int width, int height, String value){
		
		DropDownListItem item = new DropDownListItem(imageResource, width, height, value);
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
		
	}
	
	public void addListItem(DropDownList.DropDownListItem item){
		listItems.add(item);
		((IDropDownListImplementation)this.contentItemImplementation).addListItem(item);
	}
	
	public boolean containsValue(String value){
		for(DropDownListItem item: this.listItems){
			if(item.getValue().equals(value)) return true;
		}
		return false;
	}
	
	public void clear(){
		for (DropDownListItem item:listItems){		
			((IDropDownListImplementation)this.contentItemImplementation).removeItem(item);
		}		
		
		listItems.clear();
	}
	
	public void removeItem(DropDownList.DropDownListItem item){
		((IDropDownListImplementation)this.contentItemImplementation).removeItem(item);
		listItems.remove(item);
	}
	
	public String getSelectedValue(){
		if(selectedItem != null)
			return this.selectedItem.getValue();
		else
			return null;
	}
	
	public String getSelectedTitle(){
		return this.selectedItem.getTitle();
	}
	
	public int getItemHeight() {
		return itemHeight;
	}

	public void setItemHeight(int itemHeight) {
		this.itemHeight = itemHeight;
	}

	public List<DropDownListItem> getListItems(){
		return listItems;
	}

	public int getLineSpace() {
		return lineSpace;
	}

	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}
	
	public DropDownListItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(DropDownListItem selectedItem) {
		this.selectedItem = selectedItem;
		((IDropDownListImplementation)this.contentItemImplementation).setSelectedItem(selectedItem);
	}
	
	public class DropDownListItem{
		private String title;
		private String value;
		private URL imageResource;
		private SimpleButton itemButton;
		
		public DropDownListItem(String title, String value){
			this.title = title;
			this.value = value;
			this.itemButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
			this.itemButton.setText(this.title);
		}
		
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
		
		public URL getImageResource() {
			return imageResource;
		}
		
		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public SimpleButton getItemButton() {
			return itemButton;
		}

		public void setItemButton(SimpleButton itemButton) {
			this.itemButton = itemButton;
		}
		
	}

	public interface DropDownListListener{
		public void itemSelected(DropDownListItem item);
	}

	@Override
	public void addDropDownListListener(DropDownListListener listener) {
		((IDropDownListImplementation)this.contentItemImplementation).addDropDownListListener(listener);
	}

	@Override
	public void removeDropDownListListeners() {
		((IDropDownListImplementation)this.contentItemImplementation).removeDropDownListListeners();
	}

	@Override
	public void removeDropDownListListener(DropDownListListener listener) {
		((IDropDownListImplementation)this.contentItemImplementation).removeDropDownListListener(listener);
	}
			
}
