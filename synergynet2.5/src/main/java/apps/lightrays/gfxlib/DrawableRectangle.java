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

package apps.lightrays.gfxlib;

import java.awt.Graphics2D;

/**
 * The Class DrawableRectangle.
 */
public class DrawableRectangle extends DrawableElement {

	/** The height. */
	private int height;

	/** The height_half. */
	private int height_half;

	/** The width. */
	private int width;

	/** The width_half. */
	private int width_half;
	
	/**
	 * Instantiates a new drawable rectangle.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public DrawableRectangle(int width, int height) {
		this.width = width;
		this.height = height;

		width_half = width / 2;
		height_half = height / 2;
	}
	
	/*
	 * (non-Javadoc)
	 * @see apps.lightrays.gfxlib.DrawableElement#draw(java.awt.Graphics2D,
	 * long)
	 */
	public void draw(Graphics2D gfx, long tick_count) {
		gfx.drawRect(x - width_half, y - height_half, width, height);
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
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the width_half.
	 *
	 * @return the width_half
	 */
	public int getWidth_half() {
		return width_half;
	}
	
	/**
	 * Sets the height.
	 *
	 * @param height
	 *            the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
}
