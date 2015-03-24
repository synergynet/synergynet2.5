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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.URL;
import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.items.BackgroundController;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IBackgroundControllerImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.ImageInfo;
import synergynetframework.appsystem.contentsystem.jme.JMEContentSystem;
import synergynetframework.jme.cursorsystem.MultiTouchElementRegistry;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;

import com.jme.scene.Spatial.CullHint;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

/**
 * The Class JMEBackgroundController.
 */
public class JMEBackgroundController extends JMEQuadContentItem implements
		IBackgroundControllerImplementation {
	
	/** The background ortho control point rotate translate scale. */
	protected OrthoControlPointRotateTranslateScale backgroundOrthoControlPointRotateTranslateScale;

	/** The background rotate translate scalable. */
	protected boolean backgroundRotateTranslateScalable;

	/** The item. */
	private BackgroundController item;

	/**
	 * Instantiates a new JME background controller.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMEBackgroundController(ContentItem contentItem) {
		super(contentItem, new Quad(contentItem.getName(), DisplaySystem
				.getDisplaySystem().getWidth(), DisplaySystem
				.getDisplaySystem().getHeight()));
		((Quad) this.spatial).setCullHint(CullHint.Always);
		item = (BackgroundController) contentItem;

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#drawImage(java.net.URL)
	 */
	@Override
	public void drawImage(URL imageResource) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#drawImage(java.net.URL, int, int, int, int)
	 */
	@Override
	public void drawImage(URL imageResource, int x, int y, int width, int height) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#flushGraphics()
	 */
	@Override
	public void flushGraphics() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#getGraphicsContext()
	 */
	@Override
	public Graphics2D getGraphicsContext() {
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#getImages()
	 */
	@Override
	public HashMap<URL, ImageInfo> getImages() {
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#
	 * init()
	 */
	public void init() {
		this.item.setHeight(item.getContentSystem().getScreenHeight());
		this.item.setWidth(item.getContentSystem().getScreenWidth());
		this.item.setBackgroundColour(Color.black);
		this.item.setBorderSize(0);
		this.centerItem();
		this.item.setBringToTopable(false);
		this.item.setRotateTranslateScalable(false);
		
		if (backgroundRotateTranslateScalable) {
			backgroundOrthoControlPointRotateTranslateScale = new OrthoControlPointRotateTranslateScale(
					spatial,
					((JMEContentSystem) contentItem.getContentSystem())
							.getOrthoRootNode());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#removeAllImages()
	 */
	@Override
	public void removeAllImages() {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IFrameImplementation#removeImage(java.net.URL)
	 */
	@Override
	public void removeImage(URL imageResource) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem
	 * #setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IBackgroundControllerImplementation#setBackgroundRotateTranslateScalable(
	 * boolean)
	 */
	public void setBackgroundRotateTranslateScalable(boolean isEnabled) {
		this.backgroundRotateTranslateScalable = isEnabled;
		
		// enable multitouch element
		if (this.backgroundRotateTranslateScalable) {
			if (this.backgroundOrthoControlPointRotateTranslateScale != null) {
				if (MultiTouchElementRegistry.getInstance().isRegistered(
						backgroundOrthoControlPointRotateTranslateScale)) {
					MultiTouchElementRegistry
							.getInstance()
							.unregister(
									this.backgroundOrthoControlPointRotateTranslateScale);
				}
				this.backgroundOrthoControlPointRotateTranslateScale = null;
			}
			backgroundOrthoControlPointRotateTranslateScale = new OrthoControlPointRotateTranslateScale(
					spatial,
					((JMEContentSystem) contentItem.getContentSystem())
							.getOrthoRootNode());
		}

		// disable multitouch element
		else {
			if ((this.backgroundOrthoControlPointRotateTranslateScale != null)
					&& MultiTouchElementRegistry.getInstance().isRegistered(
							backgroundOrthoControlPointRotateTranslateScale)) {
				MultiTouchElementRegistry.getInstance().unregister(
						this.backgroundOrthoControlPointRotateTranslateScale);
				this.backgroundOrthoControlPointRotateTranslateScale = null;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem
	 * #setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem
	 * #setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		
	}
	
}
