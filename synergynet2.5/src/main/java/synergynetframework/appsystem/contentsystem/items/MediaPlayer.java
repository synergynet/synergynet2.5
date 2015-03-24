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

package synergynetframework.appsystem.contentsystem.items;

import java.net.URL;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IMediaPlayerImplementation;
import synergynetframework.appsystem.contentsystem.jme.items.JMEMediaPlayer.PlayListener;

/**
 * The Class MediaPlayer.
 */
public class MediaPlayer extends OrthoContainer implements
		IMediaPlayerImplementation {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8279443253333792979L;
	
	/** The is playing. */
	protected boolean isPlaying;

	/** The media url. */
	protected URL mediaURL;

	/** The pixels per unit. */
	protected float pixelsPerUnit = 1;
	
	/**
	 * Instantiates a new media player.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public MediaPlayer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IMediaPlayerImplementation#addPlayerListener(synergynetframework.appsystem
	 * .contentsystem.jme.items.JMEMediaPlayer.PlayListener)
	 */
	@Override
	public void addPlayerListener(PlayListener l) {
		((IMediaPlayerImplementation) this.contentItemImplementation)
				.addPlayerListener(l);
	}
	
	/**
	 * Gets the media url.
	 *
	 * @return the media url
	 */
	public URL getMediaURL() {
		return mediaURL;
	}

	// setters - each must call stateChanged

	/**
	 * Gets the pixels per unit.
	 *
	 * @return the pixels per unit
	 */
	public float getPixelsPerUnit() {
		return pixelsPerUnit;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#getPlayerFrame()
	 */
	@Override
	public OrthoContentItem getPlayerFrame() {
		return ((IMediaPlayerImplementation) this.contentItemImplementation)
				.getPlayerFrame();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#getVideoPlayer()
	 */
	@Override
	public VideoPlayer getVideoPlayer() {
		return ((IMediaPlayerImplementation) this.contentItemImplementation)
				.getVideoPlayer();
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

	// convenience methods
	/**
	 * Play.
	 */
	public void play() {
		setPlaying(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IMediaPlayerImplementation#removePlayListener(synergynetframework.appsystem
	 * .contentsystem.jme.items.JMEMediaPlayer.PlayListener)
	 */
	@Override
	public void removePlayListener(PlayListener l) {
		((IMediaPlayerImplementation) this.contentItemImplementation)
				.removePlayListener(l);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#removePlayListeners()
	 */
	@Override
	public void removePlayListeners() {
		((IMediaPlayerImplementation) this.contentItemImplementation)
				.removePlayListeners();
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IQuadContentItemImplementation#setAutoFitSize(boolean)
	 */
	@Override
	public void setAutoFitSize(boolean isEnabled) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IQuadContentItemImplementation#setHeight(int)
	 */
	@Override
	public void setHeight(int height) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#setMediaURL(java.net.URL)
	 */
	public void setMediaURL(URL url) {
		this.mediaURL = url;
		((IMediaPlayerImplementation) this.contentItemImplementation)
				.setMediaURL(url);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#setPixelsPerUnit(float)
	 */
	public void setPixelsPerUnit(float ppu) {
		pixelsPerUnit = ppu;
		((IMediaPlayerImplementation) this.contentItemImplementation)
				.setPixelsPerUnit(ppu);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IMediaPlayerImplementation#setPlaying(boolean)
	 */
	public void setPlaying(boolean b) {
		isPlaying = b;
		((IMediaPlayerImplementation) this.contentItemImplementation)
				.setPlaying(b);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IQuadContentItemImplementation#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
	}
	
	/**
	 * Stop.
	 */
	public void stop() {
		setPlaying(false);
	}
	
}
