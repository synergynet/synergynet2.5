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

package synergynetframework.jme.gfx;

import java.awt.geom.Point2D;

import com.jme.math.Plane;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.system.DisplaySystem;


/**
 * The Class JMEGfxUtils.
 */
public class JMEGfxUtils {
	
	/**
	 * Gets the cursor world coordinates on spatial plane.
	 *
	 * @param cursorScreen the cursor screen
	 * @param s the s
	 * @return the cursor world coordinates on spatial plane
	 */
	public static Vector3f getCursorWorldCoordinatesOnSpatialPlane(Vector2f cursorScreen, Spatial s) {
		Vector3f cameraLocation = DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation();
		Vector3f camToTargetSpatial = cameraLocation.subtract(s.getWorldTranslation());

		Plane p = new Plane();
		p.setNormal(camToTargetSpatial.normalize());
		p.setConstant(camToTargetSpatial.length());

		return getCursorWoldCoorinatesOnPlane(cursorScreen, s, p);
	}
	
	/**
	 * Gets the cursor wold coorinates on plane.
	 *
	 * @param cursorScreen the cursor screen
	 * @param s the s
	 * @param p the p
	 * @return the cursor wold coorinates on plane
	 */
	public static Vector3f getCursorWoldCoorinatesOnPlane(Vector2f cursorScreen, Spatial s, Plane p) {
		Vector3f cameraLocation = DisplaySystem.getDisplaySystem().getRenderer().getCamera().getLocation();
		Vector3f camToTargetSpatial = cameraLocation.subtract(s.getWorldTranslation());
		Vector3f cursorWorld = DisplaySystem.getDisplaySystem().getWorldCoordinates(cursorScreen, 1);

		Ray mouseRay = new Ray();
		mouseRay.setOrigin(cameraLocation);			

		Vector3f direction = cursorWorld.subtractLocal(cameraLocation);			
		direction.normalizeLocal();
		mouseRay.setDirection(direction);

		Vector3f locationOfIntersection = new Vector3f();
		if(mouseRay.intersectsWherePlane(p, locationOfIntersection)) {
			Vector3f directionToIntersection = locationOfIntersection.subtract(cameraLocation).normalize();
			Vector3f newPosition = directionToIntersection.mult(camToTargetSpatial.length()).add(cameraLocation);
			return newPosition;
		}else{
			return null;
		}		
	}


	/**
	 * Table to screen3f.
	 *
	 * @param position the position
	 * @return the vector2f
	 */
	public static Vector2f tableToScreen3f(Point2D.Float position) {
		Vector2f v2f = new Vector2f();
		v2f.x = position.x*DisplaySystem.getDisplaySystem().getWidth();
		v2f.y = position.y*DisplaySystem.getDisplaySystem().getHeight();
		return v2f;
	}

	/**
	 * Distance.
	 *
	 * @param x1 the x1
	 * @param x2 the x2
	 * @param y1 the y1
	 * @param y2 the y2
	 * @return the float
	 */
	public static float distance(float x1, float x2, float y1, float y2)
	{
		float dx = x1- x2;
		float dy = y1-y2;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}

	/**
	 * Gets an array of the three vertices representing the triangle of the
	 * supplied mesh with the given triangle index, in WORLD coordinates.
	 *
	 * @param m the m
	 * @param triangleIndex the triangle index
	 * @return the world vertices
	 */
	public static Vector3f[] getWorldVertices(TriMesh m, int triangleIndex) {
		Vector3f[] vertices = new Vector3f[3];
		m.getTriangle(triangleIndex, vertices);
		for(Vector3f v : vertices) {
			v.multLocal(m.getWorldScale());
			m.getWorldRotation().mult(v, v);
			v.addLocal(m.getWorldTranslation());
		}	
		return vertices;
	}
}
