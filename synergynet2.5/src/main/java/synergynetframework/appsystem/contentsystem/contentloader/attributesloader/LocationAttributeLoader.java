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

package synergynetframework.appsystem.contentsystem.contentloader.attributesloader;

import java.util.Map;
import java.util.logging.Logger;
import java.io.IOException;

import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;


/**
 * The Class LocationAttributeLoader.
 */
public class LocationAttributeLoader extends AttributesLoader {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(LocationAttributeLoader.class.getName());	
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.contentloader.attributesloader.AttributesLoader#loadAttributes(java.util.Map, java.lang.String)
	 */
	public void loadAttributes(Map<String, Map<String, String>> items, String filePath){
		
		try {
			this.getAttriNotes(filePath, "contentitems_location_schema");
		}catch (XPathExpressionException e) {
			log.severe(e.toString());
		} catch (ParserConfigurationException e) {
			log.severe(e.toString());
		} catch (SAXException e) {
			log.severe(e.toString());
		} catch (IOException e) {
			log.severe(e.toString());
		}
				
		String defaultLocationX="";
		String defaultLocationY="";
		String defaultLocationZ="";
		String defaultSortOrder="";
		
		if (rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_LOCATION_X)!=null){
			defaultLocationX = rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_LOCATION_X).getNodeValue().trim();
		}
		if (rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_LOCATION_Y)!=null){
			defaultLocationY = rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_LOCATION_Y).getNodeValue().trim();
		}
		if (rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_LOCATION_Z)!=null){
			defaultLocationZ = rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_LOCATION_Z).getNodeValue().trim();
		}
		if (rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_SORTORDER)!=null){
			defaultLocationZ = rootAttributeNodeMap.getNamedItem(AttributeConstants.DEFAULT_SORTORDER).getNodeValue().trim();
		}
		
		for (String key:items.keySet()){
			if (!defaultLocationX.equals(""))
				items.get(key).put(AttributeConstants.ITEM_LOCATION_X, defaultLocationX);
			if (!defaultLocationY.equals(""))
				items.get(key).put(AttributeConstants.ITEM_LOCATION_Y, defaultLocationY);
			if (!defaultLocationZ.equals(""))
				items.get(key).put(AttributeConstants.ITEM_LOCATION_Z, defaultLocationZ);	
			if (!defaultSortOrder.equals(""))
				items.get(key).put(AttributeConstants.ITEM_SORTORDER, defaultLocationZ);
		}
			
		Map<String, String> itemAttributes;
		
		for(int i = 0; i < list.getLength(); i++) {	
			
			attributeNodeMap=list.item(i).getAttributes();	
			itemAttributes = items.get(attributeNodeMap.getNamedItem(AttributeConstants.ITEM_ID).getNodeValue().trim());	
			
			String locationX="";
			if (!defaultLocationX.equals("")){
				locationX = defaultLocationX;
			}
			if (attributeNodeMap.getNamedItem(AttributeConstants.ITEM_LOCATION_X)!=null){
				locationX = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_LOCATION_X).getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_LOCATION_X, locationX);
			}
			
			String locationY="";
			if (!defaultLocationY.equals("")){
				locationY = defaultLocationY;
			}
			if (attributeNodeMap.getNamedItem(AttributeConstants.ITEM_LOCATION_Y)!=null){
				locationY = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_LOCATION_Y).getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_LOCATION_Y, locationY);
			}
			
			String locationZ="";
			if (!defaultLocationZ.equals("")){
				locationZ = defaultLocationZ;
			}		
			if (attributeNodeMap.getNamedItem(AttributeConstants.ITEM_LOCATION_Z)!=null){
				locationZ = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_LOCATION_Z).getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_LOCATION_Z, locationZ);
			}		
			
			String sortOrder="";
			if (!defaultSortOrder.equals("")){
				sortOrder = defaultSortOrder;
			}		
			if (attributeNodeMap.getNamedItem(AttributeConstants.ITEM_SORTORDER)!=null){
				sortOrder = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_SORTORDER).getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_SORTORDER, sortOrder);
			}		
		}			
	}
	

}
