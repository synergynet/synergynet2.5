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

package synergynetframework.appsystem.contentsystem.jme.items;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.UUID;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.shapes.IObjShape;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.TriMesh;
import com.jme.util.export.binary.BinaryImporter;
import com.jme.util.geom.BufferUtils;
import com.jmex.model.converters.ObjToJme;

/**
 * The Class JMEObjShape.
 */
public class JMEObjShape extends JMEFrame implements IObjShape {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEPDFViewer.class
			.getName());

	/** The location. */
	private String location;

	/** The shape tri mesh. */
	private TriMesh shapeTriMesh;
	
	/**
	 * Instantiates a new JME obj shape.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public JMEObjShape(ContentItem contentItem) {
		this(contentItem, new TriMesh());
	}

	/**
	 * Instantiates a new JME obj shape.
	 *
	 * @param contentItem
	 *            the content item
	 * @param spatial
	 *            the spatial
	 */
	public JMEObjShape(ContentItem contentItem, Spatial spatial) {
		super(contentItem);
		spatial.setName(UUID.randomUUID().toString());
		this.shapeTriMesh = (TriMesh) this.spatial;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .shapes.IObjShape#getObjFileText()
	 */
	@Override
	public String getObjFileText() throws IOException {
		
		BufferedReader inputStream = null;
		
		String result = "";
		try {
			inputStream = new BufferedReader(new FileReader(location));
			
			String c;
			while ((c = inputStream.readLine()) != null) {
				result += c + "\n";
			}
		} catch (IOException e) {
			log.warning(e.toString());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .shapes.IObjShape#setShapeGeometry(java.lang.String)
	 */
	@Override
	public void setShapeGeometry(String geomLoc) throws Exception {

		ByteArrayInputStream bs = new ByteArrayInputStream(geomLoc.getBytes());

		ObjToJme converter = new ObjToJme();
		converter.setProperty("mtllib", bs);
		converter.setProperty("texdir", bs);
		ByteArrayOutputStream BO = new ByteArrayOutputStream();
		converter.convert(bs, BO);
		TriMesh model = (TriMesh) BinaryImporter.getInstance().load(
				new ByteArrayInputStream(BO.toByteArray()));
		shapeTriMesh.reconstruct(model.getVertexBuffer(),
				model.getNormalBuffer(), model.getColorBuffer(),
				model.getTextureCoords(0));
		shapeTriMesh.setIndexBuffer(model.getIndexBuffer());
		shapeTriMesh.setTangentBuffer(model.getTangentBuffer());
		shapeTriMesh.updateGeometricState(0f, true);
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .shapes.IObjShape#setShapeGeometry(java.net.URL)
	 */
	@Override
	public void setShapeGeometry(URL geomLoc) throws Exception {

		ObjToJme converter = new ObjToJme();
		converter.setProperty("mtllib", geomLoc);
		converter.setProperty("texdir", geomLoc);
		ByteArrayOutputStream BO = new ByteArrayOutputStream();
		converter.convert(geomLoc.openStream(), BO);
		TriMesh model = (TriMesh) BinaryImporter.getInstance().load(
				new ByteArrayInputStream(BO.toByteArray()));
		shapeTriMesh.reconstruct(model.getVertexBuffer(),
				model.getNormalBuffer(), model.getColorBuffer(),
				model.getTextureCoords(0));
		shapeTriMesh.setIndexBuffer(model.getIndexBuffer());
		shapeTriMesh.setTangentBuffer(model.getTangentBuffer());
		shapeTriMesh.updateGeometricState(0f, true);

		location = geomLoc.toString();
		location = location.replaceAll("file:", "");
		location = location.replaceAll("%20", " ");

	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .shapes.IObjShape#setSolidColour(com.jme.renderer.ColorRGBA)
	 */
	@Override
	public void setSolidColour(ColorRGBA colour) {
		shapeTriMesh.setRandomColors();
		FloatBuffer colorBuf = shapeTriMesh.getColorBuffer();
		
		if (colorBuf == null) {
			colorBuf = BufferUtils.createColorBuffer(shapeTriMesh
					.getVertexCount());
		}
		
		colorBuf.rewind();
		for (int x = 0, cLength = colorBuf.remaining(); x < cLength; x += 4) {
			colorBuf.put(colour.r);
			colorBuf.put(colour.g);
			colorBuf.put(colour.b);
			colorBuf.put(colour.a);
		}
		colorBuf.flip();
		shapeTriMesh.updateGeometricState(0f, true);
		
	}

}
