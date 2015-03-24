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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

/**
 * The Class DrawableString.
 */
public class DrawableString extends DrawableElement {
	
	/** The font. */
	protected Font font;

	/** The font_metrics. */
	protected FontMetrics font_metrics;

	/** The string. */
	protected String string;

	/**
	 * Instantiates a new drawable string.
	 *
	 * @param s
	 *            the s
	 */
	public DrawableString(String s) {
		super();
		this.string = s;
	}
	
	/*
	 * (non-Javadoc)
	 * @see apps.lightrays.gfxlib.DrawableElement#draw(java.awt.Graphics2D,
	 * long)
	 */
	public void draw(Graphics2D gfx, long tick_count) {
		gfx.setColor(colour);
		gfx.setFont(font);
		if (font_metrics == null) {
			font_metrics = gfx.getFontMetrics();
		}

		gfx.setComposite(getAlphaComposite());

		gfx.drawString(string, x, y);
	}

	/**
	 * Gets the font.
	 *
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Gets the string.
	 *
	 * @return the string
	 */
	public String getString() {
		return string;
	}
	
	/**
	 * Sets the font.
	 *
	 * @param font
	 *            the new font
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * Sets the string.
	 *
	 * @param s
	 *            the new string
	 */
	public void setString(String s) {
		this.string = s;
	}
	
}
