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

package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.util.UUID;

import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry.ColourRGBA;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry.FloatPoint;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry.FloatVector;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry.TriangleIndexes;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

public class ShapeTriMesh extends TriMesh {
	private static final long serialVersionUID = 5434466779604169019L;

	public ShapeTriMesh() {
		super();
		this.setName(UUID.randomUUID().toString());
	}
	
	public void updateGeometry(TwoDShapeGeometry geom) {		
    	Vector3f[] tri_vertexes = convertVectors(geom.getTriVertexes()); 		
		Vector3f[] normals = convertVectors(geom.getNormals());		
		ColorRGBA[] colors = convertColours(geom.getColors()); 
		Vector2f[] texCoords = convertTextures(geom.getTexCoords()); 		
		int[] tri_indexes = convertTriangleIndexes(geom.getTriIndexes());
		
		reconstruct(
				BufferUtils.createFloatBuffer(tri_vertexes), 
				BufferUtils.createFloatBuffer(normals), 
				BufferUtils.createFloatBuffer(colors), 
				new TexCoords(BufferUtils.createFloatBuffer(texCoords)), 
				BufferUtils.createIntBuffer(tri_indexes));
    }

	private int[] convertTriangleIndexes(TriangleIndexes[] triIndexes) {
		int[] idxs = new int[3 * triIndexes.length];
		int j = 0;
		for(int i = 0; i < triIndexes.length; i++) {
			idxs[j] = triIndexes[i].i;
			idxs[j+1] = triIndexes[i].j;
			idxs[j+2] = triIndexes[i].k;
			j+=3;
		}
		return idxs;
	}

	private Vector2f[] convertTextures(FloatPoint[] texCoords) {
		Vector2f[] t = new Vector2f[texCoords.length];
		for(int i = 0; i < texCoords.length; i++) {
			t[i] = new Vector2f(texCoords[i].x, texCoords[i].y);
		}
		return t;
	}

	private ColorRGBA[] convertColours(ColourRGBA[] colors) {
		ColorRGBA[] c = new ColorRGBA[colors.length];
		for(int i = 0; i < colors.length; i++) {
			c[i] = new ColorRGBA(colors[i].r, colors[i].g, colors[i].b, colors[i].a);
		}
		return c;
	}

	private Vector3f[] convertVectors(FloatVector[] triVertexes) {
		Vector3f[] v = new Vector3f[triVertexes.length];
		for(int i = 0; i < triVertexes.length; i++) {
			v[i] = new Vector3f(triVertexes[i].x, triVertexes[i].y, triVertexes[i].z);
		}
		return v;
	}
}