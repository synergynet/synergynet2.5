/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

package synergynetframework.jme.cursorsystem.elements.threed;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

/**
 * The Class MultiTouchScale.
 */
public class MultiTouchScale extends ThreeDMultiTouchElement {

	/**
	 * The listener interface for receiving scaleChange events. The class that
	 * is interested in processing a scaleChange event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addScaleChangeListener<code> method. When
	 * the scaleChange event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ScaleChangeEvent
	 */
	public interface ScaleChangeListener {

		/**
		 * Scale changed.
		 *
		 * @param newValue
		 *            the new value
		 */
		public void scaleChanged(float newValue);
	}
	
	/** The listeners. */
	protected List<ScaleChangeListener> listeners = new ArrayList<ScaleChangeListener>();

	/** The min scale. */
	private float minScale;

	/**
	 * Instantiates a new multi touch scale.
	 *
	 * @param s
	 *            the s
	 * @param minScale
	 *            the min scale
	 */
	public MultiTouchScale(Spatial s, float minScale) {
		super(s);
		this.minScale = minScale;
	}

	/**
	 * Instantiates a new multi touch scale.
	 *
	 * @param pickSpatial
	 *            the pick spatial
	 * @param targetSpatial
	 *            the target spatial
	 */
	public MultiTouchScale(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}
	
	/**
	 * Adds the scale change listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addScaleChangeListener(ScaleChangeListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
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
		if (this.getNumRegisteredCursors() < 2) {
			return;
		}

		Spatial targetSpatial = getTargetSpatial();
		ScreenCursor c1 = getScreenCursorByIndex(0);
		ScreenCursor c2 = getScreenCursorByIndex(1);

		Vector2f c1Current = c1.getCurrentCursorScreenPosition().getPosition();
		Vector2f c2Current = c2.getCurrentCursorScreenPosition().getPosition();
		Vector2f c1Origin = c1.getCursorOrigin().getPosition();
		Vector2f c2Origin = c2.getCursorOrigin().getPosition();

		float currentDistance = c2Current.subtract(c1Current).length();
		float originDistance = c2Origin.subtract(c1Origin).length();
		
		float scaleFactor = currentDistance / originDistance;
		if (scaleFactor < minScale) {
			scaleFactor = minScale;
		}

		targetSpatial.setLocalScale(this.getScaleAtOrigin().mult(scaleFactor));
		notifyScaleChange(scaleFactor);
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
	 * Notify scale change.
	 *
	 * @param s
	 *            the s
	 */
	private void notifyScaleChange(float s) {
		for (ScaleChangeListener l : listeners) {
			l.scaleChanged(s);
		}
	}
}