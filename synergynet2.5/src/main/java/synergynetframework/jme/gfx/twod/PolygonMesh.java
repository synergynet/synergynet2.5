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

package synergynetframework.jme.gfx.twod;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.net.URL;

import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;

import com.jme.image.Texture;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.awt.swingui.ImageGraphics;

public class PolygonMesh extends GraphicsImageQuad {

	private static final long serialVersionUID = 1L;
	private static int borderWidth = 0;
	private static Color borderColour = Color.white;

	private int[] xPoints;
	private int[] yPoints;
	public float[][]coords;
	public ImageGraphics gfx;

	private String shapeType = "";

	public PolygonMesh(String name, int x, int y){
		super(name, x, y);
		this.setIsCollidable(true);
		gfx = getImageGraphics();
	}

	boolean linear = true;
	private ColorRGBA otherColour;

	public void changeTextureMode() {
		if (linear){
			texture.setMinificationFilter(MinificationFilter.NearestNeighborNoMipMaps);
			texture.setMagnificationFilter(MagnificationFilter.NearestNeighbor);
		}else{
			texture.setMinificationFilter(MinificationFilter.Trilinear);			
			texture.setMagnificationFilter(MagnificationFilter.Bilinear);
		}
	}

	public void addPolygonFilled(float[][] coords, ColorRGBA c, boolean border){
		this.coords = coords;
		xPoints = new int[coords.length];
		yPoints = new int[coords.length];

		for (int i = 0; i < coords.length; i++){
			xPoints[i] = (int)coords[i][0];
			yPoints[i] = (int)coords[i][1];
		}

		Color colour = new Color(c.r, c.g, c.b);
		gfx.setColor(colour);

		gfx.setStroke(new BasicStroke(1));
		gfx.fillPolygon(xPoints, yPoints, coords.length);

		if (border){
			gfx.setColor(borderColour);
			gfx.setStroke(new BasicStroke(borderWidth));
			gfx.drawPolygon(xPoints, yPoints, coords.length);
		}
		updateGraphics();
	}

	public void addCircleFilled(int radius, ColorRGBA c, boolean border){

		Color colour = new Color(c.r, c.g, c.b);

		gfx.setColor(colour);

		gfx.setStroke(new BasicStroke(1));

		gfx.fillArc(0, 0, radius-1, radius/2-1, 0, 360);

		if (border){
			gfx.setColor(borderColour);
			gfx.setStroke(new BasicStroke(borderWidth));
			gfx.drawArc(borderWidth/2, borderWidth/2,
					radius-borderWidth-1, radius/2-borderWidth-1, 0, 360);
		}
		updateGraphics();
	}

	public void addOvalFilled(int xRadius, int yRadius, ColorRGBA c, boolean border){

		Color colour = new Color(c.r, c.g, c.b);

		gfx.setColor(colour);

		gfx.setStroke(new BasicStroke(1));

		gfx.fillArc(0, 0, xRadius-1, yRadius-1, 0, 360);

		if (border){
			gfx.setColor(borderColour);
			gfx.setStroke(new BasicStroke(borderWidth));
			gfx.drawArc(borderWidth/2, borderWidth/2,
					xRadius-borderWidth-1, yRadius-borderWidth-1, 0, 360);
		}
		updateGraphics();
	}

	public void addTriangleFilled(ColorRGBA c, boolean border){

		Polygon T = new Polygon();
		T.addPoint(borderWidth/2, borderWidth/2);
		T.addPoint(this.imageWidth, borderWidth/2);
		T.addPoint(borderWidth/2, this.imageHeight);

		Color colour = new Color(c.r, c.g, c.b);

		gfx.setColor(colour);

		gfx.setStroke(new BasicStroke(1));
		gfx.fillPolygon(T);

		if (border){
			gfx.setColor(borderColour);
			gfx.setStroke(new BasicStroke(borderWidth));
			gfx.drawPolygon(T);
		}
		updateGraphics();
	}

	public void addSquareFilled(ColorRGBA c, int curve, boolean border){

		Color colour = new Color(c.r, c.g, c.b);
		gfx.setColor(colour);

		gfx.setStroke(new BasicStroke(1));
		gfx.fillRoundRect(borderWidth/2, borderWidth/2,
				imageWidth-borderWidth, imageHeight-borderWidth, curve,
				(int)((float)imageHeight*((float)curve/(float)imageWidth)));

		if (border){
			gfx.setColor(borderColour);
			gfx.setStroke(new BasicStroke(borderWidth));
			gfx.drawRoundRect(borderWidth/2, borderWidth/2,
					imageWidth-borderWidth, imageHeight-borderWidth, curve,
					(int)((float)imageHeight*((float)curve/(float)imageWidth)));
		}
		updateGraphics();
	}

	public void addImage(URL resource) {
		TextureState ts = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		Texture t = TextureManager.loadTexture(resource, Texture.MinificationFilter.Trilinear, Texture.MagnificationFilter.Bilinear);
		ts.setTexture(t);

		this.setRenderState(ts);

	}

	public void clear(){
		gfx.clearRect(0, 0, imageWidth, imageHeight);
		updateGraphics();
	}

	public void setShapeType(String shapeType){
		this.shapeType = shapeType;
	}

	public String getShapeType(){
		return shapeType;
	}

	public static void setBorder(ColorRGBA c, int borderWidth){
		PolygonMesh.borderColour = new Color(c.r, c.g, c.b);
		PolygonMesh.borderWidth = borderWidth;

	}

	public void setOtherColour(ColorRGBA otherColour){
		this.otherColour = otherColour;
	}

	public ColorRGBA getOtherColour() {
		return otherColour;
	}

}
