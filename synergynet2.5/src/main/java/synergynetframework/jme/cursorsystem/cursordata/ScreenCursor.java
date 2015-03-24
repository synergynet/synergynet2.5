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

package synergynetframework.jme.cursorsystem.cursordata;

import java.util.ArrayList;

import synergynetframework.jme.pickingsystem.data.PickResultData;


/**
 * Coordinate sytem for this class is jme screen coordinates.
 *
 * @author dcs0ah1
 */

public class ScreenCursor {
	
	/** The Constant EPSILON. */
	private static final float EPSILON = 0.1f;
	
	/** The id. */
	protected long id;
	
	/** The screen positions. */
	protected ArrayList<ScreenCursorRecord> screenPositions;
	
	/** The cursor created time. */
	protected long cursorCreatedTime;
	
	/** The pick result. */
	protected PickResultData pickResult;
	
	/** The old cursor. */
	protected ScreenCursorRecord oldCursor;
	

	/**
	 * Instantiates a new screen cursor.
	 *
	 * @param id the id
	 */
	public ScreenCursor(long id) {
		this.id = id;
		screenPositions = new ArrayList<ScreenCursorRecord>(100);
		cursorCreatedTime = System.nanoTime();
	}

	/**
	 * Adds the position item.
	 *
	 * @param item the item
	 */
	public void addPositionItem(ScreenCursorRecord item) {
		screenPositions.add(item);
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getID() {
		return id;
	}
	
	/**
	 * Gets the cursor origin.
	 *
	 * @return the cursor origin
	 */
	public ScreenCursorRecord getCursorOrigin() {
		if(screenPositions.size() <= 0) return null;
		return screenPositions.get(0);
	}
	
	/**
	 * Get current position in jME Screen Coordinates.
	 *
	 * @return the current cursor screen position
	 */
	public ScreenCursorRecord getCurrentCursorScreenPosition() {
		if(screenPositions.size() <= 0) return null;		
		return screenPositions.get(screenPositions.size() - 1);
	}
	
	/**
	 * Gets the old cursor screen position.
	 *
	 * @return the old cursor screen position
	 */
	public ScreenCursorRecord getOldCursorScreenPosition() {
		if (oldCursor!=null)
			return oldCursor;
		else
			return screenPositions.get(screenPositions.size() - 1);
	}
	
	/**
	 * Sets the old cursor screen position.
	 *
	 * @param c the new old cursor screen position
	 */
	public void setOldCursorScreenPosition(ScreenCursorRecord c){
		oldCursor=c;
	}
	
	
	/**
	 * Gets the current position index.
	 *
	 * @return the current position index
	 */
	public int getCurrentPositionIndex() {
		return screenPositions.size() - 1;
	}
	
	/**
	 * Get position at specified index in jME Screen Coordinates.
	 *
	 * @param i the i
	 * @return the position at index
	 */
	public ScreenCursorRecord getPositionAtIndex(int i) {
		return screenPositions.get(i);
	}
	
	/**
	 * Gets the points.
	 *
	 * @return the points
	 */
	public ArrayList<ScreenCursorRecord> getPoints() {
		return screenPositions;
	}

	/**
	 * Gets the num points.
	 *
	 * @return the num points
	 */
	public int getNumPoints() {
		return screenPositions.size();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return (int)id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj instanceof ScreenCursor) {
			return obj.hashCode() == hashCode();
		}else
			return false;
	}
	
	/**
	 * Gets the cursor created time.
	 *
	 * @return the cursor created time
	 */
	public long getCursorCreatedTime() {
		return this.cursorCreatedTime;
	}
	
	/**
	 * Gets the cursor live time.
	 *
	 * @return the cursor live time
	 */
	public long getCursorLiveTime() {
		return System.nanoTime() - this.cursorCreatedTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return id + "@" + getCurrentCursorScreenPosition();
	}

	/**
	 * Checks if is probably the same as.
	 *
	 * @param c the c
	 * @return true, if is probably the same as
	 */
	public boolean isProbablyTheSameAs(ScreenCursor c) {
		float dx = Math.abs(this.getCurrentCursorScreenPosition().x - c.getCurrentCursorScreenPosition().x);
		float dy = Math.abs(this.getCurrentCursorScreenPosition().y - c.getCurrentCursorScreenPosition().y);
		if(dx < EPSILON && dy < EPSILON)
			return true;
		else
			return false;
	}

	/**
	 * Sets the pick result.
	 *
	 * @param pr the new pick result
	 */
	public void setPickResult(PickResultData pr) {
		this.pickResult = pr;
	}

	/**
	 * Gets the pick result.
	 *
	 * @return the pick result
	 */
	public PickResultData getPickResult() {
		return pickResult;
	}
}
