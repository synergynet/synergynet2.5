/*
 * Modified Grid from cigame project,
 * http://www.captiveimagination.com/snv/public/cigame Original copyright
 * notice: Copyright (c) 2007 shingoki All rights reserved. Redistribution and
 * use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met: * Redistributions of source
 * code must retain the above copyright notice, this list of conditions and the
 * following disclaimer. * Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution. *
 * Neither the name of 'shingoki' nor the names of its contributors may be used
 * to endorse or promote products derived from this software without specific
 * prior written permission. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS
 * AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package synergynetframework.jme.gfx.twod;

import java.nio.FloatBuffer;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

/**
 * <code>Grid</code> defines a four sided, two dimensional rectangular shape.
 * The local height of the <code>Quad</code> defines it's size about the y-axis,
 * while the width defines the x-axis. The z-axis will always be 0. The plane is
 * subdivided into one or more sections along each axis, to form a grid of
 * smaller quads.
 *
 * @author shingoki
 * @version $Id: Grid.java 675 2007-03-31 23:33:25Z dougnukem $
 */
public class Grid extends TriMesh {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The x quads. */
	protected int xQuads;

	/** The y quads. */
	int yQuads;
	
	/**
	 * Constructor creates a new <code>Quade</code> object with the provided
	 * width and height.
	 *
	 * @param name
	 *            The name of the <code>Quad</code>.
	 * @param width
	 *            The width of the <code>Quad</code>.
	 * @param height
	 *            The height of the <code>Quad</code>.
	 * @param xQuads
	 *            The number of quads along the x axis
	 * @param yQuads
	 *            The number of quads along the y axis
	 * @param centered
	 *            If true, grid is centered on 0,0,0, otherwise the grid is from
	 *            0, 0, 0 to width, height, 0
	 */
	public Grid(String name, float width, float height, int xQuads, int yQuads,
			boolean centered) {
		super(name);
		if ((xQuads < 1) || (yQuads < 1)) {
			throw new IllegalArgumentException(
					"xQuads and yQuads must be greater than 0.");
		}
		initialize(width, height, xQuads, yQuads, centered);
	}

	/**
	 * <code>initialize</code> builds the data for the <code>Grid</code> object.
	 *
	 * @param width
	 *            the width of the <code>Quad</code>.
	 * @param height
	 *            the height of the <code>Quad</code>.
	 * @param xQuads
	 *            The number of quads along the x axis
	 * @param yQuads
	 *            The number of quads along the y axis
	 * @param centered
	 *            If true, grid is centered on 0,0,0, otherwise the grid is from
	 *            0, 0, 0 to width, height, 0
	 */
	public void initialize(float width, float height, int xQuads, int yQuads,
			boolean centered) {

		this.xQuads = xQuads;
		this.yQuads = yQuads;
		
		// We have one more vertex than the number of quads, along each axis
		setVertexCount((xQuads + 1) * (yQuads + 1));

		// We need enough vertices in the buffer
		setVertexBuffer(BufferUtils.createVector3Buffer(getVertexCount()));

		// One normal per vertex
		setNormalBuffer(BufferUtils.createVector3Buffer(getVertexCount()));
		
		// One UV position per vertex
		FloatBuffer tbuf = BufferUtils.createVector2Buffer(getVertexCount());
		
		setTextureCoords(new TexCoords(tbuf));
		
		// setTextureBuffer(0,tbuf);
		
		// Two triangles per quad
		setTriangleQuantity(2 * xQuads * yQuads);
		
		// 3 indices per triangle
		setIndexBuffer(BufferUtils.createIntBuffer(getTriangleCount() * 3));
		
		// Set vertex positions, in reading order
		float offset = centered ? -0.5f : 0;
		for (int x = 0; x < (xQuads + 1); x++) {
			for (int y = 0; y < (yQuads + 1); y++) {
				getVertexBuffer()
						.put(width
								* (offset + (((float) x) / ((float) xQuads))))
						.put(height
								* (offset + (((float) y) / ((float) yQuads))))
						.put(0);
			}
		}
		
		// All normals face along z axis
		for (int i = 0; i < getVertexCount(); i++) {
			getNormalBuffer().put(0).put(0).put(1);
		}
		
		// Textures are set evenly from 0 to 1 on each axis, vertices are in
		// reading order,
		// so texture coords are the same
		for (int x = 0; x < (xQuads + 1); x++) {
			for (int y = 0; y < (yQuads + 1); y++) {
				tbuf.put(((float) x) / ((float) xQuads)).put(
						((float) y) / ((float) yQuads));
			}
		}
		
		setDefaultColor(ColorRGBA.white);
		
		// Set up triangles. x and y are the top left of the quad we are
		// building,
		// moving through all verts that are not on the right column or bottom
		// row
		// We put a pair of tris:
		// __
		// |\ \ |
		// |_\ \|
		for (int x = 0; x < xQuads; x++) {
			for (int y = 0; y < yQuads; y++) {
				int topLeft = x + (y * (xQuads + 1));
				
				// first triangle
				getIndexBuffer().put(topLeft); // top left
				getIndexBuffer().put(topLeft + (xQuads + 1)); // down a line of
																// verts
				getIndexBuffer().put(topLeft + (xQuads + 1) + 1); // down a
																	// line, and
																	// one to
																	// the right
				
				// second triangle
				getIndexBuffer().put(topLeft); // top left
				getIndexBuffer().put(topLeft + (xQuads + 1) + 1); // down a
																	// line, and
																	// one to
																	// the right
				getIndexBuffer().put(topLeft + 1); // one to the right
			}
		}
	}
	
	/**
	 * Move a point within the grid.
	 *
	 * @param xIndex
	 *            The x index of the point, from 0 to xQuads
	 * @param yIndex
	 *            The y index of the point, from 0 to yQuads
	 * @param x
	 *            The new x coordinate of the point
	 * @param y
	 *            The new y coordinate of the point
	 */
	public void movePoint(int xIndex, int yIndex, float x, float y) {
		getVertexBuffer().position((xIndex + (yIndex * (xQuads + 1))) * 3);
		getVertexBuffer().put(x).put(y).put(0);
	}

	/**
	 * Move the texture coord of a point within the grid.
	 *
	 * @param xIndex
	 *            The x index of the point, from 0 to xQuads
	 * @param yIndex
	 *            The y index of the point, from 0 to yQuads
	 * @param x
	 *            The new texture x coordinate of the point
	 * @param y
	 *            The new texture y coordinate of the point
	 */
	public void moveUV(int xIndex, int yIndex, float x, float y) {
		// todo: fix moveUV
		// TriangleBatch batch = getBatch(0);
		//
		//
		// batch.getTextureBuffer(0).position((xIndex + yIndex * (xQuads + 1)) *
		// 2);
		// batch.getTextureBuffer(0).put(x).put(y);
	}
	
}
