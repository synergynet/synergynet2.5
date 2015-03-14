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

package apps.threedbuttonsexperiment.gesture;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


public class MultiTouchButtonPress extends ThreeDMultiTouchElement {
	
	private float buttonHeight;
	
	protected List<FreeButtonListener> listeners = new ArrayList<FreeButtonListener>();
	
	public MultiTouchButtonPress(Spatial s) {
		super(s);
	}
	
	public MultiTouchButtonPress(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}
	
	public float getButtonHeight() {
		return buttonHeight;
	}

	public void setButtonHeight(float buttonHeight) {
		this.buttonHeight = buttonHeight;
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}
	
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {	
		for (FreeButtonListener l:listeners){
			l.buttonPressed();
		}
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		for (FreeButtonListener l:listeners){
			l.buttonReleased();
		}		
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {		
		
	}
	
	public interface FreeButtonListener{
		public void buttonPressed();
		public void buttonReleased();
	}
	
	public void addButtonListener(FreeButtonListener listener){
		this.listeners.add(listener);
	}
	
}
