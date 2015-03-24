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

package apps.threedmanipulation.gestures;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.scene.Spatial;

/**
 * The Class ObjectRightClick.
 */
public class ObjectRightClick extends ThreeDMultiTouchElement {
	
	/**
	 * The listener interface for receiving cursorEvent events. The class that
	 * is interested in processing a cursorEvent event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addCursorEventListener<code> method. When
	 * the cursorEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see CursorEventEvent
	 */
	public interface CursorEventListener {
		
		/**
		 * Cursor right clicked.
		 *
		 * @param c
		 *            the c
		 * @param event
		 *            the event
		 */
		public void cursorRightClicked(ScreenCursor c,
				MultiTouchCursorEvent event);
	}

	/** The listeners. */
	protected List<CursorEventListener> listeners = new ArrayList<CursorEventListener>();

	/** The right click distance. */
	protected float rightClickDistance = 10f;

	/**
	 * Instantiates a new object right click.
	 *
	 * @param pickingAndTargetSpatial
	 *            the picking and target spatial
	 */
	public ObjectRightClick(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	/**
	 * Instantiates a new object right click.
	 *
	 * @param pickingSpatial
	 *            the picking spatial
	 * @param targetSpatial
	 *            the target spatial
	 */
	public ObjectRightClick(Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}
	
	/**
	 * Adds the multi touch listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addMultiTouchListener(CursorEventListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		
		for (CursorEventListener l : listeners) {

			// if (RightClickDetector.isDoubleClick(screenCursors, c,
			// rightClickDistance)){
			l.cursorRightClicked(c, event);
			// }
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased
	 * (synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
	}

	/**
	 * Removes the multi touch listener.
	 *
	 * @param l
	 *            the l
	 */
	public void removeMultiTouchListener(CursorEventListener l) {
		listeners.remove(l);
	}

	/**
	 * Sets the right click distance.
	 *
	 * @param rightClickDistance
	 *            the new right click distance
	 */
	public void setRightClickDistance(float rightClickDistance) {
		this.rightClickDistance = rightClickDistance;
	}
	
}
