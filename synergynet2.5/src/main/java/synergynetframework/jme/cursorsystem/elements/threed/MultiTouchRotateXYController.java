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

public class MultiTouchRotateXYController extends ThreeDMultiTouchElement {

	public static final Vector3f AXIS_X = new Vector3f(1,0,0).normalize();
	public static final Vector3f AXIS_Y = new Vector3f(0,1,0).normalize();
	protected Spatial controlledSpatial;
	
	public MultiTouchRotateXYController(Spatial pickingAndTargetSpatial, Spatial controlledSpatial) {
		super(pickingAndTargetSpatial);
		this.controlledSpatial = controlledSpatial;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {		
		
		int previousPostionIndex=0;
		if (getScreenCursorByIndex(0).getCurrentPositionIndex()>0){
			previousPostionIndex = getScreenCursorByIndex(0).getCurrentPositionIndex()-1;
		}
		
		Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getPositionAtIndex(previousPostionIndex).getPosition();
		Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();	
		Vector2f cursor1ToSpatial = screenPosCursor1.subtract(originalScreenPosCursor1);
		
		float[] angles = {-cursor1ToSpatial.y/100, cursor1ToSpatial.x/100, 0};
		Quaternion q = new Quaternion();
		q.fromAngles(angles);		
		q.multLocal(getTargetSpatial().getLocalRotation());
		getTargetSpatial().setLocalRotation(q);
		this.controlledSpatial.setLocalRotation(q);
		
				
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		System.out.println("angle: "+getTargetSpatial().getLocalRotation().toString());
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

}
