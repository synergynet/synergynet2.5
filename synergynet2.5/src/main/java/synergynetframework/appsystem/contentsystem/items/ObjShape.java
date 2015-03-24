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

package synergynetframework.appsystem.contentsystem.items;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.IObjShape;

import com.jme.renderer.ColorRGBA;

/**
 * The Class ObjShape.
 */
public class ObjShape extends Frame implements Cloneable {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(ObjShape.class.getName());

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7358045606444271667L;
	
	/** The impl. */
	IObjShape impl = null;

	/**
	 * Instantiates a new obj shape.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public ObjShape(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/**
	 * Gets the obj file text.
	 *
	 * @return the obj file text
	 */
	public String getObjFileText() {
		try {
			return impl.getObjFileText();
		} catch (IOException e) {
			log.warning("Could not get ObjShape obj file text.\n"
					+ e.toString());
			return "";
		}
	}

	/**
	 * Sets the shape geometry.
	 *
	 * @param geomLoc
	 *            the new shape geometry
	 */
	public void setShapeGeometry(String geomLoc) {

		impl = (IObjShape) contentItemImplementation;
		try {
			impl.setShapeGeometry(geomLoc);
		} catch (Exception e) {
			log.warning("Failed to set shape Geometry as the .obj string cannot be read.\n"
					+ e.toString());
		}
	}

	/**
	 * Sets the shape geometry.
	 *
	 * @param geom
	 *            the new shape geometry
	 */
	public void setShapeGeometry(URL geom) {
		impl = (IObjShape) contentItemImplementation;
		try {
			impl.setShapeGeometry(geom);
		} catch (Exception e) {
			log.warning("Failed to set shape Geometry as Obj shape not found at given address.\n"
					+ e.toString());
		}
	}
	
	/**
	 * Sets the solid colour.
	 *
	 * @param colour
	 *            the new solid colour
	 */
	public void setSolidColour(ColorRGBA colour) {
		if (impl != null) {
			impl.setSolidColour(colour);
		}
	}
	
}
