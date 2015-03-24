/*
 * Copyright (c) 2008 University of Durham, England
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

import com.jme.math.Plane;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;
import com.jmex.physics.PhysicsNode;

import synergynetframework.jme.cursorsystem.ThreeDMultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.jme.gfx.JMEGfxUtils;
import synergynetframework.jme.pickingsystem.data.ThreeDPickResultData;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;



/**
 * The Class MultiTouchMoveableXYPlaneWithPhysics.
 */
public class MultiTouchMoveableXYPlaneWithPhysics extends ThreeDMultiTouchElement {
	
	/** The physics node. */
	private PhysicsNode physicsNode;

	/**
	 * Instantiates a new multi touch moveable xy plane with physics.
	 *
	 * @param s the s
	 * @param physicsNode the physics node
	 */
	public MultiTouchMoveableXYPlaneWithPhysics(Spatial s, PhysicsNode physicsNode) {
		super(s);
		this.physicsNode = physicsNode;
	}
	
	/**
	 * Instantiates a new multi touch moveable xy plane with physics.
	 *
	 * @param pickSpatial the pick spatial
	 * @param targetSpatial the target spatial
	 * @param physicsNode the physics node
	 */
	public MultiTouchMoveableXYPlaneWithPhysics(Spatial pickSpatial, Spatial targetSpatial, PhysicsNode physicsNode) {
		super(pickSpatial, targetSpatial);
		this.physicsNode = physicsNode;
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
		physicsNode.setActive(false);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {		
		physicsNode.setActive(true);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(synergynetframework.jme.cursorsystem.cursordata.ScreenCursor, synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {		
		Vector2f screenPos;
		
		if(screenCursors.size() <= 1) {
			 screenPos = c.getCurrentCursorScreenPosition().getPosition();
		}else{
			float avgx = 0f;
			float avgy = 0f;
			for(ScreenCursor t : screenCursors) {
				avgx += t.getCurrentCursorScreenPosition().x;
				avgy += t.getCurrentCursorScreenPosition().y;
			}
			avgx /= screenCursors.size();
			avgy /= screenCursors.size();
			screenPos = new Vector2f(avgx, avgy);
		}			
		
		Vector3f cameraLocation = DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation();
		Vector3f camToTargetSpatial = cameraLocation.subtract(targetSpatial.getWorldTranslation());

		Plane p = new Plane();
		p.setNormal(camToTargetSpatial.normalize());
		p.setConstant(camToTargetSpatial.length());				
		
		Ray mouseRay = new Ray();
		mouseRay.setOrigin(cameraLocation);					
		
		long eventCursorID = event.getCursorID();
		ThreeDPickResultData pickResultData = getPickDataForCursorID(eventCursorID);
		Vector2f screenOffset = pickResultData.getCursorToSpatialScreenOffset();
		Vector2f cursorPosition = screenPos.subtract(screenOffset);
		
		Vector3f newPosition = JMEGfxUtils.getCursorWorldCoordinatesOnSpatialPlane(cursorPosition, targetSpatial);
		
		if(newPosition != null) {
			targetSpatial.getParent().worldToLocal(newPosition, targetSpatial.getLocalTranslation());
		}
	}


	
}
