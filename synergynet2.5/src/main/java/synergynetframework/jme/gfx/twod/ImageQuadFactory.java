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

package synergynetframework.jme.gfx.twod;

import java.awt.Image;
import java.net.URL;

import com.jme.image.Texture;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;


/**
 * A factory for creating ImageQuad objects.
 */
public class ImageQuadFactory {
	
	/** The Constant ALPHA_ENABLE. */
	public static final int ALPHA_ENABLE = 0;
	
	/** The Constant ALPHA_DISABLE. */
	public static final int ALPHA_DISABLE = 1;
	
	/**
	 * Creates a new ImageQuad object.
	 *
	 * @param name the name
	 * @param width the width
	 * @param resource the resource
	 * @return the quad
	 */
	public static Quad createQuadWithImageResource(String name, float width, URL resource) {
		return createQuadWithImageResource(name, width, resource, ALPHA_DISABLE);
	}
	
	/**
	 * Creates a new ImageQuad object.
	 *
	 * @param name the name
	 * @param width the width
	 * @param resource the resource
	 * @param useAlpha the use alpha
	 * @return the quad
	 */
	public static Quad createQuadWithUncompressedImageResource(String name, float width, URL resource, int useAlpha) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(resource, Texture.MinificationFilter.NearestNeighborLinearMipMap, Texture.MagnificationFilter.NearestNeighbor, com.jme.image.Image.Format.GuessNoCompression,1,true);
		ts.setTexture(t);
		float aspectRatio = (float) t.getImage().getWidth() / (float) t.getImage().getHeight();
		float height = width / aspectRatio;

		Quad q = new Quad(name, width, height);
		q.setRenderState(ts);
		if(useAlpha == ALPHA_ENABLE) {
			BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestEnabled(true);
			as.setTestFunction(BlendState.TestFunction.GreaterThan);
			q.setRenderState(as);
		}
		q.updateRenderState();
		
		return q;
	}
	
	/**
	 * Creates a new ImageQuad object.
	 *
	 * @param name the name
	 * @param width the width
	 * @param resource the resource
	 * @param useAlpha the use alpha
	 * @return the quad
	 */
	public static Quad createQuadWithImageResource(String name, float width, URL resource, int useAlpha) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(resource, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		ts.setTexture(t);

		float aspectRatio = (float) t.getImage().getWidth() / (float) t.getImage().getHeight();
		float height = width / aspectRatio;

		Quad q = new Quad(name, width, height);
		q.setRenderState(ts);
		if(useAlpha == ALPHA_ENABLE) {
			BlendState as = DisplaySystem.getDisplaySystem().getRenderer().createBlendState();
			as.setBlendEnabled(true);
			as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
			as.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
			as.setTestEnabled(true);
			as.setTestFunction(BlendState.TestFunction.GreaterThan);
			q.setRenderState(as);
		}
		q.updateRenderState();
		
		return q;
	}
	
	/**
	 * Creates a new ImageQuad object.
	 *
	 * @param name the name
	 * @param width the width
	 * @param img the img
	 * @return the quad
	 */
	public static Quad createQuadWithImage(String name, float width, Image img) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(img, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear, false);
		ts.setTexture(t);

		float aspectRatio = (float) t.getImage().getWidth() / (float) t.getImage().getHeight();
		float height = width / aspectRatio;		
		
		Quad q = new Quad(name, width, height);
		q.setRenderState(ts);
		q.updateRenderState();
		
		return q;		
	}
	
	/**
	 * Creates a new ImageQuad object.
	 *
	 * @param name the name
	 * @param width the width
	 * @param tex the tex
	 * @return the quad
	 */
	public static Quad createQuadWithTexture(String name, float width, Texture tex) {		
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setTexture(tex);
		
		float aspectRatio = (float) tex.getImage().getWidth() / (float) tex.getImage().getHeight();
		float height = width / aspectRatio;		
		
		Quad q = new Quad(name, width, height);
		q.setRenderState(ts);
		q.updateRenderState();

		return q;		
	}
}
