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

package apps.threedmanipulationexperiment.gestures;

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
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursorRecord;
import synergynetframework.jme.gfx.JMEGfxUtils;
import synergynetframework.jme.pickingsystem.data.ThreeDPickResultData;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


/**
 * The Class ObjectRotateTranslate.
 */
public class ObjectRotateTranslate extends ThreeDMultiTouchElement {

	/** The cursor1 pos. */
	protected Vector2f cursor1Pos = new Vector2f();
	
	/** The cursor2 pos. */
	protected Vector2f cursor2Pos = new Vector2f();
	
	/** The cursor1 old pos. */
	protected Vector2f cursor1OldPos = new Vector2f();
	
	/** The cursor2 old pos. */
	protected Vector2f cursor2OldPos = new Vector2f();
	
	/** The scale min. */
	protected float scaleMin = Float.NaN;
	
	/** The scale max. */
	protected float scaleMax = Float.NaN;
	
	/** The pick results. */
	protected PickResults pickResults = null;
	
	/** The remote object. */
	protected Spatial remoteObject = null;
	
	/** The threshold. */
	protected float threshold = 0.25f;
	
	/** The rotation speed. */
	protected int rotationSpeed = 100;
	
	/** The listeners. */
	protected List<ObjectRotateTranslateScaleListener> listeners = new ArrayList<ObjectRotateTranslateScaleListener>();
	
	/** The experiment eventlisteners. */
	protected List<ExperimentEventListener> experimentEventlisteners = new ArrayList<ExperimentEventListener>();
	
	/** The touch number. */
	protected int touchNumber=0;
	
	/** The has task completion message sent. */
	protected boolean hasTaskCompletionMessageSent=false;
	
	/**
	 * Instantiates a new object rotate translate.
	 *
	 * @param pickingAndTargetSpatial the picking and target spatial
	 */
	public ObjectRotateTranslate(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	/**
	 * Instantiates a new object rotate translate.
	 *
	 * @param pickSpatial the pick spatial
	 * @param targetSpatial the target spatial
	 */
	public ObjectRotateTranslate(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}
	
	/**
	 * Instantiates a new object rotate translate.
	 *
	 * @param pickSpatial the pick spatial
	 * @param targetSpatial the target spatial
	 * @param remoteObject the remote object
	 */
	public ObjectRotateTranslate(Spatial pickSpatial, Spatial targetSpatial, Spatial remoteObject) {
		super(pickSpatial, targetSpatial);
		this.remoteObject = remoteObject;

	}
	
	/**
	 * Reset touch number.
	 */
	public void resetTouchNumber(){
		touchNumber =0;
		hasTaskCompletionMessageSent=false;
	}
	
	/**
	 * Sets the rotation speed.
	 *
	 * @param rotationSpeed the new rotation speed
	 */
	public void setRotationSpeed(int rotationSpeed){
		this.rotationSpeed = rotationSpeed;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		
		if(screenCursors.size() == 1) {
			updateCursor1();
			applySingleCursorTransform();
			setOldCursor();
		}else if (screenCursors.size() == 2){
			updateCursor1();
			updateCursor2();
			applyMultiCursorTransform();
			setOldCursor();
		}
		
		
			
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
		if(screenCursors.size() == 1) {
			cursor1OldPos.x = c.getCurrentCursorScreenPosition().x;
			cursor1OldPos.y = c.getCurrentCursorScreenPosition().y;
			this.touchNumber++;
		}else if(screenCursors.size() == 2) {
			updateCursor1();
			updateCursor2();
			cursor1OldPos = cursor1Pos.clone();
			cursor2OldPos = cursor2Pos.clone();
		}

	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

	/**
	 * Apply single cursor transform.
	 */
	protected void applySingleCursorTransform() {
		int previousPostionIndex=0;
		if (getScreenCursorByIndex(0).getCurrentPositionIndex()>0){
			previousPostionIndex = getScreenCursorByIndex(0).getCurrentPositionIndex()-1;
		}
		
		Vector2f originalScreenPosCursor1 = getScreenCursorByIndex(0).getPositionAtIndex(previousPostionIndex).getPosition();
		Vector2f screenPosCursor1 = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().getPosition();	
		Vector2f cursor1ToSpatial = screenPosCursor1.subtract(originalScreenPosCursor1);

		float[] angles = {-cursor1ToSpatial.y/rotationSpeed, cursor1ToSpatial.x/rotationSpeed, 0};
		Quaternion q = new Quaternion();
		q.fromAngles(angles);	
		q.multLocal(targetSpatial.getLocalRotation());
		targetSpatial.setLocalRotation(q);
		
		if (remoteObject!=null){
			remoteObject.setLocalRotation(q);
			remoteObject.updateGeometricState(0f, true);
		}
		
		Quaternion currentAngle = this.targetSpatial.getLocalRotation();
		float angleToX = 0;
		float angleToY = 0;
		float angleToZ = 0;
		angleToX = currentAngle.toAngleAxis(Vector3f.UNIT_X);
		angleToY = currentAngle.toAngleAxis(Vector3f.UNIT_Y);
		angleToZ = currentAngle.toAngleAxis(Vector3f.UNIT_Z);
			
		System.out.println(angleToX);
		
		if ((Math.abs(angleToX)<=threshold || FastMath.TWO_PI-angleToX<=threshold) && (Math.abs(angleToY)<=threshold || FastMath.TWO_PI-angleToY<=threshold) && (Math.abs(angleToZ)<=threshold || FastMath.TWO_PI-angleToZ<=threshold) ){
			if (hasTaskCompletionMessageSent) return;
			hasTaskCompletionMessageSent=true;
			for (ExperimentEventListener l: experimentEventlisteners){
				l.taskCompleted(touchNumber);
			}
		}
	}

	/**
	 * Apply multi cursor transform.
	 */
	protected void applyMultiCursorTransform() {
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
										
		//	targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
			
		//	worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),
		//			new Quaternion(targetSpatial.getWorldRotation().clone()), targetSpatial.getWorldScale().clone(), System.nanoTime()));
			
			for (ObjectRotateTranslateScaleListener l: listeners)
				l.itemMoved(targetSpatial, newPosition.x, newPosition.y, spatialScreenAtPick.x, spatialScreenAtPick.y);
			
			
			targetSpatial.setLocalRotation(getCurrentTargetSpatialRotationFromCursorChange());
					
			//if((getScaleAtOrigin().mult(cursorScale).x < scaleMax) && (getScaleAtOrigin().mult(cursorScale).x > scaleMin)) {
				//targetSpatial.setLocalScale(getScaleAtOrigin().mult(cursorScale));
				//targetSpatial.setLocalTranslation(targetSpatial.getLocalTranslation().x, targetSpatial.getLocalTranslation().y, targetSpatial.getLocalTranslation().z+(cursorScale-1)/10);
			//}

			targetSpatial.updateGeometricState(0f, true);
			
			if (remoteObject!=null){
				remoteObject.setLocalRotation(targetSpatial.getLocalRotation());
				remoteObject.updateGeometricState(0f, true);
			}	
			
			Quaternion currentAngle = this.targetSpatial.getLocalRotation();
			float angleToX = 0;
			float angleToY = 0;
			float angleToZ = 0;
			angleToX = currentAngle.toAngleAxis(Vector3f.UNIT_X);
			angleToY = currentAngle.toAngleAxis(Vector3f.UNIT_Y);
			angleToZ = currentAngle.toAngleAxis(Vector3f.UNIT_Z);
				
			if ((Math.abs(angleToX)<=threshold || FastMath.TWO_PI-angleToX<=threshold) && (Math.abs(angleToY)<=threshold || FastMath.TWO_PI-angleToY<=threshold) && (Math.abs(angleToZ)<=threshold || FastMath.TWO_PI-angleToZ<=threshold) ){
					
				if (hasTaskCompletionMessageSent) return;
				hasTaskCompletionMessageSent=true;
				for (ExperimentEventListener l: experimentEventlisteners){
					l.taskCompleted(touchNumber);
				}
			}
		}
	
	}
	
	/**
	 * Update cursor1.
	 */
	protected void updateCursor1() {

		cursor1Pos.x = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x;
		cursor1Pos.y = getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y;


		cursor1OldPos.x = getScreenCursorByIndex(0).getOldCursorScreenPosition().x;
		cursor1OldPos.y = getScreenCursorByIndex(0).getOldCursorScreenPosition().y;
	}

	/**
	 * Update cursor2.
	 */
	protected void updateCursor2() {
		cursor2Pos.x = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().x;
		cursor2Pos.y = getScreenCursorByIndex(1).getCurrentCursorScreenPosition().y;



		cursor2OldPos.x = getScreenCursorByIndex(1).getOldCursorScreenPosition().x;
		cursor2OldPos.y = getScreenCursorByIndex(1).getOldCursorScreenPosition().y;
	}
	
	/**
	 * Sets the old cursor.
	 */
	protected void setOldCursor(){
		for (ScreenCursor c:screenCursors){
			ScreenCursorRecord s = new ScreenCursorRecord(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y );
			c.setOldCursorScreenPosition(s);
		}
	}

	/**
	 * Gets the scale min.
	 *
	 * @return the scale min
	 */
	public float getScaleMin() {
		return scaleMin;
	}

	/**
	 * Sets the scale min.
	 *
	 * @param scaleMin the new scale min
	 */
	public void setScaleMin(float scaleMin) {
		this.scaleMin = scaleMin;
	}

	/**
	 * Gets the scale max.
	 *
	 * @return the scale max
	 */
	public float getScaleMax() {
		return scaleMax;
	}

	/**
	 * Sets the scale max.
	 *
	 * @param scaleMax the new scale max
	 */
	public void setScaleMax(float scaleMax) {
		this.scaleMax = scaleMax;
	}

	/**
	 * Sets the scale limits.
	 *
	 * @param min the min
	 * @param max the max
	 */
	public void setScaleLimits(float min, float max) {
		setScaleMin(min);
		setScaleMax(max);
	}
	
	/**
	 * Adds the rotate translate scale listener.
	 *
	 * @param l the l
	 */
	public void addRotateTranslateScaleListener(ObjectRotateTranslateScaleListener l){
		listeners.add(l);
	}

	/**
	 * Removes the rotate translate scale listener.
	 *
	 * @param l the l
	 */
	public void removeRotateTranslateScaleListener(ObjectRotateTranslateScaleListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}
	
	/**
	 * The listener interface for receiving objectRotateTranslateScale events.
	 * The class that is interested in processing a objectRotateTranslateScale
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addObjectRotateTranslateScaleListener<code> method. When
	 * the objectRotateTranslateScale event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ObjectRotateTranslateScaleEvent
	 */
	public interface ObjectRotateTranslateScaleListener {
		
		/**
		 * Item moved.
		 *
		 * @param targetSpatial the target spatial
		 * @param newLocationX the new location x
		 * @param newLocationY the new location y
		 * @param oldLocationX the old location x
		 * @param oldLocationY the old location y
		 */
		public void itemMoved(Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
	}
	
	/**
	 * Adds the experiment event listener.
	 *
	 * @param l the l
	 */
	public void addExperimentEventListener(ExperimentEventListener l){
		experimentEventlisteners.add(l);
	}

	/**
	 * Removes the experiment event listener.
	 *
	 * @param l the l
	 */
	public void removeExperimentEventListener(ExperimentEventListener l){
		if (experimentEventlisteners.contains(l))
			experimentEventlisteners.remove(l);
	}

	/**
	 * The listener interface for receiving experimentEvent events.
	 * The class that is interested in processing a experimentEvent
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addExperimentEventListener<code> method. When
	 * the experimentEvent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ExperimentEventEvent
	 */
	public interface ExperimentEventListener {
		
		/**
		 * Task completed.
		 *
		 * @param touchNumber the touch number
		 */
		public void taskCompleted(int touchNumber);
	}

}
