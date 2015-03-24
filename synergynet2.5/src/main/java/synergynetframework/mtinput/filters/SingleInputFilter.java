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

package synergynetframework.mtinput.filters;

import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputFilter;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

/**
 * Ensures only one cursor is active at any one time.
 *
 * @author dcs0ah1
 */
public class SingleInputFilter implements IMultiTouchInputFilter {

	/** The Constant INVALID_CURSOR. */
	private static final int INVALID_CURSOR = Integer.MIN_VALUE;

	/** The active cursor. */
	private long activeCursor = INVALID_CURSOR;

	/** The next. */
	private IMultiTouchEventListener next;
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
		if (event.getCursorID() == activeCursor) {
			next.cursorChanged(event);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
		if (event.getCursorID() == activeCursor) {
			next.cursorClicked(event);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		if (activeCursor == INVALID_CURSOR) {
			activeCursor = event.getCursorID();
			next.cursorPressed(event);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		if (event.getCursorID() == activeCursor) {
			activeCursor = INVALID_CURSOR;
			next.cursorReleased(event);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {
		next.objectAdded(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {
		next.objectChanged(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {
		next.objectRemoved(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputFilter#setNext(
	 * synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void setNext(IMultiTouchEventListener el) {
		this.next = el;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputFilter#update(float)
	 */
	public void update(float tpf) {
	}
	
}
