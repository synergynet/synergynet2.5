package apps.lightrays.raytracer.shaders;

import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.RayHitInfo;
import apps.lightrays.raytracer.scene.Scene;
import apps.lightrays.raytracer.scene.geometry.Plane;

public class PlaneShader extends PhongShader {
	
	protected Colour  pixel;
	
	public PlaneShader(Scene scene, Ray incident_ray, RayHitInfo hit, Plane obj) {
		super(scene, incident_ray, hit, obj);
		this.pixel = obj.getPixel(intersect);
	}

	public void getColour(Colour c) {
		// first calculate the Phong shade
		// then attenuate it by the texture
	    super.getColour(c);
	    c.attenuate(pixel);		
	}

}

