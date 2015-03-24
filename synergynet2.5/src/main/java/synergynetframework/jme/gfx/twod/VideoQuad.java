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

import java.net.URL;

import org.llama.jmf.ByteBufferRenderer;
import org.llama.jmf.JMFVideoImage;

import synergynetframework.jme.Updateable;

import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;


/**
 * The Class VideoQuad.
 */
public class VideoQuad extends Quad implements Updateable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1917902983466944978L;
	
	/** The image. */
	private JMFVideoImage image;
	
	/** The tex. */
	private Texture tex;
	
	/**
	 * Instantiates a new video quad.
	 *
	 * @param objectName the object name
	 * @param videoResource the video resource
	 * @param pixelsPerUnit the pixels per unit
	 */
	public VideoQuad(String objectName, URL videoResource, float pixelsPerUnit) {
		super(objectName, 1, 1);
		ByteBufferRenderer.printframes = false;
		ByteBufferRenderer.useFOBSOptimization = true;
		ByteBufferRenderer.useFOBSPatch = true;
		
		try {
			image = new JMFVideoImage(videoResource, true, JMFVideoImage.SCALE_MAXIMIZE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		float x = image.getVideoWidth() / pixelsPerUnit;
		float y = image.getVideoHeight() / pixelsPerUnit;
		
		if (image.isFlipped()) {
			resize(x, -y);
		} else {
			resize(x, y);
		}
		
		tex = new Texture2D();
		tex.setMinificationFilter(Texture.MinificationFilter.Trilinear);
		tex.setMagnificationFilter(Texture.MagnificationFilter.Bilinear);
		tex.setImage(image);
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		ts.setEnabled(true);
		ts.setTexture(tex);
		setRenderState(ts);
		image.startMovie();

	}	
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.Updateable#update(float)
	 */
	public void update(float interpolation) {
		if (!image.update(tex, false)) {
			image.waitSome(3);
		}
	}
}

