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

package apps.remotecontrol.tableportal;


import synergynetframework.jme.cursorsystem.elements.twod.ClipRegion;

import com.jme.bounding.OrthogonalBoundingBox;
import com.jme.math.FastMath;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.scene.state.ClipState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.BufferUtils;

public class ClipRectangleHud implements ClipRegion{
	private Spatial spat;
    private ClipState cs;

    private float wi;
    private float he;
	Vector3f p1 = new Vector3f();
	Vector3f p2 = new Vector3f();
	Vector3f p3 = new Vector3f();
	Vector3f p4 = new Vector3f();
    
    public ClipRectangleHud(Spatial spat, float wi, float he) {
    	this.spat = spat;
    	this.wi = wi;
    	this.he = he;
        cs = DisplaySystem.getDisplaySystem().getRenderer().createClipState();

        updateEquations();
        for (int i = 0; i < 4; i++) {
            cs.setEnableClipPlane(i, true);
        }
    }

	public void setSpatialClip(Spatial spatial, boolean isClipEnabled) {
		if(isClipEnabled)
			spatial.setRenderState(cs);
		else	
			spatial.clearRenderState(cs.getStateType());
		spatial.updateRenderState();
	}
	
    public void updateEquations() {
        spat.updateWorldVectors();
        float x0 = spat.getWorldTranslation().x - wi / 2;
        float x1 = spat.getWorldTranslation().x + wi / 2;
        float y0 = spat.getWorldTranslation().y - he / 2;
        float y1 = spat.getWorldTranslation().y + he / 2;
	
		


		spat.localToWorld(new Vector3f(wi/2,he/2,0), p1);
		spat.localToWorld(new Vector3f(wi/2,-he/2,0), p2);
		spat.localToWorld(new Vector3f(-wi/2,-he/2,0), p3);
		spat.localToWorld(new Vector3f(-wi/2,he/2,0), p4);

		float slope, b;
		
		// line 1
		if(p3.x - p4.x == 0){
			// vertical line
			slope = 1;
	        cs.setClipPlaneEquation(0,slope,0f,0,-x0);
		}else{	
			slope = (p3.y-p4.y)/(p3.x-p4.x);
			b = p4.y - slope*p4.x;
			cs.setClipPlaneEquation(0,slope,-1,0, b);
			
			if(spat.getWorldRotation().w <0)
				cs.setClipPlaneEquation(0,slope,-1,0, b);
			else
				cs.setClipPlaneEquation(0,-slope,1,0, -b);
		}
		
		// line 2
		if(p2.x - p1.x == 0){
			// vertical line
			slope = 1;
	        	
			cs.setClipPlaneEquation(1,-slope,0f,0,x1);
		}else{	
			slope = (p2.y-p1.y)/(p2.x-p1.x);
			b = p1.y - slope*p1.x;
			if(spat.getWorldRotation().w <0)	
				cs.setClipPlaneEquation(1,-slope,1,0, -b);
			else
				cs.setClipPlaneEquation(1,slope,-1,0, b);
		}
		
		
		// line 3
		if(p1.x - p4.x == 0){
			// horizontal line
			slope = 1;
	        cs.setClipPlaneEquation(2,slope,0f,0,-y0);
		}else{	
			slope = (p1.y-p4.y)/(p1.x-p4.x);
			b = p4.y - slope*p4.x;

			float angle = FastMath.RAD_TO_DEG * spat.getWorldRotation().toAngleAxis(Vector3f.UNIT_Z);
			if(angle>=90 && angle <=270)
				cs.setClipPlaneEquation(2,-slope,1,0, -b);
			else
				cs.setClipPlaneEquation(2,slope,-1,0, b);
		}
		
		
		// line 4
		if(p2.x - p3.x == 0){
			// horizontal line
			slope = 1;
	        cs.setClipPlaneEquation(3,-slope,0f,0,y1);
		}else{	
			slope = (p2.y-p3.y)/(p2.x-p3.x);
			b = p3.y - slope*p3.x;

			float angle = FastMath.RAD_TO_DEG * spat.getWorldRotation().toAngleAxis(Vector3f.UNIT_Z);
			if(angle>=90 && angle <=270)
				cs.setClipPlaneEquation(3,slope,-1,0, b);
			else
				cs.setClipPlaneEquation(3,-slope,1,0, -b);
		}		
    }

	@Override
	public boolean isPicked(Vector2f screenPos) {
		TriMesh m = new TriMesh("mymesh");
		
		Vector3f[] vertexes = {p1,p2,p3,p4};
		Vector3f[] normals = {new Vector3f(0, 0, 1),new Vector3f(0, 0, 1),new Vector3f(0, 0, 1),new Vector3f(0, 0, 1)
			};
		int[] indexes = { 0, 1, 2, 2, 3, 0 };
			 
		m.reconstruct(BufferUtils.createFloatBuffer(vertexes), BufferUtils.createFloatBuffer(normals), null, null, BufferUtils.createIntBuffer(indexes));
		m.setModelBound(new OrthogonalBoundingBox());
		m.updateModelBound();
		return meshPicked(m, screenPos.x, screenPos.y);	}

    
	private boolean meshPicked(TriMesh mesh, float x, float y) {
		for(int tc = 0; tc < mesh.getTriangleCount(); tc++) {
			Vector3f[] vertices = new Vector3f[3];
			mesh.getTriangle(tc, vertices);
			for(Vector3f v : vertices) {
				v.multLocal(mesh.getWorldScale());
				mesh.getWorldRotation().mult(v, v);
				v.addLocal(mesh.getWorldTranslation());
			}
			Vector3f rayOrigin = new Vector3f(x, y, -1);
			Ray r = new Ray(rayOrigin, new Vector3f(0, 0, 1));
			Vector3f pos = new Vector3f();
			if(r.intersectWhere(vertices[0], vertices[1], vertices[2], pos)) {
				return true;
			}
		}
		return false;
	}


}

