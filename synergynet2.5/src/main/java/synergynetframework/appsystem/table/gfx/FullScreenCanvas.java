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

package synergynetframework.appsystem.table.gfx;

import java.awt.Color;

import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.ImageGraphics;


/**
 * The Class FullScreenCanvas.
 */
public class FullScreenCanvas extends Quad {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3197516808277245917L;

	/** The graphics. */
	protected static ImageGraphics graphics;

	/** The pixel width. */
	protected int pixelWidth;
	
	/** The pixel height. */
	protected int pixelHeight;
	
	/** The ts. */
	private TextureState ts;
	
	/** The texture. */
	private Texture texture;
	
	/**
	 * Instantiates a new full screen canvas.
	 *
	 * @param name the name
	 */
	public FullScreenCanvas(String name) {
		super(name);
		pixelWidth = DisplaySystem.getDisplaySystem().getRenderer().getWidth();
		pixelHeight = DisplaySystem.getDisplaySystem().getRenderer().getHeight();
		if(graphics == null) {
			createSharedCanvas();
		}
		updateGeometry(pixelWidth, pixelHeight);
		applyCanvasAsTexture();
	}

	/**
	 * Apply canvas as texture.
	 */
	private void applyCanvasAsTexture() {
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);
		texture = new Texture2D();
		texture.setMinificationFilter(MinificationFilter.NearestNeighborNoMipMaps);
		texture.setMagnificationFilter(MagnificationFilter.NearestNeighbor);
		texture.setWrap(WrapMode.Repeat);
		texture.setImage(graphics.getImage());
		texture.setScale(new Vector3f(1, -1, 1));	
		ts.setTexture(texture);
		ts.apply();
		this.setRenderState(ts);
		
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled(true);
		alpha.setBlendEnabled(true);
		alpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled(true);
		alpha.setTestFunction(BlendState.TestFunction.GreaterThan);
		this.setRenderState(alpha);

		this.updateRenderState();
	}

	/**
	 * Creates the shared canvas.
	 */
	private void createSharedCanvas() {		
		graphics = ImageGraphics.createInstance(pixelWidth, pixelHeight, 0);		
	}
	
	/**
	 * Gets the graphics.
	 *
	 * @return the graphics
	 */
	public ImageGraphics getGraphics() {
		return graphics;
	}
	
	/* (non-Javadoc)
	 * @see com.jme.scene.TriMesh#draw(com.jme.renderer.Renderer)
	 */
	public void draw( Renderer r ) {
		if ( graphics.isDirty()) {
			if ( graphics != null ) {
				graphics.update(texture, false);
			}
		}
		super.draw( r );
	}
	
	/**
	 * Gets the image height.
	 *
	 * @return the image height
	 */
	public int getImageHeight() {
		return pixelHeight;
	}

	/**
	 * Gets the image width.
	 *
	 * @return the image width
	 */
	public int getImageWidth() {
		return pixelWidth;
	}
	
	/* (non-Javadoc)
	 * @see com.jme.scene.shape.Quad#getHeight()
	 */
	public float getHeight() {
		return pixelHeight;
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.shape.Quad#getWidth()
	 */
	public float getWidth() {
		return pixelWidth;
	}
	
	/**
	 * Clear.
	 *
	 * @param c the c
	 */
	public void clear(Color c) {
		graphics.setColor(c);
		graphics.fillRect(0, 0, pixelWidth, pixelHeight);
	}
}
