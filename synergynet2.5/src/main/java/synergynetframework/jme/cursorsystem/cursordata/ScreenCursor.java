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
 * Coordinate sytem for this class is jme screen coordinates
 * 
 * @author dcs0ah1
 *
 */

public class ScreenCursor {
	private static final float EPSILON = 0.1f;
	protected long id;
	protected ArrayList<ScreenCursorRecord> screenPositions;
	protected long cursorCreatedTime;
	protected PickResultData pickResult;
	protected ScreenCursorRecord oldCursor;
	

	public ScreenCursor(long id) {
		this.id = id;
		screenPositions = new ArrayList<ScreenCursorRecord>(100);
		cursorCreatedTime = System.nanoTime();
	}

	public void addPositionItem(ScreenCursorRecord item) {
		screenPositions.add(item);
	}
	
	public long getID() {
		return id;
	}
	
	public ScreenCursorRecord getCursorOrigin() {
		if(screenPositions.size() <= 0) return null;
		return screenPositions.get(0);
	}
	
	/**
	 * Get current position in jME Screen Coordinates
	 * @return
	 */
	public ScreenCursorRecord getCurrentCursorScreenPosition() {
		if(screenPositions.size() <= 0) return null;		
		return screenPositions.get(screenPositions.size() - 1);
	}
	
	public ScreenCursorRecord getOldCursorScreenPosition() {
		if (oldCursor!=null)
			return oldCursor;
		else
			return screenPositions.get(screenPositions.size() - 1);
	}
	
	public void setOldCursorScreenPosition(ScreenCursorRecord c){
		oldCursor=c;
	}
	
	
	public int getCurrentPositionIndex() {
		return screenPositions.size() - 1;
	}
	
	/**
	 * Get position at specified index in jME Screen Coordinates
	 * @param i
	 * @return
	 */
	public ScreenCursorRecord getPositionAtIndex(int i) {
		return screenPositions.get(i);
	}
	
	public ArrayList<ScreenCursorRecord> getPoints() {
		return screenPositions;
	}

	public int getNumPoints() {
		return screenPositions.size();
	}
	
	public int hashCode() {
		return (int)id;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ScreenCursor) {
			return obj.hashCode() == hashCode();
		}else
			return false;
	}
	
	public long getCursorCreatedTime() {
		return this.cursorCreatedTime;
	}
	
	public long getCursorLiveTime() {
		return System.nanoTime() - this.cursorCreatedTime;
	}

	public String toString() {
		return id + "@" + getCurrentCursorScreenPosition();
	}

	public boolean isProbablyTheSameAs(ScreenCursor c) {
		float dx = Math.abs(this.getCurrentCursorScreenPosition().x - c.getCurrentCursorScreenPosition().x);
		float dy = Math.abs(this.getCurrentCursorScreenPosition().y - c.getCurrentCursorScreenPosition().y);
		if(dx < EPSILON && dy < EPSILON)
			return true;
		else
			return false;
	}

	public void setPickResult(PickResultData pr) {
		this.pickResult = pr;
	}

	public PickResultData getPickResult() {
		return pickResult;
	}
}
