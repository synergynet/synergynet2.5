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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundFrame;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IRoundFrameImplementation;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.items.utils.ImageInfo;
import synergynetframework.jme.cursorsystem.MultiTouchCursorSystem;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoCursorEventDispatcher;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageDisc;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.Vector2f;
import com.jmex.awt.swingui.ImageGraphics;

/**
 * The Class JMERoundFrame.
 */
public class JMERoundFrame extends JMERoundContentItem implements
		IRoundFrameImplementation {

	/** The gfx. */
	protected ImageGraphics gfx;

	/** The graphics image disc. */
	protected GraphicsImageDisc graphicsImageDisc;

	/** The item. */
	protected RoundFrame item;

	/**
	 * Instantiates a new JME round frame.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMERoundFrame(ContentItem contentItem) {
		super(contentItem, new GraphicsImageDisc(contentItem.getName(), 100));
		graphicsImageDisc = (GraphicsImageDisc) this.spatial;
		gfx = graphicsImageDisc.getImageGraphics();
		item = (RoundFrame) contentItem;
		graphicsImageDisc.setLocalTranslation(0, 0, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #cursorChanged(synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoCursorEventDispatcher,
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorChanged(commonCursorEventDispatcher, c, event);
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event
				.getPosition());
		for (ScreenCursorListener l : this.screenCursorListeners) {
			l.screenCursorChanged(this.contentItem, event.getCursorID(),
					screenPos.x, screenPos.y, event.getPressure());
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #cursorClicked(synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoCursorEventDispatcher,
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorClicked(commonCursorEventDispatcher, c, event);
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event
				.getPosition());
		for (ScreenCursorListener l : this.screenCursorListeners) {
			l.screenCursorClicked(this.contentItem, event.getCursorID(),
					screenPos.x, screenPos.y, event.getPressure());
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #cursorPressed(synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoCursorEventDispatcher,
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorPressed(commonCursorEventDispatcher, c, event);
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event
				.getPosition());
		for (ScreenCursorListener l : this.screenCursorListeners) {
			l.screenCursorPressed(this.contentItem, event.getCursorID(),
					screenPos.x, screenPos.y, event.getPressure());
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #cursorReleased(synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoCursorEventDispatcher,
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(
			OrthoCursorEventDispatcher commonCursorEventDispatcher,
			ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorReleased(commonCursorEventDispatcher, c, event);
		Vector2f screenPos = MultiTouchCursorSystem.tableToScreen(event
				.getPosition());
		for (ScreenCursorListener l : this.screenCursorListeners) {
			l.screenCursorReleased(this.contentItem, event.getCursorID(),
					screenPos.x, screenPos.y, event.getPressure());
		}
		
	}
	
	/**
	 * Draw.
	 */
	protected void draw() {

	}

	/**
	 * Draw border.
	 */
	private void drawBorder() {
		
		int borderSize = item.getBorderSize();
		gfx.setColor(item.getBorderColour());

		for (int i = 0; i < borderSize; i++) {
			gfx.drawOval(i, i, (int) (item.getRadius() * 2) - (i * 2),
					(int) (item.getRadius() * 2) - (i * 2));
		}

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundFrameImplementation#drawImage(java.net.URL)
	 */
	@Override
	public void drawImage(URL imageResource) {
		render();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundFrameImplementation#drawImage(java.net.URL, int, int, int, int)
	 */
	@Override
	public void drawImage(URL imageResource, int x, int y, int width, int height) {
		render();
	}
	
	/**
	 * Draw images.
	 */
	protected void drawImages() {
		// draw images
		if (item.getImageResources() != null) {
			for (ImageInfo imgInfo : item.getImageResources().values()) {
				if (imgInfo.getImageResource() != null) {
					Image image = new ImageIcon(imgInfo.getImageResource())
							.getImage();
					gfx.drawImage(image, imgInfo.getX(), imgInfo.getY(),
							imgInfo.getWidth(), imgInfo.getHeight(), null);
				}
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundFrameImplementation#getImages()
	 */
	@Override
	public HashMap<URL, ImageInfo> getImages() {
		return item.getImages();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#
	 * init()
	 */
	@Override
	public void init() {
		super.init();
		updateSize();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundFrameImplementation#removeAllImages()
	 */
	@Override
	public void removeAllImages() {
		render();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IRoundFrameImplementation#removeImage(java.net.URL)
	 */
	@Override
	public void removeImage(URL imageResource) {
		render();
	}
	
	/**
	 * Render.
	 */
	protected void render() {

		// draw background
		gfx.setColor(item.getBackgroundColour());
		gfx.fillRect(0, 0, (int) (item.getRadius() * 2),
				(int) (item.getRadius() * 2));
		
		// draw content
		draw();
		
		// draw border
		drawBorder();

		// draw images
		drawImages();

		this.spatial.updateRenderState();
		this.spatial.updateGeometricState(0f, false);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #setBackGround(synergynetframework.appsystem.contentsystem.items.utils.
	 * Background)
	 */
	@Override
	public void setBackGround(Background backGround) {
		render();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem
	 * #
	 * setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {
		render();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMERoundContentItem
	 * #setRadius(float)
	 */
	@Override
	public void setRadius(float radius) {
		updateSize();
	}

	/**
	 * Update size.
	 */
	protected void updateSize() {

		float r = item.getRadius();
		this.graphicsImageDisc.updateGeometry(r);

		if (r < 1) {
			r = 1;
		}
		this.graphicsImageDisc.recreateImageForSize(r);
		gfx = graphicsImageDisc.getImageGraphics();

		render();
	}
	
}
