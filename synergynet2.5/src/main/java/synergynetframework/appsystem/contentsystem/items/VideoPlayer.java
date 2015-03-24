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

package synergynetframework.appsystem.contentsystem.items;

import java.net.URL;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation;


/**
 * The Class VideoPlayer.
 */
public class VideoPlayer extends QuadContentItem implements IVideoPlayerImplementation {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8279443252885792979L;	

	/** The video url. */
	protected URL videoURL;
	
	/** The is playing. */
	protected boolean isPlaying = true;
	
	/** The pixels per unit. */
	protected float pixelsPerUnit = 1;
	
	/** The video time. */
	public double videoTime = 0;
		
	/**
	 * Instantiates a new video player.
	 *
	 * @param contentSystem the content system
	 * @param name the name
	 */
	public VideoPlayer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	// convenience methods
	/**
	 * Play.
	 */
	public void play() {
		setPlaying(true);
	}	

	/**
	 * Stop.
	 */
	public void stop() {
		setPlaying(false);		
	}
	
	// setters - each must call stateChanged
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setVideoURL(java.net.URL)
	 */
	public void setVideoURL(URL url) {
		this.videoURL = url;
		((IVideoPlayerImplementation)this.contentItemImplementation).setVideoURL(url);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setPlaying(boolean)
	 */
	public void setPlaying(boolean b) {
		isPlaying = b;	
		((IVideoPlayerImplementation)this.contentItemImplementation).setPlaying(b);
	}	
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setPixelsPerUnit(float)
	 */
	public void setPixelsPerUnit(float ppu) {
		pixelsPerUnit = ppu;
		((IVideoPlayerImplementation)this.contentItemImplementation).setPixelsPerUnit(ppu);
	}
	
	// getters	
	/**
	 * Checks if is playing.
	 *
	 * @return true, if is playing
	 */
	public boolean isPlaying() {
		return isPlaying;
	}
	
	/**
	 * Gets the video url.
	 *
	 * @return the video url
	 */
	public URL getVideoURL() {
		return videoURL;
	}

	/**
	 * Gets the pixels per unit.
	 *
	 * @return the pixels per unit
	 */
	public float getPixelsPerUnit() {
		return pixelsPerUnit;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getMediaImplementationObject()
	 */
	@Override
	public Object getMediaImplementationObject() {
		return ((IVideoPlayerImplementation)this.contentItemImplementation).getMediaImplementationObject();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.QuadContentItem#getHeight()
	 */
	@Override
	public int getHeight() {
		return (int)(((IVideoPlayerImplementation)this.contentItemImplementation).getVideoHeight()/this.pixelsPerUnit);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.QuadContentItem#getWidth()
	 */
	@Override
	public int getWidth() {
		return (int)(((IVideoPlayerImplementation)this.contentItemImplementation).getVideoWidth()/this.pixelsPerUnit);
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getVideoHeight()
	 */
	@Override
	public float getVideoHeight() {
		return ((IVideoPlayerImplementation)this.contentItemImplementation).getVideoHeight();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getVideoWidth()
	 */
	@Override
	public float getVideoWidth() {
		return ((IVideoPlayerImplementation)this.contentItemImplementation).getVideoWidth();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.ContentItem#update(float)
	 */
	@Override
	public void update(float tpf){
		videoTime = ((IVideoPlayerImplementation)this.contentItemImplementation).getVideoTime();
		super.update(tpf);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#getVideoTime()
	 */
	@Override
	public double getVideoTime() {
		return videoTime;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IVideoPlayerImplementation#setVideoTime(double)
	 */
	@Override
	public void setVideoTime(double seconds) {
		videoTime = seconds;
		((IVideoPlayerImplementation)this.contentItemImplementation).setVideoTime(seconds);
	}
}
