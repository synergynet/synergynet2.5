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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.PPTViewer;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IPPTViewerImplementation;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

import com.jme.util.GameTaskQueueManager;

/**
 * The Class JMEPPTViewer.
 */
public class JMEPPTViewer extends JMEFrame implements IPPTViewerImplementation {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEPPTViewer.class
			.getName());

	/** The current slide. */
	protected int currentSlide;

	/** The graphics quad. */
	protected final GraphicsImageQuad graphicsQuad;

	/** The height. */
	protected int height = 350;

	/** The item. */
	private PPTViewer item;

	/** The off gfx. */
	protected Graphics2D offGfx;

	/** The off image. */
	protected Image offImage;
	
	/** The pgsize. */
	protected Dimension pgsize;

	/** The ppt. */
	protected SlideShow ppt;

	/** The slides. */
	protected Slide[] slides;
	
	/** The width. */
	protected int width = 200;
	
	/**
	 * Instantiates a new JMEPPT viewer.
	 *
	 * @param item
	 *            the item
	 */
	public JMEPPTViewer(ContentItem item) {
		super(item);
		this.item = (PPTViewer) item;
		graphicsQuad = this.graphicsImageQuad;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IDocViewerImplementation#changePage()
	 */
	@Override
	public void changePage() {
		this.currentSlide = item.getCurrentPageIndex();
		drawSlide();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#draw()
	 */
	@Override
	protected void draw() {
		super.draw();
	}
	
	/**
	 * Draw slide.
	 */
	protected void drawSlide() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if ((slides != null) && (gfx != null)) {
					offImage = new BufferedImage(pgsize.width, pgsize.height,
							BufferedImage.TYPE_INT_RGB);
					offGfx = (Graphics2D) offImage.getGraphics();
					offGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
							RenderingHints.VALUE_ANTIALIAS_ON);
					slides[currentSlide].draw(offGfx);
					gfx.drawImage(offImage, 0, 0, item.getWidth(),
							item.getHeight(), 0, 0, pgsize.width,
							pgsize.height, null);
				}
				GameTaskQueueManager.getManager().update(
						new Callable<Object>() {
							public Object call() throws Exception {
								graphicsQuad.updateGraphics();
								return null;
							}
						});
				
			}
			
		});
		t.start();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#init()
	 */
	@Override
	public void init() {
		if (this.item.getPPTFile() != null) {
			this.setPPTFile(this.item.getPPTFile());
		}
	}
	
	/**
	 * Resize ppt view.
	 */
	private void resizePPTView() {
		if (pgsize == null) {
			return;
		}
		this.graphicsImageQuad.updateGeometry(width, height);
		this.graphicsImageQuad.recreateImageForSize(width, height);
		gfx = this.graphicsImageQuad.getImageGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		this.graphicsImageQuad.updateGeometricState(0f, true);
		this.graphicsImageQuad.updateModelBound();

		drawSlide();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setHeight
	 * (int)
	 */
	@Override
	public void setHeight(int height) {
		float aspectRatio = (pgsize.width / (float) pgsize.height);
		this.height = height;
		this.width = (int) (aspectRatio * height);
		item.setWidth(width);
		resizePPTView();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IPPTViewerImplementation#setPPTFile(java.net.URL)
	 */
	@Override
	public void setPPTFile(URL pptFile) {
		try {
			InputStream is = item.getPPTFile().openStream();
			ppt = new SlideShow(is);
			is.close();
			pgsize = ppt.getPageSize();
			slides = ppt.getSlides();
			item.setPageCount(slides.length);
			currentSlide = 0;
			setHeight(this.height);
			resizePPTView();
		} catch (FileNotFoundException e) {
			log.warning(e.toString());
		} catch (IOException e) {
			log.warning(e.toString());
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setWidth
	 * (int)
	 */
	@Override
	public void setWidth(int width) {
		float aspectRatio = (pgsize.width / (float) pgsize.height);
		this.width = width;
		this.height = (int) ((1 / aspectRatio) * this.width);
		this.item.setHeight(height);
		resizePPTView();
	}
}
