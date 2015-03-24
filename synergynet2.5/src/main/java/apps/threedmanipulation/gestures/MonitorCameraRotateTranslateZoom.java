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

package apps.threedmanipulation.gestures;

import java.util.List;

import apps.threedmanipulation.utils.CameraModel;

import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.jme.gfx.JMEGfxUtils;
import synergynetframework.jme.pickingsystem.data.ThreeDPickResultData;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


/**
 * The Class MonitorCameraRotateTranslateZoom.
 */
public class MonitorCameraRotateTranslateZoom extends ThreeDMultiTouchElement {

	/** The mode viewrotatable. */
	public static String MODE_VIEWROTATABLE = "viewrotatable";
	
	/** The mode cameramanipulable. */
	public static String MODE_CAMERAMANIPULABLE = "cameramanipulable";
	
	/** The mode remotecontrol. */
	public static String MODE_REMOTECONTROL = "remotecontrol";
	
	/** The scale min. */
	protected float scaleMin = Float.NaN;
	
	/** The scale max. */
	protected float scaleMax = Float.NaN;
	
	/** The pick results. */
	protected PickResults pickResults = null;
	
	/** The pick root node. */
	protected Node pickRootNode; 
	
	/** The telescope manipulate ojbect. */
	protected OjbectManipulation telescopeManipulateOjbect;
	
	/** The manipulatable ojbects. */
	protected List<Spatial> manipulatableOjbects;
	
	/** The mode. */
	protected String mode = MODE_CAMERAMANIPULABLE;
	
	/**
	 * Instantiates a new monitor camera rotate translate zoom.
	 *
	 * @param pickingAndTargetSpatial the picking and target spatial
	 */
	public MonitorCameraRotateTranslateZoom(Spatial pickingAndTargetSpatial) {
		super(pickingAndTargetSpatial);
	}

	/**
	 * Instantiates a new monitor camera rotate translate zoom.
	 *
	 * @param pickSpatial the pick spatial
	 * @param targetSpatial the target spatial
	 */
	public MonitorCameraRotateTranslateZoom(Spatial pickSpatial, Spatial targetSpatial) {
		super(pickSpatial, targetSpatial);
	}
	
	/**
	 * Instantiates a new monitor camera rotate translate zoom.
	 *
	 * @param pickSpatial the pick spatial
	 * @param targetSpatial the target spatial
	 * @param telescopeManipulateOjbect the telescope manipulate ojbect
	 * @param manipulatableOjbects the manipulatable ojbects
	 */
	public MonitorCameraRotateTranslateZoom(Spatial pickSpatial, Spatial targetSpatial, OjbectManipulation telescopeManipulateOjbect,  List<Spatial> manipulatableOjbects) {
		super(pickSpatial, targetSpatial);
		
		pickRootNode = targetSpatial.getParent();
		pickResults = new BoundingPickResults();
		pickResults.setCheckDistance(true); 
		this.telescopeManipulateOjbect = telescopeManipulateOjbect;
		this.manipulatableOjbects = manipulatableOjbects;
	}
	
	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	public void setMode (String mode){
		this.mode = mode;
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
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
			
			Vector3f newPosition = JMEGfxUtils.getCursorWorldCoordinatesOnSpatialPlane(screenPosCursor1, targetSpatial);
			
			
			CameraNode camNode = (CameraNode)targetSpatial;
			
			if (mode.equals(MODE_VIEWROTATABLE)){		
				float[] angles = {-cursor1ToSpatial.y/300, cursor1ToSpatial.x/300, 0};
				Quaternion q = new Quaternion();
				q.fromAngles(angles);	
				q.multLocal(targetSpatial.getLocalRotation());
				targetSpatial.setLocalRotation(q);
			}
			else if (mode.equals(MODE_CAMERAMANIPULABLE)){
			
				Vector3f viewUp= camNode.getCamera().getUp();
				Vector3f viewLeft = camNode.getCamera().getLeft();
			
				Quaternion qLeft = new Quaternion();
				qLeft.fromAngleAxis(cursor1ToSpatial.y/300, viewLeft);
				qLeft.multLocal(targetSpatial.getLocalRotation());
				targetSpatial.setLocalRotation(qLeft);
				
				Quaternion qUp = new Quaternion();
				qUp.fromAngleAxis(cursor1ToSpatial.x/300, viewUp);
				qUp.multLocal(targetSpatial.getLocalRotation());
				targetSpatial.setLocalRotation(qUp);
		
			}
			else {
				targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
			}
			
			
			//picking object in scence						
			pickResults.clear();	
			
			Ray ray = new Ray(camNode.getCamera().getLocation(), camNode.getCamera().getDirection());	
			pickRootNode.calculatePick(ray, pickResults);				

			int pickedObjectNumber = pickResults.getNumber();
			Spatial pickedObject = null;
			for (int i=0; i<pickedObjectNumber; i++){
				if (pickResults.getPickData(i).getTargetMesh().getParent().getClass()!=CameraModel.class){
					pickedObject = pickResults.getPickData(i).getTargetMesh();
					break;
				}
			}
			
			if (pickedObject == null)
				return;
			
			if (pickResults.getNumber()<=0)
				telescopeManipulateOjbect.setControlledSpatial(null);
			else if (manipulatableOjbects.contains(pickedObject))
				telescopeManipulateOjbect.setControlledSpatial(pickedObject);
			else if ( manipulatableOjbects.contains(pickedObject.getParent()))
				telescopeManipulateOjbect.setControlledSpatial(pickedObject.getParent());
			else
				telescopeManipulateOjbect.setControlledSpatial(null);	
			
			System.out.println("selected object = "+pickedObject);
			System.out.println("camera direction is "+camNode.getCamera().getDirection());
			
		

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

			float cursorScale = screenPosCursor2.subtract(screenPosCursor1).length() / originalScreenPosCursor2.subtract(originalScreenPosCursor1).length();

			float newDistFromCursor1ToSpatial = cursorScale * cursor1ToSpatialAtPick.length();

			float dx = newDistFromCursor1ToSpatial * FastMath.cos(newCursor1ToSpatialAngle);
			float dy = newDistFromCursor1ToSpatial * FastMath.sin(newCursor1ToSpatialAngle);

			Vector2f newScreenPosition = screenPosCursor1.add(new Vector2f(dx, -dy));

			Vector3f newPosition = JMEGfxUtils.getCursorWorldCoordinatesOnSpatialPlane(newScreenPosition, targetSpatial);
			if(newPosition != null) {
							
				System.out.println("y = "+ newPosition.y);
				if (newPosition.y>1.5){
				targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
				
				worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),
						new Quaternion(targetSpatial.getWorldRotation().clone()), targetSpatial.getWorldScale().clone(), System.nanoTime()));
				}
				
				if (mode.equals(MODE_VIEWROTATABLE)){		
					targetSpatial.setLocalRotation(getCurrentTargetSpatialRotationFromCursorChange());
				}
				

				if((getScaleAtOrigin().mult(cursorScale).x < scaleMax) && (getScaleAtOrigin().mult(cursorScale).x > scaleMin)) {
					targetSpatial.setLocalScale(getScaleAtOrigin().mult(cursorScale));
				}

				targetSpatial.updateGeometricState(0f, true);
				
				pickResults.clear();	
				CameraNode camNode = (CameraNode)targetSpatial;
				Ray ray = new Ray(camNode.getCamera().getLocation(), camNode.getCamera().getDirection());	
				pickRootNode.calculatePick(ray, pickResults);				

				if (pickResults.getNumber()<=0)
					telescopeManipulateOjbect.setControlledSpatial(null);
				else if (manipulatableOjbects.contains(pickResults.getPickData(0).getTargetMesh()))
					telescopeManipulateOjbect.setControlledSpatial(pickResults.getPickData(0).getTargetMesh());
				else if ( manipulatableOjbects.contains(pickResults.getPickData(0).getTargetMesh().getParent()))
					telescopeManipulateOjbect.setControlledSpatial(pickResults.getPickData(0).getTargetMesh().getParent());
				else
					telescopeManipulateOjbect.setControlledSpatial(null);
				
			}

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

	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}


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

}
