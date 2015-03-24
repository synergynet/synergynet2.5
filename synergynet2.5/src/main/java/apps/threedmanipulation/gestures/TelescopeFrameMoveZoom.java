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

import java.util.ArrayList;
import java.util.List;

import apps.threedmanipulation.listener.ToolListener;

import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jme.math.FastMath;

import synergynetframework.jme.cursorsystem.TwoDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursorRecord;
import synergynetframework.jme.cursorsystem.cursordata.WorldCursorRecord;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;


/**
 * The Class TelescopeFrameMoveZoom.
 *
 * @author dcs0ah1, pwpp25, dcs2ima
 */
public class TelescopeFrameMoveZoom extends TwoDMultiTouchElement{

	/** The cursor1 pos. */
	protected Vector2f cursor1Pos = new Vector2f();
	
	/** The cursor2 pos. */
	protected Vector2f cursor2Pos = new Vector2f();
	
	/** The cursor1 old pos. */
	protected Vector2f cursor1OldPos = new Vector2f();
	
	/** The cursor2 old pos. */
	protected Vector2f cursor2OldPos = new Vector2f();

	/** The max scale. */
	protected float maxScale = 2.0f;
	
	/** The min scale. */
	protected float minScale = 0.5f;

	/** The more than two gives rts. */
	private boolean moreThanTwoGivesRTS = false;
	
	/** The telescope manipulate ojbect. */
	protected OjbectManipulation telescopeManipulateOjbect;
	
	/** The manipulatable ojbects. */
	protected List<Spatial> manipulatableOjbects;
	
	/** The cam node. */
	private CameraNode camNode;
	
	/** The listeners. */
	protected List<RotateTranslateScaleListener> listeners = new ArrayList<RotateTranslateScaleListener>();
	
	/** The tool listeners. */
	protected List<ToolListener> toolListeners = new ArrayList<ToolListener>();
	
	/** The pick results. */
	protected PickResults pickResults = null;
	
	/** The pick root node. */
	protected Node pickRootNode; 

	/**
	 * Instantiates a new telescope frame move zoom.
	 *
	 * @param pickingAndTargetSpatial the picking and target spatial
	 */
	public TelescopeFrameMoveZoom(Spatial pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}

	/**
	 * Instantiates a new telescope frame move zoom.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 */
	public TelescopeFrameMoveZoom(Spatial pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
	}
	
	/**
	 * Instantiates a new telescope frame move zoom.
	 *
	 * @param pickingSpatial the picking spatial
	 * @param targetSpatial the target spatial
	 * @param camNode the cam node
	 * @param telescopeManipulateOjbect the telescope manipulate ojbect
	 * @param manipulatableOjbects the manipulatable ojbects
	 */
	public TelescopeFrameMoveZoom(Spatial pickingSpatial, Spatial targetSpatial, CameraNode camNode, OjbectManipulation telescopeManipulateOjbect, List<Spatial> manipulatableOjbects) {
		super(pickingSpatial, targetSpatial);
		this.camNode = camNode;
		this.telescopeManipulateOjbect = telescopeManipulateOjbect;
		this.manipulatableOjbects = manipulatableOjbects;
		
		pickRootNode = camNode.getParent();
		pickResults = new BoundingPickResults();
		pickResults.setCheckDistance(true); 
	}

	/**
	 * Allow more than two to rotate and scale.
	 *
	 * @param b the b
	 */
	public void allowMoreThanTwoToRotateAndScale(boolean b) {
		moreThanTwoGivesRTS = b;
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
		}else if(screenCursors.size() > 2) {
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
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		if(screenCursors.size() == 1) {
			Vector2f rotatedPosition = this.screenToTable(c.getCurrentCursorScreenPosition().x, c.getCurrentCursorScreenPosition().y);
			cursor1OldPos.x = rotatedPosition.x;
			cursor1OldPos.y = rotatedPosition.y;
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
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {}

	/**
	 * Apply single cursor transform.
	 */
	protected void applySingleCursorTransform() {

		
		Vector2f posChange = cursor1Pos.subtract(cursor1OldPos);
		Vector3f newP = new Vector3f(targetSpatial.getLocalTranslation().x += posChange.x,	targetSpatial.getLocalTranslation().y += posChange.y, targetSpatial.getLocalTranslation().z);
		if(newP != null && targetSpatial.getParent() != null) {
			targetSpatial.getParent().worldToLocal(newP, targetSpatial.getLocalTranslation());
			worldLocations.add(new WorldCursorRecord(targetSpatial.getWorldTranslation().clone(),  new Quaternion(targetSpatial.getWorldRotation()).clone(), targetSpatial.getWorldScale().clone(), System.nanoTime()));

			if (newP.x<0 || newP.y<0 || newP.x>DisplaySystem.getDisplaySystem().getWidth() || newP.y> DisplaySystem.getDisplaySystem().getHeight()){	
				for (ToolListener l: toolListeners)
					l.disposeTool(newP.x, newP.y);
			}
				
			//for (RotateTranslateScaleListener l: listeners)
			//l.itemMoved(this, targetSpatial, newP.x, newP.y, cursor1OldPos.x, cursor1OldPos.y);
				
			Vector3f cursorWorldStart = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(newP.x, newP.y), 0);
			Vector3f cursorWorldEnd = DisplaySystem.getDisplaySystem().getWorldCoordinates(new Vector2f(newP.x, newP.y), 1);					
			Vector3f direction = cursorWorldEnd.subtract(cursorWorldStart);
			camNode.lookAt(direction.mult(10), new Vector3f(0, 1,0));
					
			//picking object in scence						
			pickResults.clear();		
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
		

	/**
	 * Apply multi cursor transform.
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
			
			Quaternion tq = new Quaternion();
			tq.fromAngleAxis(-angleChange, AXIS_Z);
			tq.multLocal(targetSpatial.getLocalRotation());
			float[] axisR = new float[3];
			tq.toAngles(axisR);
			pickingSpatial.setLocalRotation(tq);

			float angle = pickingSpatial.getLocalRotation().toAngleAxis(Vector3f.UNIT_Z);
			if(pickingSpatial.getLocalRotation().z <0) angle = FastMath.TWO_PI - angle;
			
			if (angleChange>0 ){
				if (camNode.getCamera().getFrustumNear()<45)
					camNode.getCamera().setFrustumNear(camNode.getCamera().getFrustumNear()+0.1f);
			}
			else{
				if (camNode.getCamera().getFrustumNear()>1)
					camNode.getCamera().setFrustumNear(camNode.getCamera().getFrustumNear()-0.1f);
			}
			
		}
	}

	/**
	 * Update cursor1.
	 */
	protected void updateCursor1() {

		Vector2f rotatedPosition = this.screenToTable(getScreenCursorByIndex(0).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(0).getCurrentCursorScreenPosition().y);
		cursor1Pos.x = rotatedPosition.x;
		cursor1Pos.y = rotatedPosition.y;

		rotatedPosition = this.screenToTable(getScreenCursorByIndex(0).getOldCursorScreenPosition().x, getScreenCursorByIndex(0).getOldCursorScreenPosition().y);
		cursor1OldPos.x = rotatedPosition.x;
		cursor1OldPos.y = rotatedPosition.y;

	}

	/**
	 * Update cursor2.
	 */
	protected void updateCursor2() {

		Vector2f rotatedPosition = this.screenToTable(getScreenCursorByIndex(1).getCurrentCursorScreenPosition().x, getScreenCursorByIndex(1).getCurrentCursorScreenPosition().y);
		cursor2Pos.x = rotatedPosition.x;
		cursor2Pos.y = rotatedPosition.y;

		rotatedPosition = this.screenToTable(getScreenCursorByIndex(1).getOldCursorScreenPosition().x, getScreenCursorByIndex(1).getOldCursorScreenPosition().y);
		cursor2OldPos.x = rotatedPosition.x;
		cursor2OldPos.y = rotatedPosition.y;

	}

	/**
	 * Sets the scale limits.
	 *
	 * @param min the min
	 * @param max the max
	 */
	public void setScaleLimits(float min, float max) {
		minScale = min;
		maxScale = max;
	}

	/**
	 * Sets the rotate limits.
	 *
	 * @param min the min
	 * @param max the max
	 */
	public void setRotateLimits(float min, float max) {
//		minRotate = min;
//		maxRotate = max;
		//TODO: implement!
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
	 * Adds the rotate translate scale listener.
	 *
	 * @param l the l
	 */
	public void addRotateTranslateScaleListener(RotateTranslateScaleListener l){
		listeners.add(l);
	}

	/**
	 * Removes the rotate translate scale listener.
	 *
	 * @param l the l
	 */
	public void removeRotateTranslateScaleListener(RotateTranslateScaleListener l){
		if (listeners.contains(l))
			listeners.remove(l);
	}
	
	/**
	 * Adds the tool listener.
	 *
	 * @param l the l
	 */
	public void addToolListener(ToolListener l){
		toolListeners.add(l);
	}

	/**
	 * Removes the tool listener.
	 *
	 * @param l the l
	 */
	public void removeToolListener(ToolListener l){
		if (toolListeners.contains(l))
			toolListeners.remove(l);
	}

	/**
	 * The listener interface for receiving rotateTranslateScale events.
	 * The class that is interested in processing a rotateTranslateScale
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addRotateTranslateScaleListener<code> method. When
	 * the rotateTranslateScale event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see RotateTranslateScaleEvent
	 */
	public interface RotateTranslateScaleListener {
		
		/**
		 * Item rotated.
		 *
		 * @param multiTouchElement the multi touch element
		 * @param targetSpatial the target spatial
		 * @param newAngle the new angle
		 * @param oldAngle the old angle
		 */
		public void itemRotated(TelescopeFrameMoveZoom multiTouchElement, Spatial targetSpatial, float newAngle, float oldAngle);
		
		/**
		 * Item moved.
		 *
		 * @param multiTouchElement the multi touch element
		 * @param targetSpatial the target spatial
		 * @param newLocationX the new location x
		 * @param newLocationY the new location y
		 * @param oldLocationX the old location x
		 * @param oldLocationY the old location y
		 */
		public void itemMoved(TelescopeFrameMoveZoom multiTouchElement, Spatial targetSpatial,  float newLocationX, float newLocationY, float oldLocationX, float oldLocationY);
		
		/**
		 * Item scaled.
		 *
		 * @param multiTouchElement the multi touch element
		 * @param targetSpatial the target spatial
		 * @param scaleChange the scale change
		 */
		public void itemScaled(TelescopeFrameMoveZoom multiTouchElement, Spatial targetSpatial,  float scaleChange);
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
		Vector2f currentCenter = new Vector2f(targetSpatial.getParent().getWorldTranslation().x, targetSpatial.getParent().getWorldTranslation().y);
		Vector2f currentCenterToPoint = screenPosition.subtract(currentCenter);
		float newAngle = -(currentCenterToPoint.getAngle()-parentAngle);
		float length = currentCenterToPoint.length() / targetSpatial.getParent().getLocalScale().x;
		float newX = FastMath.cos(newAngle)*length;
		float newY = FastMath.sin(newAngle)*length;

		return new Vector2f(newX, newY);

	}

}
