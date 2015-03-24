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

package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;


/**
 * The Interface IListContainerImplementation.
 */
public interface IListContainerImplementation  extends IWindowImplementation{
	
	/**
	 * Adds the sub item.
	 *
	 * @param index the index
	 * @param item the item
	 */
	public void addSubItem(int index, QuadContentItem item);	
	
	/**
	 * Adds the sub item.
	 *
	 * @param item the item
	 */
	public void addSubItem(QuadContentItem item);
	
	/**
	 * Adds the sub menu.
	 *
	 * @param menuButton the menu button
	 * @param listContainer the list container
	 */
	public void addSubMenu(SimpleButton menuButton, ListContainer listContainer);
	
	/**
	 * Removes the item.
	 *
	 * @param item the item
	 */
	public void removeItem(QuadContentItem item);
	
	/**
	 * Sets the space to top.
	 *
	 * @param spaceToTop the new space to top
	 */
	public void setSpaceToTop(int spaceToTop);
	
	/**
	 * Sets the line space.
	 *
	 * @param lineSpace the new line space
	 */
	public void setLineSpace(int lineSpace);
	
	/**
	 * Sets the space to side.
	 *
	 * @param spaceToSide the new space to side
	 */
	public void setSpaceToSide(int spaceToSide);
	
	/**
	 * Sets the space to bottom.
	 *
	 * @param spaceToBottom the new space to bottom
	 */
	public void setSpaceToBottom(int spaceToBottom);
	
	/**
	 * Update visibility.
	 */
	public void updateVisibility();
	
	/**
	 * Sets the horizontal.
	 *
	 * @param horizontal the new horizontal
	 */
	public void setHorizontal(boolean horizontal);
	
	/**
	 * Sets the auto fit size.
	 *
	 * @param isAutoFitSize the new auto fit size
	 */
	public void setAutoFitSize(boolean isAutoFitSize);
}
