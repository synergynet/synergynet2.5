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


/**
 * The listener interface for receiving orthoControlPointRotateTranslateScaleWith events.
 * The class that is interested in processing a orthoControlPointRotateTranslateScaleWith
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addOrthoControlPointRotateTranslateScaleWithListener<code> method. When
 * the orthoControlPointRotateTranslateScaleWith event occurs, that object's appropriate
 * method is invoked.
 *
 * @see OrthoControlPointRotateTranslateScaleWithEvent
 */
public class OrthoControlPointRotateTranslateScaleWithListener extends OrthoControlPointRotateTranslateScale{

	/** The listeners. */
	protected List<OrthoControlPointRotateTranslateScaleListener> listeners = new ArrayList<OrthoControlPointRotateTranslateScaleListener>();
	
	/** The last pressed event. */
	protected MultiTouchCursorEvent lastPressedEvent;
	
	/** The last clicked event. */
	protected MultiTouchCursorEvent lastClickedEvent;
	
	/** The last changed event. */
	protected MultiTouchCursorEvent lastChangedEvent;
	
	/** The last released event. */
	protected MultiTouchCursorEvent lastReleasedEvent;
	
	/** The is rotate translate scale enabled. */
	boolean isRotateTranslateScaleEnabled = true;
	
	/**
	 * Instantiates a new ortho control point rotate translate scale with listener.
	 *
	 * @param pickingAndTargetSpatial the picking and target spatial
	 */
	public OrthoControlPointRotateTranslateScaleWithListener(
			Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}
	
	/**
	 * Instantiates a new ortho control point rotate translate scale with listener.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 */
	public OrthoControlPointRotateTranslateScaleWithListener(
			Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#cursorClicked(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorClicked(c, event);
		this.lastClickedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorClicked(this, c, event);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {	
		super.cursorPressed(c, event);
		this.lastPressedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorPressed(this, c, event);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorReleased(c, event);
		this.lastReleasedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorReleased(this, c, event);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if(isRotateTranslateScaleEnabled)		
			super.cursorChanged(c, event);
		this.lastChangedEvent = event;
		for(OrthoControlPointRotateTranslateScaleListener l : listeners)
			l.cursorChanged(this, c, event);
	}
	
	/**
	 * Adds the multi touch listener.
	 *
	 * @param l the l
	 */
	public void addMultiTouchListener(OrthoControlPointRotateTranslateScaleListener l) {
		if(!listeners.contains(l))
			listeners.add(l);
	}
	
	/**
	 * Removes the multi touch listener.
	 *
	 * @param l the l
	 */
	public void removeMultiTouchListener(MultiTouchButtonListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Removes the multi touch listeners.
	 */
	public void removeMultiTouchListeners(){
		listeners.clear();
	}
	
	/**
	 * The listener interface for receiving orthoControlPointRotateTranslateScale events.
	 * The class that is interested in processing a orthoControlPointRotateTranslateScale
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOrthoControlPointRotateTranslateScaleListener<code> method. When
	 * the orthoControlPointRotateTranslateScale event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OrthoControlPointRotateTranslateScaleEvent
	 */
	public interface OrthoControlPointRotateTranslateScaleListener {
		
		/**
		 * Cursor pressed.
		 *
		 * @param ocprts the ocprts
		 * @param c the c
		 * @param event the event
		 */
		public void cursorPressed(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
		
		/**
		 * Cursor changed.
		 *
		 * @param ocprts the ocprts
		 * @param c the c
		 * @param event the event
		 */
		public void cursorChanged(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
		
		/**
		 * Cursor released.
		 *
		 * @param ocprts the ocprts
		 * @param c the c
		 * @param event the event
		 */
		public void cursorReleased(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
		
		/**
		 * Cursor clicked.
		 *
		 * @param ocprts the ocprts
		 * @param c the c
		 * @param event the event
		 */
		public void cursorClicked(OrthoControlPointRotateTranslateScaleWithListener ocprts, ScreenCursor c, MultiTouchCursorEvent event);
	}
	
	/**
	 * Gets the last pressed event.
	 *
	 * @return the last pressed event
	 */
	public MultiTouchCursorEvent getLastPressedEvent() {
		return lastPressedEvent;
	}

	/**
	 * Sets the last pressed event.
	 *
	 * @param lastPressedEvent the new last pressed event
	 */
	public void setLastPressedEvent(MultiTouchCursorEvent lastPressedEvent) {
		this.lastPressedEvent = lastPressedEvent;
	}

	/**
	 * Gets the last clicked event.
	 *
	 * @return the last clicked event
	 */
	public MultiTouchCursorEvent getLastClickedEvent() {
		return lastClickedEvent;
	}

	/**
	 * Sets the last clicked event.
	 *
	 * @param lastClickedEvent the new last clicked event
	 */
	public void setLastClickedEvent(MultiTouchCursorEvent lastClickedEvent) {
		this.lastClickedEvent = lastClickedEvent;
	}

	/**
	 * Gets the last changed event.
	 *
	 * @return the last changed event
	 */
	public MultiTouchCursorEvent getLastChangedEvent() {
		return lastChangedEvent;
	}

	/**
	 * Sets the last changed event.
	 *
	 * @param lastChangedEvent the new last changed event
	 */
	public void setLastChangedEvent(MultiTouchCursorEvent lastChangedEvent) {
		this.lastChangedEvent = lastChangedEvent;
	}

	/**
	 * Gets the last released event.
	 *
	 * @return the last released event
	 */
	public MultiTouchCursorEvent getLastReleasedEvent() {
		return lastReleasedEvent;
	}

	/**
	 * Sets the last released event.
	 *
	 * @param lastReleasedEvent the new last released event
	 */
	public void setLastReleasedEvent(MultiTouchCursorEvent lastReleasedEvent) {
		this.lastReleasedEvent = lastReleasedEvent;
	}
	
	/**
	 * Gets the drag distance from cursor pressed event.
	 *
	 * @return the drag distance from cursor pressed event
	 */
	public double getDragDistanceFromCursorPressedEvent() {
		if(this.lastChangedEvent != null && this.lastPressedEvent != null) {
			return lastChangedEvent.getPosition().distance(lastPressedEvent.getPosition());
		}
		
		return Double.NaN;
	}

	/** The props. */
	private Properties props = new Properties();
	
	/**
	 * Sets the property.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void setProperty(String key, Object value) {
		props.put(key, value);		
	}
	
	/**
	 * Gets the property.
	 *
	 * @param key the key
	 * @return the property
	 */
	public Object getProperty(String key) {
		return props.get(key);
	}
	
	/**
	 * Enable rotate translate scale.
	 *
	 * @param isEnabled the is enabled
	 */
	public void enableRotateTranslateScale(boolean isEnabled){
		isRotateTranslateScaleEnabled = isEnabled;
	}
	
	/**
	 * Checks if is rotate translate scale enabled.
	 *
	 * @return true, if is rotate translate scale enabled
	 */
	public boolean isRotateTranslateScaleEnabled(){
		return isRotateTranslateScaleEnabled;
	}
}

