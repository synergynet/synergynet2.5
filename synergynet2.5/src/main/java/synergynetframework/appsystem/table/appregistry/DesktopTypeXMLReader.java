/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.table.appregistry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import synergynetframework.appsystem.Resources;

/**
 * Loads the application list from the xml configuration.
 */
public class DesktopTypeXMLReader {

	/** The splash screen enabled. */
	public static boolean splashScreenEnabled = true;

	/** The splash screen resource. */
	public static String splashScreenResource = "";

	/** The Constant TABLE_MODE_CLIENT. */
	public static final String TABLE_MODE_CLIENT = "client";

	/** The Constant TABLE_MODE_CONTROLLER. */
	public static final String TABLE_MODE_CONTROLLER = "controller";

	/** The Constant TABLE_MODE_PROJECTOR. */
	public static final String TABLE_MODE_PROJECTOR = "projector";

	/** The table mode. */
	public static String tableMode = TABLE_MODE_CLIENT;

	/**
	 * Load from configuration.
	 *
	 * @param configXMLInputStream
	 *            the config xml input stream
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static void loadFromConfiguration(InputStream configXMLInputStream)
			throws SAXException, IOException, ParserConfigurationException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaSource",
				Resources
						.getResource("appsetup/schemas/tableconfiguration.xsd")
						.toString());
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(new ErrorHandler() {
			public void error(SAXParseException exception) throws SAXException {
				System.out.println("Error: " + exception.getMessage());
			}
			
			public void fatalError(SAXParseException exception)
					throws SAXException {
				System.out.println("Fatal error: " + exception.getMessage());
			}
			
			public void warning(SAXParseException exception)
					throws SAXException {
				System.out.println("Warning: " + exception.getMessage());
			}
		});
		
		Document document = builder.parse(configXMLInputStream);
		
		NamespaceContext ctx = new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if (prefix.equals("tns")) {
					return "http://tel.dur.ac.uk/xml/schemas/tableconfiguration";
				}
				return null;
			}
			
			public String getPrefix(String uri) {
				return null;
			}
			
			public Iterator<?> getPrefixes(String val) {
				return null;
			}
		};
		
		XPathFactory pathFactory = XPathFactory.newInstance();
		XPath path = pathFactory.newXPath();
		path.setNamespaceContext(ctx);
		
		try {
			splashScreenEnabled = Boolean.parseBoolean(path
					.evaluate(
							"/tns:config/tns:options/tns:splashScreenEnabled",
							document).toString());
			splashScreenResource = path.evaluate(
					"/tns:config/tns:options/tns:splashScreenResource",
					document);
			tableMode = path.evaluate("/tns:config/tns:options/tns:mode",
					document);
			if ((tableMode == null) || (tableMode.length() == 0)) {
				tableMode = "client";
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}
	
}
