package apps.lightrays.raytracer.scene.geometry;

import apps.lightrays.raytracer.scene.OpticalProperties;
import apps.lightrays.raytracer.scene.Point;
import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.RayHitInfo;
import apps.lightrays.raytracer.scene.SceneObject;
import apps.lightrays.raytracer.scene.Vector;
import apps.lightrays.raytracer.shaders.PhongShader;
import apps.lightrays.raytracer.shaders.Shader;

public class Sphere extends SceneObject {

	protected Point center;
	protected double radius;
	protected double radius_squared;
	protected Vector normal;
	protected RayHitInfo hit;
	
	public Sphere() {		
		super();
	}	
	
	public Sphere(Point center, double radius, OpticalProperties optics) {
		super();
		set(center, radius, optics);
	}
	
	public void setCenter(Point center) {
		this.center = center;	
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
		this.radius_squared = radius * radius;		
	}
	
	public void set(Point center, double radius, OpticalProperties optics) {
		this.center = center;
		setRadius(radius);
		this.optic_properties = optics;
		this.radius_squared = radius * radius;
	}
	
	public Point getCenter() {
		return center;
	}
	
	public RayHitInfo intersect(Ray r) {
	    // origin to center vector
	    Vector oc = new Vector(center, r.origin);
	
	    // square distance
	    double oc2 = oc.dotproduct(oc);
	    double dist2 = oc2 - radius_squared;
	
	    if ( dist2 <= Vector.EPSILON && dist2 >= -Vector.EPSILON ) // on the surface	 
	        return null;
	    
	    // closest approach along the ray
	    double tca = oc.dotproduct(r.getDirection());
	
	    if ( dist2 > Vector.EPSILON && tca < 0.0) // outside and pointing away
	        return null;
	    
	    // half cord squared
	    double hc2 = radius_squared - oc2 + tca * tca;
	
	    if (hc2 < 0.0) // no real solution
	        return null;
	
	
	    // we have an intersection, so setup the hit info
	    
	    hit = new RayHitInfo();
	    hit.object = this;
	    

	    if (dist2 > Vector.EPSILON) // outside
	    	hit.distance = tca - Math.sqrt(hc2);
	        //r.setIntersectedObject(this, tca - Math.sqrt(hc2));
	    else
	    	hit.distance = tca + Math.sqrt(hc2);
	        //r.setIntersectedObject(this, tca + Math.sqrt(hc2));    	    
	    
		// Find the point of intersection
	    Vector tmp = new Vector(r.getDirection());	    
	    tmp.scale(hit.distance);
	    Point intersect = new Point(r.getOrigin());
	    intersect.add(tmp.toPoint());			
		hit.intersect = intersect;
			    
	    Vector norm = new Vector(intersect, center);
	    norm.normalise();	    
	    
	    hit.normal = norm;  
	    	

	    
	    return hit;
	}

	public Vector getNormal(Point intersect) {
	    return hit.normal;
	}
	


	public Shader createShader(Ray r) {		
		return new PhongShader(scene, r, hit, this);
	}
	
	public String toString() {
		return this.getClass().getName() + radius + "@" + center + this.optic_properties;
	}

}
