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

package synergynetframework.jme.cursorsystem.elements.threed;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


/**
 * The Class MultiTouchRotateXY.
 */
public class MultiTouchRotateXY extends ThreeDMultiTouchElement {

	/** The Constant AXIS_X. */
	public static final Vector3f AXIS_X = new Vector3f(1,0,0).normalize();
	
	/** The Constant AXIS_Y. */
	public static final Vector3f AXIS_Y = new Vector3f(0,1,0).normalize();
	
	/**
	 * Instantiates a new multi touch rotate xy.
	 *
	 * @param pickingAndTargetSpatial the picking and target spatial
	 */
	public MultiTouchRotateXY(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {		
		
		Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getCursorOrigin().getPosition();
		Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();	
		Vector2f cursor1ToSpatial = screenPosCursor1.subtract(originalScreenPosCursor1);
		
		float[] angles = {-cursor1ToSpatial.y/100, cursor1ToSpatial.x/100, 0};
		//System.out.println("angle: "+getTargetSpatial().getLocalRotation().toString());
		Quaternion q = new Quaternion();
		q.fromAngles(angles);		
		q.multLocal(getTargetSpatial().getLocalRotation());
		System.out.println("getting Quaternion "+getTargetSpatial().getLocalRotation());
		System.out.println("Setting Quaternion "+q.toString());
		getTargetSpatial().setLocalRotation(q);
		
		/*
		Quaternion tq = new Quaternion();
		tq.fromAngleAxis(-cursor1ToSpatial.x/100, AXIS_Y);
		tq.mult(getTargetSpatial().getLocalRotation());
		getTargetSpatial().setLocalRotation(tq);		
		getTargetSpatial().updateGeometricState(0f, false);
		
		Quaternion tq1 = new Quaternion();
		tq1.fromAngleAxis(-cursor1ToSpatial.y/100, AXIS_X);
		tq1.mult(getTargetSpatial().getLocalRotation());
		getTargetSpatial().setLocalRotation(tq1);
		*/
				
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

}
