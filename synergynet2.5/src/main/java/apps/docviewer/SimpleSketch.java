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

package apps.docviewer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.HashMap;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

import synergynetframework.jme.gfx.twod.DrawableSpatialImage;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

public class SimpleSketch extends GraphicsImageQuad implements DrawableSpatialImage {
	private static final long serialVersionUID = -1822085394111434612L;
	
	protected ImageGraphics gfx;

	private int heightPixels;
	private int widthPixels;
	
	protected BufferedImage drawImage;

	private Graphics2D drawGfx;
	
	protected Map<Long, Point> lastPoint = new HashMap<Long, Point>();
	
	public SimpleSketch(String name, int widthPixels, int heightPixels) {
		super(name, widthPixels, heightPixels);
		this.widthPixels = widthPixels;
		this.heightPixels = heightPixels;
		recreateImageForSize(widthPixels, heightPixels);
		gfx = getImageGraphics();
		
		drawImage = new BufferedImage(widthPixels,heightPixels, BufferedImage.TYPE_INT_ARGB);
		drawGfx = (Graphics2D) drawImage.getGraphics();
		drawGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawGfx.setColor(Color.white);
		drawGfx.fillRect(0, 0, widthPixels, heightPixels);
		drawGfx.setColor(Color.DARK_GRAY);
		drawGfx.fillRect(0, 0, widthPixels, 40);
		drawGfx.setColor(Color.GRAY);
		drawGfx.drawString("Sketch Pad", 180, 30);
		draw();
		drawingFinished();
	}
	
	public void draw() {
	}
		
	public void cursorDragged(long id, int x, int y) {
		drawGfx.setColor(Color.black);		
		Point p = lastPoint.get(id);		
		if(p == null) {
			p = new Point(x, y);
			lastPoint.put(id, p);
		}
		drawGfx.drawLine(p.x, p.y, x, y);
		lastPoint.put(id, new Point(x, y));
		drawingFinished();
	}

	private void drawingFinished() {
		gfx.drawImage(drawImage, 0, 0, null);
		updateGraphics();
	}

	@Override
	public void cursorPressed(long cursorID, int x, int y) {
		lastPoint.put(cursorID, new Point(x,y));
		drawGfx.drawLine(x, y, x, y);
	}

	@Override
	public void cursorReleased(long cursorID, int x, int y) {
		lastPoint.remove(cursorID);
	}
	
	@Override
	public void cursorClicked(long cursorID, int x, int y) {}


	public float getWidth() {
		return widthPixels;
	}

	public float getHeight() {
		return heightPixels;
	}

	public int getImageHeight() {
		return heightPixels;
	}

	public int getImageWidth() {
		return widthPixels;
	}

	public Spatial getSpatial() {
		return this;
	}

}
