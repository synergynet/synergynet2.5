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
import java.util.Properties;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.elements.MultiTouchButton.MultiTouchButtonListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.scene.Spatial;

public class OrthoControlPointRotateTranslateScaleWithListener extends OrthoControlPointRotateTranslateScale{

	protected List<OrthoControlPointRotateTranslateScaleListener> listeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
	protected MultiTouchCursorEvent lastPressedEvent;
	protected MultiTouchCursorEvent lastClickedEvent;
	protected MultiTouchCursorEvent lastChangedEvent;
	protected MultiTouchCursorEvent lastReleasedEvent;
	
	boolean isRotateTranslateScaleEnabled = true;
	
	public OrthoControlPointRotateTranslateScaleWithListener(
			Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}
	
	public OrthoControlPointRotateTranslateScaleWithListener(
			Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}
	
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorClicked(c, event);
		this.lastClickedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorClicked(this, c, event);
	}
	
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {	
		super.cursorPressed(c, event);
		this.lastPressedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorPressed(this, c, event);
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorReleased(c, event);
		this.lastReleasedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorReleased(this, c, event);
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if(isRotateTranslateScaleEnabled)		
			super.cursorChanged(c, event);
		this.lastChangedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorChanged(this, c, event);
	}
	
	public void addMultiTouchListener(OrthoControlPointRotateTranslateScaleListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}
	
	public void removeMultiTouchListener(MultiTouchButtonListener l) {
		listeners.remove(l);
	}
	
	public void removeMultiTouchListeners(){
		listeners.clear();
	}
	
	public interface OrthoControlPointRotateTranslateScaleListener {
		public void cursorPressed(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
		public void cursorChanged(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
		public void cursorReleased(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
		public void cursorClicked(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
	}
	
	public MultiTouchCursorEvent getLastPressedEvent() {
		return lastPressedEvent;
	}

	public void setLastPressedEvent(MultiTouchCursorEvent lastPressedEvent) {
		this.lastPressedEvent = lastPressedEvent;
	}

	public MultiTouchCursorEvent getLastClickedEvent() {
		return lastClickedEvent;
	}

	public void setLastClickedEvent(MultiTouchCursorEvent lastClickedEvent) {
		this.lastClickedEvent = lastClickedEvent;
	}

	public MultiTouchCursorEvent getLastChangedEvent() {
		return lastChangedEvent;
	}

	public void setLastChangedEvent(MultiTouchCursorEvent lastChangedEvent) {
		this.lastChangedEvent = lastChangedEvent;
	}

	public MultiTouchCursorEvent getLastReleasedEvent() {
		return lastReleasedEvent;
	}

	public void setLastReleasedEvent(MultiTouchCursorEvent lastReleasedEvent) {
		this.lastReleasedEvent = lastReleasedEvent;
	}
	
	public double getDragDistanceFromCursorPressedEvent() {
		if(this.lastChangedEvent != null && this.lastPressedEvent != null) {
			return lastChangedEvent.getPosition().distance(lastPressedEvent.getPosition());
		}
		
		return Double.NaN;
	}

	private Properties props = new Properties();
	
	public void setProperty(String key, Object value) {
		props.put(key, value);		
	}
	
	public Object getProperty(String key) {
		return props.get(key);
	}
	
	public void enableRotateTranslateScale(boolean isEnabled){
		isRotateTranslateScaleEnabled = isEnabled;
	}
	
	public boolean isRotateTranslateScaleEnabled(){
		return isRotateTranslateScaleEnabled;
	}
}

