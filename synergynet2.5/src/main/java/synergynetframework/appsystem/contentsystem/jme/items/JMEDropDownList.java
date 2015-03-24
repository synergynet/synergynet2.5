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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;


/**
 * The Class JMEDropDownList.
 */
public class JMEDropDownList extends JMEWindow implements IDropDownListImplementation{
	
	/** The list. */
	private DropDownList list;
	
	/** The selected item box. */
	private TextLabel selectedItemBox;
	
	/** The drop down button. */
	private SimpleButton dropDownButton;
	
	/** The drop down list. */
	private ListContainer dropDownList;
	
	/** The listeners. */
	private transient List<DropDownListListener> listeners = new ArrayList<DropDownListListener>();

	/**
	 * Instantiates a new JME drop down list.
	 *
	 * @param contentItem the content item
	 */
	public JMEDropDownList(ContentItem contentItem) {
		super(contentItem);		
		list = ((DropDownList)contentItem);	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEWindow#init()
	 */
	public void init(){
		super.init();
		selectedItemBox = (TextLabel)this.contentItem.getContentSystem().createContentItem(TextLabel.class);
		selectedItemBox.setAutoFitSize(false);

		dropDownButton = (SimpleButton)this.contentItem.getContentSystem().createContentItem(SimpleButton.class);
		dropDownButton.setAutoFitSize(false);
		dropDownButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				if (dropDownList.isVisible())
					dropDownList.setVisible(false);
				else
					dropDownList.setVisible(true);
			}
		});
		
		dropDownList = (ListContainer)this.contentItem.getContentSystem().createContentItem(ListContainer.class);	
		dropDownList.setSpaceToTop(7);
		dropDownList.setVisible(false);
		dropDownList.setHeight(0);
		
		list.addSubItem(selectedItemBox);
		list.addSubItem(dropDownButton);
		list.addSubItem(dropDownList);
		
		render();
	}
	
	/**
	 * Render.
	 */
	public void render(){
			
		dropDownButton.setHeight(list.getHeight());
		dropDownButton.setWidth(list.getHeight());
		dropDownButton.setBorderSize(list.getBorderSize());
		dropDownButton.setBorderColour(list.getBorderColour());
		dropDownButton.setBackgroundColour(list.getBackgroundColour());
		dropDownButton.setText("V");
		dropDownButton.setLocalLocation(list.getWidth()/2-list.getHeight()/2, 0);	
		
		selectedItemBox.setHeight(list.getHeight());
		selectedItemBox.setWidth(list.getWidth()-list.getHeight());
		selectedItemBox.setBorderSize(list.getBorderSize());
		selectedItemBox.setBorderColour(list.getBorderColour());
		selectedItemBox.setBackgroundColour(list.getBackgroundColour());
		selectedItemBox.setLocalLocation(0-list.getHeight()/2, 0);
		if (list.getSelectedItem()==null)
			selectedItemBox.setText("");
		else{
			if(list.getSelectedItem().getImageResource() != null){
				selectedItemBox.removeAllImages();
				selectedItemBox.drawImage(list.getSelectedItem().getImageResource());
			}
			else
				selectedItemBox.setText(list.getSelectedItem().getTitle());
		}
		
		dropDownList.setItemHeight(list.getItemHeight());
		dropDownList.setWidth(list.getWidth());
		dropDownList.setBorderSize(list.getBorderSize());
		dropDownList.setBorderColour(list.getBorderColour());
		dropDownList.setBackgroundColour(list.getBackgroundColour());
		dropDownList.setLocalLocation(-dropDownList.getWidth()/2, -dropDownList.getHeight()-list.getHeight()/2+list.getBorderSize());
		dropDownList.setLineSpace(list.getLineSpace());
	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#setLineSpace(int)
	 */
	@Override
	public void setLineSpace(int lineSpace) {
		render();	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#addListItem(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem)
	 */
	@Override
	public void addListItem(final DropDownListItem item) {
		SimpleButton button = (SimpleButton)item.getItemButton();
		button.setBorderSize(0);
		button.setBackgroundColour(list.getBackgroundColour());
		button.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				list.setSelectedItem(item);					
				render();
				dropDownList.setVisible(false);
			}
		});
		dropDownList.addSubItem(button);
		render();		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#removeItem(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem)
	 */
	@Override
	public void removeItem(DropDownListItem item) {
		dropDownList.removeSubItem(item.getItemButton());
		render();		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#addDropDownListListener(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener)
	 */
	public void addDropDownListListener(DropDownListListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#removeDropDownListListeners()
	 */
	@Override
	public void removeDropDownListListeners(){
		listeners.clear();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#setSelectedItem(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem)
	 */
	@Override
	public void setSelectedItem(DropDownListItem selectedItem){
		render();
		for(DropDownListListener listener: listeners)
			listener.itemSelected(selectedItem);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IDropDownListImplementation#removeDropDownListListener(synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener)
	 */
	@Override
	public void removeDropDownListListener(DropDownListListener listener) {
		listeners.remove(listener);
	}

}
