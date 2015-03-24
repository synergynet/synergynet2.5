/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.jme.mtinputbridge.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynetframework.jme.cursorsystem.MultiTouchCursorSystem;
import synergynetframework.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.IMultiTouchInputFilter;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

/**
 * Use dead reckoning and picking system to detect false cursor position.
 *
 * @author dcs2ima
 */
public class DeadReckoningFilter implements IMultiTouchInputFilter {
	
	/**
	 * The Class CursorRecord.
	 */
	class CursorRecord {

		/** The picked spatial. */
		private Spatial pickedSpatial;

		/** The record time. */
		private long recordTime;

		/** The screen position. */
		private Vector2f screenPosition;

		/**
		 * Instantiates a new cursor record.
		 *
		 * @param screenPosition
		 *            the screen position
		 * @param pickedSpatial
		 *            the picked spatial
		 * @param recordTime
		 *            the record time
		 */
		public CursorRecord(Vector2f screenPosition, Spatial pickedSpatial,
				long recordTime) {
			this.screenPosition = screenPosition;
			this.pickedSpatial = pickedSpatial;
			this.recordTime = recordTime;
		}

		/**
		 * Gets the picked spatial.
		 *
		 * @return the picked spatial
		 */
		public Spatial getPickedSpatial() {
			return pickedSpatial;
		}

		/**
		 * Gets the record time.
		 *
		 * @return the record time
		 */
		public long getRecordTime() {
			return recordTime;
		}

		/**
		 * Gets the screen position.
		 *
		 * @return the screen position
		 */
		public Vector2f getScreenPosition() {
			return screenPosition;
		}
	}

	/** The Constant CURSOR_CHANGED. */
	public static final String CURSOR_CHANGED = "CURSOR_CHANGED";

	/** The Constant CURSOR_CLICKED. */
	public static final String CURSOR_CLICKED = "CURSOR_CLICKED";

	/** The Constant CURSOR_PRESSED. */
	public static final String CURSOR_PRESSED = "CURSOR_PRESSED";

	/** The Constant CURSOR_RELEASED. */
	public static final String CURSOR_RELEASED = "CURSOR_RELEASED";

	/** The Constant DEAD_RECKONING_THRESHOLD. */
	private static final int DEAD_RECKONING_THRESHOLD = 100; // 100 pixels

	/** The Constant OBJECT_ADDED. */
	public static final String OBJECT_ADDED = "OBJECT_ADDED";

	/** The Constant OBJECT_CHANGED. */
	public static final String OBJECT_CHANGED = "OBJECT_CHANGED";

	/** The Constant OBJECT_REMOVED. */
	public static final String OBJECT_REMOVED = "OBJECT_REMOVED";

	/** The cursor history map. */
	private Map<Long, List<CursorRecord>> cursorHistoryMap = new HashMap<Long, List<CursorRecord>>();
	
	/** The next. */
	private IMultiTouchEventListener next;

	/** The pick system. */
	private IJMEMultiTouchPicker pickSystem;

	/**
	 * Instantiates a new dead reckoning filter.
	 */
	public DeadReckoningFilter() {
		this.pickSystem = MultiTouchInputFilterManager.getInstance()
				.getPickingSystem();
	}
	
	/**
	 * Check false expected position.
	 *
	 * @param event
	 *            the event
	 * @return true, if successful
	 */
	private boolean checkFalseExpectedPosition(MultiTouchCursorEvent event) {
		Vector2f currentCursorPosition = MultiTouchCursorSystem
				.tableToScreen(event.getPosition());
		Vector2f expectedCursorPosition = getExpectedPosition(event
				.getCursorID());
		float distance = 0;
		if (expectedCursorPosition != null) {
			distance = (currentCursorPosition.subtract(expectedCursorPosition))
					.length();
		}
		return distance > DEAD_RECKONING_THRESHOLD;
	}
	
	/**
	 * Check false picked spatial.
	 *
	 * @param currentPickedSpatial
	 *            the current picked spatial
	 * @param cursorID
	 *            the cursor id
	 * @return true, if successful
	 */
	private boolean checkFalsePickedSpatial(Spatial currentPickedSpatial,
			long cursorID) {
		boolean falsePickedSpatial = false;
		if (cursorHistoryMap.containsKey(cursorID)) {
			List<CursorRecord> cursorRecords = cursorHistoryMap.get(cursorID);
			Spatial lastPickedSpatial = cursorRecords.get(
					cursorRecords.size() - 1).getPickedSpatial();
			if ((lastPickedSpatial != null)
					&& (currentPickedSpatial != null)
					&& lastPickedSpatial.getName().equals(
							currentPickedSpatial.getName())) {
				falsePickedSpatial = false;
			} else if ((lastPickedSpatial == null)
					&& (currentPickedSpatial == null)) {
				falsePickedSpatial = false;
			} else {
				falsePickedSpatial = true;
			}
		}
		return falsePickedSpatial;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
		Spatial currentPickedSpatial = getPickedSpatial(event);
		if (checkFalseExpectedPosition(event)
				&& checkFalsePickedSpatial(currentPickedSpatial,
						event.getCursorID())) {
			return;
		}
		Vector2f screenPosition = MultiTouchCursorSystem.tableToScreen(event
				.getPosition());
		updateCursorHistory(event.getCursorID(), screenPosition,
				currentPickedSpatial);
		next.cursorChanged(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
		next.cursorClicked(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		Vector2f screenPosition = MultiTouchCursorSystem.tableToScreen(event
				.getPosition());
		Spatial spatial = getPickedSpatial(event);
		updateCursorHistory(event.getCursorID(), screenPosition, spatial);
		next.cursorPressed(event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		resetCursorHistory(event.getCursorID());
		next.cursorReleased(event);
	}
	
	/**
	 * Gets the expected position.
	 *
	 * @param cursorID
	 *            the cursor id
	 * @return the expected position
	 */
	private Vector2f getExpectedPosition(long cursorID) {
		if (!cursorHistoryMap.containsKey(cursorID)) {
			return null;
		}
		List<CursorRecord> recordStack = cursorHistoryMap.get(cursorID);
		if (recordStack.size() == 0) {
			return null;
		}
		if (recordStack.size() == 1) {
			return recordStack.get(0).getScreenPosition();
		}
		int lastIndex = recordStack.size() - 1;
		int nextLastIndex = lastIndex - 1;
		Vector2f lastPosition = recordStack.get(lastIndex).getScreenPosition();
		long lastTime = recordStack.get(lastIndex).getRecordTime();
		Vector2f nextLastPosition = recordStack.get(nextLastIndex)
				.getScreenPosition();
		long nextLastTime = recordStack.get(nextLastIndex).getRecordTime();
		float diffTime = lastTime - nextLastTime;
		Vector2f velocity = lastPosition.subtract(nextLastPosition).mult(
				1f / diffTime);
		return lastPosition.add(velocity.mult(System.nanoTime() - lastTime));
	}
	
	/**
	 * Gets the picked spatial.
	 *
	 * @param event
	 *            the event
	 * @return the picked spatial
	 */
	private Spatial getPickedSpatial(MultiTouchCursorEvent event) {
		PickRequest req = new PickRequest(event.getCursorID(),
				MultiTouchCursorSystem.tableToScreen(event.getPosition()));
		List<PickResultData> pickResults;
		try {
			pickResults = pickSystem.doPick(req);
			for (PickResultData pr : pickResults) {
				return pr.getPickedSpatial();
			}
		} catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {
		next.objectAdded(event);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {
		next.objectChanged(event);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(
	 * synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {
		next.objectRemoved(event);
	}

	/**
	 * Reset cursor history.
	 *
	 * @param cursorID
	 *            the cursor id
	 */
	private void resetCursorHistory(long cursorID) {
		if (cursorHistoryMap.containsKey(cursorID)) {
			cursorHistoryMap.remove(cursorID);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputFilter#setNext(
	 * synergynetframework.mtinput.IMultiTouchEventListener)
	 */
	public void setNext(IMultiTouchEventListener el) {
		this.next = el;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchInputFilter#update(float)
	 */
	public void update(float tpf) {
	}

	/**
	 * Update cursor history.
	 *
	 * @param cursorID
	 *            the cursor id
	 * @param screenPosition
	 *            the screen position
	 * @param pickedSpatial
	 *            the picked spatial
	 */
	private void updateCursorHistory(long cursorID, Vector2f screenPosition,
			Spatial pickedSpatial) {
		if (cursorHistoryMap.containsKey(cursorID)) {
			List<CursorRecord> recordStack = cursorHistoryMap.get(cursorID);
			recordStack.add(new CursorRecord(screenPosition, pickedSpatial,
					System.nanoTime()));
		} else {
			List<CursorRecord> recordStack = new ArrayList<CursorRecord>();
			recordStack.add(new CursorRecord(screenPosition, pickedSpatial,
					System.nanoTime()));
			cursorHistoryMap.put(cursorID, recordStack);
		}
	}
}
