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

package apps.watertank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;

import synergynetframework.appsystem.table.gfx.FullScreenCanvas;
import synergynetframework.jme.gfx.twod.DrawableSpatialImage;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class WaterTank.
 */
public class WaterTank extends FullScreenCanvas implements DrawableSpatialImage {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7014741028303257761L;

	/** The size. */
	protected int delay, size;
	
	/** The gfx. */
	private ImageGraphics gfx;

	/** The b. */
	protected int i, a, b;

	/** The im. */
	protected Image im;

	/** The off image. */
	protected Image image, offImage;

	/** The Mouse y. */
	protected int MouseX, MouseY;

	/** The off graphics. */
	protected Graphics offGraphics;

	/** The mapind. */
	protected int oldind, newind, mapind;

	/** The ripple. */
	protected int ripple[];

	/** The ripplemap. */
	protected short ripplemap[];

	/** The riprad. */
	protected int riprad;

	/** The source. */
	protected MemoryImageSource source;

	/** The texture. */
	protected int texture[];

	/** The tolerance. */
	protected float tolerance = 10;

	/** The hheight. */
	protected int width, height, hwidth, hheight;
	
	/**
	 * Instantiates a new water tank.
	 *
	 * @param name
	 *            the name
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public WaterTank(String name, int width, int height) {
		super(name);
		gfx = getGraphics();

		this.width = width;
		this.height = height;
		this.hwidth = width;
		this.hheight = height;
		
		init();
		draw();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorClicked(long,
	 * int, int)
	 */
	@Override
	public void cursorClicked(long cursorID, int x, int y) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorDragged(long,
	 * int, int)
	 */
	@Override
	public void cursorDragged(long id, int x, int y) {
		disturb(x, y);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorPressed(long,
	 * int, int)
	 */
	@Override
	public void cursorPressed(long cursorID, int x, int y) {
		disturb(x, y);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorReleased(
	 * long, int, int)
	 */
	@Override
	public void cursorReleased(long cursorID, int x, int y) {
		disturb(x, y);
	}

	/**
	 * Disturb.
	 *
	 * @param dx
	 *            the dx
	 * @param dy
	 *            the dy
	 */
	public void disturb(int dx, int dy) {
		for (int j = dy - riprad; j < (dy + riprad); j++) {
			for (int k = dx - riprad; k < (dx + riprad); k++) {
				if ((j >= 0) && (j < height) && (k >= 0) && (k < width)) {
					ripplemap[oldind + (j * width) + k] += 512;
				}
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#draw()
	 */
	public void draw() {
		newframe();
		source.newPixels();
		offGraphics.drawImage(image, 0, 0, width, height, null);
		gfx.drawImage(offImage, 0, 0, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#getSpatial()
	 */
	public Spatial getSpatial() {
		return this;
	}
	
	/**
	 * Inits the.
	 */
	public void init() {
		// Retrieve the base image
		ImageIcon ii = new ImageIcon(WaterTank.class.getResource("pebbles.jpg"));
		Image smallIm = ii.getImage();

		im = smallIm.getScaledInstance(width, height,
				java.awt.Image.SCALE_SMOOTH);
		
		hwidth = width >> 1;
				hheight = height >> 1;
				riprad = 2;
		
		size = width * (height + 2) * 2;
				ripplemap = new short[size];
				ripple = new int[width * height];
				texture = new int[width * height];
				oldind = width;
				newind = width * (height + 3);
		
		PixelGrabber pg = new PixelGrabber(im, 0, 0, width, height, texture, 0,
				width);
				try {
					pg.grabPixels();
				} catch (InterruptedException e) {
		}
		
		source = new MemoryImageSource(width, height, ripple, 0, width);
				source.setAnimated(true);
				source.setFullBufferUpdates(true);
		
		image = Toolkit.getDefaultToolkit().createImage(source);
				offImage = new BufferedImage(width, height, 1);
				offGraphics = offImage.getGraphics();
				// offGraphics.setColor(Color.red); // for fps drawing
	}
	
	/**
	 * Newframe.
	 */
	public void newframe() {
		
		// Toggle maps each frame
		i = oldind;
		oldind = newind;
		newind = i;
		
		i = 0;
		mapind = oldind;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				short data = (short) ((ripplemap[mapind - width]
						+ ripplemap[mapind + width] + ripplemap[mapind - 1] + ripplemap[mapind + 1]) >> 1);
				data -= ripplemap[newind + i];
				data -= data >> 5;
				ripplemap[newind + i] = data;
				
				// where data=0 then still, where data>0 then wave
				data = (short) (width - data);
				
				// offsets
				a = (((x - hwidth) * data) / width) + hwidth;
				b = (((y - hheight) * data) / width) + hheight;
				
				// bounds check
				if (a >= width) {
					a = width - 1;
				}
				if (a < 0) {
					a = 0;
				}
				if (b >= height) {
					b = height - 1;
				}
				if (b < 0) {
					b = 0;
				}
				
				ripple[i] = texture[a + (b * width)];
				mapind++;
				i++;
			}
		}
	}
	
	/**
	 * Start draw thread.
	 *
	 * @param parentApp
	 *            the parent app
	 */
	public void startDrawThread(final WaterTankApp parentApp) {
		Runnable messageDispatch = new Runnable() {
			public void run() {
				while (parentApp.isActive()) {
					draw();
				}
			}
			
		};
		Thread captureThread = new Thread(messageDispatch);
		captureThread.start();
	}
	
}
