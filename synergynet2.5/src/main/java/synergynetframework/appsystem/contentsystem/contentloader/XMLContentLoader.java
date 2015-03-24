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

package synergynetframework.appsystem.contentsystem.contentloader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import data.DataResources;


import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.attributesloader.ApparenceAttributeLoader;
import synergynetframework.appsystem.contentsystem.contentloader.attributesloader.AttributesLoader;
import synergynetframework.appsystem.contentsystem.contentloader.attributesloader.DefaultApparenceAttributeLoader;
import synergynetframework.appsystem.contentsystem.contentloader.attributesloader.GestureAttributeLoader;
import synergynetframework.appsystem.contentsystem.contentloader.attributesloader.LocationAttributeLoader;
import synergynetframework.appsystem.contentsystem.contentloader.attributesrender.AttributeRender;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.contentloader.contentitemcreator.ContentItemCreator;
import synergynetframework.appsystem.contentsystem.items.ContentItem;


/**
 * The Class XMLContentLoader.
 */
public class XMLContentLoader implements IContentLoader {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(XMLContentLoader.class.getName());	
	//get the list of ContentItems and the related attributes 
	/** The items. */
	Map<String, Map<String, String>> items = new HashMap <String, Map<String, String>>();
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.contentloader.IContentLoader#loadContent(java.lang.String, synergynetframework.appsystem.contentsystem.ContentSystem)
	 */
	public  Set<ContentItem> loadContent(String xmlPath, ContentSystem contentsys){
		try {
			return loadXMLContent(xmlPath, contentsys);
		} catch (XPathExpressionException e) {
			log.severe(e.toString());
		} catch (ParserConfigurationException e) {
			log.severe(e.toString());
		} catch (SAXException e) {
			log.severe(e.toString());
		} catch (IOException e) {
			log.severe(e.toString());
		}
		return null;
	}
	
	/**
	 * Load xml content.
	 *
	 * @param xmlPath the xml path
	 * @param contentsys the contentsys
	 * @return the sets the
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws XPathExpressionException the x path expression exception
	 */
	private Set<ContentItem> loadXMLContent(String xmlPath, ContentSystem contentsys) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", DataResources.getResource("data/contentitems/schemas/contentitems_schema.xsd").toString());
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler( new ErrorHandler(){
			public void error(SAXParseException exception) throws SAXException { System.out.println("Error: " + exception.getMessage()); }
			public void fatalError(SAXParseException exception) throws SAXException { System.out.println("Fatal error: " + exception.getMessage()); }
			public void warning(SAXParseException exception) throws SAXException { System.out.println("Warning: " + exception.getMessage()); }
		});        
		Document document = builder.parse(DataResources.getResourceAsStream(xmlPath)); 
		NamespaceContext ctx = new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if(prefix.equals("tns")) return "http://tel.dur.ac.uk/xml/schemas/contentitems_schema";
				return null;
			}

			public Iterator<?> getPrefixes(String val) { return null; }           
			public String getPrefix(String uri) { return null; }
		}; 
		XPathFactory pathFactory = XPathFactory.newInstance();        
		XPath path = pathFactory.newXPath();
		path.setNamespaceContext(ctx);
		
		Node rootNode = (Node)path.evaluate("/tns:contentitems", document, XPathConstants.NODE);
		NamedNodeMap attrs = rootNode.getAttributes();
		NodeList list = (NodeList) path.evaluate("/tns:contentitems/tns:items/tns:item", document, XPathConstants.NODESET);
		
		//load attributes
		this.loadAttributes(attrs, list);	
		log.info("Attributes of the content items are loaded from XML file.");
		
		//create contentItems
		Map<ContentItem, Map<String, String>> contentItems = ContentItemCreator.create(items, contentsys);
		log.info("content items are loaded from XML file.");
		
		//render contentItems
		AttributeRender.render(contentItems, contentsys);
		log.info("Attributes of the content items are rendered.");
		
		return (Set<ContentItem>)contentItems.keySet();
	}
	
	/**
	 * Gets the item attributes.
	 *
	 * @param attributeNodeMap the attribute node map
	 * @return the item attributes
	 */
	private Map<String, String> getItemAttributes(NamedNodeMap attributeNodeMap){
		
		Map<String, String> itemAttributes = new HashMap <String, String>();
		
		Node attrNode;
		for (int i=0; i<attributeNodeMap.getLength(); i++)
		{
			attrNode = attributeNodeMap.item(i);
			if (!attrNode.getNodeValue().trim().isEmpty())
				itemAttributes.put(attrNode.getNodeName(), attrNode.getNodeValue());
		}		
		
		return itemAttributes;
	}
	
	/**
	 * Load attributes.
	 *
	 * @param attrs the attrs
	 * @param list the list
	 */
	private void loadAttributes(NamedNodeMap attrs, NodeList list){
		//get Attributes Files 
		String defaultAppearanceFilePath="";
		String appearanceFilePath="";
		String locationFilePath="";	
		String gestureFilePath="";	
		
		//get default appearance file
		Node attrNode = attrs.getNamedItem(AttributeConstants.DEFAULT_APPEARANCE_FILE_PATH);		
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty()){
			defaultAppearanceFilePath = attrNode.getNodeValue().trim();	
		}	
		//get appearance file
		attrNode = attrs.getNamedItem(AttributeConstants.APPEARANCE_FILE_PATH);		
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty()){
			appearanceFilePath = attrNode.getNodeValue().trim();				
		}	
		//get location file
		attrNode = attrs.getNamedItem(AttributeConstants.LOCATION_FILE_PATH);		
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty()){
			locationFilePath = attrNode.getNodeValue().trim();				
		}
		//get gesture file
		attrNode = attrs.getNamedItem(AttributeConstants.GESTURE_FILE_PATH);		
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty()){
			gestureFilePath = attrNode.getNodeValue().trim();				
		}
		
		for(int i = 0; i < list.getLength(); i++) {					
			NodeList t = list.item(i).getChildNodes();
			for(int j = 0; j < t.getLength(); j++) {
				Map<String, String> itemAttributes = getItemAttributes(list.item(i).getAttributes());
				items.put(itemAttributes.get(AttributeConstants.ITEM_ID), itemAttributes);
			}
		}
		
		if (!defaultAppearanceFilePath.equals("")){
			AttributesLoader defaultAppearanceAttributeLoader = new DefaultApparenceAttributeLoader ();
			defaultAppearanceAttributeLoader.loadAttributes(items, defaultAppearanceFilePath);
		}
		if (!appearanceFilePath.equals("")){
			AttributesLoader appearanceAttributeLoader = new ApparenceAttributeLoader ();
			appearanceAttributeLoader.loadAttributes(items, appearanceFilePath);
		}
		if (!locationFilePath.equals("")){
			AttributesLoader locationAttributeLoader = new LocationAttributeLoader ();
			locationAttributeLoader.loadAttributes(items, locationFilePath);
		}
		if (!gestureFilePath.equals("")){
			AttributesLoader guestureAttributeLoader = new GestureAttributeLoader ();
			guestureAttributeLoader.loadAttributes(items, gestureFilePath);
		}
	}
	
}
