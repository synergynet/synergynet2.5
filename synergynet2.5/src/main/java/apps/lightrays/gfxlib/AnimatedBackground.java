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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * The Class AnimatedBackground.
 */
public class AnimatedBackground extends DrawableElement {

	/** The cs. */
	private Color[] cs = { new Color(0.9f, 0.9f, 0.9f),
			new Color(0.7f, 0.7f, 0.7f), new Color(0.5f, 0.5f, 0.5f),
			new Color(0.8f, 0.8f, 0.8f), new Color(0.9f, 0.9f, 0.9f),
			new Color(0.6f, 0.6f, 0.6f), new Color(0.9f, 0.9f, 0.9f),
			new Color(0.5f, 0.5f, 0.5f) };

	/** The height. */
	protected int height;

	/** The vs. */
	private int[] vs = { 1, 5, 2, -4, -3, 6, 2, -1 };

	/** The width. */
	protected int width;

	/** The ws. */
	private int[] ws = { 100, 30, 40, 20, 200, 50, 100, 150 };

	/** The xs. */
	private int[] xs = { 0, 50, 100, 150, 300, 500, 30, 70 };

	/**
	 * Instantiates a new animated background.
	 *
	 * @param w
	 *            the w
	 * @param h
	 *            the h
	 */
	public AnimatedBackground(int w, int h) {
		super();
		this.width = w;
		this.height = h;
	}

	/*
	 * (non-Javadoc)
	 * @see apps.lightrays.gfxlib.DrawableElement#draw(java.awt.Graphics2D,
	 * long)
	 */
	public void draw(Graphics2D gfx, long tick_count) {
		gfx.setComposite(getAlphaComposite());
		gfx.setColor(colour);
		gfx.fillRect(0, 0, width, height);
		
		for (int i = 0; i < xs.length; i++) {
			xs[i] = xs[i] + vs[i];
			if (xs[i] > (width + ws[i])) {
				xs[i] = -ws[i];
			}
			if ((xs[i] + ws[i]) < 0) {
				xs[i] = width + ws[i];
			}
			gfx.setColor(cs[i]);
			gfx.fillRect(xs[i], y, ws[i], height);

		}

	}
	
}
