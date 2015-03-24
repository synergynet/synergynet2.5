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
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. based on
 * MidiSynth.java from Sun Microsystems
 * @(#)MidiSynth.java 1.15 99/12/03 Copyright (c) 1999 Sun Microsystems, Inc.
 * All Rights Reserved. Sun grants you ("Licensee") a non-exclusive, royalty
 * free, license to use, modify and redistribute this software in source and
 * binary code form, provided that i) this copyright notice and license appear
 * on all copies of the software; and ii) Licensee does not utilize the software
 * in a manner which is disparaging to Sun. This software is provided "AS IS,"
 * without a warranty of any kind. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE
 * SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS
 * OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE
 * SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear facility.
 * Licensee represents and warrants that it will not use or redistribute the
 * Software for such purposes.
 */

package apps.groove;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Piano.
 */
public class Piano {

	/** The black keys. */
	private List<Key> blackKeys = new ArrayList<Key>();
	
	/** The g2. */
	private Graphics2D g2;

	/** The keys. */
	private List<Key> keys = new ArrayList<Key>();

	/** The kh. */
	int kw = 16, kh = 80;

	/** The white keys. */
	private List<Key> whiteKeys = new ArrayList<Key>();
	
	/**
	 * Instantiates a new piano.
	 *
	 * @param gfx
	 *            the gfx
	 */
	public Piano(Graphics2D gfx) {
		this.g2 = gfx;
		build();
		render();
	}

	/**
	 * Builds the.
	 */
	private void build() {
		int transpose = 24;
		int whiteIDs[] = { 0, 2, 4, 5, 7, 9, 11 };
		
		for (int i = 0, x = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++, x += kw) {
				int keyNum = (i * 12) + whiteIDs[j] + transpose;
				whiteKeys.add(new Key(x, 0, kw, kh, keyNum));
			}
		}
		for (int i = 0, x = 0; i < 6; i++, x += kw) {
			int keyNum = (i * 12) + transpose;
			blackKeys
					.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 1));
			blackKeys
					.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 3));
			x += kw;
			blackKeys
					.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 6));
			blackKeys
					.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 8));
			blackKeys
					.add(new Key((x += kw) - 4, 0, kw / 2, kh / 2, keyNum + 10));
		}
		
		keys.addAll(blackKeys);
		keys.addAll(whiteKeys);
		
	}
	
	/**
	 * Gets the key.
	 *
	 * @param point
	 *            the point
	 * @return the key
	 */
	public Key getKey(Point point) {
		for (int i = 0; i < keys.size(); i++) {
			if (keys.get(i).contains(point)) {
				return keys.get(i);
			}
		}
		return null;
	}

	/**
	 * Render.
	 */
	public void render() {
		g2.setColor(Color.white);
		g2.fillRect(0, 0, 42 * kw, kh);
		
		for (int i = 0; i < whiteKeys.size(); i++) {
			Key key = whiteKeys.get(i);
			if (key.isNoteOn()) {
				g2.setColor(Color.blue);
				g2.fill(key);
			}
			g2.setColor(Color.black);
			g2.draw(key);
		}
		for (int i = 0; i < blackKeys.size(); i++) {
			Key key = blackKeys.get(i);
			if (key.isNoteOn()) {
				g2.setColor(Color.blue);
				g2.fill(key);
				g2.setColor(Color.black);
				g2.draw(key);
			} else {
				g2.setColor(Color.black);
				g2.fill(key);
			}
		}
	}
}
