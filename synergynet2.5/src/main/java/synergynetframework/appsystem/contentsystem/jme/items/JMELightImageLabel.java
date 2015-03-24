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

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import com.jme.image.Texture;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation;


/**
 * The Class JMELightImageLabel.
 */
public class JMELightImageLabel extends JMEQuadContentItem implements ILightImageLabelImplementation {

	/** The item. */
	LightImageLabel item;
	
	/** The image. */
	Image image = null;
	
	/**
	 * Instantiates a new JME light image label.
	 *
	 * @param contentItem the content item
	 */
	public JMELightImageLabel(ContentItem contentItem) {
		super(contentItem, new Quad(contentItem.getName(), 1,1));
		item = (LightImageLabel) contentItem;
		((Quad)spatial).updateGeometry(item.getWidth(), item.getHeight());
		((Quad)spatial).updateRenderState();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#init()
	 */
	@Override
	public void init(){
		super.init();
		((Quad)spatial).updateGeometry(item.getWidth(), item.getHeight());
		drawImage(item.getImageResource());
		if(image != null) drawImage(image);
		((Quad)spatial).updateRenderState();
		this.setScaleLimit(item.getMinScale(), item.getMaxScale());
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
		float width = item.getWidth();
		if(item.isAutoFitSize() && item.getImageResource() != null){
			ImageIcon image = new ImageIcon(item.getImageResource());
			float aspectRatio = (float) image.getIconWidth() / (float) image.getIconHeight();
			width = height * aspectRatio;
		}
		((Quad)spatial).updateGeometry(width, height);
		((Quad)spatial).updateRenderState();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		float height = item.getHeight();
		if(item.isAspectRatioEnabled() && item.getImageResource() != null){
			ImageIcon image = new ImageIcon(item.getImageResource());
			float aspectRatio = (float) image.getIconWidth() / (float) image.getIconHeight();
			height = width / aspectRatio;
		}
		((Quad)spatial).updateGeometry(width, height);
		((Quad)spatial).updateRenderState();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation#drawImage(java.net.URL)
	 */
	@Override
	public void drawImage(URL imageResource) {
		if(imageResource == null) return;
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(imageResource, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		ts.setTexture(t);

		((Quad)spatial).setRenderState(ts);

		if(item.isAplaEnabled()) {
			BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestEnabled(true);
			as.setTestFunction(BlendState.TestFunction.GreaterThan);
			spatial.setRenderState(as);
		}
		((Quad)spatial).updateGeometry(t.getImage().getWidth(), t.getImage().getHeight());
		((Quad)spatial).updateModelBound();
		((Quad)spatial).updateRenderState();
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation#drawImage(java.awt.Image)
	 */
	@Override
	public void drawImage(Image image) {
		if(image == null) return;
		this.image = image;
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(image, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, true);
		ts.setTexture(t);

		((Quad)spatial).setRenderState(ts);

		if(item.isAplaEnabled()) {
			BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestEnabled(true);
			as.setTestFunction(BlendState.TestFunction.GreaterThan);
			spatial.setRenderState(as);
		}
		((Quad)spatial).updateGeometry(t.getImage().getWidth(), t.getImage().getHeight());
		((Quad)spatial).updateModelBound();
		((Quad)spatial).updateRenderState();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation#getImageResource()
	 */
	@Override
	public URL getImageResource() {
		 
		return null;
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem#setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		 
		
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation#isAplaEnabled()
	 */
	@Override
	public boolean isAplaEnabled() {
		 
		return false;
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation#useAlpha(boolean)
	 */
	@Override
	public void useAlpha(boolean useAlpha) {
		 
		
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation#enableAspectRatio(boolean)
	 */
	@Override
	public void enableAspectRatio(boolean isAspectRationEnabled) {
		 
		
	}


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ILightImageLabelImplementation#isAspectRatioEnabled()
	 */
	@Override
	public boolean isAspectRatioEnabled() {
		 
		return false;
	}

}
