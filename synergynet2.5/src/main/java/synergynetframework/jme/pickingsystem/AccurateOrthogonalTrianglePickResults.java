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

package synergynetframework.jme.pickingsystem;

import java.util.ArrayList;
import java.util.List;

import com.jme.intersection.PickData;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;

public class AccurateOrthogonalTrianglePickResults extends TrianglePickResults {
	
	private Vector2f mousePos;
	private List<PickedSpatial> pickedItems = new ArrayList<PickedSpatial>();

	public AccurateOrthogonalTrianglePickResults(Vector2f mousePos) {
		this.setCheckDistance(true);
		this.mousePos = mousePos;
	}

	@Override
	public void processPick() {		
		for (int i = 0; i < getNumber(); i++) {
			PickData pData = getPickData(i);
			PickedSpatial ps = null;
			if(pData.getTargetMesh() instanceof TriMesh){
				TriMesh mesh = (TriMesh) pData.getTargetMesh();
				ps = meshPicked(mesh);
			}
			else if(pData.getTargetMesh().getWorldBound().contains(new Vector3f(mousePos.x, mousePos.y, 0))) {
				ps = new PickedSpatial(pData.getTargetMesh(), new Vector3f(mousePos.x, mousePos.y, 0));
			}
			if(ps != null) {
				getPickedItems().add(ps);
			}
		}
	}

	private PickedSpatial meshPicked(TriMesh mesh) {
		PickedSpatial picked = null;
		for(int tc = 0; tc < mesh.getTriangleCount(); tc++) {
			Vector3f[] vertices = new Vector3f[3];
			mesh.getTriangle(tc, vertices);
			for(Vector3f v : vertices) {
				v.multLocal(mesh.getWorldScale());
				mesh.getWorldRotation().mult(v, v);
				v.addLocal(mesh.getWorldTranslation());
			}
			Vector3f rayOrigin = new Vector3f(mousePos.x, mousePos.y, -1);
			Ray r = new Ray(rayOrigin, new Vector3f(0, 0, 1));
			Vector3f pos = new Vector3f();
			if(r.intersectWhere(vertices[0], vertices[1], vertices[2], pos)) {
				picked = new PickedSpatial(mesh, pos);
			}
		}
		return picked;
	}

	public List<PickedSpatial> getPickedItems() {
		return pickedItems;
	}	
}
