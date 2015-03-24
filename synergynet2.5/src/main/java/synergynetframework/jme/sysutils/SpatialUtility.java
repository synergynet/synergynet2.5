/*
 * Copyright (c) 2012 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package synergynetframework.jme.sysutils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import com.jme.math.Triangle;
import com.jme.math.Vector3f;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;

/**
 * The Class SpatialUtility.
 */
public class SpatialUtility {
	
	/** The edge collection. */
	private static HashMap<Spatial, ArrayList<Vector3f[]>> edgeCollection = new HashMap<Spatial, ArrayList<Vector3f[]>>();

	/** The relevant coords collection. */
	private static HashMap<Spatial, ArrayList<Integer>> relevantCoordsCollection = new HashMap<Spatial, ArrayList<Integer>>();

	/**
	 * Contains same edge.
	 *
	 * @param vec
	 *            the vec
	 * @param edges
	 *            the edges
	 * @return the array list
	 */
	private static ArrayList<Vector3f[]> containsSameEdge(Vector3f[] vec,
			ArrayList<Vector3f[]> edges) {
		
		boolean result = false;

		ArrayList<Vector3f[]> newEdges = new ArrayList<Vector3f[]>();

		for (int i = 0; i < edges.size(); i++) {
			if ((vec[0].equals(edges.get(i)[0]) && vec[1]
					.equals(edges.get(i)[1]))
					|| (vec[0].equals(edges.get(i)[1]) && vec[1].equals(edges
							.get(i)[0]))) {
				result = true;
			} else {
				newEdges.add(edges.get(i));
			}
		}
		
		if (!result) {
			newEdges = null;
		}

		return newEdges;
	}
	
	/**
	 * Find edges.
	 *
	 * @param s
	 *            the s
	 * @param removeContainer
	 *            the remove container
	 * @return the array list
	 */
	public static ArrayList<Vector3f[]> findEdges(Spatial s,
			boolean removeContainer) {
		
		if (edgeCollection.containsKey(s)) {
			return edgeCollection.get(s);
		}

		ArrayList<Vector3f[]> edges = new ArrayList<Vector3f[]>();
		
		TriMesh geom = (TriMesh) s;

		geom.updateWorldBound();
		geom.updateModelBound();
		
		Triangle[] tris = geom.getMeshAsTriangles(null);
		
		for (Triangle t : tris) {
			
			ArrayList<Vector3f> containerVectors = new ArrayList<Vector3f>();
			
			if (removeContainer) {
				containerVectors.add(new Vector3f(-522, -394, 0));
				containerVectors.add(new Vector3f(-522, 394, 0));
				containerVectors.add(new Vector3f(522, -394, 0));
				containerVectors.add(new Vector3f(522, 394, 0));
			}
			
			Vector3f[] vecOne = { t.get(0), t.get(1) };
			ArrayList<Vector3f[]> aResult = containsSameEdge(vecOne, edges);
			
			if ((aResult == null) && !containerVectors.contains(t.get(0))
					&& !containerVectors.contains(t.get(1))) {
				edges.add(vecOne);
			} else if (aResult != null) {
				edges = aResult;
			}
			
			Vector3f[] vecTwo = { t.get(1), t.get(2) };
			aResult = containsSameEdge(vecTwo, edges);
			if ((aResult == null) && !containerVectors.contains(t.get(1))
					&& !containerVectors.contains(t.get(2))) {
				edges.add(vecTwo);
			} else if (aResult != null) {
				edges = aResult;
			}
			
			Vector3f[] vecThree = { t.get(2), t.get(0) };
			aResult = containsSameEdge(vecThree, edges);
			if ((aResult == null) && !containerVectors.contains(t.get(2))
					&& !containerVectors.contains(t.get(0))) {
				edges.add(vecThree);
			} else if (aResult != null) {
				edges = aResult;
			}
		}
		
		edgeCollection.put(s, edges);
		
		return edges;
		
	}

	/**
	 * Generate relevant coords.
	 *
	 * @param s
	 *            the s
	 * @param coordsIndex
	 *            the coords index
	 * @return the array list
	 */
	public static ArrayList<Vector3f> generateRelevantCoords(Spatial s,
			ArrayList<Integer> coordsIndex) {

		TriMesh geom = (TriMesh) s;
		
		FloatBuffer coordBufferTwo = geom.getWorldCoords(null);
		
		ArrayList<Vector3f> usefulCoords = new ArrayList<Vector3f>();
		
		for (int i = 0; i < coordBufferTwo.limit(); i += 3) {
			usefulCoords.add(new Vector3f(coordBufferTwo.get(i), coordBufferTwo
					.get(i + 1), 0));
		}
		
		ArrayList<Vector3f> coordsTwo = new ArrayList<Vector3f>();
		
		for (int i = 0; i < coordsIndex.size(); i++) {
			coordsTwo.add(usefulCoords.get(coordsIndex.get(i)));
		}
		
		return coordsTwo;
	}

	/**
	 * Gets the index of relevant coords.
	 *
	 * @param s
	 *            the s
	 * @return the index of relevant coords
	 */
	public static ArrayList<Integer> getIndexOfRelevantCoords(Spatial s) {
		
		if (relevantCoordsCollection.containsKey(s)) {
			return relevantCoordsCollection.get(s);
		}

		ArrayList<Vector3f[]> edges = findEdges(s, false);

		ArrayList<Integer> coordsIndex = new ArrayList<Integer>();
		TriMesh geom = (TriMesh) s;
		ArrayList<Vector3f> coordsOfInterest = new ArrayList<Vector3f>();
		for (int i = 0; i < edges.size(); i++) {
			if (!coordsOfInterest.contains(edges.get(i)[0])) {
				coordsOfInterest.add(edges.get(i)[0]);
			}
			if (!coordsOfInterest.contains(edges.get(i)[1])) {
				coordsOfInterest.add(edges.get(i)[1]);
			}
		}
		FloatBuffer coordBufferTwo = geom.getVertexBuffer();
		
		ArrayList<Vector3f> usefulCoords = new ArrayList<Vector3f>();
		
		for (int i = 0; i < coordBufferTwo.limit(); i += 3) {
			usefulCoords.add(new Vector3f(coordBufferTwo.get(i), coordBufferTwo
					.get(i + 1), 0));
		}
		
		for (int i = 0; i < coordsOfInterest.size(); i++) {
			coordsIndex.add(usefulCoords.indexOf(coordsOfInterest.get(i)));
		}
		
		relevantCoordsCollection.put(s, coordsIndex);
		
		return coordsIndex;
	}
	
	/**
	 * Gets the max dimension.
	 *
	 * @param targetSpatial
	 *            the target spatial
	 * @return the max dimension
	 */
	public static float getMaxDimension(Spatial targetSpatial) {
		float result = -1;
		if (!(targetSpatial instanceof TriMesh)) {
			return result;
		}
		TriMesh geom = (TriMesh) targetSpatial;
		
		FloatBuffer coordBuffer = geom.getWorldCoords(null);
		ArrayList<Vector3f> coords = new ArrayList<Vector3f>();
		
		for (int i = 0; i < coordBuffer.limit(); i += 3) {
			coords.add(new Vector3f(coordBuffer.get(i), coordBuffer.get(i + 1),
					0));
		}
		
		for (int i = 0; i < coords.size(); i++) {
			for (int j = i + 1; j < coords.size(); j++) {
				float newDistance = coords.get(i).distance(coords.get(j));
				if (result == -1) {
					result = newDistance;
				} else {
					if (newDistance < result) {
						result = newDistance;
					}
				}
			}
		}
		return result;
	}
	
}
