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

package synergynetframework.appsystem.launcher.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 * The Class RetrievalSystem.
 */
public class RetrievalSystem {

	/**
	 * Retrieve distribution.
	 *
	 * @param targetDirectory the target directory
	 * @param baseURL the base url
	 * @param configXMLInputStream the config xml input stream
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws XPathExpressionException the x path expression exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	public static void retrieveDistribution(File targetDirectory, URL baseURL, InputStream configXMLInputStream) throws SAXException, IOException, ParserConfigurationException, InstantiationException, IllegalAccessException, ClassNotFoundException, XPathExpressionException, NoSuchAlgorithmException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler( new ErrorHandler(){
			public void error(SAXParseException exception) throws SAXException { System.out.println("Error: " + exception.getMessage()); }
			public void fatalError(SAXParseException exception) throws SAXException { System.out.println("Fatal error: " + exception.getMessage()); }
			public void warning(SAXParseException exception) throws SAXException { System.out.println("Warning: " + exception.getMessage()); }
		});        

		Document document = builder.parse(configXMLInputStream); 
		XPathFactory pathFactory = XPathFactory.newInstance();        
		XPath path = pathFactory.newXPath();

		NodeList list = (NodeList) path.evaluate("/distribution/directory", document, XPathConstants.NODESET);
		for(int i = 0; i < list.getLength(); i++) {				
			String s = list.item(i).getAttributes().getNamedItem("path").getNodeValue();			
			processDirectory(targetDirectory, s);
		}
		
		list = (NodeList) path.evaluate("/distribution/file", document, XPathConstants.NODESET);
		for(int i = 0; i < list.getLength(); i++) {				
			String s = list.item(i).getAttributes().getNamedItem("path").getNodeValue();
			String md5 = list.item(i).getAttributes().getNamedItem("md5").getNodeValue();
			processFile(targetDirectory, baseURL, s, md5);
		}
		
		LogWindow.getInstance().log("Done.");
	}

	/**
	 * Process file.
	 *
	 * @param targetDirectory the target directory
	 * @param baseURL the base url
	 * @param s the s
	 * @param md5 the md5
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	private static void processFile(File targetDirectory, URL baseURL, String s, String md5) throws IOException, NoSuchAlgorithmException {
		s = s.substring(1); // trim leading slash
		File localFile = new File(targetDirectory, s);
		if(localFile.exists()) {
			String localMD5 = getMD5(localFile);
			if(localMD5.equals(md5)) {
				LogWindow.getInstance().log(localFile.getCanonicalPath() + " is at the latest version.");
			}else{
				LogWindow.getInstance().log(localFile.getCanonicalPath() + " is out of date. Downloading latest...");
				retrieve(new URL(baseURL.toString() + "/" + s), localFile);
			}
		}else{
			LogWindow.getInstance().log(localFile.getCanonicalPath() + " does not exist. Downloading...");
			retrieve(new URL(baseURL.toString() + "/" + s), localFile);
		}
	}

	/**
	 * Retrieve.
	 *
	 * @param url the url
	 * @param localFile the local file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void retrieve(URL url, File localFile) throws IOException {
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(localFile);
		int read;
		byte[] buf = new byte[2048];
		while((read = is.read(buf)) != -1) {
			os.write(buf, 0, read);
		}
		is.close();
		os.close();
	}

	/**
	 * Process directory.
	 *
	 * @param targetDirectory the target directory
	 * @param s the s
	 */
	private static void processDirectory(File targetDirectory, String s) {
		s = s.substring(1); // trim leading slash 
		if(s.length()>0) {
			File f = new File(targetDirectory, s);
			f.mkdir();
		}
	}

	/**
	 * Gets the m d5.
	 *
	 * @param f the f
	 * @return the m d5
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getMD5(File f) throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		InputStream is = new FileInputStream(f);

		is = new DigestInputStream(is, md);
		byte[] buf = new byte[2048];
		@SuppressWarnings("unused")
		int read;
		while((read = is.read(buf))!= -1) {
			//
		}
		
		is.close();

		byte[] digest = md.digest();
		return new BigInteger(1,digest).toString(16);
	}

	
}
