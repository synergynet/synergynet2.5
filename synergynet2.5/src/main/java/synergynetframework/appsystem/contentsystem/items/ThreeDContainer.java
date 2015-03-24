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
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IThreeDContainerImplementation;


/**
 * The Class ThreeDContainer.
 */
public class ThreeDContainer extends ThreeDContentItem {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2782161111250661239L;
	
	/** The sub content items. */
	private List<ContentItem> subContentItems = new ArrayList<ContentItem>();
	
	/**
	 * Instantiates a new three d container.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public ThreeDContainer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/**
	 * Adds the sub item.
	 *
	 * @param contentItem the content item
	 */
	public void addSubItem(ThreeDContentItem contentItem){
		
		if (!subContentItems.contains(contentItem)){
			subContentItems.add(contentItem);
			contentItem.parent = this;
			((IThreeDContainerImplementation)this.contentItemImplementation).addSubItem(contentItem);	
		
		}
	}
	
	/**
	 * Contains.
	 *
	 * @param contentItem the content item
	 * @return true, if successful
	 */
	public boolean contains(ContentItem contentItem){
	    return subContentItems.contains(contentItem);
	}
	    
	/**
	 * Removes the sub item.
	 *
	 * @param contentItem the content item
	 */
	public void removeSubItem(ThreeDContentItem contentItem){
		if (subContentItems.contains(contentItem)){
			this.subContentItems.remove(contentItem);
			contentItem.parent = null;
			((IThreeDContainerImplementation)this.contentItemImplementation).removeSubItem(contentItem);
			contentSystem.removeContentItem(contentItem);
			
			
		}
	}
	
	/**
	 * Gets the all items include system items.
	 *
	 * @return the all items include system items
	 */
	public List<ContentItem> getAllItemsIncludeSystemItems(){
	    return this.subContentItems;
	}
		
}