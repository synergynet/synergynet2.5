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

import synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener;


/**
 * The Interface IControlBarImplementation.
 */
public interface IControlBarImplementation extends IOrthoContainerImplementation {

	/**
	 * Sets the control bar length.
	 *
	 * @param controlBarLength the new control bar length
	 */
	public void setControlBarLength(float controlBarLength);
	
	/**
	 * Sets the control bar width.
	 *
	 * @param controlBarWidth the new control bar width
	 */
	public void setControlBarWidth(float controlBarWidth);
	
	/**
	 * Sets the current position.
	 *
	 * @param currentPosition the new current position
	 */
	public void setCurrentPosition(float currentPosition);
	
	/**
	 * Adds the control bar mover listener.
	 *
	 * @param l the l
	 */
	public void addControlBarMoverListener(ControlBarMoverListener l);
	
	/**
	 * Sets the control bar mover enabled.
	 *
	 * @param controlBarMoverEnabled the new control bar mover enabled
	 */
	public void setControlBarMoverEnabled(boolean controlBarMoverEnabled);
	
	/**
	 * Sets the bar image resource.
	 *
	 * @param imageResource the new bar image resource
	 */
	public void setBarImageResource(URL imageResource);
	
	/**
	 * Sets the finished bar image resource.
	 *
	 * @param imageResource the new finished bar image resource
	 */
	public void setFinishedBarImageResource(URL imageResource);
	
	/**
	 * Sets the cursor image resource.
	 *
	 * @param imageResource the new cursor image resource
	 */
	public void setCursorImageResource(URL imageResource);
	
	/**
	 * Update control bar.
	 */
	public void updateControlBar();
	
	
}
