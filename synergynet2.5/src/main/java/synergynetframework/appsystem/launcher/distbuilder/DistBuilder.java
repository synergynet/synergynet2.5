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

package synergynetframework.appsystem.launcher.distbuilder;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import synergynetframework.utils.crypto.CryptoUtils;

/**
 * The Class DistBuilder.
 */
public class DistBuilder extends Task {
	
	/** The atts. */
	private static AttributesImpl atts;

	/** The hd. */
	private static TransformerHandler hd;

	/** The out. */
	static PrintWriter out;

	/**
	 * Process directory.
	 *
	 * @param root
	 *            the root
	 * @param d
	 *            the d
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws SAXException
	 *             the SAX exception
	 */
	public static void processDirectory(File root, File d) throws IOException,
			NoSuchAlgorithmException, SAXException {
		int charsToRemove = root.getCanonicalPath().length();
		String dir = d.getCanonicalPath().substring(charsToRemove);
		if (dir.length() > 0) {
			writeDirEntry(dir);
		} else {
			writeDirEntry("/");
		}
		
		File[] directories = d.listFiles(new FileFilter() {
			public boolean accept(File t) {
				return t.isDirectory();
			}
		});
		
		File[] files = d.listFiles(new FileFilter() {
			
			public boolean accept(File t) {
				return t.isFile();
			}
			
		});
		for (File x : files) {
			writeFileEntry(x.getCanonicalPath().substring(charsToRemove),
					CryptoUtils.md5(x));
		}
		
		for (File x : directories) {
			processDirectory(root, x);
		}
	}

	/**
	 * Write dir entry.
	 *
	 * @param dir
	 *            the dir
	 * @throws SAXException
	 *             the SAX exception
	 */
	private static void writeDirEntry(String dir) throws SAXException {
		atts.clear();
		atts.addAttribute("", "", "path", "CDATA", dir);
		hd.startElement("", "", "directory", atts);
		hd.endElement("", "", "directory");
	}
	
	/**
	 * Write file entry.
	 *
	 * @param path
	 *            the path
	 * @param md5
	 *            the md5
	 * @throws SAXException
	 *             the SAX exception
	 */
	private static void writeFileEntry(String path, String md5)
			throws SAXException {
		atts.clear();
		atts.addAttribute("", "", "path", "CDATA", path);
		atts.addAttribute("", "", "md5", "CDATA", md5);
		hd.startElement("", "", "file", atts);
		// hd.characters(info.toCharArray(),0,info.length());
		hd.endElement("", "", "file");
	}

	/** The outfile. */
	private File outfile;
	
	/** The source dir. */
	private File sourceDir;
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	public void execute() throws BuildException {
		System.out.println(sourceDir.getAbsolutePath());
		System.out.println(outfile.getAbsolutePath());
		
		try {
			out = new PrintWriter(outfile);
			StreamResult streamResult = new StreamResult(out);
			SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
					.newInstance();
			hd = tf.newTransformerHandler();
			Transformer serializer = hd.getTransformer();
			serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			hd.setResult(streamResult);
			hd.startDocument();
			atts = new AttributesImpl();
			
			hd.startElement("", "", "distribution", atts);
			
			if (!sourceDir.isDirectory()) {
				throw new Exception("Argument is not a directory.");
			}
			
			processDirectory(sourceDir, sourceDir);

			hd.endElement("", "", "distribution");
			hd.endDocument();

			out.close();
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			throw new BuildException(e);
		} catch (SAXException e) {
			// e.printStackTrace();
			throw new BuildException(e);
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			throw new BuildException(e);
		} catch (IOException e) {
			// e.printStackTrace();
			throw new BuildException(e);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new BuildException(e);
		}
		
	}
	
	/**
	 * Sets the outfile.
	 *
	 * @param outfile
	 *            the new outfile
	 */
	public void setOutfile(File outfile) {
		this.outfile = outfile;
	}
	
	/**
	 * Sets the sourcedir.
	 *
	 * @param sourceDir
	 *            the new sourcedir
	 */
	public void setSourcedir(File sourceDir) {
		this.sourceDir = sourceDir;
	}
	
}
