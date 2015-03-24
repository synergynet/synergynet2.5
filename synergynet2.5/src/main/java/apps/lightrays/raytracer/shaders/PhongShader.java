package apps.lightrays.raytracer.shaders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.Light;
import apps.lightrays.raytracer.scene.Point;
import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.RayHitInfo;
import apps.lightrays.raytracer.scene.RefractedRay;
import apps.lightrays.raytracer.scene.Scene;
import apps.lightrays.raytracer.scene.SceneObject;
import apps.lightrays.raytracer.scene.ShadowRay;
import apps.lightrays.raytracer.scene.Vector;

/**
 * The Class PhongShader.
 */
public class PhongShader extends Shader {
	
	/** The cosine. */
	double cosine;
	// double light_cos;
	
	// geometry
	/** The intersect. */
	Point intersect;
	
	/** The normal. */
	Vector normal;
	
	// Secondary rays
	/** The reflected_ray. */
	Ray reflected_ray;
	
	/** The refracted_ray. */
	RefractedRay refracted_ray;
	// ShadowRay shadow_ray;
	
	/** The shadow_rays. */
	List<ShadowRay> shadow_rays = new ArrayList<ShadowRay>();
	
	/**
	 * Instantiates a new phong shader.
	 *
	 * @param scene
	 *            the scene
	 * @param incident_ray
	 *            the incident_ray
	 * @param hit
	 *            the hit
	 * @param obj
	 *            the obj
	 */
	public PhongShader(Scene scene, Ray incident_ray, RayHitInfo hit,
			SceneObject obj) {
		super(scene, incident_ray, obj);
		this.intersect = hit.intersect;
		this.normal = hit.normal;
		this.cosine = normal.dotproduct(incident_ray.getDirection());
		
		Iterator<Light> i = scene.getLighting().getIterator();
		Light light;

		while (i.hasNext()) {
			// vector pointing towards light
			light = i.next();
			Vector light_v = new Vector(light.getCenter(), intersect);
			light_v.normalise();
			
			double light_cos = light_v.dotproduct(normal);
			
			// Shadow ray towards the source of light
			if ((optical_properties.diffusion > 0.0) && (light_cos > 0.0)) {
				ShadowRay shadow_ray = new ShadowRay(scene, intersect, light_v,
						depth + 1);
				shadow_ray.cosine = light_cos;
				shadow_ray.light_brightness = light.getRelativeBrightness();
				shadow_rays.add(shadow_ray);
			}
		}
		
		// Reflection
		if (optical_properties.reflectiveness > 0.0) {
			Vector ref = reflect();
			reflected_ray = new Ray(scene, intersect, ref, depth + 1);
		}
		
		// Refraction
		if (optical_properties.transparency > 0.0) {
			Vector ref = refract();
			if (ref.nonzero()) {
				refracted_ray = new RefractedRay(scene, intersect, ref,
						depth + 1, optical_properties.refractiveness);
			}
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.lightrays.raytracer.shaders.Shader#getColour(apps.lightrays.raytracer
	 * .scene.Colour)
	 */
	public void getColour(Colour c) {
		// Diffused light
		Colour diffuse = new Colour();
		/*
		 * if (shadow_ray != null) { shadow_ray.fire(diffuse);
		 * diffuse.attenuate(shadow_ray.attenuation);
		 * diffuse.attenuate(light_cos); }
		 */
		Iterator<ShadowRay> i = shadow_rays.iterator();
		ShadowRay ray;
		while (i.hasNext()) {
			ray = i.next();
			Colour raydiff = new Colour();
			ray.fire(raydiff);
			
			raydiff.attenuate(ray.attenuation);
			raydiff.attenuate(ray.cosine);
			raydiff.attenuate(ray.light_brightness);
			diffuse.combineWith(raydiff);
		}
		
		diffuse.attenuate(optical_properties.diffusion);
		diffuse.combineWith(scene.getAmbientColour());
		diffuse.attenuate(optical_properties.colour);
		c.set(diffuse);
		
		// Reflected light
		
		if (reflected_ray != null) {
			Colour specular = new Colour();
			reflected_ray.fire(specular);
			specular.attenuate(optical_properties.reflectiveness);
			c.combineWith(specular);
		}
		
		// Refracted light
		
		if (refracted_ray != null) {
			Colour refr = new Colour();
			refracted_ray.fire(refr);
			refr.attenuate(optical_properties.transparency);
			c.combineWith(refr);
		}
	}
	
	/**
	 * Reflect.
	 *
	 * @return the vector
	 */
	public Vector reflect() {
		// Angle of reflection = angle of incidence
		// refl = incid - 2 * ( normal . incid ) normal
		Vector result = new Vector(normal);
		result.scale(-2 * cosine);
		result.add(incident_ray.getDirection());
		return result;
	}
	
	/**
	 * Refract.
	 *
	 * @return the vector
	 */
	public Vector refract() {
		double refr = 0;
		if (cosine >= 0.0) {
			refr = optical_properties.refractiveness;
		} else if (optical_properties.refractiveness > 0.0) {
			refr = 1.0 / optical_properties.refractiveness;
		}
		
		double disc_2 = (refr * refr * ((cosine * cosine) - 1)) + 1;
		
		if (disc_2 > 0.0) {
			double discr = Math.sqrt(disc_2);
			// a = - b cos - discr
			double alpha = -refr * cosine;
			if (cosine < 0.0) {
				alpha -= discr;
			} else {
				alpha += discr;
			}
			
			// r = a n + b inc
			Vector result = new Vector(incident_ray.getDirection());
			result.scale(refr);
			
			Vector n = new Vector(normal);
			n.scale(alpha);
			result.add(n);
			return result;
		} else {
			return new Vector(0, 0, 0);
		}
	}
	
}