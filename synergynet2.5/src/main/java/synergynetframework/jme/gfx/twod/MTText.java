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

package synergynetframework.jme.gfx.twod;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import com.jme.renderer.ColorRGBA;
import com.jmex.awt.swingui.ImageGraphics;

import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;


/**
 * The Class MTText.
 */
public class MTText extends GraphicsImageQuad {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The gfx. */
	protected ImageGraphics gfx;
	
	/** The text. */
	protected StringBuffer text = new StringBuffer();


	/**
	 * Instantiates a new MT text.
	 *
	 * @param name the name
	 * @param colour the colour
	 * @param font the font
	 * @param setText the set text
	 * @param width the width
	 */
	public MTText(String name, ColorRGBA colour, Font font, String setText, int width) {
		super(name, width, getHeight(font), width, (int)getHeight(font));
		gfx = getImageGraphics();
		setColour(colour);
		setFont(font);
		setText(setText);
	}

	/**
	 * Instantiates a new MT text.
	 *
	 * @param name the name
	 * @param colour the colour
	 * @param font the font
	 * @param setText the set text
	 */
	public MTText(String name, ColorRGBA colour, Font font, String setText) {
		super(name, getWidth(setText, font), getHeight(font), (int)getWidth(setText, font), (int)getHeight(font));
		gfx = getImageGraphics();
		setColour(colour);
		setFont(font);
		setText(setText);
	}

	/**
	 * Gets the width.
	 *
	 * @param text the text
	 * @param font the font
	 * @return the width
	 */
	public static float getWidth(String text, Font font){
		GraphicsImageQuad temp = new GraphicsImageQuad("temp", 0, 0);
		ImageGraphics g = temp.getImageGraphics();
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		return metrics.stringWidth(text);
	}

	/**
	 * Gets the height.
	 *
	 * @param font the font
	 * @return the height
	 */
	public static float getHeight(Font font){
		GraphicsImageQuad temp = new GraphicsImageQuad("temp", 0, 0);
		ImageGraphics g = temp.getImageGraphics();
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		return metrics.getHeight();
	}

	/**
	 * Sets the text.
	 *
	 * @param newText the new text
	 */
	public void setText(String newText){
		gfx.clearRect(0, 0, imageWidth, imageHeight);
		this.text.setLength(0);
		this.text.append(newText);
		draw();
	}

	/**
	 * Append text.
	 *
	 * @param additionalText the additional text
	 */
	public void appendText(String additionalText) {
		this.text.append(additionalText);
		draw();
	}

	/**
	 * Sets the colour.
	 *
	 * @param c the new colour
	 */
	public void setColour(ColorRGBA c) {
		gfx.setColor(new Color(c.r, c.g, c.b));
	}

	/**
	 * Change colour.
	 *
	 * @param c the c
	 */
	public void changeColour(ColorRGBA c) {
		String thisText = this.text.toString();
		setColour(c);
		setText(thisText);
	}

	/**
	 * Draw.
	 */
	protected void draw() {
		gfx.drawString(text.toString(), 0, imageHeight - imageHeight/4);
		updateGraphics();
	}

	/**
	 * Sets the font.
	 *
	 * @param font the new font
	 */
	public void setFont(Font font) {
		gfx.setFont(font);

	}

}
