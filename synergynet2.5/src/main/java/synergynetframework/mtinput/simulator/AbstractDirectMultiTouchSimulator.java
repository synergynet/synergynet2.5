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

package synergynetframework.mtinput.simulator;

import java.awt.geom.Point2D;

import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

/**
 * The Class AbstractDirectMultiTouchSimulator.
 */
public abstract class AbstractDirectMultiTouchSimulator extends
		AbstractMultiTouchSimulator {
	
	/**
	 * Instantiates a new abstract direct multi touch simulator.
	 *
	 * @param tableWidth
	 *            the table width
	 * @param tableHeight
	 *            the table height
	 */
	public AbstractDirectMultiTouchSimulator(int tableWidth, int tableHeight) {
		super(tableWidth, tableHeight);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator#
	 * deleteCursor(int, float, float)
	 */
	@Override
	public void deleteCursor(int id, float x, float y) {
		Point2D.Float pos = new Point2D.Float(x, 1 - y);
		for (IMultiTouchEventListener l : listeners) {
			MultiTouchCursorEvent event = new MultiTouchCursorEvent(id, pos);
			int clickCount = clickDetector.cursorReleasedGetClickCount(id, pos);
			if (clickCount > 0) {
				event.setClickCount(clickCount);
				l.cursorClicked(event);
			}
			l.cursorReleased(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator#
	 * deleteTwoCursors(int, float, float, int, float, float)
	 */
	@Override
	public void deleteTwoCursors(int id1, float x1, float y1, int id2,
			float x2, float y2) {
		deleteCursor(id1, x1, y1);
		deleteCursor(id2, x2, y2);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator#newCursor
	 * (int, float, float)
	 */
	@Override
	public void newCursor(int id, float x, float y) {
		Point2D.Float pos = new Point2D.Float(x, 1 - y);
		MultiTouchCursorEvent event = new MultiTouchCursorEvent(id, pos);
		clickDetector.newCursorPressed(id, pos);
		for (IMultiTouchEventListener l : listeners) {
			l.cursorPressed(event);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator#
	 * updateCursor(int, float, float)
	 */
	@Override
	public void updateCursor(int id, float x, float y) {
		for (IMultiTouchEventListener l : listeners) {
			Point2D.Float pos = new Point2D.Float(x, 1 - y);
			MultiTouchCursorEvent event = new MultiTouchCursorEvent(id, pos);
			l.cursorChanged(event);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.mtinput.simulator.AbstractMultiTouchSimulator#
	 * updateTwoCursors(int, float, float, int, float, float)
	 */
	@Override
	public void updateTwoCursors(int id1, float x, float y, int id2, float x2,
			float y2) {
		updateCursor(id1, x, y);
		updateCursor(id2, x2, y2);
	}
	
}
