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
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL`ITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.appsystem.contentsystem.jme.items;

import java.net.URL;
import java.util.logging.Logger;

import org.llama.jmf.ByteBufferRenderer;
import org.llama.jmf.JMFVideoImage;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.VideoPlayer;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation;
import synergynetframework.appsystem.contentsystem.items.utils.Background;
import synergynetframework.appsystem.contentsystem.items.utils.Border;
import synergynetframework.appsystem.contentsystem.jme.UpdateableJMEContentItemImplementation;

import com.jme.image.Texture;
import com.jme.image.Texture2D;
import com.jme.image.Texture.ApplyMode;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Quad;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;


/**
 * The Class JMEVideoPlayer.
 */
public class JMEVideoPlayer extends JMEQuadContentItem implements IVideoPlayerImplementation, UpdateableJMEContentItemImplementation {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEVideoPlayer.class.getName());
	
	/** The image. */
	protected JMFVideoImage image;
	
	/** The tex. */
	protected Texture tex;
	
	/** The video quad. */
	protected Quad videoQuad;
	
	/** The item. */
	protected VideoPlayer item;
	
	/**
	 * Instantiates a new JME video player.
	 *
	 * @param item the item
	 */
	public JMEVideoPlayer(ContentItem item) {
		super(item, new Quad(item.getName(), 250, 250));
		this.videoQuad = (Quad)this.getImplementationObject();
		this.item = (VideoPlayer)item;
		ByteBufferRenderer.printframes = false;
		ByteBufferRenderer.useFOBSOptimization = true;
		ByteBufferRenderer.useFOBSPatch = true;

	}	

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEContentItem#init()
	 */
	@Override
	public void init(){
		if(this.item.getVideoURL() != null){
			this.item.setVideoURL(this.item.getVideoURL());
			this.item.setVideoTime(item.getVideoTime());
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#update(float)
	 */
	public void update(float interpolation) {
		super.update(interpolation);
		if(image != null) {
			if (!image.update(tex, false)) {
				image.waitSome(3);
				item.videoTime = image.getMediaTime().getSeconds();
			}
		}
	}

	/**
	 * Load video.
	 *
	 * @param player the player
	 * @param videoResource the video resource
	 */
	private void loadVideo(VideoPlayer player, URL videoResource) {		
		try {
			image = new JMFVideoImage(videoResource, true, JMFVideoImage.SCALE_MAXIMIZE);
			tex = new Texture2D();			
			tex.setMinificationFilter(MinificationFilter.Trilinear);
			tex.setMagnificationFilter(MagnificationFilter.Bilinear);
			tex.setImage(image);
			tex.setApply(ApplyMode.Replace);
			TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
			ts.setEnabled(true);
			ts.setTexture(tex);
			this.videoQuad.setRenderState(ts);
			this.videoQuad.updateRenderState();
		} catch (Exception e) {
			log.warning("Exception occurs when rendering video.\n"+e.toString());
		}
		
		resize();
	}
	
	/**
	 * Resize.
	 */
	protected void resize(){
		if (image!=null && item!=null){
			float x = image.getVideoWidth() / item.getPixelsPerUnit();
			float y = image.getVideoHeight() / item.getPixelsPerUnit();

			if (image.isFlipped()) {
				this.videoQuad.resize(x, -y);
			} else {
				this.videoQuad.resize(x, y);
			}
			image.startMovie();
		}
	}

	/**
	 * Content system changed.
	 *
	 * @param system the system
	 */
	public void contentSystemChanged(ContentSystem system) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.JMEContentItemImplementation#getSpatial()
	 */
	@Override
	public Spatial getSpatial() {
		return this.videoQuad;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setPixelsPerUnit(float)
	 */
	@Override
	public void setPixelsPerUnit(float ppu) {
		resize();
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setPlaying(boolean)
	 */
	@Override
	public void setPlaying(boolean b) {
		if(b) 
			image.startMovie();
		else 
			image.stopMovie();	
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setVideoURL(java.net.URL)
	 */
	@Override
	public void setVideoURL(URL url) {
		loadVideo(item, url);
		
	}
		
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getMediaImplementationObject()
	 */
	public Object getMediaImplementationObject() {
		return image;
	}

	//UNUSED SETTER FOR VIDEO
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem#setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#setBackGround(synergynetframework.appsystem.contentsystem.items.utils.Background)
	 */
	@Override
	public void setBackGround(Background backGround) {
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEOrthoContentItem#setBorder(synergynetframework.appsystem.contentsystem.items.utils.Border)
	 */
	@Override
	public void setBorder(Border border) {
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.jme.items.JMEQuadContentItem#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getVideoHeight()
	 */
	@Override
	public float getVideoHeight() {
		if (image!=null)
			return image.getVideoHeight();
		return 0;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getVideoWidth()
	 */
	@Override
	public float getVideoWidth() {
		if (image!=null)
			return image.getVideoWidth();
		return 0;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getVideoTime()
	 */
	@Override
	public double getVideoTime() {
		if (image!=null)
			return image.getMediaTime().getSeconds();
		return 0;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setVideoTime(double)
	 */
	@Override
	public void setVideoTime(double seconds) {
		image.startsFrom(seconds);
	}
	
}