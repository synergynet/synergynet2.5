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

package apps.threedpuzzle.gestures;

import java.util.ArrayList;
import java.util.List;

import com.jme.intersection.PickResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.jme.gfx.JMEGfxUtils;
import synergynetframework.jme.pickingsystem.data.ThreeDPickResultData;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

public class TwoFingersGesture extends ThreeDMultiTouchElement {

	protected float scaleMin = Float.NaN;
	protected float scaleMax = Float.NaN;
	protected PickResults pickResults = null;
	
	protected List<ObjectRotateTranslateScaleListener> listeners = new ArrayList<ObjectRotateTranslateScaleListener>();
	
	public TwoFingersGesture(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	public TwoFingersGesture(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}
	
	
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() <2 ) {
			
			int previousPostionIndex=0;
			if (getScreenCursorByIndex(0).getCurrentPositionIndex()>0){
				previousPostionIndex = getScreenCursorByIndex(0).getCurrentPositionIndex()-1;
			}
			
			Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getPositionAtIndex(previousPostionIndex).getPosition();
			Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();	
			Vector2f cursor1ToSpatial = screenPosCursor1.subtract(originalScreenPosCursor1);

			float[] angles = {-cursor1ToSpatial.y/150, cursor1ToSpatial.x/150, 0};
			Quaternion q = new Quaternion();
			q.fromAngles(angles);	
			q.multLocal(targetSpatial.getLocalRotation());
			targetSpatial.setLocalRotation(q);
			
			for (ObjectRotateTranslateScaleListener l: listeners)
				l.itemRotated(targetSpatial, q.clone());
			
			
		}else if(screenCursors.size() == 2){
			Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getCursorOrigin().getPosition();
			Vector2f originalScreenPosCursor2 = getScreenCursorByIndex(1).getCursorOrigin().getPosition();
			Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();
			Vector2f screenPosCursor2 = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().getPosition();
			ThreeDPickResultData pr = getPickResultFromCursorIndex(0);
			Vector2f spatialScreenAtPick = pr.getSpatialScreenLocationAtPick();
			Vector2f cursor1ToSpatialAtPick = spatialScreenAtPick.subtract(originalScreenPosCursor1);

			float originalCursorAngle = originalScreenPosCursor2.subtract(originalScreenPosCursor1).getAngle();
			float newCursorAngle = screenPosCursor2.subtract(screenPosCursor1).getAngle();
			float theta = newCursorAngle - originalCursorAngle;

			float newCursor1ToSpatialAngle = cursor1ToSpatialAtPick.getAngle() + theta;

			//float cursorScale = screenPosCursor2.subtract(screenPosCursor1).length() / originalScreenPosCursor2.subtract(originalScreenPosCursor1).length();
		
			//float newDistFromCursor1ToSpatial = cursorScale * cursor1ToSpatialAtPick.length();
			
			float newDistFromCursor1ToSpatial = cursor1ToSpatialAtPick.length();

			float dx = newDistFromCursor1ToSpatial * FastMath.cos(newCursor1ToSpatialAngle);
			float dy = newDistFromCursor1ToSpatial * FastMath.sin(newCursor1ToSpatialAngle);

			Vector2f newScreenPosition = screenPosCursor1.add(new Vector2f(dx, -dy));

			Vector3f newPosition = JMEGfxUtils.getCursorWorldCoordinatesOnSpatialPlane(newScreenPosition, targetSpatial);
			if(newPosition != null) {
				
				float oldZ=targetSpatial.getLocalTranslation().z;
				targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
				targetSpatial.getLocalTranslation().z = oldZ;
				
				worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),
						new Quaternion(targetSpatial.getWorldRotation().clone()), targetSpatial.getWorldScale().clone(), System.nanoTime()));
				
				for (ObjectRotateTranslateScaleListener l: listeners)
					l.itemMoved(targetSpatial, newPosition.x, newPosition.y, newPosition.z);
				
				Quaternion q = getCurrentTargetSpatialRotationFromCursorChange();
				targetSpatial.setLocalRotation(q.clone());
				for (ObjectRotateTranslateScaleListener l: listeners)
					l.itemRotated(targetSpatial, q.clone());
						
				//if((getScaleAtOrigin().mult(cursorScale).x < scaleMax) && (getScaleAtOrigin().mult(cursorScale).x > scaleMin)) {
					//targetSpatial.setLocalScale(getScaleAtOrigin().mult(cursorScale));
					//targetSpatial.setLocalTranslation(targetSpatial.getLocalTranslation().x, targetSpatial.getLocalTranslation().y, targetSpatial.getLocalTranslation().z+(cursorScale-1)/10);
				//}

				targetSpatial.updateGeometricState(0f, true);
								
			}
		}
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}


	public float getScaleMin() {
		return scaleMin;
	}

	public void setScaleMin(float scaleMin) {
		this.scaleMin = scaleMin;
	}

	public float getScaleMax() {
		return scaleMax;
	}

	public void setScaleMax(float scaleMax) {
		this.scaleMax = scaleMax;
	}

	public void setScaleLimits(float min, float max) {
		setScaleMin(min);
		setScaleMax(max);
	}
	
	public void addRotateTranslateScaleListener(ObjectRotateTranslateScaleListener l){
		listeners.add(l);
	}

	public void removeRotateTranslateScaleListener(ObjectRotateTranslateScaleListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}
	
	public interface ObjectRotateTranslateScaleListener {
		public void itemMoved(Spatial targetSpatial,  float newLocationX, float newLocationY, float newLocationZ);
		public void itemRotated(Spatial targetSpatial, Quaternion quaternion);
	}

}
