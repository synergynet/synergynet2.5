package apps.lightrays.raytracer.shaders;

import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.OpticalProperties;
import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.Scene;
import apps.lightrays.raytracer.scene.SceneObject;


/**
 * The Class Shader.
 */
public abstract class Shader {

	/** The optical_properties. */
	protected OpticalProperties optical_properties;
	
	/** The scene. */
	protected Scene scene;
	
	/** The incident_ray. */
	protected Ray incident_ray;
	
	/** The depth. */
	protected int depth;
	
	/**
	 * Instantiates a new shader.
	 *
	 * @param scene the scene
	 * @param incident_ray the incident_ray
	 * @param obj the obj
	 */
	public Shader(Scene scene, Ray incident_ray, SceneObject obj) {
		this.scene = scene;
		this.optical_properties = obj.getOpticProperties();
		this.incident_ray = incident_ray;
		this.depth = incident_ray.getDepth();
	}

	/**
	 * Gets the colour.
	 *
	 * @param c the c
	 * @return the colour
	 */
	public abstract void getColour(Colour c);

	/**
	 * Gets the depth.
	 *
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Sets the depth.
	 *
	 * @param depth the new depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * Gets the incident_ray.
	 *
	 * @return the incident_ray
	 */
	public Ray getIncident_ray() {
		return incident_ray;
	}

	/**
	 * Sets the incident_ray.
	 *
	 * @param incident_ray the new incident_ray
	 */
	public void setIncident_ray(Ray incident_ray) {
		this.incident_ray = incident_ray;
	}

	/**
	 * Gets the optical_properties.
	 *
	 * @return the optical_properties
	 */
	public OpticalProperties getOptical_properties() {
		return optical_properties;
	}

	/**
	 * Sets the optical_properties.
	 *
	 * @param optical_properties the new optical_properties
	 */
	public void setOptical_properties(OpticalProperties optical_properties) {
		this.optical_properties = optical_properties;
	}	
}
