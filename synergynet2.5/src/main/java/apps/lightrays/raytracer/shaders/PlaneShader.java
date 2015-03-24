package apps.lightrays.raytracer.shaders;

import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.RayHitInfo;
import apps.lightrays.raytracer.scene.Scene;
import apps.lightrays.raytracer.scene.geometry.Plane;


/**
 * The Class PlaneShader.
 */
public class PlaneShader extends PhongShader {
	
	/** The pixel. */
	protected Colour  pixel;
	
	/**
	 * Instantiates a new plane shader.
	 *
	 * @param scene the scene
	 * @param incident_ray the incident_ray
	 * @param hit the hit
	 * @param obj the obj
	 */
	public PlaneShader(Scene scene, Ray incident_ray, RayHitInfo hit, Plane obj) {
		super(scene, incident_ray, hit, obj);
		this.pixel = obj.getPixel(intersect);
	}

	/* (non-Javadoc)
	 * @see apps.lightrays.raytracer.shaders.PhongShader#getColour(apps.lightrays.raytracer.scene.Colour)
	 */
	public void getColour(Colour c) {
		// first calculate the Phong shade
		// then attenuate it by the texture
	    super.getColour(c);
	    c.attenuate(pixel);		
	}

}

