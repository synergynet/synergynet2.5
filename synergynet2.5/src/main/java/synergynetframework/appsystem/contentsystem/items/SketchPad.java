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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;

/**
 * The Class SketchPad.
 */
public class SketchPad extends Frame implements ISketchPadImplementation {
	
	/**
	 * The listener interface for receiving draw events. The class that is
	 * interested in processing a draw event implements this interface, and the
	 * object created with that class is registered with a component using the
	 * component's <code>addDrawListener<code> method. When
	 * the draw event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see DrawEvent
	 */
	public interface DrawListener {

		/**
		 * Item drawn.
		 *
		 * @param drawData
		 *            the draw data
		 */
		public void itemDrawn(DrawData drawData);

		/**
		 * Pad cleared.
		 */
		public void padCleared();
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5706125284971679198L;

	/** The background color. */
	protected Color backgroundColor = Color.white;

	/** The clear rectangle. */
	protected Rectangle clearRectangle = new Rectangle();

	/** The draw data. */
	protected List<DrawData> drawData = new ArrayList<DrawData>();

	/** The is draw enabled. */
	protected boolean isDrawEnabled = true;

	/** The line width. */
	protected float lineWidth = 1.0f;

	/** The sketch rectangle. */
	protected Rectangle sketchRectangle = new Rectangle();
	
	/** The text color. */
	protected Color textColor = Color.black;
	
	/**
	 * Instantiates a new sketch pad.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public SketchPad(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
		this.sketchRectangle.x = 0;
		this.sketchRectangle.y = 0;
		this.sketchRectangle.height = this.height;
		this.sketchRectangle.width = this.width;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#addDrawListener(synergynetframework.appsystem.
	 * contentsystem.items.SketchPad.DrawListener)
	 */
	@Override
	public void addDrawListener(DrawListener listener) {
		((ISketchPadImplementation) this.contentItemImplementation)
				.addDrawListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * ISketchPadImplementation#clear(synergynetframework.appsystem.contentsystem
	 * .jme.items.utils.DrawData)
	 */
	@Override
	public void clear(DrawData drawData) {
		((ISketchPadImplementation) this.contentItemImplementation)
				.clear(drawData);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#clearAll()
	 */
	@Override
	public void clearAll() {
		((ISketchPadImplementation) this.contentItemImplementation).clearAll();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * ISketchPadImplementation#draw(synergynetframework.appsystem.contentsystem
	 * .jme.items.utils.DrawData)
	 */
	@Override
	public void draw(DrawData drawData) {
		((ISketchPadImplementation) this.contentItemImplementation)
				.draw(drawData);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#draw(java.util.List)
	 */
	@Override
	public void draw(List<DrawData> drawData) {
		((ISketchPadImplementation) this.contentItemImplementation)
				.draw(drawData);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#drawLine(long, java.awt.Point, java.awt.Point,
	 * java.awt.Color, float)
	 */
	@Override
	public void drawLine(long cursorId, Point startPoint, Point endPoint,
			Color color, float width) {
		((ISketchPadImplementation) this.contentItemImplementation).drawLine(
				cursorId, startPoint, endPoint, color, width);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#drawString(java.lang.String, int, int)
	 */
	@Override
	public void drawString(String string, int x, int y) {
		((ISketchPadImplementation) this.contentItemImplementation).drawString(
				string, x, y);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#fillRectangle(java.awt.Rectangle,
	 * java.awt.Color)
	 */
	@Override
	public void fillRectangle(Rectangle rectangle, Color color) {
		((ISketchPadImplementation) this.contentItemImplementation)
				.fillRectangle(rectangle, color);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.ContentItem#
	 * getBackgroundColour()
	 */
	@Override
	public Color getBackgroundColour() {
		return backgroundColor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#getClearArea()
	 */
	@Override
	public Rectangle getClearArea() {
		return clearRectangle;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#getDrawData()
	 */
	@Override
	public List<DrawData> getDrawData() {
		return drawData;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#getLineWidth()
	 */
	@Override
	public float getLineWidth() {
		return lineWidth;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#getSketchArea()
	 */
	@Override
	public Rectangle getSketchArea() {
		return sketchRectangle;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#getTextColor()
	 */
	@Override
	public Color getTextColor() {
		return textColor;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#isDrawEnabled()
	 */
	@Override
	public boolean isDrawEnabled() {
		return isDrawEnabled;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * ISketchPadImplementation#removeDrawListener(synergynetframework.appsystem
	 * .contentsystem.items.SketchPad.DrawListener)
	 */
	@Override
	public void removeDrawListener(DrawListener listener) {
		((ISketchPadImplementation) this.contentItemImplementation)
				.removeDrawListener(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#removeDrawListeners()
	 */
	@Override
	public void removeDrawListeners() {
		((ISketchPadImplementation) this.contentItemImplementation)
				.removeDrawListeners();
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.ContentItem#
	 * setBackgroundColour(java.awt.Color)
	 */
	@Override
	public void setBackgroundColour(Color color) {
		this.backgroundColor = color;
		((ISketchPadImplementation) this.contentItemImplementation)
				.setBackgroundColour(backgroundColor);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#setClearArea(java.awt.Rectangle)
	 */
	@Override
	public void setClearArea(Rectangle rectangle) {
		this.clearRectangle = rectangle;
		((ISketchPadImplementation) this.contentItemImplementation)
				.setClearArea(rectangle);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#setDrawEnabled(boolean)
	 */
	@Override
	public void setDrawEnabled(boolean isDrawEnabled) {
		this.isDrawEnabled = isDrawEnabled;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.QuadContentItem#setHeight
	 * (int)
	 */
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		((ISketchPadImplementation) this.contentItemImplementation)
				.setHeight(height);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#setLineWidth(float)
	 */
	@Override
	public void setLineWidth(float width) {
		this.lineWidth = width;
		((ISketchPadImplementation) this.contentItemImplementation)
				.setLineWidth(lineWidth);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#setSketchArea(java.awt.Rectangle)
	 */
	@Override
	public void setSketchArea(Rectangle rectangle) {
		this.sketchRectangle = rectangle;
		((ISketchPadImplementation) this.contentItemImplementation)
				.setSketchArea(rectangle);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .ISketchPadImplementation#setTextColor(java.awt.Color)
	 */
	@Override
	public void setTextColor(Color color) {
		this.textColor = color;
		((ISketchPadImplementation) this.contentItemImplementation)
				.setTextColor(textColor);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.QuadContentItem#setWidth
	 * (int)
	 */
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		((ISketchPadImplementation) this.contentItemImplementation)
				.setWidth(width);
	}
}
