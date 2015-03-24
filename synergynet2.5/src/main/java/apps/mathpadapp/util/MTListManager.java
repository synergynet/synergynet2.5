/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package apps.mathpadapp.util;

import java.awt.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;

/**
 * The Class MTListManager.
 */
public class MTListManager {
	
	/** The current list. */
	public ListContainer currentList;

	/** The is enabled. */
	private boolean isEnabled = true;

	/** The item lists. */
	private List<ListContainer> itemLists;

	/** The items. */
	private List<Object> items;

	/** The list. */
	private MTList list;

	/** The list content items. */
	private HashMap<Object, ContentItem> listContentItems;

	/** The list index. */
	private int listIndex = -1;

	/** The selected items. */
	private List<Object> selectedItems;
	
	/**
	 * Instantiates a new MT list manager.
	 *
	 * @param list
	 *            the list
	 */
	public MTListManager(MTList list) {
		this.list = list;
		items = new ArrayList<Object>();
		selectedItems = new ArrayList<Object>();
		listContentItems = new HashMap<Object, ContentItem>();
		itemLists = new ArrayList<ListContainer>();
	}

	/**
	 * Adds the item.
	 *
	 * @param str
	 *            the str
	 * @param value
	 *            the value
	 */
	public void addItem(String str, Object value) {
		if (!items.contains(value) && (currentList != null)) {
			if (((currentList.getAllItemsIncludeSystemItems().size() + 2) * list.listItemHeight) >= list.listHeight) {
				this.addNewItemList();
			}
			ContentItem listItem = list.createListItem(str, value, currentList);
			items.add(value);
			listContentItems.put(value, listItem);
			this.setEnabled(this.isEnabled());
		}
	}
	
	/**
	 * Adds the new item list.
	 */
	public void addNewItemList() {
		ListContainer newList = list.createNewItemList();
		if (this.isEnabled()) {
			newList.setBackgroundColour(list.listItemBgColor);
		} else {
			newList.setBackgroundColour(Color.LIGHT_GRAY);
		}
		itemLists.add(newList);
		currentList = newList;
		for (ListContainer list : itemLists) {
			list.setVisible(false);
		}
		currentList.setVisible(true);
		listIndex++;
		if (list.getListNoLabel() != null) {
			list.getListNoLabel().setText(
					"Page " + (listIndex + 1) + " of " + itemLists.size());
		}
	}

	/**
	 * Delete all items.
	 */
	public void deleteAllItems() {
		int currentList = listIndex;
		items.clear();
		selectedItems.clear();
		listContentItems.clear();
		this.rebuildLists();
		this.showList(currentList);
	}

	/**
	 * Delete item.
	 *
	 * @param item
	 *            the item
	 */
	public void deleteItem(Object item) {
		int currentList = listIndex;
		items.remove(item);
		if (selectedItems.contains(item)) {
			selectedItems.remove(item);
		}
		if (listContentItems.containsKey(item)) {
			listContentItems.remove(item);
		}
		this.rebuildLists();
		this.showList(currentList);
	}
	
	/**
	 * Delete selected items.
	 */
	public void deleteSelectedItems() {
		int currentList = listIndex;
		for (Object item : selectedItems) {
			items.remove(item);
			listContentItems.remove(item);
		}
		selectedItems.clear();
		this.rebuildLists();
		this.showList(currentList);
	}

	/**
	 * Deselect all items.
	 */
	public void deselectAllItems() {
		selectedItems.clear();
		for (ContentItem contentItem : listContentItems.values()) {
			this.setListItemHighlighted(contentItem, false);
		}
	}

	/**
	 * Deselect item.
	 *
	 * @param item
	 *            the item
	 */
	public void deselectItem(Object item) {
		if (items.contains(item) && selectedItems.contains(item)) {
			// Deselect item
			selectedItems.remove(item);
			if (listContentItems.containsKey(item)) {
				ContentItem contentItem = listContentItems.get(item);
				this.setListItemHighlighted(contentItem, false);
			}
		}
	}

	/**
	 * Gets the all items.
	 *
	 * @return the all items
	 */
	public List<Object> getAllItems() {
		return items;
	}

	/**
	 * Gets the item lists.
	 *
	 * @return the item lists
	 */
	public List<ListContainer> getItemLists() {
		return itemLists;
	}
	
	/**
	 * Gets the list item.
	 *
	 * @param item
	 *            the item
	 * @return the list item
	 */
	public ContentItem getListItem(Object item) {
		return listContentItems.get(item);
	}

	/**
	 * Gets the list items.
	 *
	 * @return the list items
	 */
	public HashMap<Object, ContentItem> getListItems() {
		return listContentItems;
	}

	/**
	 * Gets the selected items.
	 *
	 * @return the selected items
	 */
	public List<Object> getSelectedItems() {
		return selectedItems;
	}
	
	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled() {
		return this.isEnabled;
	}

	/**
	 * Rebuild lists.
	 */
	private void rebuildLists() {
		listIndex = -1;
		for (ListContainer listContainer : this.itemLists) {
			list.getContainer().removeSubItem(listContainer);
		}
		itemLists.clear();
		this.addNewItemList();
		HashMap<Object, ContentItem> backupItems = new HashMap<Object, ContentItem>(
				listContentItems);
		listContentItems.clear();
		items.clear();
		for (Object bItem : backupItems.keySet()) {
			ContentItem contentItem = backupItems.get(bItem);
			if (contentItem instanceof SimpleButton) {
				SimpleButton btn = (SimpleButton) contentItem;
				this.addItem(btn.getText(), bItem);
				if ((btn.getImages() != null) && !btn.getImages().isEmpty()) {
					this.setIcon(bItem, btn.getImages().keySet().iterator()
							.next());
				}
			}
		}
		list.getContainer().setAsTopObject();
	}
	
	/**
	 * Select all items.
	 */
	public void selectAllItems() {
		selectedItems.clear();
		selectedItems.addAll(items);
		for (Object item : listContentItems.keySet()) {
			if (listContentItems.get(item).isVisible()) {
				selectedItems.add(item);
				ContentItem contentItem = listContentItems.get(item);
				this.setListItemHighlighted(contentItem, true);
			}
		}
	}

	/**
	 * Select item.
	 *
	 * @param item
	 *            the item
	 */
	public void selectItem(Object item) {
		if (items.contains(item) && !selectedItems.contains(item)) {
			// Select Item
			selectedItems.add(item);
			ContentItem contentItem = listContentItems.get(item);
			this.setListItemHighlighted(contentItem, true);
		}
	}
	
	/**
	 * Sets the enabled.
	 *
	 * @param isEnabled
	 *            the new enabled
	 */
	public void setEnabled(boolean isEnabled) {
		this.deselectAllItems();
		this.isEnabled = isEnabled;
		if (!isEnabled) {
			for (ContentItem contentItem : this.list.getContainer()
					.getAllItemsIncludeSystemItems()) {
				// contentItem.setBackgroundColour(Color.LIGHT_GRAY);
				((OrthoContentItem) contentItem)
						.setRotateTranslateScalable(false);
			}
			for (ContentItem contentItem : listContentItems.values()) {
				if (contentItem instanceof SimpleButton) {
					((SimpleButton) contentItem)
							.setRotateTranslateScalable(false);
					// ((SimpleButton)contentItem).setBackgroundColour(Color.LIGHT_GRAY);
				}
			}
		} else {
			for (ContentItem contentItem : this.list.getContainer()
					.getAllItemsIncludeSystemItems()) {
				// contentItem.setBackgroundColour(Color.white);
				((OrthoContentItem) contentItem)
						.setRotateTranslateScalable(true);
			}
			for (ContentItem contentItem : listContentItems.values()) {
				if (contentItem instanceof SimpleButton) {
					((SimpleButton) contentItem)
							.setRotateTranslateScalable(true);
					// ((SimpleButton)contentItem).setBackgroundColour(Color.white);
				}
			}
		}
	}
	
	/**
	 * Sets the icon.
	 *
	 * @param item
	 *            the item
	 * @param iconResource
	 *            the icon resource
	 */
	public void setIcon(Object item, URL iconResource) {
		if (listContentItems.containsKey(item) && (iconResource != null)) {
			ContentItem contentItem = listContentItems.get(item);
			if (contentItem instanceof SimpleButton) {
				((SimpleButton) contentItem).removeAllImages();
				((SimpleButton) contentItem).drawImage(iconResource, 0, 0, 25,
						25);
			}
		}
	}
	
	/**
	 * Sets the list item highlighted.
	 *
	 * @param contentItem
	 *            the content item
	 * @param isHighlighted
	 *            the is highlighted
	 */
	protected void setListItemHighlighted(ContentItem contentItem,
			boolean isHighlighted) {
		if (contentItem instanceof SimpleButton) {
			if (isHighlighted) {
				((SimpleButton) contentItem)
						.setBackgroundColour(list.listItemTextColor);
				((SimpleButton) contentItem)
						.setTextColour(list.listItemBgColor);
			} else {
				((SimpleButton) contentItem)
						.setBackgroundColour(list.listItemBgColor);
				((SimpleButton) contentItem)
						.setTextColour(list.listItemTextColor);
			}
		}
	}

	/**
	 * Show list.
	 *
	 * @param index
	 *            the index
	 */
	public void showList(int index) {
		if ((index >= 0) && (index < itemLists.size())) {
			itemLists.get(index).setVisible(true);
			currentList = itemLists.get(index);
			for (int i = 0; i < itemLists.size(); i++) {
				if (i != index) {
					itemLists.get(i).setVisible(false);
				}
			}
			listIndex = index;
			list.getListNoLabel().setText(
					"Page " + (index + 1) + " of " + itemLists.size());
		}
	}
	
	/**
	 * Show next.
	 */
	public void showNext() {
		this.showList(listIndex + 1);
	}

	/**
	 * Show previous.
	 */
	public void showPrevious() {
		this.showList(listIndex - 1);
	}
}
