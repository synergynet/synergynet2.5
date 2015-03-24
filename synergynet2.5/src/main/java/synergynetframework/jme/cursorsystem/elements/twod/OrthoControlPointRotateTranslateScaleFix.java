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

package synergynetframework.jme.cursorsystem.elements.twod;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.fixutils.FixGesture;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.scene.Spatial;

/**
 * The Class OrthoControlPointRotateTranslateScaleFix.
 *
 * @author dcs0ah1, dcs2ima
 */
public class OrthoControlPointRotateTranslateScaleFix extends
		OrthoControlPointRotateTranslateScale {
	
	/** The fixed. */
	protected boolean fixed = false;

	/** The fix gesture. */
	protected FixGesture fixGesture;

	/**
	 * Instantiates a new ortho control point rotate translate scale fix.
	 *
	 * @param pickingAndTargetSpatial
	 *            the picking and target spatial
	 * @param fixGesture
	 *            the fix gesture
	 */
	public OrthoControlPointRotateTranslateScaleFix(
			Spatial pickingAndTargetSpatial, FixGesture fixGesture) {
		super(pickingAndTargetSpatial);
		this.fixGesture = fixGesture;
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoControlPointRotateTranslateScale
	 * #cursorChanged(synergynetframework.jme
	 * .cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if (isFixed()) {
			return;
		}
		super.cursorChanged(c, event);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoControlPointRotateTranslateScale
	 * #cursorClicked(synergynetframework.jme
	 * .cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorClicked(c, event);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoControlPointRotateTranslateScale
	 * #cursorPressed(synergynetframework.jme
	 * .cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorPressed(c, event);
		if (!isFixed()) {
			if ((fixGesture != null)
					&& fixGesture.checkForFixGesture(this, c, event)) {
				setFixed(true);
			}
		} else {
			if ((fixGesture != null)
					&& fixGesture.checkForUnfixGesture(this, c, event)) {
				setFixed(false);
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.
	 * OrthoControlPointRotateTranslateScale
	 * #cursorReleased(synergynetframework.jme
	 * .cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		super.cursorReleased(c, event);
	}

	/**
	 * Checks if is fixed.
	 *
	 * @return true, if is fixed
	 */
	public boolean isFixed() {
		return fixed;
	}

	/**
	 * Sets the fixed.
	 *
	 * @param fixed
	 *            the new fixed
	 */
	public void setFixed(boolean fixed) {
		this.fixed = fixed;
	}
}
