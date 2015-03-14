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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;

import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

/**
 * Assumes Single Cursor only
 */
public class SingleTouchRotateTranslate extends OrthoControlPointRotateTranslateScale {
	List<Vector3f> vertices = new ArrayList<Vector3f>();
	private Vector3f oldSpatialPos;
	private boolean rotateMode = false;



	public SingleTouchRotateTranslate(TriMesh pickingAndTargetSpatial) {
		this(pickingAndTargetSpatial, pickingAndTargetSpatial);
	}

	public SingleTouchRotateTranslate(TriMesh pickingSpatial, Spatial targetSpatial) {
		super(pickingSpatial, targetSpatial);
		FloatBuffer fb = pickingSpatial.getVertexBuffer();		
		for(int i = 0; i < pickingSpatial.getVertexCount(); i++) {
			float[] vs = new float[3];
			fb.get(vs);
			Vector3f v = new Vector3f(vs[0], vs[1], vs[2]);
			vertices.add(v);
		}
	}

	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		if(event.getClickCount() == 2) {
			Quaternion flip = new Quaternion();
			flip = flip.fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));
			targetSpatial.getLocalRotation().multLocal(flip);

		}
	}

	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		cursor1OldPos.x = c.getCurrentCursorScreenPosition().x;
		cursor1OldPos.y = c.getCurrentCursorScreenPosition().y;
		oldSpatialPos = targetSpatial.getWorldTranslation().clone();
		Vector2f pos = c.getCurrentCursorScreenPosition().getPosition();
		Vector3f worldPos = new Vector3f(pos.x, pos.y, 0);
		if(isCloseToVertex(worldPos)) {
			rotateMode  = true;
		}
	}

	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		rotateMode = false;
	}

	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {		
		updateCursor1();
		float oldAngle = cursor1OldPos.subtract(new Vector2f(oldSpatialPos.x, oldSpatialPos.y)).getAngle();
		float currentAngle = cursor1Pos.subtract(new Vector2f(targetSpatial.getWorldTranslation().x, targetSpatial.getWorldTranslation().y)).getAngle();
		float angleChange = oldAngle - currentAngle;

		if(rotateMode) {
			Quaternion tq = new Quaternion();
			tq.fromAngleAxis(angleChange, AXIS_Z);
			tq.multLocal(targetSpatial.getLocalRotation());
			targetSpatial.setLocalRotation(tq);
		}else{
			applySingleCursorTransform();
		}

		oldSpatialPos = targetSpatial.getWorldTranslation();
		setOldCursor();
	}

	public boolean isCloseToVertex(Vector3f point) {
		for(Vector3f t : vertices) {
			Vector3f v = new Vector3f(t);
			v.multLocal(pickingSpatial.getWorldScale());
			pickingSpatial.getWorldRotation().mult(v, v);
			v.addLocal(pickingSpatial.getWorldTranslation());
			if(point.subtract(v).length() < 30) return true;
		}
		return false;
	}
}
