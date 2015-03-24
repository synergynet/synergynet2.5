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

package apps.lightrays.gfxlib;

import java.awt.Dimension;


/**
 * The Class Pixels.
 */
public class Pixels {

	// 0xAARRGGBB
	/** The pixels. */
	public int[] pixels;
	
	/** The width. */
	public int width;
	
	/** The height. */
	public int height;
	
	/**
	 * Instantiates a new pixels.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public Pixels(int width, int height) {
		this(new int[width * height], width, height);
	}	
	
	/**
	 * Instantiates a new pixels.
	 *
	 * @param pixels the pixels
	 * @param w the w
	 * @param h the h
	 */
	public Pixels(int[] pixels, int w, int h) {
		this.pixels = pixels;
		this.width = w;
		this.height = h;
	}	
	
	/**
	 * Sets the rgb.
	 *
	 * @param x the x
	 * @param y the y
	 * @param r the r
	 * @param g the g
	 * @param b the b
	 */
	public void setRGB(int x, int y, int r, int g, int b) {
		int index = x + (y * width);
		pixels[index] = rgbToInt(r, g, b);
	}
	
	/**
	 * Rgb to int.
	 *
	 * @param r the r
	 * @param g the g
	 * @param b the b
	 * @return the int
	 */
	public static int rgbToInt(int r, int g, int b) {
		int c = (r << 24) | (g << 16) | b;
		return c;
	}
	
	/**
	 * Gets the pixel.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the pixel
	 */
	public int getPixel(int x, int y) {
		int index = x + (y * width);
		return pixels[index];
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the pixels.
	 *
	 * @return the pixels
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return width + "," + height;
	}	

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Dimension getSize() {
		return new Dimension(width, height);
	}

	/**
	 * Sets the pixel.
	 *
	 * @param x the x
	 * @param y the y
	 * @param c the c
	 */
	public void setPixel(int x, int y, int c) {
		pixels[(y*width) + x] = c;		
	}

}
