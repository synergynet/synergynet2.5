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

import java.util.Map;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation;


/**
 * The Class JMEListContainer.
 */
public class JMEListContainer extends JMEWindow implements IListContainerImplementation{
	
	/** The list. */
	private ListContainer list;
	
	/**
	 * Instantiates a new JME list container.
	 *
	 * @param contentItem the content item
	 */
	public JMEListContainer(ContentItem contentItem) {
		super(contentItem);		
		list =  ((ListContainer)contentItem);	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#addSubItem(int, synergynetframework.appsystem.contentsystem.items.QuadContentItem)
	 */
	@Override
	public void addSubItem(int index, QuadContentItem item) {
		render();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#addSubItem(synergynetframework.appsystem.contentsystem.items.QuadContentItem)
	 */
	@Override
	public void addSubItem(QuadContentItem item) {
		render();
	}
	

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#addSubMenu(synergynetframework.appsystem.contentsystem.items.SimpleButton, synergynetframework.appsystem.contentsystem.items.ListContainer)
	 */
	@Override
	public void addSubMenu(SimpleButton button, ListContainer listContainer) {
		render();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#removeItem(synergynetframework.appsystem.contentsystem.items.QuadContentItem)
	 */
	@Override
	public void removeItem(QuadContentItem item) {
		render();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#setHorizontal(boolean)
	 */
	@Override
	public void setHorizontal(boolean isHorizontal){
		render();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean AutoFitSize){
		render();
	}
	
	/**
	 * Render.
	 */
	public void render(){
		/*	
		Map<SimpleButton, ListContainer> subMenus = list.getSubMenus();
		int listHeight =list.getSpaceToTop();
		int listWidth = list.getWidth();
		for(QuadContentItem item:list.getListItems()){
			item.setWidth(listWidth - list.getSpaceToSide());		
			listHeight=listHeight+list.getItemHeight();
		}
		listHeight = listHeight+ list.getLineSpace()*(list.getListItems().size()-1) + list.getSpaceToBottom();
		
		((ListContainer)contentItem).setHeight(listHeight);
		
		int itemTopLeftLocation = listHeight - list.getSpaceToTop();
		for (QuadContentItem item: list.getListItems()){	
			item.setHeight(list.getItemHeight());
			item.setLocation(listWidth/2, itemTopLeftLocation - list.getItemHeight()/2);			
			itemTopLeftLocation = itemTopLeftLocation - list.getItemHeight() -list.getLineSpace();
		}	
				
		for (SimpleButton button:subMenus.keySet()){			
			ListContainer subMenu = subMenus.get(button);
			button.setHeight(list.getItemHeight());
			subMenu.setLocation(button.getLocation().getX()+listWidth/2, button.getLocation().getY());
		}
		
		this.backgroundFrame.setLocation(listWidth/2, listHeight/2);
		*/
		if(!list.isHorizontal()){
			Map<SimpleButton, ListContainer> subMenus = list.getSubMenus();
			int listHeight =list.getSpaceToTop();
			int listWidth = list.getWidth();
			for(QuadContentItem item:list.getListItems()){
				item.setWidth(listWidth - list.getSpaceToSide());		
				listHeight=listHeight+list.getItemHeight();
			}
			if(list.isAutoFitSize())	
				listHeight = listHeight+ list.getLineSpace()*(list.getListItems().size()-1) + list.getSpaceToBottom();
			else
				listHeight = list.getHeight();
			
			((ListContainer)contentItem).setHeight(listHeight);
			
			int itemTopLeftLocation = listHeight - list.getSpaceToTop();
			for (QuadContentItem item: list.getListItems()){	
				item.setHeight(list.getItemHeight());
				item.setLocalLocation(listWidth/2, itemTopLeftLocation - list.getItemHeight()/2);			
				itemTopLeftLocation = itemTopLeftLocation - list.getItemHeight() -list.getLineSpace();
			}	
					
			for (SimpleButton button:subMenus.keySet()){			
				ListContainer subMenu = subMenus.get(button);
				button.setHeight(list.getItemHeight());
				subMenu.setLocalLocation(button.getLocalLocation().getX()+listWidth/2, button.getLocalLocation().getY()-subMenu.getHeight()+subMenu.getSpaceToTop());
			}
			
			this.backgroundFrame.setLocalLocation(listWidth/2, listHeight/2);
		}else{
			Map<SimpleButton, ListContainer> subMenus = list.getSubMenus();
			int listWidth =list.getSpaceToSide();
			int listHeight = list.getHeight();
			for(QuadContentItem item:list.getListItems()){
				item.setWidth(listHeight - list.getSpaceToTop());		
				listWidth=listWidth+list.getItemWidth();
			}
			if(list.isAutoFitSize())					
				listWidth = listWidth+ list.getLineSpace()*(list.getListItems().size()-1) + list.getSpaceToSide();
			else
				listWidth = list.getWidth();
				
			((ListContainer)contentItem).setWidth(listWidth);

			int itemTopLeftLocation = list.getSpaceToSide();
			for (QuadContentItem item: list.getListItems()){	
				item.setWidth(list.getItemWidth());
				item.setHeight(list.getItemHeight());
				item.setLocalLocation(itemTopLeftLocation + list.getItemWidth()/2, listHeight/2);			
				itemTopLeftLocation = itemTopLeftLocation + list.getItemWidth() +list.getLineSpace();
			}
			
			for (SimpleButton button:subMenus.keySet()){			
				ListContainer subMenu = subMenus.get(button);
				button.setWidth(list.getItemWidth());
				subMenu.setLocalLocation(button.getLocalLocation().getX(), button.getLocalLocation().getY()+listHeight/2);
			}
			
			this.backgroundFrame.setLocalLocation(listWidth/2, listHeight/2);	
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#setLineSpace(int)
	 */
	@Override
	public void setLineSpace(int lineSpace) {
		render();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#setSpaceToBottom(int)
	 */
	@Override
	public void setSpaceToBottom(int spaceToBottom) {
		render();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#setSpaceToSide(int)
	 */
	@Override
	public void setSpaceToSide(int spaceToSide) {
		render();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#setSpaceToTop(int)
	 */
	@Override
	public void setSpaceToTop(int spaceToTop) {
		render();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IListContainerImplementation#updateVisibility()
	 */
	@Override
	public void updateVisibility() {
		render();
		
	}


}
