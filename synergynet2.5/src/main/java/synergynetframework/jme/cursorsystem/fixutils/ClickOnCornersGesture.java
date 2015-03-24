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

package synergynetframework.jme.cursorsystem.fixutils;

import synergynetframework.jme.cursorsystem.MultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


/**
 * The Class ClickOnCornersGesture.
 */
public class ClickOnCornersGesture implements FixGesture {
	
	/** The Constant NO_CLICKS_TO_FIX. */
	public static final int NO_CLICKS_TO_FIX = 4;
	
	/** The no clicks. */
	private int noClicks = NO_CLICKS_TO_FIX;
	
	/**
	 * Instantiates a new click on corners gesture.
	 */
	public ClickOnCornersGesture() {}	
	
	/**
	 * Instantiates a new click on corners gesture.
	 *
	 * @param noClicks the no clicks
	 */
	public ClickOnCornersGesture(int noClicks) {
		this.noClicks = noClicks;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.fixutils.FixGesture#checkForFixGesture(synergynetframework.jme.cursorsystem.MultiTouchElement, synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public boolean checkForFixGesture(MultiTouchElement element,
			ScreenCursor c, MultiTouchCursorEvent event) {
		return element.getNumRegisteredCursors() >= noClicks;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.fixutils.FixGesture#checkForUnfixGesture(synergynetframework.jme.cursorsystem.MultiTouchElement, synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public boolean checkForUnfixGesture(MultiTouchElement element,
			ScreenCursor c, MultiTouchCursorEvent event) {
		return element.getNumRegisteredCursors() >= noClicks;
	}

}
