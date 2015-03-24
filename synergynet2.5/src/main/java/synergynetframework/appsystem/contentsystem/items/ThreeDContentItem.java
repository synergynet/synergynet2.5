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

import java.io.Serializable;

import synergynetframework.appsystem.contentsystem.ContentSystem;


/**
 * The Class ThreeDContentItem.
 */
public class ThreeDContentItem extends ContentItem implements Cloneable, Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3271026777712734168L;
	
	/** The parent. */
	protected ThreeDContainer parent=null;

	
	/**
	 * Instantiates a new three d content item.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public ThreeDContentItem(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.ContentItem#bindImplementationOjbect()
	 */
	public void bindImplementationOjbect(){
		super.bindImplementationOjbect();
		this.enableMultiTouchElementListeners();
	}
	
	/**
	 * Enable multi touch element listeners.
	 */
	public void enableMultiTouchElementListeners(){
	}
	
	/**
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public ThreeDContainer getParent(){
		return parent;
	}	
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.ContentItem#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		ThreeDContentItem clonedItem = (ThreeDContentItem)super.clone();
		clonedItem.parent = null;
		return clonedItem;
		
	}
	
}