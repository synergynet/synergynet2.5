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

package synergynetframework.jme.gfx.twod.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.lwjgl.opengl.OpenGLException;

import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.WrapMode;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jmex.awt.swingui.ImageGraphics;

public class GraphicsImageQuad extends Quad {
	private static final long serialVersionUID = -3895330617113645191L;

	protected ImageGraphics graphics;
	protected TextureState ts;
	protected Texture texture;
	protected int imageWidth;
	protected int imageHeight;

	public GraphicsImageQuad(String name, float width, float height) {
		this(name, width, height, 256, 128);
	}
	
	public GraphicsImageQuad(String name, int imageWidth, int imageHeight, float pixelsPerUnit) {
		this(name, (float)imageWidth / pixelsPerUnit, imageHeight / pixelsPerUnit, imageWidth, imageHeight);
	}
	
	public GraphicsImageQuad(String name, float width, float height, int imageWidth, int imageHeight) {
		super(name);	
		updateGeometry(width, height);
		this.width = width;
		this.height = height;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		init();	
	}

	private void init() {
		recreateImageForSize(imageWidth, imageHeight);
			
		BlendState alpha = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
		alpha.setEnabled( true );
		alpha.setBlendEnabled( true );
		alpha.setSourceFunction( BlendState.SourceFunction.SourceAlpha);
		alpha.setDestinationFunction( BlendState.DestinationFunction.OneMinusSourceAlpha);
		alpha.setTestEnabled( true );
		alpha.setTestFunction( BlendState.TestFunction.GreaterThan);
		this.setRenderState( alpha );

		this.updateRenderState();
	
	}
	
	public void recreateImageForSize(int widthPixels, int heightPixels) {
		ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setCorrectionType(TextureState.CorrectionType.Perspective);		
		texture = new Texture2D();
		texture.setMinificationFilter(Texture.MinificationFilter.NearestNeighborNoMipMaps);
		texture.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		texture.setWrap(WrapMode.Repeat);

		this.imageWidth = widthPixels;
		this.imageHeight = heightPixels;
		graphics = ImageGraphics.createInstance(widthPixels, heightPixels, 0);
		
		enableAntiAlias( graphics );
		
		texture.setImage( graphics.getImage() );
		texture.setScale( new Vector3f( 1f, -1f, 1 ) );
	
		ts.setTexture( texture,0 );
		ts.apply();
		setRenderState( ts );	
		updateRenderState();
	}
	
	public void setTextureApplyMode(ApplyMode mode){
		texture.setApply(mode);
		updateRenderState();
	}
	
	public ImageGraphics getImageGraphics() {
		return graphics;
	}

	public void updateGraphics() {
		if(graphics != null) {
			try{
				graphics.update(texture, false);
			}catch(OpenGLException e){
				
			}
		}
	}
	
	private void enableAntiAlias( Graphics2D graphics ) {
		RenderingHints hints = graphics.getRenderingHints();
		if ( hints == null ) {
			hints = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		}
		else {
			hints.put( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		}
		graphics.setRenderingHints( hints );
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public int getImageHeight() {
		return imageHeight;
	}
	
	public TextureState getTextureState(){
		return ts;
	}
}


