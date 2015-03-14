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

package synergynetframework.appsystem.table.appregistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import synergynetframework.appsystem.Resources;

public class ApplicationRegistryXMLReader {
	private static final Logger log = Logger.getLogger(ApplicationRegistryXMLReader.class.getName());


	public static void loadFromConfiguration(InputStream configXMLInputStream, ApplicationRegistry registry) throws SAXException, IOException, ParserConfigurationException, InstantiationException, IllegalAccessException, ClassNotFoundException, XPathExpressionException {
		log.info("Loading Table Configuration XML");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", Resources.getResource("appsetup/schemas/tableconfiguration.xsd").toString());
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler( new ErrorHandler(){
			public void error(SAXParseException exception) throws SAXException { System.out.println("Error: " + exception.getMessage()); }
			public void fatalError(SAXParseException exception) throws SAXException { System.out.println("Fatal error: " + exception.getMessage()); }
			public void warning(SAXParseException exception) throws SAXException { System.out.println("Warning: " + exception.getMessage()); }
		});        

		Document document = builder.parse(configXMLInputStream); 
		NamespaceContext ctx = new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if(prefix.equals("tns")) return "http://tel.dur.ac.uk/xml/schemas/tableconfiguration";
				return null;
			}

			public Iterator<?> getPrefixes(String val) { return null; }           
			public String getPrefix(String uri) { return null; }
		}; 
		XPathFactory pathFactory = XPathFactory.newInstance();        
		XPath path = pathFactory.newXPath();
		path.setNamespaceContext(ctx);

		NodeList list = (NodeList) path.evaluate("/tns:config/tns:applications/tns:application", document, XPathConstants.NODESET);

		for(int i = 0; i < list.getLength(); i++) {
			String appConfigXML = list.item(i).getAttributes().getNamedItem("configpath").getNodeValue();   
			boolean enabled = Boolean.parseBoolean(list.item(i).getAttributes().getNamedItem("enabled").getNodeValue());
			boolean isDefault = Boolean.parseBoolean(list.item(i).getAttributes().getNamedItem("default").getNodeValue()); 
			if(enabled) {
				loadApplicationConfiguration(appConfigXML, registry, isDefault);
			}
		}
	}


	private static void loadApplicationConfiguration(String appConfigXML, ApplicationRegistry registry, boolean isDefault) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		log.info("Loading Application XML configuration from " + appConfigXML);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
		factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", Resources.getResource("appsetup/schemas/synergynetapplication.xsd").toString());
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler( new ErrorHandler(){
			public void error(SAXParseException exception) throws SAXException { System.out.println("Error: " + exception.getMessage()); }
			public void fatalError(SAXParseException exception) throws SAXException { System.out.println("Fatal error: " + exception.getMessage()); }
			public void warning(SAXParseException exception) throws SAXException { System.out.println("Warning: " + exception.getMessage()); }
		});        

		Document document = builder.parse(Resources.getResourceAsStream(appConfigXML)); 
		NamespaceContext ctx = new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if(prefix.equals("ac")) return "http://tel.dur.ac.uk/xml/schemas/synergynetappconfig";
				return null;
			}

			public Iterator<?> getPrefixes(String val) { return null; }           
			public String getPrefix(String uri) { return null; }
		}; 
		XPathFactory pathFactory = XPathFactory.newInstance();        
		XPath path = pathFactory.newXPath();
		path.setNamespaceContext(ctx);
		
		if(DesktopTypeXMLReader.tableMode.equals(DesktopTypeXMLReader.TABLE_MODE_CONTROLLER)) {		
			ApplicationInfo infoController = getControllerApplication(path, document);
			if(infoController == null) {
				ApplicationInfo infoClient = getClientApplication(path, document);
				registry.register(infoClient);
				if(isDefault) {
					registry.setDefault(infoClient.getTheClassName());
				}
			}else{
				registry.register(infoController);
				if(isDefault) {
					registry.setDefault(infoController.getTheClassName());
				}
			}
		} else if(DesktopTypeXMLReader.tableMode.equals(DesktopTypeXMLReader.TABLE_MODE_PROJECTOR)){
			ApplicationInfo infoProjector = getProjectorApplication(path, document);
			registry.register(infoProjector);
			if(isDefault) {
				registry.setDefault(infoProjector.getTheClassName());
			}
		}else {
			ApplicationInfo infoClient = getClientApplication(path, document);
			registry.register(infoClient);
			if(isDefault) {
				registry.setDefault(infoClient.getTheClassName());
			}
		} 
	}
	
	private static ApplicationInfo getControllerApplication(XPath path, Document document) throws XPathExpressionException, DOMException, ClassNotFoundException {
		Node component = (Node) path.evaluate("/ac:application/ac:controllercomponent", document, XPathConstants.NODE);
		if(component == null) return null;
		
		Node reactivatePolicy = (Node) path.evaluate("/ac:application/ac:controllercomponent/ac:reactivatepolicy", document, XPathConstants.NODE);
		String classname = component.getAttributes().getNamedItem("classname").getTextContent();
		Node appInfo = (Node) path.evaluate("/ac:application/ac:info", document, XPathConstants.NODE);
		String appName = appInfo.getAttributes().getNamedItem("name").getTextContent();
		String uuid = appInfo.getAttributes().getNamedItem("uuid").getTextContent();
		boolean showIcon = Boolean.parseBoolean(component.getAttributes().getNamedItem("showicon").getTextContent());
		String versionString = appInfo.getAttributes().getNamedItem("version").getTextContent();
		ApplicationInfo info = new ApplicationInfo(classname, appName, versionString, reactivatePolicy.getTextContent());
		info.setUUID(uuid);
		info.setApplicationType(ApplicationInfo.APPLICATION_TYPE_CONTROLLER);
		if(showIcon) {
			String iconresource = component.getAttributes().getNamedItem("iconresource").getTextContent();
			info.setIconResource(iconresource);
			info.setShowIcon(true);
		}else{
			info.setShowIcon(false);
		}
		return info;
	}


	public static ApplicationInfo getClientApplication(XPath path, Document document) throws XPathExpressionException, DOMException, ClassNotFoundException {
		Node component = (Node) path.evaluate("/ac:application/ac:clientcomponent", document, XPathConstants.NODE);
		if(component == null) return null;
		
		Node reactivatePolicy = (Node) path.evaluate("/ac:application/ac:clientcomponent/ac:reactivatepolicy", document, XPathConstants.NODE);
		String classname = component.getAttributes().getNamedItem("classname").getTextContent();
		Node appInfo = (Node) path.evaluate("/ac:application/ac:info", document, XPathConstants.NODE);
		String appName = appInfo.getAttributes().getNamedItem("name").getTextContent();
		String uuid = appInfo.getAttributes().getNamedItem("uuid").getTextContent();
		boolean showIcon = Boolean.parseBoolean(component.getAttributes().getNamedItem("showicon").getTextContent());
		String versionString = appInfo.getAttributes().getNamedItem("version").getTextContent();
		ApplicationInfo info = new ApplicationInfo(classname, appName, versionString, reactivatePolicy.getTextContent());
		info.setUUID(uuid);
		info.setApplicationType(ApplicationInfo.APPLICATION_TYPE_CLIENT);
		if(showIcon) {
			String iconresource = component.getAttributes().getNamedItem("iconresource").getTextContent();
			info.setIconResource(iconresource);
			info.setShowIcon(true);
		}else{
			info.setShowIcon(false);
		}
		return info;
	}
	
	public static ApplicationInfo getProjectorApplication(XPath path, Document document) throws XPathExpressionException, DOMException, ClassNotFoundException {
		Node component = (Node) path.evaluate("/ac:application/ac:projectorcomponent", document, XPathConstants.NODE);
		if(component == null) return null;
		
		Node reactivatePolicy = (Node) path.evaluate("/ac:application/ac:projectorcomponent/ac:reactivatepolicy", document, XPathConstants.NODE);
		String classname = component.getAttributes().getNamedItem("classname").getTextContent();
		Node appInfo = (Node) path.evaluate("/ac:application/ac:info", document, XPathConstants.NODE);
		String appName = appInfo.getAttributes().getNamedItem("name").getTextContent();
		String uuid = appInfo.getAttributes().getNamedItem("uuid").getTextContent();
		boolean showIcon = Boolean.parseBoolean(component.getAttributes().getNamedItem("showicon").getTextContent());
		String versionString = appInfo.getAttributes().getNamedItem("version").getTextContent();
		ApplicationInfo info = new ApplicationInfo(classname, appName, versionString, reactivatePolicy.getTextContent());
		info.setUUID(uuid);
		info.setApplicationType(ApplicationInfo.APPLICATION_TYPE_PROJECTOR);
		if(showIcon) {
			String iconresource = component.getAttributes().getNamedItem("iconresource").getTextContent();
			info.setIconResource(iconresource);
			info.setShowIcon(true);
		}else{
			info.setShowIcon(false);
		}
		return info;
	}
}
