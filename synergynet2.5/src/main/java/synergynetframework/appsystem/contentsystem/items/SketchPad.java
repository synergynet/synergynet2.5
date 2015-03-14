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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;

public class SketchPad extends Frame implements ISketchPadImplementation{

	protected Rectangle sketchRectangle = new Rectangle();
	protected Rectangle clearRectangle = new Rectangle();
	protected Color textColor = Color.black;
	protected Color backgroundColor = Color.white;
	protected float lineWidth = 1.0f;
	protected List<DrawData> drawData = new ArrayList<DrawData>();
	protected boolean isDrawEnabled = true;
	
	private static final long serialVersionUID = 5706125284971679198L;

	public SketchPad(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
		this.sketchRectangle.x = 0;
		this.sketchRectangle.y = 0;
		this.sketchRectangle.height = this.height;
		this.sketchRectangle.width = this.width;
	}

	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		((ISketchPadImplementation)this.contentItemImplementation).setHeight(height);
	}

	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		((ISketchPadImplementation)this.contentItemImplementation).setWidth(width);
	}
	
	@Override
	public void setSketchArea(Rectangle rectangle) {
		this.sketchRectangle= rectangle;
		((ISketchPadImplementation)this.contentItemImplementation).setSketchArea(rectangle);
	}

	@Override
	public Rectangle getSketchArea() {
		return sketchRectangle;
	}

	@Override
	public void clearAll() {
		((ISketchPadImplementation)this.contentItemImplementation).clearAll();
	}

	@Override
	public void clear(DrawData drawData) {
		((ISketchPadImplementation)this.contentItemImplementation).clear(drawData);
	}
	
	@Override
	public void setTextColor(Color color) {
		this.textColor = color;
		((ISketchPadImplementation)this.contentItemImplementation).setTextColor(textColor);
	}

	@Override
	public Color getTextColor() {
		return textColor;
	}

	@Override
	public float getLineWidth() {
		return lineWidth;
	}

	@Override
	public void setLineWidth(float width) {
		this.lineWidth = width;
		((ISketchPadImplementation)this.contentItemImplementation).setLineWidth(lineWidth);

	}

	@Override
	public Color getBackgroundColour() {
		return backgroundColor;
	}

	@Override
	public void setBackgroundColour(Color color) {
		this.backgroundColor = color;
		((ISketchPadImplementation)this.contentItemImplementation).setBackgroundColour(backgroundColor);

	}
	
	@Override
	public void draw(DrawData drawData) {
		((ISketchPadImplementation)this.contentItemImplementation).draw(drawData);		
	}

	
	@Override
	public void draw(List<DrawData> drawData) {
		((ISketchPadImplementation)this.contentItemImplementation).draw(drawData);		
	}

	@Override
	public List<DrawData> getDrawData() {
		return drawData;
	}

	@Override
	public void addDrawListener(DrawListener listener) {
		((ISketchPadImplementation)this.contentItemImplementation).addDrawListener(listener);
	}

	@Override
	public void removeDrawListener(DrawListener listener) {
		((ISketchPadImplementation)this.contentItemImplementation).removeDrawListener(listener);
	}

	@Override
	public void removeDrawListeners() {
		((ISketchPadImplementation)this.contentItemImplementation).removeDrawListeners();
	}

	
	public interface DrawListener{
		public void itemDrawn(DrawData drawData);
		public void padCleared();
	}


	@Override
	public void fillRectangle(Rectangle rectangle, Color color) {
		((ISketchPadImplementation)this.contentItemImplementation).fillRectangle(rectangle, color);
	}

	@Override
	public void drawLine(long cursorId, Point startPoint, Point endPoint, Color color, float width){
		((ISketchPadImplementation)this.contentItemImplementation).drawLine(cursorId, startPoint, endPoint, color, width);
	}
	
	@Override
	public void drawString(String string, int x, int y) {
		((ISketchPadImplementation)this.contentItemImplementation).drawString(string, x, y);
	}

	@Override
	public Rectangle getClearArea() {
		return clearRectangle;
	}

	@Override
	public void setClearArea(Rectangle rectangle) {
		this.clearRectangle = rectangle;
		((ISketchPadImplementation)this.contentItemImplementation).setClearArea(rectangle);
	}

	@Override
	public void setDrawEnabled(boolean isDrawEnabled) {
		this.isDrawEnabled = isDrawEnabled;
	}

	@Override
	public boolean isDrawEnabled() {
		return isDrawEnabled;
	}
}
