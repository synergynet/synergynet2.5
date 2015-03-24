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

package synergynetframework.mtinput.simulator;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.mtinput.ClickDetector;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputSource;



/**
 * Default implementation of the jME simulator system.  Left click to
 * simulate a single finger.  Right click, drag and release to simulate
 * two fingers that can then be moved (mouse movement), rotated around
 * their centre (using SHIFT) or scaled in and out (using CTRL).
 * 
 * @author ashatch
 *
 */
public abstract class AbstractMultiTouchSimulator implements IMultiTouchSimulator, IMultiTouchInputSource {

	/** The current cursor. */
	protected AbstractSimCursor currentCursor;
	
	/** The cursor id. */
	protected int cursorID;
	
	/** The table width. */
	protected int tableWidth;
	
	/** The table height. */
	protected int tableHeight;
	
	/** The listeners. */
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();	
	
	/** The click detector. */
	protected ClickDetector clickDetector = new ClickDetector(500, 0.02f);
	
	/**
	 * Instantiates a new abstract multi touch simulator.
	 *
	 * @param tableWidth the table width
	 * @param tableHeight the table height
	 */
	public AbstractMultiTouchSimulator(int tableWidth, int tableHeight) {		
		this.tableWidth = tableWidth;
		this.tableHeight = tableHeight;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#registerMultiTouchEventListener(synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#registerMultiTouchEventListener(synergynetframework.mtinput.IMultiTouchEventListener, int)
	 */
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index) {
		if(!listeners.contains(listener)) listeners.add(index, listener);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#unregisterMultiTouchEventListener(synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener) {
		listeners.remove(listener);
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputSource#setClickSensitivity(long, float)
	 */
	public void setClickSensitivity(long time, float distance) {
		clickDetector.setSensitivity(time, distance);
	}

	
	/**
	 * Start.
	 */
	public void start() {}
	
	/**
	 * Stop.
	 */
	public void stop() {}
	

	/**
	 * Mouse pressed.
	 *
	 * @param x the x
	 * @param y the y
	 * @param button the button
	 */
	public void mousePressed(int x, int y, int button) {
		if(currentCursor != null) {
			currentCursor.mousePressed(x, y, button);
			return;
		}

		switch(button) {
		case AbstractSimCursor.MOUSE_BUTTON_LEFT: {
			currentCursor = new SingleFingerSimCursor(this, getNewCursorID(), tableWidth, tableHeight);
			currentCursor.mousePressed(x, y, button);
			break;
		}		
		case AbstractSimCursor.MOUSE_BUTTON_MIDDLE: {
			currentCursor = new TripleFingerSimCursor(this, getNewCursorID(), getNewCursorID(), getNewCursorID(), tableWidth, tableHeight);
			currentCursor.mousePressed(x, y, button);
			break;
		}
		
		case AbstractSimCursor.MOUSE_BUTTON_RIGHT: {
			// right mouse button
			currentCursor = new DoubleFingerSimCursor(this, getNewCursorID(), getNewCursorID(), tableWidth, tableHeight);
			currentCursor.mousePressed(x, y, button);
			break;
		}

		}
		
	}

	/**
	 * Mouse released.
	 *
	 * @param x the x
	 * @param y the y
	 * @param button the button
	 */
	public void mouseReleased(int x, int y, int button) {
		if(currentCursor == null) return;
		currentCursor.mouseReleased(x, y, button);		
	}

	/**
	 * Mouse dragged.
	 *
	 * @param x the x
	 * @param y the y
	 * @param button the button
	 */
	public void mouseDragged(int x, int y, int button) {
		if(currentCursor == null) return;
		currentCursor.mouseDragged(x, y, button);	
	}

	/**
	 * Mouse moved.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void mouseMoved(int x, int y) {
		if(currentCursor == null) return;
		currentCursor.mouseMoved(x, y);
	}

	/**
	 * Key pressed.
	 *
	 * @param key the key
	 */
	public void keyPressed(String key) {
		if(currentCursor == null) return;
		currentCursor.keyPressed(key);		
	}

	/**
	 * Key released.
	 *
	 * @param key the key
	 */
	public void keyReleased(String key) {
		if(currentCursor == null) return;
		currentCursor.keyReleased(key);
	}

	/**
	 * Gets the current cursor.
	 *
	 * @return the current cursor
	 */
	public AbstractSimCursor getCurrentCursor() {
		return currentCursor;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.IMultiTouchSimulator#clearCursor()
	 */
	public void clearCursor() {
		currentCursor = null;		
	}

	/**
	 * Gets the new cursor id.
	 *
	 * @return the new cursor id
	 */
	public int getNewCursorID() {		
		return cursorID++;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.IMultiTouchSimulator#updateCursor(int, float, float)
	 */
	public abstract void updateCursor(int id, float x, float y);
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.IMultiTouchSimulator#updateTwoCursors(int, float, float, int, float, float)
	 */
	public abstract void updateTwoCursors(int id1, float x, float y, int id2, float x2, float y2);
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.IMultiTouchSimulator#deleteCursor(int, float, float)
	 */
	public abstract void deleteCursor(int id, float x, float y);
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.IMultiTouchSimulator#deleteTwoCursors(int, float, float, int, float, float)
	 */
	public abstract void deleteTwoCursors(int id1, float x1, float y1, int id2, float x2, float y2);
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.IMultiTouchSimulator#newCursor(int, float, float)
	 */
	public abstract void newCursor(int id, float x, float y);
}
