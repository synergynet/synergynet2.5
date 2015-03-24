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

package synergynetframework.appsystem.launcher.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.ScrollPane;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

/**
 * The Class SynergyNetLauncher.
 */
public class SynergyNetLauncher {
	
	/**
	 * The Enum OSName.
	 */
	public static enum OSName {

		/** The linux. */
		LINUX,

		/** The mac. */
		MAC,

		/** The windows. */
		WINDOWS
	}

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SynergyNetLauncher.class
			.getName());
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 * @throws XPathExpressionException
	 *             the x path expression exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static void main(String[] args) throws IOException,
			NoSuchAlgorithmException, URISyntaxException,
			XPathExpressionException, SAXException,
			ParserConfigurationException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		JFrame f = new JFrame("SynergyNet Launcher");
		ControlPanel controlPanel = new ControlPanel();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = f.getContentPane();
		contentPane.setLayout(new BorderLayout());
		ScrollPane scroller = new ScrollPane();
		scroller.add(LogWindow.getInstance());
		contentPane.add(scroller, BorderLayout.CENTER);
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		f.setSize(640, 480);
		f.setVisible(true);

		File downloadTo = new File("."); // relative to pwd
		downloadTo.mkdir();

		String urlBase = "http://smart.dur.ac.uk/~dcs0ah1/synergynetdist";
		URL url = new URL(urlBase + "/dist.xml");
		RetrievalSystem.retrieveDistribution(downloadTo, new URL(urlBase),
				url.openStream());
		log.info("Retrieve distribution from " + url.toString());

		controlPanel.enableLaunch();
	}
	
}
