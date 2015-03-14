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

package synergynetframework.appsystem.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import synergynetframework.appsystem.Resources;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;

public class ServiceManager {
	private static final Logger log = Logger.getLogger(ServiceManager.class.getName());
	private static ServiceManager instance;

	protected Map<String, SynergyNetService> services = new HashMap<String,SynergyNetService>();

	public static ServiceManager getInstance() {
		synchronized(ServiceManager.class) {
			if(instance == null) {
				instance = new ServiceManager();
			}
			return instance;
		}
	}

	private ServiceManager() {
		log.info("ServiceManager created. Adding shutdown hook to runtime.");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown();
			}
		});
	}
	
	public SynergyNetService get(Class<? extends SynergyNetService> theClass) throws CouldNotStartServiceException {
		String classname = theClass.getName();
		if(services.get(classname) != null) {
			return services.get(classname);
		}
		log.info("Attempting to register " + classname);
		try {
			log.info("Creating an instance of " + classname);
			SynergyNetService s = (SynergyNetService)Class.forName(classname).newInstance();			
			services.put(classname, s);
			s.start();
			return s;
		} catch (InstantiationException e) {
			log.warning(e.toString());
		} catch (IllegalAccessException e) {
			log.warning(e.toString());
		} catch (ClassNotFoundException e) {
			log.warning(e.toString());
		}
		return null;
	}

	public void loadFromConfiguration(InputStream configXMLInputStream) throws SAXException, IOException, ParserConfigurationException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		log.info("Loading servcies information from XML");
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

		try {
			log.info("Finding services in XML document...");
			NodeList list = (NodeList) path.evaluate("/tns:config/tns:services/*", document, XPathConstants.NODESET);
			log.info("Found " + list.getLength() + " services.");
			for(int i = 0; i < list.getLength(); i++) {				
				String classname = list.item(i).getAttributes().getNamedItem("classname").getNodeValue();				
				boolean enabled = Boolean.parseBoolean(list.item(i).getAttributes().getNamedItem("enabled").getNodeValue());
				if(enabled) {        				
					log.info("Service " + classname + " is enabled");
					try {
						registerAndStartup(path, document, classname);
					} catch (XPathExpressionException e) {
						e.printStackTrace();
					}
				}else{
					log.info("Service " + classname + " is NOT enabled.");
				}
			}
		} catch (XPathExpressionException e1) {
			log.warning(e1.toString());
		}

	}

	@SuppressWarnings("unchecked")
	private static void registerAndStartup(XPath path, Document document, String classname) throws XPathExpressionException {

		List<String> dependencies = getDependencies(path, document, classname);
		if(dependencies.size() > 0) {
			log.info("Dependencies exist for " + classname);
			for(String s : dependencies) {
				log.info("Processing dependency " + s);
				registerAndStartup(path, document, s);
			}
			log.info("Dependencies processed.");
		}
		log.info("Starting " + classname);
		Class<?> theClass;
		try {
			theClass = Class.forName(classname);
			SynergyNetService s = ServiceManager.getInstance().get((Class<? extends SynergyNetService>) theClass);
			if(!s.hasStarted()) {
				s.start();
			}
			log.info("Startup complete for " + classname);
		} catch (ClassNotFoundException e) {
			log.warning(e.toString());
		} catch (CouldNotStartServiceException e) {
			log.warning(e.toString());			
		}
	}

	private static List<String> getDependencies(XPath path, Document document, String classname) throws XPathExpressionException {
		List<String> dependencies = new ArrayList<String>();
		String eval = "/tns:config/tns:services/tns:service[@classname=\"" + classname + "\"]/tns:depends/*";
		NodeList nodes = (NodeList) path.evaluate(eval, document, XPathConstants.NODESET);
		for(int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			String dependsOn = n.getAttributes().getNamedItem("classname").getTextContent();
			dependencies.add(dependsOn);
		}
		return dependencies;
	}

	public void shutdown() {
		for(String key : services.keySet()) {			
			SynergyNetService s = services.get(key);
			log.info("Shutting down " + key);
			s.shutdown();
		}
		services.clear();
	}
	
	public void unregister(String classname){
		if(services.containsKey(classname)) services.remove(classname);
	}
	
	public void update() {
		for(SynergyNetService s : services.values()) {
			s.update();
		}
	}
}
