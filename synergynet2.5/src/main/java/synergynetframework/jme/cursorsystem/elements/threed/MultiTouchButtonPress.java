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

package synergynetframework.jme.cursorsystem.elements.threed;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;



/**
 * The Class MultiTouchButtonPress.
 */
public class MultiTouchButtonPress extends ThreeDMultiTouchElement {
	
	/** The button height. */
	private float buttonHeight;
	
	/** The listeners. */
	protected List<FreeButtonListener> listeners = new ArrayList<FreeButtonListener>();
	
	/**
	 * Instantiates a new multi touch button press.
	 *
	 * @param s the s
	 */
	public MultiTouchButtonPress(Spatial s) {
		super(s);
	}
	
	/**
	 * Instantiates a new multi touch button press.
	 *
	 * @param pickSpatial the pick spatial
	 * @param targetSpatial the target spatial
	 */
	public MultiTouchButtonPress(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}
	
	/**
	 * Gets the button height.
	 *
	 * @return the button height
	 */
	public float getButtonHeight() {
		return buttonHeight;
	}

	/**
	 * Sets the button height.
	 *
	 * @param buttonHeight the new button height
	 */
	public void setButtonHeight(float buttonHeight) {
		this.buttonHeight = buttonHeight;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		targetSpatial.getLocalTranslation().z -=this.buttonHeight*2.5f;
		
		for (FreeButtonListener l:listeners){
			l.buttonPressed();
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		targetSpatial.getLocalTranslation().z +=this.buttonHeight*2.5f;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {		
		
	}
	
	/**
	 * The listener interface for receiving freeButton events.
	 * The class that is interested in processing a freeButton
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addFreeButtonListener<code> method. When
	 * the freeButton event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see FreeButtonEvent
	 */
	public interface FreeButtonListener{
		
		/**
		 * Button pressed.
		 */
		public void buttonPressed();
	}
	
	/**
	 * Adds the button listener.
	 *
	 * @param listener the listener
	 */
	public void addButtonListener(FreeButtonListener listener){
		this.listeners.add(listener);
	}
	
}
