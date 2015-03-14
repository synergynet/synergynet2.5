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

public class VideoPlayer extends QuadContentItem implements IVideoPlayerImplementation {
	
	private static final long serialVersionUID = -8279443252885792979L;	

	protected URL videoURL;
	protected boolean isPlaying = true;
	protected float pixelsPerUnit = 1;
	public double videoTime = 0;
		
	public VideoPlayer(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	// convenience methods
	public void play() {
		setPlaying(true);
	}	

	public void stop() {
		setPlaying(false);		
	}
	
	// setters - each must call stateChanged
	
	public void setVideoURL(URL url) {
		this.videoURL = url;
		((IVideoPlayerImplementation)this.contentItemImplementation).setVideoURL(url);
	}
	
	public void setPlaying(boolean b) {
		isPlaying = b;	
		((IVideoPlayerImplementation)this.contentItemImplementation).setPlaying(b);
	}	
	
	public void setPixelsPerUnit(float ppu) {
		pixelsPerUnit = ppu;
		((IVideoPlayerImplementation)this.contentItemImplementation).setPixelsPerUnit(ppu);
	}
	
	// getters	
	public boolean isPlaying() {
		return isPlaying;
	}
	
	public URL getVideoURL() {
		return videoURL;
	}

	public float getPixelsPerUnit() {
		return pixelsPerUnit;
	}

	@Override
	public Object getMediaImplementationObject() {
		return ((IVideoPlayerImplementation)this.contentItemImplementation).getMediaImplementationObject();
	}

	@Override
	public int getHeight() {
		return (int)(((IVideoPlayerImplementation)this.contentItemImplementation).getVideoHeight()/this.pixelsPerUnit);
	}

	@Override
	public int getWidth() {
		return (int)(((IVideoPlayerImplementation)this.contentItemImplementation).getVideoWidth()/this.pixelsPerUnit);
		
	}

	@Override
	public float getVideoHeight() {
		return ((IVideoPlayerImplementation)this.contentItemImplementation).getVideoHeight();
	}

	@Override
	public float getVideoWidth() {
		return ((IVideoPlayerImplementation)this.contentItemImplementation).getVideoWidth();
	}
	
	@Override
	public void update(float tpf){
		videoTime = ((IVideoPlayerImplementation)this.contentItemImplementation).getVideoTime();
		super.update(tpf);
	}

	@Override
	public double getVideoTime() {
		return videoTime;
	}

	@Override
	public void setVideoTime(double seconds) {
		videoTime = seconds;
		((IVideoPlayerImplementation)this.contentItemImplementation).setVideoTime(seconds);
	}
}
