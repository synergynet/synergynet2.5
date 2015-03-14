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

package synergynetframework.awt;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

/**
 * Experimental class to have multi-touch input cause AWT events.
 * @author dcs0ah1
 *
 */
public class MultiTouchEventDispatcher implements IMultiTouchEventListener {

	private Component target;

	public MultiTouchEventDispatcher(Component target) {
		this.target = target;
	}

	public void cursorChanged(MultiTouchCursorEvent evt) {
		int x = (int)(evt.getPosition().x * target.getSize().width);
		int y = (int)(evt.getPosition().y * target.getSize().height);
		
		long when = System.currentTimeMillis();
		boolean popupTrigger = false;
		int button = MouseEvent.BUTTON1;
		
		AWTEvent awtEvent = new MouseEvent(target, MouseEvent.MOUSE_DRAGGED, when, MouseEvent.BUTTON1_DOWN_MASK, x, y, 1, popupTrigger, button);
		target.dispatchEvent(awtEvent);
	}

	public void cursorClicked(MultiTouchCursorEvent arg0) {
	}

	public void cursorPressed(MultiTouchCursorEvent evt) {		
		int x = (int)(evt.getPosition().x * target.getSize().width);
		int y = (int)(evt.getPosition().y * target.getSize().height);		
		long when = System.currentTimeMillis();
		int modifiers = 0;
		boolean popupTrigger = false;
		int button = MouseEvent.BUTTON1;
		
		FocusEvent focusEvent = new FocusEvent(target, FocusEvent.FOCUS_GAINED);
		target.dispatchEvent(focusEvent);		
		
		AWTEvent mouseEntered = new MouseEvent(target, MouseEvent.MOUSE_ENTERED, when + 1, 0, x, y, 0, popupTrigger, button);
		target.dispatchEvent(mouseEntered);
		
		AWTEvent mouseMoved = new MouseEvent(target, MouseEvent.MOUSE_MOVED, when + 2, modifiers, x, y, 0, popupTrigger, button);
		target.dispatchEvent(mouseMoved);
		
		AWTEvent mousePressed = new MouseEvent(target, MouseEvent.MOUSE_PRESSED, when + 3, MouseEvent.BUTTON1_DOWN_MASK, x, y, 0, popupTrigger, button);
		target.dispatchEvent(mousePressed);		
	}

	public void cursorReleased(MultiTouchCursorEvent evt) {
		int x = (int)(evt.getPosition().x * target.getSize().width);
		int y = (int)(evt.getPosition().y * target.getSize().height);		
		long when = System.currentTimeMillis();
		boolean popupTrigger = false;
		int button = MouseEvent.BUTTON1;
		
		AWTEvent awtEvent = new MouseEvent(target, MouseEvent.MOUSE_RELEASED, when, MouseEvent.BUTTON1_DOWN_MASK, x, y, 0, popupTrigger, button);
		target.dispatchEvent(awtEvent);
		
		// TODO: clicking always at the moment - probably don't want to all of the time.
		awtEvent = new MouseEvent(target, MouseEvent.MOUSE_CLICKED, when + 1, 0, x, y, 1, popupTrigger, button);
		target.dispatchEvent(awtEvent);
	}

	public void objectAdded(MultiTouchObjectEvent arg0) {
	}

	public void objectChanged(MultiTouchObjectEvent arg0) {
	}

	public void objectRemoved(MultiTouchObjectEvent arg0) {
	}

}
