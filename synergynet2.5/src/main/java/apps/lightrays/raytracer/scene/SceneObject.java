package apps.lightrays.raytracer.scene;

import apps.lightrays.raytracer.shaders.Shader;


/**
 * The Class SceneObject.
 */
public abstract class SceneObject {

	/** The optic_properties. */
	protected OpticalProperties optic_properties;
	
	/** The scene. */
	protected Scene scene;
	
	/**
	 * Instantiates a new scene object.
	 */
	public SceneObject() {
		super();
	}
	
	/**
	 * Sets the scene.
	 *
	 * @param scene the new scene
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	/**
	 * Intersect.
	 *
	 * @param r the r
	 * @return the ray hit info
	 */
	public abstract RayHitInfo intersect(Ray r);
	
	/**
	 * Gets the normal.
	 *
	 * @param intersect the intersect
	 * @return the normal
	 */
	public abstract Vector getNormal (Point intersect);
	
	/**
	 * Creates the shader.
	 *
	 * @param r the r
	 * @return the shader
	 */
	public abstract Shader createShader(Ray r);

	/**
	 * Gets the optic properties.
	 *
	 * @return the optic properties
	 */
	public OpticalProperties getOpticProperties() {
		return optic_properties;
	}

	/**
	 * Sets the optic properties.
	 *
	 * @param optic_properties the new optic properties
	 */
	public void setOpticProperties(OpticalProperties optic_properties) {
		this.optic_properties = optic_properties;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
