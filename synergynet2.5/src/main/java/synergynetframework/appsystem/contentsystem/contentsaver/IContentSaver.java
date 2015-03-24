/*
 * Copyright (c) 2009 University of Durham, England
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


package synergynetframework.appsystem.contentsystem.contentsaver;

import java.util.Map;


/**
 * The Interface IContentSaver.
 */
public interface IContentSaver {
	
	/**
	 * Save.
	 */
	public void save();
	
	/**
	 * Sets the file.
	 *
	 * @param fileName the new file
	 */
	public void setFile(String fileName);
	
	/**
	 * Adds the common attribute.
	 *
	 * @param attributeName the attribute name
	 * @param attributeValue the attribute value
	 */
	public void addCommonAttribute(String attributeName, String attributeValue);
	
	/**
	 * Adds the items.
	 *
	 * @param items the items
	 */
	public void addItems(Map<String, Map<String, String>> items);
	
	/**
	 * Adds the item.
	 *
	 * @param itemID the item id
	 * @param attributes the attributes
	 */
	public void addItem(String itemID, Map<String, String> attributes);
	
	/**
	 * Adds the sub saver.
	 *
	 * @param attributeSaver the attribute saver
	 */
	public void addSubSaver(IContentSaver attributeSaver);
	
}
