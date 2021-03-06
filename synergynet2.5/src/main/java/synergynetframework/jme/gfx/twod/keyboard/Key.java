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

package synergynetframework.jme.gfx.twod.keyboard;

import java.awt.Rectangle;
import java.io.Serializable;

/**
 * The Class Key.
 */
public class Key implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3089776557743879105L;

	/** The area. */
	public Rectangle area;

	/** The key. */
	public int key;

	/**
	 * Instantiates a new key.
	 *
	 * @param r
	 *            the r
	 * @param key
	 *            the key
	 */
	public Key(Rectangle r, int key) {
		this.area = r;
		this.key = key;
	}

	/**
	 * Gets the key char.
	 *
	 * @param upperCaseModeOn
	 *            the upper case mode on
	 * @return the key char
	 */
	public char getKeyChar(boolean upperCaseModeOn) {
		return getKeyText(upperCaseModeOn);
	}

	/**
	 * Gets the key text.
	 *
	 * @param upperCaseModeOn
	 *            the upper case mode on
	 * @return the key text
	 */
	private char getKeyText(boolean upperCaseModeOn) {
		char c = (char) key;

		if (upperCaseModeOn) {
			c = Character.toUpperCase(c);
		} else {
			c = Character.toLowerCase(c);
		}

		return c;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return key + "(" + getKeyText(false) + ")@" + area;
	}
}
