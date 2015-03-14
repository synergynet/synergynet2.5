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

package synergynetframework.jme.cursorsystem.elements.twod;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
//import com.jme.system.DisplaySystem;
import com.jme.math.FastMath;

import synergynetframework.jme.cursorsystem.TwoDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursorRecord;
import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

/**
 *
 * @author dcs0ah1, pwpp25, dcs2ima
 *
 */
public class OrthoControlPointRotateTranslateScale extends TwoDMultiTouchElement{

	protected Vector2f cursor1Pos = new Vector2f();
	protected Vector2f cursor2Pos = new Vector2f();
	protected Vector2f cursor1OldPos = new Vector2f();
	protected Vector2f cursor2OldPos = new Vector2f();

	protected float maxScale = 4.0f;
	protected float minScale = 1f;

	private boolean moreThanTwoGivesRTS = false;
	private boolean singleTouchFreeMove = false;

	protected List<RotateTranslateScaleListener> listeners = new ArrayList<RotateTranslateScaleListener>();
	private float minRotate;
	private float maxRotate;
	private boolean rotateLimits = false;

	public OrthoControlPointRotateTranslateScale(Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}

	public OrthoControlPointRotateTranslateScale(Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}

	public void allowMoreThanTwoToRotateAndScale(boolean b) {
		moreThanTwoGivesRTS = b;
	}

	public void allowSingleTouchFreeMove(boolean b) {
		singleTouchFreeMove = b;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() == 1) {
			updateCursor1();
			applySingleCursorTransform();
			setOldCursor();
		}else if (screenCursors.size() >= 2){
			updateCursor1();
			updateCursor2();
			applyMultiCursorTransform();
			setOldCursor();
		}/*else if(screenCursors.size() > 2) {
			if(moreThanTwoGivesRTS) {
				updateCursor1();
				updateCursor2();
				applyMultiCursorTransform();
				setOldCursor();
			}else{
				updateCursor1();
				applySingleCursorTransform();
				setOldCursor();
			}
		}*/
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() == 1) {
			Vector2f rotatedPosition = this.screenToTable(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y);
			cursor1OldPos.x = rotatedPosition.x;
			cursor1OldPos.y = rotatedPosition.y;
			//cursor1OldPos.x = c.getCurrentCursorScreenPosition().x;
			//cursor1OldPos.y = c.getCurrentCursorScreenPosition().y;
		}else if(screenCursors.size() == 2) {
			updateCursor1();
			updateCursor2();
			cursor1OldPos = cursor1Pos.clone();
			cursor2OldPos = cursor2Pos.clone();
		}else if(screenCursors.size() > 2) {
			if(moreThanTwoGivesRTS) {
				updateCursor1();
				updateCursor2();
				cursor1OldPos = cursor1Pos.clone();
				cursor2OldPos = cursor2Pos.clone();
			}else{
				Vector2f rotatedPosition = this.screenToTable(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y);
				cursor1OldPos.x = rotatedPosition.x;
				cursor1OldPos.y = rotatedPosition.y;
				//cursor1OldPos.x = c.getCurrentCursorScreenPosition().x;
				//cursor1OldPos.y = c.getCurrentCursorScreenPosition().y;
			}
		}
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	protected void applySingleCursorTransform() {

		float minAngleChange = 0.01f;
		Vector2f moveDirection = cursor1Pos.subtract(cursor1OldPos).normalize();
		Vector2f centerToCursorDirection = cursor1Pos.subtract(targetSpatial.getLocalTranslation().x, targetSpatial.getLocalTranslation().y).normalize();

		if(!singleTouchFreeMove || moveDirection.smallestAngleBetween(centerToCursorDirection)<(30 * FastMath.DEG_TO_RAD)){
			Vector2f posChange = cursor1Pos.subtract(cursor1OldPos);
			Vector3f newP = new Vector3f(targetSpatial.getLocalTranslation().x += posChange.x,	targetSpatial.getLocalTranslation().y += posChange.y, targetSpatial.getLocalTranslation().z);
			if(newP != null && targetSpatial.getParent() != null) {
				targetSpatial.getParent().worldToLocal(newP, targetSpatial.getLocalTranslation());
				worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));

				for (RotateTranslateScaleListener l: listeners)
				l.itemMoved(this, targetSpatial, newP.x, newP.y, cursor1OldPos.x, cursor1OldPos.y);
			}
		}
		else{

			Vector2f spatialLoc = new Vector2f();
			spatialLoc.x = targetSpatial.getLocalTranslation().x;
			spatialLoc.y = targetSpatial.getLocalTranslation().y;
			Vector2f centerToSpatial = spatialLoc.subtract(cursor1OldPos);

			float initialAngle = moveDirection.angleBetween(centerToCursorDirection);

			if(initialAngle<0 && FastMath.abs(initialAngle)<FastMath.PI){
				minAngleChange = -minAngleChange;
			}else if(initialAngle <0 && FastMath.abs(initialAngle)>FastMath.PI){
			}else if(initialAngle <0 || FastMath.abs(initialAngle)>FastMath.PI){
				minAngleChange = -minAngleChange;
			}
			minAngleChange*= cursor1Pos.distance(cursor1OldPos);

			float currentCenterToSpatialAngel = centerToSpatial.getAngle()+minAngleChange;

			float newDistFromCurrentCenterToSpatial = centerToSpatial.length();

			float dx = newDistFromCurrentCenterToSpatial * FastMath.cos(currentCenterToSpatialAngel);
			float dy = newDistFromCurrentCenterToSpatial * FastMath.sin(currentCenterToSpatialAngel);

			Vector2f newScreenPosition = cursor1Pos.add(new Vector2f(dx, -dy));

			// when two blobs are v. close together, dx and dy can end up being NaN
			if(Float.isNaN(dx) || Float.isNaN(dy)) newScreenPosition = cursor1Pos;

			Vector3f newPos = new Vector3f(newScreenPosition.x, newScreenPosition.y, targetSpatial.getLocalTranslation().z);

			if(newPos != null && targetSpatial.getParent() != null) {

				//update rotation
				Quaternion oldtq = targetSpatial.getLocalRotation();
				Quaternion tq = new Quaternion();
				tq.fromAngleAxis(-minAngleChange, AXIS_Z);
				tq.multLocal(targetSpatial.getLocalRotation());
				targetSpatial.setLocalRotation(tq);

				//update location
				targetSpatial.getParent().worldToLocal(newPos, targetSpatial.getLocalTranslation());
				targetSpatial.getLocalTranslation().x = newScreenPosition.x;
				targetSpatial.getLocalTranslation().y = newScreenPosition.y;
				for (RotateTranslateScaleListener l: listeners)
					l.itemMoved(this, targetSpatial, newScreenPosition.x, newScreenPosition.y, cursor1Pos.x, cursor1Pos.y);
				worldLocations.add(new WorldCursorRecord(targetSpatial.getLocalTranslation().clone(),  new Quaternion(targetSpatial.getLocalRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));


				float angle = targetSpatial.getLocalRotation().toAngleAxis(Vector3f.UNIT_Z);
				if(targetSpatial.getLocalRotation().z <0) angle = FastMath.TWO_PI - angle;

				for (RotateTranslateScaleListener l: listeners)
					l.itemRotated(this, targetSpatial, angle, oldtq.toAngleAxis(AXIS_Z));
			}
		}

	}

	protected void applyMultiCursorTransform() {

		Vector2f oldCenter = new Vector2f();
		oldCenter.interpolate(cursor1OldPos, cursor2OldPos, 0.5f);
		Vector2f currentCenter = new Vector2f();
		currentCenter.interpolate(cursor1Pos, cursor2Pos, 0.5f);

		Vector2f spatialLoc = new Vector2f();
		//spatialLoc.x = targetSpatial.getWorldTranslation().x;
		//spatialLoc.y = targetSpatial.getWorldTranslation().y;
		spatialLoc.x = targetSpatial.getLocalTranslation().x;
		spatialLoc.y = targetSpatial.getLocalTranslation().y;

		Vector2f centerToSpatial = spatialLoc.subtract(oldCenter);

		float oldAngle = cursor2OldPos.subtract(cursor1OldPos).getAngle();
		float curAngle = cursor2Pos.subtract(cursor1Pos).getAngle();
		float angleChange = curAngle - oldAngle;
		float currentCenterToSpatialAngel = centerToSpatial.getAngle()+angleChange;

		float oldScale = cursor2OldPos.subtract(cursor1OldPos).length();
		float newScale = cursor2Pos.subtract(cursor1Pos).length();
		float scaleChange = newScale / oldScale;

		if(targetSpatial.getLocalScale().x*scaleChange < minScale || targetSpatial.getLocalScale().x*scaleChange > maxScale) {
			scaleChange = 1f;
		}
		
		float newDistFromCurrentCenterToSpatial = scaleChange * centerToSpatial.length();

		float dx = newDistFromCurrentCenterToSpatial * FastMath.cos(currentCenterToSpatialAngel);
		float dy = newDistFromCurrentCenterToSpatial * FastMath.sin(currentCenterToSpatialAngel);

		Vector2f newScreenPosition = currentCenter.add(new Vector2f(dx, -dy));

		// when two blobs are v. close together, dx and dy can end up being NaN
		if(Float.isNaN(dx) || Float.isNaN(dy)) newScreenPosition = currentCenter;

		//Vector3f newPos = new Vector3f(newScreenPosition.x, newScreenPosition.y, targetSpatial.getWorldTranslation().z);

		Vector3f newPos = new Vector3f(newScreenPosition.x, newScreenPosition.y, targetSpatial.getLocalTranslation().z);

		if(newPos != null && targetSpatial.getParent() != null) {
			//update location
			//targetSpatial.getParent().worldToLocal(newPos, targetSpatial.getLocalTranslation());

			//targetSpatial.getWorldTranslation().x = newScreenPosition.x;
			//targetSpatial.getWorldTranslation().y = newScreenPosition.y;
			targetSpatial.getLocalTranslation().x = newScreenPosition.x;
			targetSpatial.getLocalTranslation().y = newScreenPosition.y;
			
			worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));

			for (RotateTranslateScaleListener l: listeners)
				l.itemMoved(this, targetSpatial, newScreenPosition.x, newScreenPosition.y, currentCenter.x, currentCenter.y);
			
			//update rotation
			Quaternion oldtq = targetSpatial.getLocalRotation();
			Quaternion tq = new Quaternion();
			tq.fromAngleAxis(-angleChange, AXIS_Z);
			tq.multLocal(targetSpatial.getLocalRotation());
			float[] axisR = new float[3];
			tq.toAngles(axisR);
			
			if(rotateLimits){
				if(axisR[2] > minRotate && axisR[2] < maxRotate) {
					targetSpatial.setLocalRotation(tq);			
				}
			}else{
				targetSpatial.setLocalRotation(tq);
			}
			
			float angle = targetSpatial.getLocalRotation().toAngleAxis(Vector3f.UNIT_Z);
			if(targetSpatial.getLocalRotation().z <0) angle = FastMath.TWO_PI - angle;

			for (RotateTranslateScaleListener l: listeners)
				l.itemRotated(this, targetSpatial, angle, oldtq.toAngleAxis(AXIS_Z));

			//update scale
			targetSpatial.getLocalScale().multLocal(scaleChange);

			for (RotateTranslateScaleListener l: listeners)
				l.itemScaled(this, targetSpatial, scaleChange);
		}
	}

	protected void updateCursor1() {

		//cursor1Pos.x = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x;
		//cursor1Pos.y = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y;

		Vector2f rotatedPosition = this.screenToTable(getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y);
		cursor1Pos.x = rotatedPosition.x;
		cursor1Pos.y = rotatedPosition.y;

		rotatedPosition = this.screenToTable(getScreenCursorByIndex(0).getOldCursorScreenPosition().x, getScreenCursorByIndex(0).getOldCursorScreenPosition().y);
		cursor1OldPos.x = rotatedPosition.x;
		cursor1OldPos.y = rotatedPosition.y;

		//cursor1OldPos.x = getScreenCursorByIndex(0).getOldCursorScreenPosition().x;
		//cursor1OldPos.y = getScreenCursorByIndex(0).getOldCursorScreenPosition().y;
	}

	protected void updateCursor2() {
		//cursor2Pos.x = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().x;
		//cursor2Pos.y = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().y;

		Vector2f rotatedPosition = this.screenToTable(getScreenCursorByIndex(1).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(1).getCurrentCursorScreenPosition().y);
		cursor2Pos.x = rotatedPosition.x;
		cursor2Pos.y = rotatedPosition.y;

		rotatedPosition = this.screenToTable(getScreenCursorByIndex(1).getOldCursorScreenPosition().x, getScreenCursorByIndex(1).getOldCursorScreenPosition().y);
		cursor2OldPos.x = rotatedPosition.x;
		cursor2OldPos.y = rotatedPosition.y;

		//cursor2OldPos.x = getScreenCursorByIndex(1).getOldCursorScreenPosition().x;
		//cursor2OldPos.y = getScreenCursorByIndex(1).getOldCursorScreenPosition().y;
	}

	public void setScaleLimits(float min, float max) {
		minScale = min;
		maxScale = max;
	}
	
	public float getScaleMax(){
		return maxScale;
	}
	
	public float getScaleMin(){
		return minScale;
	}


	public void setRotateLimits(float min, float max) {
		minRotate = min;
		maxRotate = max;
		rotateLimits = true;
	}

	protected void setOldCursor(){
		for (ScreenCursor c:screenCursors){
			ScreenCursorRecord s = new ScreenCursorRecord(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y );
			c.setOldCursorScreenPosition(s);
		}
	}

	public void addRotateTranslateScaleListener(RotateTranslateScaleListener l){
		listeners.add(l);
	}

	public void removeRotateTranslateScaleListener(RotateTranslateScaleListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}

	public interface RotateTranslateScaleListener {
		public void itemRotated(OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial, float newAngle, float oldAngle);
		public void itemMoved(OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
		public void itemScaled(OrthoControlPointRotateTranslateScale multiTouchElement, Spatial targetSpatial,  float scaleChange);
	}

	private Vector2f screenToTable(float x, float y) {

		if (targetSpatial.getParent()==null)
			return new Vector2f(x, y);

		Vector2f screenPosition = new Vector2f(x, y);
		float parentAngle = targetSpatial.getParent().getLocalRotation().toAngleAxis(Vector3f.UNIT_Z);
		Vector2f currentCenter = new Vector2f(targetSpatial.getParent().getWorldTranslation().x, targetSpatial.getParent().getWorldTranslation().y);
		Vector2f currentCenterToPoint = screenPosition.subtract(currentCenter);
		float newAngle = -(currentCenterToPoint.getAngle()-parentAngle);
		float length = currentCenterToPoint.length() / targetSpatial.getParent().getLocalScale().x;
		float newX = FastMath.cos(newAngle)*length;
		float newY = FastMath.sin(newAngle)*length;

		return new Vector2f(newX, newY);

	}



}
