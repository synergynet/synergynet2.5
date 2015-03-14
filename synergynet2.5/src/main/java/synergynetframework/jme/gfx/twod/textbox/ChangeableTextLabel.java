/*
 * Copyright (c) 2008 University of Durham, England
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

package synergynetframework.jme.gfx.twod.textbox;

import java.awt.Color;

import com.jmex.awt.swingui.ImageGraphics;

import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

public class ChangeableTextLabel extends GraphicsImageQuad {

	private static final long serialVersionUID = -2796493053368005492L;
	protected ImageGraphics gfx;
	protected StringBuffer text = new StringBuffer();

	public ChangeableTextLabel(String name, float width, float height) {
		super(name, width, height);
		gfx = getImageGraphics();
	}
	
	public void setText(String newText) {
		this.text.setLength(0);
		this.text.append(newText);
		draw();
	}
	
	public void appendText(String additionalText) {
		this.text.append(additionalText);
		draw();
	}
	
	protected void draw() {
//		gfx.setColor(Color.black);
//		gfx.fillRect(0, 0, imageWidth, imageHeight);
		gfx.setColor(Color.white);
		gfx.drawString(text.toString(), 0, imageHeight - 40);
		updateGraphics();
	}

}
