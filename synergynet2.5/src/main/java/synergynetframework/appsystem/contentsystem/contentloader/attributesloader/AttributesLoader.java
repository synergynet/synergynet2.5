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

package synergynetframework.appsystem.contentsystem.contentloader.attributesloader;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

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

/**
 * The Class AttributesLoader.
 */
public abstract class AttributesLoader {

	/** The attribute node map. */
	protected NamedNodeMap attributeNodeMap;

	/** The list. */
	protected NodeList list;

	/** The root attribute node map. */
	protected NamedNodeMap rootAttributeNodeMap;

	/**
	 * Gets the attri notes.
	 *
	 * @param filePath
	 *            the file path
	 * @param schemaName
	 *            the schema name
	 * @return the attri notes
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 */
	protected void getAttriNotes(String filePath, final String schemaName)
			throws ParserConfigurationException, SAXException, IOException,
			XPathExpressionException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
				"http://www.w3.org/2001/XMLSchema");
		factory.setAttribute(
				"http://java.sun.com/xml/jaxp/properties/schemaSource",
				DataResources.getResource(
						"data/contentitems/schemas/" + schemaName + ".xsd")
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
		
		Document document = builder.parse(DataResources
				.getResourceAsStream(filePath));
		NamespaceContext ctx = new NamespaceContext() {
			public String getNamespaceURI(String prefix) {
				if (prefix.equals("tns")) {
					return "http://tel.dur.ac.uk/xml/schemas/" + schemaName;
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

		// import default item attributes
		Node rootNode = (Node) path.evaluate("/tns:contentitems", document,
				XPathConstants.NODE);
		rootAttributeNodeMap = rootNode.getAttributes();
		list = (NodeList) path.evaluate("/tns:contentitems/tns:items/tns:item",
				document, XPathConstants.NODESET);

	}

	/**
	 * Load attributes.
	 *
	 * @param items
	 *            the items
	 * @param filePath
	 *            the file path
	 */
	public abstract void loadAttributes(Map<String, Map<String, String>> items,
			String filePath);
}
