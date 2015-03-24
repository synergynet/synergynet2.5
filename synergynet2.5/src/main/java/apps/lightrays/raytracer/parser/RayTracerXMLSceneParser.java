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

package apps.lightrays.raytracer.parser;

import java.io.IOException;
import java.io.StringReader;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import apps.lightrays.raytracer.scene.Camera;
import apps.lightrays.raytracer.scene.CameraAnimation;
import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.Light;
import apps.lightrays.raytracer.scene.Lighting;
import apps.lightrays.raytracer.scene.OpticalProperties;
import apps.lightrays.raytracer.scene.Point;
import apps.lightrays.raytracer.scene.Scene;
import apps.lightrays.raytracer.scene.SceneObject;
import apps.lightrays.raytracer.scene.Vector;
import apps.lightrays.raytracer.scene.geometry.Plane;
import apps.lightrays.raytracer.scene.geometry.Sphere;

/**
 * The Class RayTracerXMLSceneParser.
 */
public class RayTracerXMLSceneParser extends DefaultHandler {

	/** The Constant AMBIENCE. */
	public static final String AMBIENCE = "ambience";

	/** The Constant ANIMATION. */
	public static final String ANIMATION = "animation";

	/** The Constant COLOUR. */
	public static final String COLOUR = "colour";

	/** The Constant LIGHT. */
	public static final String LIGHT = "light";

	/** The Constant LOOKAT. */
	public static final String LOOKAT = "lookat";

	/** The Constant MAXDEPTH. */
	public static final String MAXDEPTH = "maxdepth";

	/** The Constant NORMAL. */
	public static final String NORMAL = "normal";
	
	/** The Constant OBJECTS. */
	public static final String OBJECTS = "objects";
	
	/** The Constant OPTICS. */
	public static final String OPTICS = "optics";

	/** The Constant PLANE. */
	public static final String PLANE = "plane";

	/** The Constant POSITION. */
	public static final String POSITION = "position";

	/** The Constant SCENE. */
	public static final String SCENE = "scene";

	/** The Constant SPHERE. */
	public static final String SPHERE = "sphere";

	/** The Constant TO. */
	public static final String TO = "to";

	/** The Constant VIEWPOINT. */
	public static final String VIEWPOINT = "viewpoint";

	/** The animation. */
	protected CameraAnimation animation = new CameraAnimation();

	/** The camera. */
	protected Camera camera = new Camera();

	/** The current_name. */
	protected String current_name = "";

	/** The current_object. */
	protected SceneObject current_object;

	/** The current_optics. */
	protected OpticalProperties current_optics;

	/** The lighting. */
	protected Lighting lighting = new Lighting();

	/** The object_list. */
	boolean object_list = false;

	/** The old_name. */
	protected String old_name = "";

	/** The reader. */
	protected XMLReader reader;

	/** The scene. */
	protected Scene scene;
	
	/**
	 * Instantiates a new ray tracer xml scene parser.
	 */
	public RayTracerXMLSceneParser() {
		try {
			reader = XMLReaderFactory.createXMLReader();
			reader.setContentHandler(this);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String s = new String(ch, start, length);
		s = s.trim();
		if (!s.equals("")) {
			if (old_name.equals(MAXDEPTH)) {
				scene.setMaxDepth(Integer.parseInt(s));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	public void endDocument() throws SAXException {
	}

	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (qName.equals(SPHERE)) {
			current_object.setOpticProperties(current_optics);
			scene.addSceneObject(current_object);
		} else if (qName.equals(PLANE)) {
			current_object.setOpticProperties(current_optics);
			scene.addSceneObject(current_object);
		} else if (qName.equals(LIGHT)) {
			current_object.setOpticProperties(current_optics);
			lighting.addLight((Light) current_object);
		} else if (qName.equals(OBJECTS)) {
			this.object_list = false;
		} else if (qName.equals(SCENE)) {
			scene.setLighting(lighting);
			lighting.addLightsToScene(scene);
		}
	}
	
	/**
	 * Gets the camera.
	 *
	 * @return the camera
	 */
	public Camera getCamera() {
		return camera;
	}
	
	/**
	 * Gets the camera animation.
	 *
	 * @return the camera animation
	 */
	public CameraAnimation getCameraAnimation() {
		return this.animation;
	}
	
	/**
	 * Gets the colour.
	 *
	 * @param atts
	 *            the atts
	 * @return the colour
	 */
	private Colour getColour(Attributes atts) {
		double r = Double.parseDouble(atts.getValue("r"));
		double g = Double.parseDouble(atts.getValue("g"));
		double b = Double.parseDouble(atts.getValue("b"));
		Colour c = new Colour(r, g, b);
		return c;
	}
	
	/**
	 * Gets the point.
	 *
	 * @param atts
	 *            the atts
	 * @return the point
	 */
	private Point getPoint(Attributes atts) {
		double x = Double.parseDouble(atts.getValue("x"));
		double y = Double.parseDouble(atts.getValue("y"));
		double z = Double.parseDouble(atts.getValue("z"));
		Point p = new Point(x, y, z);
		return p;
	}
	
	/**
	 * Gets the scene.
	 *
	 * @return the scene
	 */
	public Scene getScene() {
		return this.scene;
	}
	
	/**
	 * Gets the vector.
	 *
	 * @param atts
	 *            the atts
	 * @return the vector
	 */
	private Vector getVector(Attributes atts) {
		double x = Double.parseDouble(atts.getValue("x"));
		double y = Double.parseDouble(atts.getValue("y"));
		double z = Double.parseDouble(atts.getValue("z"));
		Vector v = new Vector(x, y, z);
		return v;
	}
	
	/**
	 * Parses the.
	 *
	 * @param xmlstring
	 *            the xmlstring
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void parse(String xmlstring) throws SAXException, IOException {
		this.scene = new Scene();
		reader.parse(new InputSource(new StringReader(xmlstring)));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	public void startDocument() throws SAXException {
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		current_name = qName;
		
		if (object_list) {
			if (current_name.equals(SPHERE)) {
				Sphere s = new Sphere();
				s.setRadius(Double.parseDouble(atts.getValue("radius")));
				current_object = s;
				current_optics = new OpticalProperties();
			} else if (current_name.equals(PLANE)) {
				Plane p = new Plane();
				current_object = p;
				current_optics = new OpticalProperties();
			} else if (current_name.equals(LIGHT)) {
				Light l = new Light();
				l.setRadius(Double.parseDouble(atts.getValue("radius")));
				l.setBrightness(Double.parseDouble(atts.getValue("brightness")));
				current_object = l;
				current_optics = new OpticalProperties();
			} else if (current_name.equals(POSITION)) {
				if (current_object instanceof Sphere) {
					((Sphere) current_object).setCenter(getPoint(atts));
				} else if (current_object instanceof Plane) {
					((Plane) current_object).setCenter(getPoint(atts));
				} else if (current_object instanceof Light) {
					((Light) current_object).setCenter(getPoint(atts));
				}
			} else if (current_name.equals(NORMAL)) {
				if (current_object instanceof Plane) {
					((Plane) current_object).setPerpendicular(getVector(atts));
				}
			} else if (current_name.equals(COLOUR)) {
				current_optics.setColour(getColour(atts));
			} else if (current_name.equals(OPTICS)) {
				double refra = Double.parseDouble(atts.getValue("refraction"));
				double transp = Double.parseDouble(atts
						.getValue("transparency"));
				double refle = Double.parseDouble(atts.getValue("reflection"));
				double diffu = Double.parseDouble(atts.getValue("diffusion"));
				boolean lumin = Boolean.parseBoolean(atts.getValue("luminous"));
				current_optics.setValues(refra, transp, refle, diffu, lumin);
			}
			
		} else {
			if (current_name.equals(OBJECTS)) {
				object_list = true;
			} else if (current_name.equals(SCENE)) {
				// do nothing
			} else if (current_name.equals(COLOUR)) {
				if (old_name.equals(AMBIENCE)) {
					scene.setAmbientColour(getColour(atts));
				}
			} else if (current_name.equals(POSITION)) {
				if (old_name.equals(VIEWPOINT)) {
					camera.setViewpoint(getVector(atts));
					animation.startEyePosition = getVector(atts);
				} else if (old_name.equals(LOOKAT)) {
					camera.setLookAt(getVector(atts));
				} else if (old_name.equals(TO)) {
					animation.endEyePosition = getVector(atts);
				}
			} else if (current_name.equals(ANIMATION)) {
				animation.frames = Integer.parseInt(atts.getValue("frames"));
			}
		}
		old_name = current_name;
		
	}
}
