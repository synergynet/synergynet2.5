/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.PDFViewer;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IPDFViewerImplementation;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * The Class JMEPDFViewer.
 */
public class JMEPDFViewer extends JMEFrame implements IPDFViewerImplementation {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEPDFViewer.class
			.getName());

	/** The height. */
	protected int height = 350;
	
	/** The item. */
	protected PDFViewer item;

	/** The page. */
	protected PDFPage page;

	/** The pdffile. */
	protected PDFFile pdffile;

	/** The width. */
	protected int width = 200;

	/**
	 * Instantiates a new JMEPDF viewer.
	 *
	 * @param item
	 *            the item
	 */
	public JMEPDFViewer(ContentItem item) {
		super(item);
		this.item = (PDFViewer) item;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IDocViewerImplementation#changePage()
	 */
	@Override
	public void changePage() {
		gotoPage();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#draw()
	 */
	@Override
	protected void draw() {
		super.draw();
		if (page != null) {
			Rectangle rect = new Rectangle(0, 0, (int) page.getBBox()
					.getWidth(), (int) page.getBBox().getHeight());
			
			// generate the image
			Image img = page.getImage(rect.width, rect.height, // width & height
					rect, // clip rect
					null, // null for the ImageObserver
					true, // fill background with white
					true // block until drawing is done
					);

			gfx.drawImage(img, 0, 0, item.getWidth(), item.getHeight(), rect.x,
					rect.y, rect.width, rect.height, null);

			this.graphicsImageQuad.updateGraphics();
		}

	}
	
	/**
	 * Gets the PDF file.
	 *
	 * @param f
	 *            the f
	 * @return the PDF file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private PDFFile getPDFFile(File f) throws IOException {

		String address = "file:/" + f.getAbsolutePath();
		address = address.replaceAll(" ", "%20");

		RandomAccessFile raf = new RandomAccessFile(f, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0,
				channel.size());
		raf.close();
		return new PDFFile(buf);
	}

	/**
	 * Gets the PDF file.
	 *
	 * @param url
	 *            the url
	 * @return the PDF file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private PDFFile getPDFFile(URL url) throws IOException {
		String address = url.toString();
		
		address = address.replaceAll("file:/", "");
		address = address.replaceAll("%20", " ");

		File f = new File(address);
		
		return getPDFFile(f);
	}

	/**
	 * Goto page.
	 */
	private void gotoPage() {
		page = pdffile.getPage(item.getCurrentPageIndex());
		draw();
	}
	
	/**
	 * Goto page.
	 *
	 * @param pagenum
	 *            the pagenum
	 */
	private void gotoPage(int pagenum) {
		page = pdffile.getPage(pagenum);
		draw();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#init()
	 */
	@Override
	public void init() {
		super.init();
		if (item.getPdfURL() != null) {
			item.setPdfURL(item.getPdfURL());
		}
	}
	
	/**
	 * Resize pdf view.
	 */
	protected void resizePDFView() {
		
		this.graphicsImageQuad.updateGeometry(width, height);
		this.graphicsImageQuad.recreateImageForSize(width, height);
		gfx = this.graphicsImageQuad.getImageGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.graphicsImageQuad.updateGeometricState(0f, true);
		this.graphicsImageQuad.updateModelBound();
		
		draw();

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setHeight
	 * (int)
	 */
	@Override
	public void setHeight(int height) {
		this.height = item.getHeight();
		this.width = (int) (pdffile.getPage(0).getAspectRatio() * height);
		this.item.setWidth(width);

		resizePDFView();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IPDFViewerImplementation#setPdfFile(java.io.File)
	 */
	@Override
	public void setPdfFile(File pdfFile) {
		try {
			this.pdffile = getPDFFile(pdfFile);
			gotoPage(0);
			resizePDFView();
			draw();
		} catch (IOException e) {
			log.warning(e.toString());
		}

		item.setPageCount(this.pdffile.getNumPages());

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IPDFViewerImplementation#setPdfURL(java.net.URL)
	 */
	@Override
	public void setPdfURL(URL pdfFile) {
		try {
			this.pdffile = getPDFFile(pdfFile);
			gotoPage(0);
			resizePDFView();
			draw();
		} catch (IOException e) {
			log.warning(e.toString());
		}

		item.setPageCount(this.pdffile.getNumPages());

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setWidth
	 * (int)
	 */
	@Override
	public void setWidth(int width) {
		
		this.width = item.getWidth();
		this.height = (int) ((1 / pdffile.getPage(0).getAspectRatio()) * width);
		this.item.setHeight(height);
		resizePDFView();
		
	}
}
