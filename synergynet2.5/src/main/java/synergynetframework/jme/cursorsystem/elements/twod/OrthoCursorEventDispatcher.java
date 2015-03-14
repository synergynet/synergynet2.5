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

package synergynetframework.jme.cursorsystem.elements.twod;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.TwoDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.RightClickDetector;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

public class OrthoCursorEventDispatcher extends TwoDMultiTouchElement{
		
	protected List<CommonCursorEventListener> listeners = new ArrayList<CommonCursorEventListener>();
	protected float rightClickDistance = 10f;
	
	public OrthoCursorEventDispatcher(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);		
	}
	
	public OrthoCursorEventDispatcher(Spatial pickingSpatial,
			Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}
	
	public void setRightClickDistance(float rightClickDistance){
		this.rightClickDistance = rightClickDistance;
	}
	
	public void addMultiTouchListener(CommonCursorEventListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}
	
	public void removeMultiTouchListener(CommonCursorEventListener l) {
		listeners.remove(l);
	}
		
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		for (CommonCursorEventListener l: listeners)
			l.cursorChanged(this, c, event);
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		for (CommonCursorEventListener l: listeners){
			l.cursorClicked(this, c, event);
			if (RightClickDetector.isDoubleClick(screenCursors, c, rightClickDistance)){
				l.cursorRightClicked(this, c, event);
			}
		}
	
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		for (CommonCursorEventListener l: listeners)
			l.cursorPressed(this, c, event);		
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		for (CommonCursorEventListener l: listeners)
			l.cursorReleased(this, c, event);		
	}
	
	public interface CommonCursorEventListener {
		public void cursorPressed(OrthoCursorEventDispatcher commonCursorEventDispatcher, ScreenCursor c, MultiTouchCursorEvent event);
		public void cursorChanged(OrthoCursorEventDispatcher commonCursorEventDispatcher, ScreenCursor c, MultiTouchCursorEvent event);
		public void cursorReleased(OrthoCursorEventDispatcher commonCursorEventDispatcher, ScreenCursor c, MultiTouchCursorEvent event);
		public void cursorClicked(OrthoCursorEventDispatcher commonCursorEventDispatcher, ScreenCursor c, MultiTouchCursorEvent event);
		public void cursorRightClicked(OrthoCursorEventDispatcher commonCursorEventDispatcher, ScreenCursor c, MultiTouchCursorEvent event);
	}

}


