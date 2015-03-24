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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.awt.Color;
import java.nio.FloatBuffer;

import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.util.geom.BufferUtils;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.TwoDShape;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.ITwoDShape;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry.ColourRGBA;
import synergynetframework.appsystem.contentsystem.jme.items.utils.ShapeTriMesh;


/**
 * The Class JMETwoDShape.
 */
public class JMETwoDShape extends JMEOrthoContentItem implements ITwoDShape {

	/** The shape tri mesh. */
	private ShapeTriMesh shapeTriMesh;
	
	/** The item. */
	private TwoDShape item;
	
	/**
	 * Instantiates a new JME two d shape.
	 *
	 * @param contentItem the content item
	 */
	public JMETwoDShape(ContentItem contentItem) {
		this(contentItem, new ShapeTriMesh());
	}
	
	/**
	 * Instantiates a new JME two d shape.
	 *
	 * @param contentItem the content item
	 * @param spatial the spatial
	 */
	public JMETwoDShape(ContentItem contentItem, Spatial spatial) {
		super(contentItem, spatial);
		this.item = (TwoDShape) contentItem;
		this.shapeTriMesh = (ShapeTriMesh) this.spatial;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.ITwoDShape#setShapeGeometry(synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.TwoDShapeGeometry)
	 */
	@Override
	public void setShapeGeometry(TwoDShapeGeometry geom) {
		shapeTriMesh.updateGeometry(geom);
		setColours(shapeTriMesh, geom.getColors());
		
		shapeTriMesh.updateGeometricState(0f, true);
	}
	
    /**
     * Sets the colours.
     *
     * @param triMesh the tri mesh
     * @param colours the colours
     */
    private void setColours(TriMesh triMesh, ColourRGBA[] colours) {
    	FloatBuffer colorBuf = triMesh.getColorBuffer();
    	
        if (colorBuf == null)
            colorBuf = BufferUtils.createColorBuffer(triMesh.getVertexCount());

        colorBuf.rewind();
        int index = 0;
        ColourRGBA current;
        for (int x = 0, cLength = colorBuf.remaining(); x < cLength; x += 4) {
        	current = colours[index];
            colorBuf.put(current.r);
            colorBuf.put(current.g);
            colorBuf.put(current.b);
            colorBuf.put(current.a);
            index++;
        }
        colorBuf.flip();
    }

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.ITwoDShape#setColours(java.awt.Color[])
	 */
	@Override
	public void setColours(Color[] colours) {
		ColourRGBA[] coloursRGBA = new ColourRGBA[colours.length];
		TwoDShapeGeometry geom = item.getShapeGeometry();
		for(int i=0;i<colours.length; i++)
			coloursRGBA[i] = geom.new ColourRGBA(((float)colours[i].getRed())/255.0f,((float)colours[i].getGreen())/255.0f,((float)colours[i].getBlue())/255.0f,((float)colours[i].getAlpha())/255.0f);
		this.setColours(shapeTriMesh, coloursRGBA);
		shapeTriMesh.updateGeometricState(0f, true);
	}


}


