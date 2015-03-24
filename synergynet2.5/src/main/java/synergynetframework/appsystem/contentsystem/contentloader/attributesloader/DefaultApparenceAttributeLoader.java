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
import java.util.HashMap;
import java.util.logging.Logger;
import java.io.IOException;

import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * The Class DefaultApparenceAttributeLoader.
 */
public class DefaultApparenceAttributeLoader extends AttributesLoader {
	
	/** The default attributes. */
	Map<String, Map<String, String>> defaultAttributes = new HashMap<String, Map<String, String>>();
	
	/** The item attributes. */
	Map<String, Map<String, String>> itemAttributes;
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DefaultApparenceAttributeLoader.class.getName());	
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.contentloader.attributesloader.AttributesLoader#loadAttributes(java.util.Map, java.lang.String)
	 */
	@Override
	public void loadAttributes(Map<String, Map<String, String>> items,
			String filePath) {
		
		itemAttributes = items;
		
		try {
			this.getAttriNotes(filePath, "contentitems_apparence_schema");
		} catch (XPathExpressionException e) {
			log.severe(e.toString());
		} catch (ParserConfigurationException e) {
			log.severe(e.toString());
		} catch (SAXException e) {
			log.severe(e.toString());
		} catch (IOException e) {
			log.severe(e.toString());
		}
		
		
		for(int i = 0; i < list.getLength(); i++) {	
			attributeNodeMap=list.item(i).getAttributes();			
			this.loadDefaultAttributesSet();	
		}	
		
		this.loadAttributes();
		
	}
	
	/**
	 * Load default attributes set.
	 */
	private void loadDefaultAttributesSet(){
		String contentItemType="";
		String category="";
		Node attrNode;
		
		attrNode = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_CONTENTITEMTYPE);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			contentItemType = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_CONTENTITEMTYPE).getNodeValue().trim();	
		attrNode = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_CATEGORY);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			category = attributeNodeMap.getNamedItem(AttributeConstants.ITEM_CATEGORY).getNodeValue().trim();	
		
		Map<String, String> itemAttrs = new HashMap<String, String>();
		AttributeLoaderUtil.loadItemAttributes(itemAttrs, attributeNodeMap);
		
		this.defaultAttributes.put(contentItemType+" - "+category, itemAttrs);
		
	}
	
	/**
	 * Load attributes.
	 */
	private void loadAttributes(){
		
		String contentItemType="";
		String category="";
		
		for (String key:this.itemAttributes.keySet()){
			contentItemType = itemAttributes.get(key).get(AttributeConstants.ITEM_CONTENTITEMTYPE);
			category = itemAttributes.get(key).get(AttributeConstants.ITEM_CATEGORY);
			
			Map<String, String> defaultAttrs = this.defaultAttributes.get(contentItemType+" - "+category);
			
			Map<String, String> itemAttrs = itemAttributes.get(key);
			
			for (String k: defaultAttrs.keySet()){
				itemAttrs.put(k, defaultAttrs.get(k));
			}
		}
	}
	
}
