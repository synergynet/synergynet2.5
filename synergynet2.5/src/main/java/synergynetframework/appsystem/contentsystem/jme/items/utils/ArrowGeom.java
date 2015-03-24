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

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.TexCoords;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;


/**
 * The Class ArrowGeom.
 */
public class ArrowGeom extends TriMesh{
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7019914709961269067L;

	/** The width. */
	protected float width;
	
	/** The height. */
	protected float height;
	
	/**
	 * Instantiates a new arrow geom.
	 *
	 * @param name the name
	 * @param width the width
	 * @param height the height
	 */
	public ArrowGeom(String name, float width, float height){
		this(name, width, height, ColorRGBA.red);
	}
	
	/**
	 * Instantiates a new arrow geom.
	 *
	 * @param name the name
	 * @param width the width
	 * @param height the height
	 * @param arrowColour the arrow colour
	 */
	public ArrowGeom(String name, float width, float height, ColorRGBA arrowColour){
		super(name);
		this.width = width;
		this.height = height;
		build(arrowColour);
	}
	
	/**
	 * Builds the.
	 *
	 * @param arrowColor the arrow color
	 */
	private void build(ColorRGBA arrowColor){
		
        Vector3f[] vertexes={
                new Vector3f(-width/2,-height,0), new Vector3f(0,0,0),new Vector3f(width/2,-height,0)
            };
        Vector3f[] normals={
                new Vector3f(0,0,1), new Vector3f(0,0,1), new Vector3f(0,0,1)
            };
        ColorRGBA[] colors={
        		arrowColor, arrowColor,	arrowColor
            };
        Vector2f[] texCoords={
                new Vector2f(-width/2,-height), new Vector2f(0,0), new Vector2f(width/2,-height),
            };
        int[] indexes={
                0,1,2
            };
        this.reconstruct(BufferUtils.createFloatBuffer(vertexes), BufferUtils.createFloatBuffer(normals),
                BufferUtils.createFloatBuffer(colors), new TexCoords(BufferUtils.createFloatBuffer(texCoords)), BufferUtils.createIntBuffer(indexes));
        
        this.updateGeometricState(0, false);
        this.updateRenderState();
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth(){
		return width;
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight(){
		return height;
	}
	
}
