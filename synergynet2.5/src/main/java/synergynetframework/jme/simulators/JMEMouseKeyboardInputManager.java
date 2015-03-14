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

package synergynetframework.jme.simulators;


import synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator;
import synergynetframework.mtinput.simulator.AbstractSimCursor;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;
import com.jme.input.MouseInputListener;

public class JMEMouseKeyboardInputManager implements MouseInputListener, KeyInputListener {

	protected AbstractMultiTouchSimulator simulator;
	protected boolean[] buttons = new boolean[5];
	private int height;

	public JMEMouseKeyboardInputManager(AbstractMultiTouchSimulator simulator, int width, int height) {		
		this.height = height;
		this.simulator = simulator;
		for(int i = 0; i < buttons.length; i++) buttons[i] = false; 	
	}

	public void onButton(int button, boolean pressed, int x, int y) {
		int btn = getSimButtonForJMEButton(button);
		buttons[btn] = pressed;
		if(pressed) {
			simulator.mousePressed(x, invert(y), btn);
		}else{
			simulator.mouseReleased(x, invert(y), btn);
		}		
	}

	private int invert(int y) {
		return height - y;
	}

	public void onMove(int delta, int delta2, int newX, int newY) {
		if(noButtonsPressed()) {
			simulator.mouseMoved(newX, invert(newY));	
		}else{
			if(buttons[AbstractSimCursor.MOUSE_BUTTON_LEFT]) {
				simulator.mouseDragged(newX, invert(newY), AbstractSimCursor.MOUSE_BUTTON_LEFT);
			} else if(buttons[AbstractSimCursor.MOUSE_BUTTON_MIDDLE]) {
				simulator.mouseDragged(newX, invert(newY), AbstractSimCursor.MOUSE_BUTTON_MIDDLE);
			} else if(buttons[AbstractSimCursor.MOUSE_BUTTON_RIGHT]) {
				simulator.mouseDragged(newX, invert(newY), AbstractSimCursor.MOUSE_BUTTON_RIGHT);
			}
		}
	}


	private boolean noButtonsPressed() {
		boolean result = false;
		for(int i = 0; i < buttons.length; i++) {
			result = result || buttons[i];

		}
		return !result;
	}

	public void onKey(char character, int keyCode, boolean pressed) {
		if(pressed) {
			if(shiftKeysPressed(keyCode)) {
				simulator.keyPressed(AbstractSimCursor.KEY_SHIFT);
			}else if(controlKeysPressed(keyCode)) {
				simulator.keyPressed(AbstractSimCursor.KEY_CONTROL);		
			}else if(spaceKeyPressed(keyCode)) {
				simulator.keyPressed(AbstractSimCursor.KEY_SPACE);
			}
		}else{
			if(shiftKeysPressed(keyCode)) {
				simulator.keyReleased(AbstractSimCursor.KEY_SHIFT);
			}else if(controlKeysPressed(keyCode)) {
				simulator.keyReleased(AbstractSimCursor.KEY_CONTROL);		
			}else if(spaceKeyPressed(keyCode)) {
				simulator.keyReleased(AbstractSimCursor.KEY_SPACE);
			}			
		}
	}

	//unneeded
	public void onWheel(int wheelDelta, int x, int y) {}

	private int getSimButtonForJMEButton(int button) {
		switch(button) {
		case 0: {
			return AbstractSimCursor.MOUSE_BUTTON_LEFT;
		}

		case 1: {
			return AbstractSimCursor.MOUSE_BUTTON_RIGHT;
		}

		case 2: {
			return AbstractSimCursor.MOUSE_BUTTON_MIDDLE;			
		}

		default: {
			return AbstractSimCursor.MOUSE_BUTTON_LEFT;
		}
		}
	}
	
	private boolean spaceKeyPressed(int keyCode) {
		return keyCode == KeyInput.KEY_SPACE;
	}

	private boolean controlKeysPressed(int keyCode) {
		return keyCode == KeyInput.KEY_LCONTROL || keyCode == KeyInput.KEY_RCONTROL;
	}

	private boolean shiftKeysPressed(int keyCode) {
		return keyCode == KeyInput.KEY_LSHIFT || keyCode == KeyInput.KEY_RSHIFT;
	}
}
