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

package synergynetframework.mtinput;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Determines whether cursor released events constitute a single click.  Needs
 * to know when new cursors are pressed, via newCursorPressed.  When a cursor
 * is released, use isCursorReleaseASingleClick to determine whether a single
 * click event occurred, based on the current sensitivity.
 * 
 * @author dcs0ah1
 */

public class ClickDetector {
	
	/** The distance. */
	protected float distance;
	
	/** The time. */
	protected long time;
	
	/** The records. */
	protected Map<Long,CursorDownRecord> records = new HashMap<Long,CursorDownRecord>();
	
	/** The old record time threshold. */
	protected long oldRecordTimeThreshold = 5 * 1000;
	
	/** The double click time threshold. */
	protected long doubleClickTimeThreshold = 500;

	/**
	 * Click detector, based on supplied sensitivity values.
	 *
	 * @param time the time
	 * @param distance the distance
	 */
	public ClickDetector(long time, float distance) {
		setSensitivity(time, distance);
	}
	
	/**
	 * Set new sensitivity values.
	 *
	 * @param time the time
	 * @param distance the distance
	 */
	public void setSensitivity(long time, float distance) {
		this.time = time;
		this.distance = distance;
	}
	
	/**
	 * Gets the distance sensitivity.
	 *
	 * @return the distance sensitivity
	 */
	public float getDistanceSensitivity() {
		return this.distance;
	}
	
	/**
	 * Gets the time sensitivity.
	 *
	 * @return the time sensitivity
	 */
	public long getTimeSensitivity() {
		return this.time;
	}
	
	/**
	 * Register cursor - only registered cursors will ever pass the test
	 * of being a single click when isCursorReleaseASingleClick is called.
	 *
	 * @param id the id
	 * @param position the position
	 */
	public void newCursorPressed(long id, Point2D.Float position) {
		records.put(id, new CursorDownRecord(id, this, System.currentTimeMillis(), position));
	}
	
	/**
	 * Returns true if the released cursor is within the sensitivity values required
	 * to constitute a single click.
	 *
	 * @param id the id
	 * @param position the position
	 * @return the int
	 */
	public int cursorReleasedGetClickCount(long id, Point2D.Float position) {
		CursorDownRecord record = records.get(id);
		if(record == null) return -1;
		record.releaseTime = System.currentTimeMillis();
		boolean isDoubleClick = false;
		boolean isSingleClick = ((System.currentTimeMillis() - record.pressTime) < time) && isCloseEnough(position, record.position);
		if(isSingleClick) {
			record.wasClick = isSingleClick;
			isDoubleClick = isCursorReleaseADoubleClick(id, position, record);
			record.wasDoubleClick = isDoubleClick;
		}		
		
		cullOldRecords();
		if(isDoubleClick) {
			return 2;
		}else if(isSingleClick){
			return 1;
		}else{
			return 0;
		}
	}
	
	/**
	 * Prints the records.
	 */
	protected void printRecords() {
		for(CursorDownRecord r : records.values()) {
			System.out.println(r.id + ": " + r.wasClick);
		}
		
	}

	/**
	 * Checks if is cursor release a double click.
	 *
	 * @param id the id
	 * @param position the position
	 * @param causedBy the caused by
	 * @return true, if is cursor release a double click
	 */
	public boolean isCursorReleaseADoubleClick(long id, Point2D.Float position, CursorDownRecord causedBy) {
		CursorDownRecord record = records.get(id);
		if(record == null) return false;
		for(CursorDownRecord r : records.values()) {
			if(r == causedBy) continue;
			if(r.area.contains(position) && r.wasClick && !r.wasDoubleClick && ((System.currentTimeMillis() - r.releaseTime) < getDoubleClickTimeThreshold())) {
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * Cull old records.
	 */
	private void cullOldRecords() {
		List<Long> toDelete = new ArrayList<Long>();
		for(CursorDownRecord r : records.values()) {
			if(System.currentTimeMillis() - r.releaseTime > oldRecordTimeThreshold ) {
				toDelete.add(r.id);
			}
		}
		for(Long idToDelete : toDelete) {
			records.remove(idToDelete);
		}		
	}
	
	/**
	 * Checks if is close enough.
	 *
	 * @param a the a
	 * @param b the b
	 * @return true, if is close enough
	 */
	private boolean isCloseEnough(Point2D.Float a, Point2D.Float b) {
		return Point2D.distance(a.x, a.y, b.x, b.y) < distance;
	}
	
	/**
	 * Sets the double click time threshold.
	 *
	 * @param doubleClickTimeThreshold the new double click time threshold
	 */
	public void setDoubleClickTimeThreshold(long doubleClickTimeThreshold) {
		this.doubleClickTimeThreshold = doubleClickTimeThreshold;
	}

	/**
	 * Gets the double click time threshold.
	 *
	 * @return the double click time threshold
	 */
	public long getDoubleClickTimeThreshold() {
		return doubleClickTimeThreshold;
	}

	/**
	 * The Class CursorDownRecord.
	 */
	private class CursorDownRecord {
		
		/** The id. */
		public long id;
		
		/** The press time. */
		public long pressTime;
		
		/** The release time. */
		public long releaseTime;
		
		/** The position. */
		public Point2D.Float position;
		
		/** The area. */
		public Rectangle2D.Float area = new Rectangle2D.Float();
		
		/** The was click. */
		public boolean wasClick = false;
		
		/** The was double click. */
		public boolean wasDoubleClick = false;
		
		/**
		 * Instantiates a new cursor down record.
		 *
		 * @param id the id
		 * @param detector the detector
		 * @param time the time
		 * @param position the position
		 */
		public CursorDownRecord(long id, ClickDetector detector, long time, Point2D.Float position) {
			this.id = id;
			this.pressTime = time;
			this.position = position;
			area.x = position.x - detector.getDistanceSensitivity()/2;
			area.y = position.y - detector.getDistanceSensitivity()/2;
			area.width = detector.getDistanceSensitivity();
			area.height = detector.getDistanceSensitivity();
		}
	}
}
