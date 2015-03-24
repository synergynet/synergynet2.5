/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.net.URL;

import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.VideoPlayer;
import synergynetframework.appsystem.contentsystem.jme.items.JMEMediaPlayer.PlayListener;


/**
 * The Interface IMediaPlayerImplementation.
 */
public interface IMediaPlayerImplementation extends IQuadContentItemImplementation {

	/**
	 * Sets the media url.
	 *
	 * @param url the new media url
	 */
	public void setMediaURL(URL url);	
	
	/**
	 * Sets the playing.
	 *
	 * @param b the new playing
	 */
	public void setPlaying(boolean b);	
	
	/**
	 * Sets the pixels per unit.
	 *
	 * @param ppu the new pixels per unit
	 */
	public void setPixelsPerUnit(float ppu);
	
	/**
	 * Gets the player frame.
	 *
	 * @return the player frame
	 */
	public OrthoContentItem getPlayerFrame();
	
	/**
	 * Gets the video player.
	 *
	 * @return the video player
	 */
	public VideoPlayer getVideoPlayer();
	
	/**
	 * Adds the player listener.
	 *
	 * @param l the l
	 */
	public void addPlayerListener(PlayListener l);
	
	/**
	 * Removes the play listener.
	 *
	 * @param l the l
	 */
	public void removePlayListener(PlayListener l);
	
	/**
	 * Removes the play listeners.
	 */
	public void removePlayListeners();
}
