/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
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

package synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents 2D shape geometry.
 *
 * @author dcs0ah1
 */
public class TwoDShapeGeometry implements Serializable {

	/**
	 * Components should be in the range 0..1
	 * 
	 * @author dcs0ah1
	 */
	public class ColourRGBA implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -4847774910317112065L;

		/** The a. */
		public float a;

		/** The b. */
		public float b;

		/** The g. */
		public float g;

		/** The r. */
		public float r;
		
		/**
		 * Instantiates a new colour rgba.
		 *
		 * @param r
		 *            the r
		 * @param g
		 *            the g
		 * @param b
		 *            the b
		 * @param a
		 *            the a
		 */
		public ColourRGBA(float r, float g, float b, float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
	}

	/**
	 * The Class FloatPoint.
	 */
	public class FloatPoint implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -9082924342409953363L;

		/** The x. */
		public float x;

		/** The y. */
		public float y;
		
		/**
		 * Instantiates a new float point.
		 *
		 * @param x
		 *            the x
		 * @param y
		 *            the y
		 */
		public FloatPoint(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * The Class FloatVector.
	 */
	public class FloatVector implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -6490963988264624303L;

		/** The x. */
		public float x;

		/** The y. */
		public float y;

		/** The z. */
		public float z;
		
		/**
		 * Instantiates a new float vector.
		 *
		 * @param x
		 *            the x
		 * @param y
		 *            the y
		 * @param z
		 *            the z
		 */
		public FloatVector(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	/**
	 * The Class TriangleIndexes.
	 */
	public class TriangleIndexes implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 729763312666906813L;

		/** The i. */
		public int i;

		/** The j. */
		public int j;

		/** The k. */
		public int k;
		
		/**
		 * Instantiates a new triangle indexes.
		 *
		 * @param i
		 *            the i
		 * @param j
		 *            the j
		 * @param k
		 *            the k
		 */
		public TriangleIndexes(int i, int j, int k) {
			this.i = i;
			this.j = j;
			this.k = k;
		}
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4060374501339794263L;

	/**
	 * Parses the colour line.
	 *
	 * @param geom
	 *            the geom
	 * @param line
	 *            the line
	 * @return the colour rgba
	 */
	private static ColourRGBA parseColourLine(TwoDShapeGeometry geom,
			String line) {
		String[] parts = line.split(" ");
		ColourRGBA c = geom.new ColourRGBA(Float.parseFloat(parts[1]),
				Float.parseFloat(parts[2]), Float.parseFloat(parts[3]),
				Float.parseFloat(parts[4]));
		return c;
	}
	
	/**
	 * Parses the float point line.
	 *
	 * @param geom
	 *            the geom
	 * @param line
	 *            the line
	 * @return the float point
	 */
	private static FloatPoint parseFloatPointLine(TwoDShapeGeometry geom,
			String line) {
		String[] parts = line.split(" ");
		FloatPoint uv = geom.new FloatPoint(Float.parseFloat(parts[1]),
				Float.parseFloat(parts[2]));
		return uv;
	}
	
	/**
	 * Parses the float vector line.
	 *
	 * @param geom
	 *            the geom
	 * @param line
	 *            the line
	 * @return the float vector
	 */
	private static FloatVector parseFloatVectorLine(TwoDShapeGeometry geom,
			String line) {
		String[] parts = line.split(" ");
		FloatVector fv = geom.new FloatVector(Float.parseFloat(parts[1]),
				Float.parseFloat(parts[2]), Float.parseFloat(parts[3]));
		return fv;
	}
	
	/**
	 * Parses the index line.
	 *
	 * @param geom
	 *            the geom
	 * @param line
	 *            the line
	 * @return the triangle indexes
	 */
	private static TriangleIndexes parseIndexLine(TwoDShapeGeometry geom,
			String line) {
		String[] parts = line.split(" ");
		TriangleIndexes index = geom.new TriangleIndexes(
				Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),
				Integer.parseInt(parts[3]));
		return index;
	}
	
	/**
	 * Read.
	 *
	 * @param is
	 *            the is
	 * @return the two d shape geometry
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static TwoDShapeGeometry read(InputStream is) throws IOException {
		TwoDShapeGeometry geom = new TwoDShapeGeometry();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		List<FloatVector> vertices = new ArrayList<FloatVector>();
		List<FloatVector> normals = new ArrayList<FloatVector>();
		List<ColourRGBA> colours = new ArrayList<ColourRGBA>();
		List<FloatPoint> uvs = new ArrayList<FloatPoint>();
		List<TriangleIndexes> indexes = new ArrayList<TriangleIndexes>();
		while ((line = br.readLine()) != null) {
			if (line.startsWith("#")) {
				// ignore
			} else if (line.startsWith("v")) {
				FloatVector fv = parseFloatVectorLine(geom, line);
				vertices.add(fv);
			} else if (line.startsWith("n")) {
				FloatVector fv = parseFloatVectorLine(geom, line);
				normals.add(fv);
			} else if (line.startsWith("c")) {
				ColourRGBA c = parseColourLine(geom, line);
				colours.add(c);
			} else if (line.startsWith("u")) {
				FloatPoint uv = parseFloatPointLine(geom, line);
				uvs.add(uv);
			} else if (line.startsWith("i")) {
				TriangleIndexes triIndex = parseIndexLine(geom, line);
				indexes.add(triIndex);
			}
		}
		
		FloatVector[] verticesArray = vertices.toArray(new FloatVector[0]);
		FloatVector[] normalsArray = normals.toArray(new FloatVector[0]);
		ColourRGBA[] colourArray = colours.toArray(new ColourRGBA[0]);
		FloatPoint[] uvArray = uvs.toArray(new FloatPoint[0]);
		TriangleIndexes[] indexArray = indexes.toArray(new TriangleIndexes[0]);
		
		geom.setTriVertexes(verticesArray);
		geom.setNormals(normalsArray);
		geom.setColors(colourArray);
		geom.setTexCoords(uvArray);
		geom.setTriIndexes(indexArray);
		
		return geom;
	}
	
	/** The colors. */
	private ColourRGBA[] colors;
	
	/** The normals. */
	private FloatVector[] normals;
	
	/** The tex coords. */
	private FloatPoint[] texCoords;
	
	/** The tri indexes. */
	private TriangleIndexes[] triIndexes;
	
	/** The tri vertexes. */
	private FloatVector[] triVertexes;
	
	/**
	 * Gets the colors.
	 *
	 * @return the colors
	 */
	public ColourRGBA[] getColors() {
		return colors;
	}
	
	/**
	 * Gets the normals.
	 *
	 * @return the normals
	 */
	public FloatVector[] getNormals() {
		return normals;
	}
	
	/**
	 * Gets the tex coords.
	 *
	 * @return the tex coords
	 */
	public FloatPoint[] getTexCoords() {
		return texCoords;
	}
	
	/**
	 * Gets the tri indexes.
	 *
	 * @return the tri indexes
	 */
	public TriangleIndexes[] getTriIndexes() {
		return triIndexes;
	}
	
	/**
	 * Gets the tri vertexes.
	 *
	 * @return the tri vertexes
	 */
	public FloatVector[] getTriVertexes() {
		return triVertexes;
	}
	
	/**
	 * Sets the colors.
	 *
	 * @param colors
	 *            the new colors
	 */
	public void setColors(ColourRGBA[] colors) {
		this.colors = colors;
	}
	
	/**
	 * Sets the normals.
	 *
	 * @param normals
	 *            the new normals
	 */
	public void setNormals(FloatVector[] normals) {
		this.normals = normals;
	}
	
	/**
	 * Sets the tex coords.
	 *
	 * @param texCoords
	 *            the new tex coords
	 */
	public void setTexCoords(FloatPoint[] texCoords) {
		this.texCoords = texCoords;
	}
	
	/**
	 * Sets the tri indexes.
	 *
	 * @param ti
	 *            the new tri indexes
	 */
	public void setTriIndexes(TriangleIndexes[] ti) {
		this.triIndexes = ti;
	}
	
	/**
	 * Sets the tri vertexes.
	 *
	 * @param triVertexes
	 *            the new tri vertexes
	 */
	public void setTriVertexes(FloatVector[] triVertexes) {
		this.triVertexes = triVertexes;
	}
}
