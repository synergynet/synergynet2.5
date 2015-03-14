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

package synergynetframework.jme.pickingsystem;

import java.util.List;

import com.jme.intersection.PickData;
import com.jme.intersection.TrianglePickResults;
import com.jme.math.Vector3f;
import com.jme.scene.TriMesh;

public class PointSelectionTrianglePickResults extends TrianglePickResults {

	protected Vector3f pointOfSelection;

	public PointSelectionTrianglePickResults() {

	}
	
	public void processPick() {
		if (getNumber() > 0) {
			for (int num = 0; num < getNumber(); num++) {
				PickData pData = getPickData(num);								
				List<Integer> tris = pData.getTargetTris();
				TriMesh mesh = (TriMesh) pData.getTargetMesh();
				for (int i = 0; i < tris.size(); i++) {
					int triIndex = tris.get( i );
					Vector3f[] vec = new Vector3f[3];
					mesh.getTriangle(triIndex, vec);
					for ( Vector3f v : vec ) {
						v.multLocal( mesh.getWorldScale() );
						mesh.getWorldRotation().mult(v, v );
						v.addLocal( mesh.getWorldTranslation() );
					}

					if (num == 0 && i == 0) {
						Vector3f loc = new Vector3f();
						pData.getRay().intersectWhere(vec[0], vec[1], vec[2], loc);
						pointOfSelection = loc;
					}
				}
			}
		}
	}

	public Vector3f getPointOfSelection() {
		return pointOfSelection;
	}
}
