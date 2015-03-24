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

package apps.projectmanagement.gesture;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale;


/**
 * The Class PeriodBarControl.
 */
public class PeriodBarControl extends OrthoControlPointRotateTranslateScale{

	/** The schedule bar listeners. */
	protected List<ScheduleBarListener> scheduleBarListeners = new ArrayList<ScheduleBarListener>();
	
	/** The length. */
	protected int length;
	
	/** The unit length. */
	protected int unitLength;
	
	/**
	 * Instantiates a new period bar control.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 * @param length the length
	 * @param unitLength the unit length
	 */
	public PeriodBarControl(Spatial pickingSpatial, Spatial targetSpatial, int length, int unitLength) {
		super(pickingSpatial, targetSpatial);
		this.length = length;
		this.unitLength = unitLength;
		maxScale = 400.0f;
		minScale =0.2f;

	}


	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#applySingleCursorTransform()
	 */
	protected void applySingleCursorTransform() {

		Vector2f posChange = cursor1Pos.subtract(cursor1OldPos);
			
		float newX = targetSpatial.getLocalTranslation().x + posChange.x;
		
		if (newX-(unitLength*targetSpatial.getLocalScale().x)/2<-length/2) newX = -length/2+(unitLength*targetSpatial.getLocalScale().x)/2;
		
		if ((newX+(unitLength*targetSpatial.getLocalScale().x)/2)>length/2 ) newX = length/2-(unitLength*targetSpatial.getLocalScale().x)/2;

		targetSpatial.getLocalTranslation().x = newX;
		worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));
		
		for (ScheduleBarListener l: scheduleBarListeners){
			l.moved(newX-(unitLength*targetSpatial.getLocalScale().x)/2, unitLength*targetSpatial.getLocalScale().x);
			
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#applyMultiCursorTransform()
	 */
	protected void applyMultiCursorTransform() {

		Vector2f oldCenter = new Vector2f();
		oldCenter.interpolate(cursor1OldPos, cursor2OldPos, 0.5f);
		Vector2f currentCenter = new Vector2f();
		currentCenter.interpolate(cursor1Pos, cursor2Pos, 0.5f);

		Vector2f spatialLoc = new Vector2f();
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
			//targetSpatial.getLocalTranslation().x = newScreenPosition.x;
			//targetSpatial.getLocalTranslation().y = newScreenPosition.y;
			
			//worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));

			//for (RotateTranslateScaleListener l: listeners)
				//l.itemMoved(this, targetSpatial, newScreenPosition.x, newScreenPosition.y, currentCenter.x, currentCenter.y);
			
			//update rotation
			@SuppressWarnings("unused")
			Quaternion oldtq = targetSpatial.getLocalRotation();
			Quaternion tq = new Quaternion();
			tq.fromAngleAxis(-angleChange, AXIS_Z);
			tq.multLocal(targetSpatial.getLocalRotation());
			float[] axisR = new float[3];
			tq.toAngles(axisR);
//			if(axisR[2] > minRotate && axisR[2] < maxRotate)
				//targetSpatial.setLocalRotation(tq);

			//float angle = targetSpatial.getLocalRotation().toAngleAxis(Vector3f.UNIT_Z);
			//if(targetSpatial.getLocalRotation().z <0) angle = FastMath.TWO_PI - angle;

			//for (RotateTranslateScaleListener l: listeners)
			//	l.itemRotated(this, targetSpatial, angle, oldtq.toAngleAxis(AXIS_Z));

			float oldStartPosition = -(unitLength*targetSpatial.getLocalScale().x)/2+targetSpatial.getLocalTranslation().x;
			
			//update scale
			if (scaleChange<=0) return;
			targetSpatial.getLocalScale().multLocal(new Vector3f(scaleChange, 1, 1));

			float newX = targetSpatial.getLocalTranslation().x;
			
			if (newX-(unitLength*targetSpatial.getLocalScale().x)/2<-length/2) newX = -length/2+(unitLength*targetSpatial.getLocalScale().x)/2;
			else if ((newX+(unitLength*targetSpatial.getLocalScale().x)/2)>length/2 ) newX = length/2-(unitLength*targetSpatial.getLocalScale().x)/2;
			else newX = oldStartPosition+(unitLength*targetSpatial.getLocalScale().x)/2;
				
			targetSpatial.getLocalTranslation().x = newX;
			worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));
			
			for (ScheduleBarListener l: scheduleBarListeners){
				l.moved(newX-(unitLength*targetSpatial.getLocalScale().x)/2, unitLength*targetSpatial.getLocalScale().x);
				
			}
			
			for (RotateTranslateScaleListener l: listeners)
				l.itemScaled(this, targetSpatial, scaleChange);
		}
	}

	/**
	 * Gets the current element2 d coords for cursor.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the current element2 d coords for cursor
	 */
	private Vector2f getCurrentElement2DCoordsForCursor(float x, float y) {
		Vector3f pos = new Vector3f(x, y,0);
		Vector3f selectionLocal = new Vector3f();
		this.targetSpatial.worldToLocal(pos, selectionLocal);
		
		return new Vector2f(selectionLocal.x, selectionLocal.y);		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#updateCursor1()
	 */
	@Override
	protected void updateCursor1() {

		Vector2f cc1 = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y);
		Vector2f rotatedPosition = this.screenToTable(cc1.x, cc1.y);
		cursor1Pos.x = rotatedPosition.x;
		cursor1Pos.y = rotatedPosition.y;

		Vector2f cc1Old = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(0).getOldCursorScreenPosition().x, getScreenCursorByIndex(0).getOldCursorScreenPosition().y);
		rotatedPosition = this.screenToTable(cc1Old.x, cc1Old.y);
		cursor1OldPos.x = rotatedPosition.x;
		cursor1OldPos.y = rotatedPosition.y;
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.elements.twod.OrthoControlPointRotateTranslateScale#updateCursor2()
	 */
	@Override
	protected void updateCursor2() {

		Vector2f cc1 = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(1).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(1).getCurrentCursorScreenPosition().y);
		Vector2f rotatedPosition = this.screenToTable(cc1.x, cc1.y);
		cursor2Pos.x = rotatedPosition.x;
		cursor2Pos.y = rotatedPosition.y;

		Vector2f cc2 = getCurrentElement2DCoordsForCursor(getScreenCursorByIndex(1).getOldCursorScreenPosition().x, getScreenCursorByIndex(1).getOldCursorScreenPosition().y);
		rotatedPosition = this.screenToTable(cc2.x, cc2.y);
		cursor2OldPos.x = rotatedPosition.x;
		cursor2OldPos.y = rotatedPosition.y;
	}
	
	/**
	 * Screen to table.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the vector2f
	 */
	private Vector2f screenToTable(float x, float y) {

		if (targetSpatial.getParent()==null)
			return new Vector2f(x, y);

		Vector2f screenPosition = new Vector2f(x, y);
		float parentAngle = targetSpatial.getParent().getLocalRotation().toAngleAxis(Vector3f.UNIT_Z);
		Vector2f currentCenter = new Vector2f(targetSpatial.getParent().getLocalTranslation().x, targetSpatial.getParent().getLocalTranslation().y);
		Vector2f currentCenterToPoint = screenPosition.subtract(currentCenter);
		float newAngle = -(currentCenterToPoint.getAngle()-parentAngle);
		float length = currentCenterToPoint.length() / targetSpatial.getParent().getLocalScale().x;
		float newX = FastMath.cos(newAngle)*length;
		float newY = FastMath.sin(newAngle)*length;

		return new Vector2f(newX, newY);

	}


	/**
	 * Adds the schedule bar listener.
	 *
	 * @param l the l
	 */
	public void addScheduleBarListener(ScheduleBarListener l){
		scheduleBarListeners.add(l);
	}

	/**
	 * Removes the schedule bar listener.
	 *
	 * @param l the l
	 */
	public void removeScheduleBarListener(ScheduleBarListener l){
		if (scheduleBarListeners.contains(l))
			scheduleBarListeners.remove(l);
	}

	/**
	 * The listener interface for receiving scheduleBar events.
	 * The class that is interested in processing a scheduleBar
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addScheduleBarListener<code> method. When
	 * the scheduleBar event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ScheduleBarEvent
	 */
	public interface ScheduleBarListener {
		
		/**
		 * Moved.
		 *
		 * @param startPointX the start point x
		 * @param length the length
		 */
		public void moved(float startPointX, float length);
	}

}
