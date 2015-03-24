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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.SketchPad;
import synergynetframework.appsystem.contentsystem.items.SketchPad.DrawListener;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawFillRectangle;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawLine;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawString;
import synergynetframework.appsystem.contentsystem.jme.items.utils.SketchPadWrapper;
import synergynetframework.jme.cursorsystem.MultiTouchElementRegistry;
import synergynetframework.jme.cursorsystem.flicksystem.FlickSystem;


/**
 * The Class JMESketchPad.
 */
public class JMESketchPad extends JMEFrame implements ISketchPadImplementation{

	/** The item. */
	protected SketchPad item;
	
	/** The wrapper. */
	protected SketchPadWrapper wrapper;
	
	/** The draw image. */
	protected BufferedImage drawImage;
	
	/** The draw gfx. */
	protected Graphics2D drawGfx;
	
	/** The draw listeners. */
	protected transient List<DrawListener> drawListeners = new ArrayList<DrawListener>();
	
	/** The last point. */
	protected Map<Long, Point> lastPoint = new HashMap<Long, Point>();

	
	/**
	 * Instantiates a new JME sketch pad.
	 *
	 * @param contentItem the content item
	 */
	public JMESketchPad(ContentItem contentItem){
		super(contentItem);
		item = (SketchPad)contentItem;
		super.render();
		this.renderSketch();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#init()
	 */
	@Override
	public void init(){
		super.init();
		this.renderSketch();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#setRotateTranslateScalable(boolean)
	 */
	@Override
	public void setRotateTranslateScalable(boolean isEnabled) {
		rotateTranslateScalable = isEnabled;
		if(!rotateTranslateScalable){
			if (this.orthoControlPointRotateTranslateScale != null && MultiTouchElementRegistry.getInstance().isRegistered(orthoControlPointRotateTranslateScale)){
				MultiTouchElementRegistry.getInstance().unregister(this.orthoControlPointRotateTranslateScale);
				this.orthoControlPointRotateTranslateScale = null;
			} else if (this.wrapper != null && MultiTouchElementRegistry.getInstance().isRegistered(wrapper)){
				MultiTouchElementRegistry.getInstance().unregister(this.wrapper);
			}
		}else if(item!= null) renderSketch();
	}

	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#setSketchArea(java.awt.Rectangle)
	 */
	@Override
	public void setSketchArea(Rectangle rectangle) {
		if (this.orthoControlPointRotateTranslateScale != null && MultiTouchElementRegistry.getInstance().isRegistered(orthoControlPointRotateTranslateScale)){
			MultiTouchElementRegistry.getInstance().unregister(this.orthoControlPointRotateTranslateScale);
			this.orthoControlPointRotateTranslateScale = null;
			this.rotateTranslateScalable = true;
		} else if (this.wrapper != null && MultiTouchElementRegistry.getInstance().isRegistered(wrapper)){
			MultiTouchElementRegistry.getInstance().unregister(this.wrapper);
		}
		wrapper = new SketchPadWrapper(this, spatial,new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height));
		wrapper.addRotateTranslateScaleListener(this);
		wrapper.allowMoreThanTwoToRotateAndScale(true);
		spatial.updateModelBound();
		drawingFinished();

	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#getSketchArea()
	 */
	@Override
	public Rectangle getSketchArea(){
		return item.getSketchArea();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		super.setHeight(height);
		this.renderSketch();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		super.setWidth(width);
		this.renderSketch();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setBackGround(synergynetframework.appsystem.contentsystem.items.utils.Background)
	 */
	@Override
	public void setBackGround(Background backGround) {
		super.setBackGround(backGround);
		drawingFinished();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEFrame#setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {
		super.setBorder(border);
		drawingFinished();
	}
	
	/**
	 * Drawing finished.
	 */
	protected void drawingFinished() {
		if(gfx != null && drawImage != null){
			gfx.drawImage(drawImage, item.getBorderSize(),item.getBorderSize(), drawImage.getWidth()-2*item.getBorderSize(), drawImage.getHeight()-2*item.getBorderSize(), null);
		}
		this.graphicsImageQuad.updateGraphics();
	}
	
	/**
	 * Cursor dragged.
	 *
	 * @param id the id
	 * @param x the x
	 * @param y the y
	 */
	public void cursorDragged(long id, int x, int y) {
		if(!item.isDrawEnabled()) return;
		Point p = lastPoint.get(id);		
		if(p == null) {
			p = new Point(x, y);
			lastPoint.put(id, p);
		}
		DrawLine drawLine = new DrawLine(id, p, new Point(x,y), drawGfx.getColor(), item.getLineWidth());
		this.draw(drawLine);
		lastPoint.put(id, new Point(x, y));
		for(DrawListener drawListener: drawListeners) drawListener.itemDrawn(drawLine);
	}
	
	/**
	 * Cursor pressed.
	 *
	 * @param cursorID the cursor id
	 * @param x the x
	 * @param y the y
	 */
	public void cursorPressed(long cursorID, int x, int y) {
		if(!item.isDrawEnabled()) return;
		lastPoint.put(cursorID, new Point(x,y));
		drawGfx.drawLine(x, y, x, y);
	}

	/**
	 * Cursor released.
	 *
	 * @param cursorID the cursor id
	 * @param x the x
	 * @param y the y
	 */
	public void cursorReleased(long cursorID, int x, int y) {
		if(!item.isDrawEnabled()) return;
		lastPoint.remove(cursorID);
	}
	
	
	/**
	 * Cursor clicked.
	 *
	 * @param cursorID the cursor id
	 * @param x the x
	 * @param y the y
	 */
	public void cursorClicked(long cursorID, int x, int y) {}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return item.getHeight();
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return item.getWidth();
	}

	/**
	 * Gets the image height.
	 *
	 * @return the image height
	 */
	public int getImageHeight() {
		return item.getHeight();
	}

	/**
	 * Gets the image width.
	 *
	 * @return the image width
	 */
	public int getImageWidth() {
		return item.getWidth();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#clearAll()
	 */
	@Override
	public void clearAll() {
		drawGfx.setColor(item.getBackgroundColour());
		drawGfx.fillRect(item.getSketchArea().x, item.getSketchArea().y, item.getSketchArea().width, item.getSketchArea().height);
		Iterator<DrawData> iter = item.getDrawData().iterator();
		while(iter.hasNext()){
			if(iter.next() instanceof DrawLine) iter.remove();
		}
		drawGfx.setColor(item.getTextColor());
		drawingFinished();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#clear(synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData)
	 */
	@Override
	public void clear(DrawData drawData){
		drawGfx.setColor(item.getBackgroundColour());
		drawData.clear(drawGfx);
		this.drawingFinished();
		drawGfx.setColor(item.getTextColor());
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#setTextColor(java.awt.Color)
	 */
	@Override
	public void setTextColor(Color color) {
		if(item.getTextColor() != null) drawGfx.setColor(item.getTextColor());
		this.drawingFinished();
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#getTextColor()
	 */
	@Override
	public Color getTextColor() {
		return item.getTextColor();
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#getLineWidth()
	 */
	@Override
	public float getLineWidth() {
		return item.getLineWidth();
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#setLineWidth(float)
	 */
	@Override
	public void setLineWidth(float width) {
		drawGfx.setStroke(new BasicStroke(item.getLineWidth()));
		this.drawingFinished();
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#getBackgroundColour()
	 */
	@Override
	public Color getBackgroundColour() {
		return item.getBackgroundColour();
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#setBackgroundColour(java.awt.Color)
	 */
	@Override
	public void setBackgroundColour(Color color) {
		drawGfx.setColor(item.getBackgroundColour());
		drawGfx.fillRect(item.getBorderSize(), item.getBorderSize(), item.getWidth()- item.getBorderSize(), item.getHeight()- item.getBorderSize());
		if(item.getTextColor() != null) drawGfx.setColor(item.getTextColor());
		drawGfx.setStroke(new BasicStroke(item.getLineWidth()));
		this.redrawContent();
		this.drawingFinished();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#draw(synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData)
	 */
	@Override
	public void draw(DrawData data) {
		item.getDrawData().add(data);
		data.draw(drawGfx);
		this.drawingFinished();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#draw(java.util.List)
	 */
	@Override
	public void draw(List<DrawData> drawData) {
		for(DrawData data: drawData) this.draw(data);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#getDrawData()
	 */
	@Override
	public List<DrawData> getDrawData() {
		return item.getDrawData();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#addDrawListener(synergynetframework.appsystem.contentsystem.items.SketchPad.DrawListener)
	 */
	@Override
	public void addDrawListener(DrawListener listener) {
		if(drawListeners == null) drawListeners = new ArrayList<DrawListener>();
		if(!drawListeners.contains(listener)) drawListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#removeDrawListener(synergynetframework.appsystem.contentsystem.items.SketchPad.DrawListener)
	 */
	@Override
	public void removeDrawListener(DrawListener listener) {
		drawListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#removeDrawListeners()
	 */
	@Override
	public void removeDrawListeners() {
		drawListeners.clear();
	}
	
	/**
	 * Render sketch.
	 */
	public void renderSketch(){
		if(item.getWidth() >0 && item.getHeight() >0){
			drawImage = new BufferedImage(item.getWidth() - item.getBorderSize(), item.getHeight() - item.getBorderSize(), BufferedImage.TYPE_INT_ARGB);
			drawGfx = (Graphics2D)drawImage.getGraphics();
			drawGfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			this.setBackgroundColour(item.getBackgroundColour());
			if(item.getTextColor() != null) drawGfx.setColor(item.getTextColor());
			drawGfx.setStroke(new BasicStroke(item.getLineWidth()));
			this.setSketchArea(item.getSketchArea());
			this.setClearArea(item.getClearArea());
			this.setLineWidth(item.getLineWidth());
			this.redrawContent();
			this.drawingFinished();
		}
	}
	
	/**
	 * Redraw content.
	 */
	protected void redrawContent(){
		if(item.getWidth() >0 && item.getHeight() >0){
			for(DrawData data: item.getDrawData()){
				drawGfx.setColor(data.getColor());
				drawGfx.setStroke(new BasicStroke(data.getWidth()));
				data.draw(drawGfx);
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#fillRectangle(java.awt.Rectangle, java.awt.Color)
	 */
	@Override
	public void fillRectangle(Rectangle rectangle, Color color) {
		if(rectangle.x<0 || rectangle.y<0 || rectangle.width> item.getWidth() || rectangle.height >item.getHeight()) throw new IllegalArgumentException("Invalid dimension");
		DrawData drawData = new DrawFillRectangle(rectangle, color);
		this.draw(drawData);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#drawLine(long, java.awt.Point, java.awt.Point, java.awt.Color, float)
	 */
	@Override
	public void drawLine(long cursorId, Point startPoint, Point endPoint, Color color, float width){
		DrawLine drawLine = new DrawLine(cursorId, startPoint, endPoint, color, width);
		this.draw(drawLine);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#drawString(java.lang.String, int, int)
	 */
	@Override
	public void drawString(String string, int x, int y) {
		if(string == null) return;
		if(x<0 || y<0 || x> item.getWidth() || x >item.getHeight()) throw new IllegalArgumentException("Invalid dimension");
		DrawData drawData = new DrawString(string, new Point(x,y), item.getTextColor());
		this.draw(drawData);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#makeFlickable(float)
	 */
	@Override
	public void makeFlickable(float deceleration){

		FlickSystem.getInstance().makeFlickable(this.spatial, this.getTopLevelNode(spatial), wrapper, deceleration);
		FlickSystem.getInstance().getMovingElement(this.getTopLevelNode(spatial)).addFlickListener(this);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#getClearArea()
	 */
	@Override
	public Rectangle getClearArea(){
		return item.getClearArea();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#setClearArea(java.awt.Rectangle)
	 */
	@Override
	public void setClearArea(final Rectangle rectangle) {
		item.addItemListener(new ItemEventAdapter(){
			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				if(x>=rectangle.x && x <=rectangle.x+rectangle.width && y >= rectangle.y && y<=rectangle.y+rectangle.height){
					JMESketchPad.this.item.clearAll();
					for(DrawListener drawListener: drawListeners) drawListener.padCleared();
				}
			}
		});			
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#isDrawEnabled()
	 */
	@Override
	public boolean isDrawEnabled() {
		return item.isDrawEnabled();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISketchPadImplementation#setDrawEnabled(boolean)
	 */
	@Override
	public void setDrawEnabled(boolean isWriteEnabled) {}
}
