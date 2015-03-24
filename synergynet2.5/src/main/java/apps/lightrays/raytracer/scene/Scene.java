package apps.lightrays.raytracer.scene;

import java.util.ArrayList;
import java.util.List;



/**
 * The Class Scene.
 */
public class Scene {
	
	/** The light. */
	private Light light;
	
	/** The max_depth. */
	private int max_depth = 4;
	
	/** The objs. */
	private List<SceneObject> objs = new ArrayList<SceneObject>();
	
	/** The lights. */
	private Lighting lights;
	
	/** The ambient_colour. */
	private Colour ambient_colour;
	
	/**
	 * Instantiates a new scene.
	 */
	public Scene() {		
	}

	/**
	 * Adds the scene object.
	 *
	 * @param obj the obj
	 */
	public void addSceneObject(SceneObject obj) {
		obj.setScene(this);
		objs.add(obj);
	}
	
	/**
	 * Gets the scene objects.
	 *
	 * @return the scene objects
	 */
	public List<SceneObject> getSceneObjects() {		
		return objs;
	}
	
	/**
	 * Sets the light.
	 *
	 * @param lightpoint the new light
	 */
	public void setLight(Point lightpoint) {	
	    Point center = new Point(lightpoint);		
		light = new Light(center);
		addSceneObject(light);		
	}
	
	/**
	 * Sets the max depth.
	 *
	 * @param max_depth the new max depth
	 */
	public void setMaxDepth(int max_depth) {
		this.max_depth = max_depth;
	}
	
	/**
	 * Gets the max depth.
	 *
	 * @return the max depth
	 */
	public int getMaxDepth() {
		return max_depth;
	}

	/**
	 * Gets the ambient colour.
	 *
	 * @return the ambient colour
	 */
	public Colour getAmbientColour() {
		return ambient_colour;
	}

	/**
	 * Sets the ambient colour.
	 *
	 * @param ambient_colour the new ambient colour
	 */
	public void setAmbientColour(Colour ambient_colour) {
		this.ambient_colour = ambient_colour;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "scene[" + light + " " + max_depth + ", " + ambient_colour + "," + objs + ", " + lights +  "]";
	}

	/**
	 * Sets the lighting.
	 *
	 * @param lighting the new lighting
	 */
	public void setLighting(Lighting lighting) {
		this.lights = lighting;
		
	}
	
	/**
	 * Gets the lighting.
	 *
	 * @return the lighting
	 */
	public Lighting getLighting() {
		return lights;
	}
}
