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

package synergynetframework.mtinput.filters;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;


import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputFilter;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

public class AveragingFilter implements IMultiTouchInputFilter {

	public static final String CURSOR_PRESSED = "CURSOR_PRESSED";
	public static final String CURSOR_CLICKED = "CURSOR_CLICKED";
	public static final String CURSOR_RELEASED = "CURSOR_RELEASED";
	public static final String CURSOR_CHANGED = "CURSOR_CHANGED";
	public static final String OBJECT_ADDED = "OBJECT_ADDED";
	public static final String OBJECT_CHANGED = "OBJECT_CHANGED";
	public static final String OBJECT_REMOVED = "OBJECT_REMOVED";
	
	private IMultiTouchEventListener next;

	//Averaging
	protected static final int POINT_HISTORY_SIZE = 8;
	protected static final float FILTER_FACTOR = 0.4f;
	protected Map<Long, Point2D.Float[]> pointHistory = new HashMap<Long,Point2D.Float[]>();
	
	public AveragingFilter() {

	}

	public void setNext(IMultiTouchEventListener el) {
		this.next = el;		
	}

	public void cursorChanged(MultiTouchCursorEvent event) {
		updatePointHistory(event.getCursorID(), event.getPosition());
		event.getPosition().setLocation(filteredLocation(event.getCursorID()));
		next.cursorChanged(event);
	}

	public void cursorClicked(MultiTouchCursorEvent event) {
		next.cursorClicked(event);
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		Point2D.Float[] pointArray = new Point2D.Float[POINT_HISTORY_SIZE];
		for (int i=0; i<POINT_HISTORY_SIZE; i++) pointArray[i] = new Point2D.Float(event.getPosition().x, event.getPosition().y);
		pointHistory.put(event.getCursorID(), pointArray);		
		next.cursorPressed(event);		
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		pointHistory.remove(event.getCursorID());
		next.cursorReleased(event);
	}

	public void objectAdded(MultiTouchObjectEvent event) {
		next.objectAdded(event);
	}

	public void objectChanged(MultiTouchObjectEvent event) {
		next.objectChanged(event);		
	}

	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	private void updatePointHistory(long cursorID, Point2D.Float lastPoint) {
		Point2D.Float[] history = pointHistory.get(cursorID);
		for (int i=history.length-1; i>0; i--) {
			history[i] = history[i-1];
		}
		history[0] = lastPoint;
	}	
	
	private Point2D.Float filteredLocation(long cursorID) {
		Point2D.Float[] history = pointHistory.get(cursorID);
		float weight = 1.0f;
		float avgX = history[0].x;
		float avgY = history[0].y;
		for (int i=0; i<history.length; i++) {
			avgX = (history[i].x * weight) + (avgX * (1.0f - weight));
			avgY = (history[i].y * weight) + (avgY * (1.0f - weight));
			weight *= FILTER_FACTOR;
		}
		return new Point2D.Float(avgX, avgY);		
	}

	public void update(float tpf) {}
	
}
