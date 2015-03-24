package apps.lightrays.raytracer.scene.geometry;

import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.OpticalProperties;
import apps.lightrays.raytracer.scene.Point;
import apps.lightrays.raytracer.scene.Ray;
import apps.lightrays.raytracer.scene.RayHitInfo;
import apps.lightrays.raytracer.scene.SceneObject;
import apps.lightrays.raytracer.scene.Vector;
import apps.lightrays.raytracer.shaders.PlaneShader;
import apps.lightrays.raytracer.shaders.Shader;


/**
 * The Class Plane.
 */
public class Plane extends SceneObject {
		// geometry
		/** The center. */
		Point center;
		
		/** The normal. */
		Vector normal;
		
		/** The cos_inv. */
		double  cos_inv;
		
		/** The hit. */
		RayHitInfo hit;
		
		/**
		 * Instantiates a new plane.
		 */
		public Plane() {
			super();
		}
			
		/**
		 * Instantiates a new plane.
		 *
		 * @param center the center
		 * @param perpendicular the perpendicular
		 * @param prop the prop
		 */
		public Plane (Point center, Vector perpendicular, OpticalProperties prop)
		{
			super();
			this.center = center;
			this.optic_properties = prop;
			this.normal = new Vector(perpendicular);
			update();
		}
		
		/**
		 * Sets the center.
		 *
		 * @param center the new center
		 */
		public void setCenter(Point center) {
			this.center = center;
			update();
		}
		
		/**
		 * Sets the perpendicular.
		 *
		 * @param perp the new perpendicular
		 */
		public void setPerpendicular(Vector perp) {
			this.normal = new Vector(perp);
			update();
		}
		
		/**
		 * Update.
		 */
		private void update() {
			if(this.normal != null) {
				this.normal.normalise();
				this.cos_inv = 1.0 / this.normal.dotproduct(new Vector(0, 0, 1));
			}
		}

		
		/**
		 * Gets the pixel.
		 *
		 * @param p the p
		 * @return the pixel
		 */
		public Colour getPixel(Point p)
		{
			return this.optic_properties.colour;
		}
			
		/* (non-Javadoc)
		 * @see apps.lightrays.raytracer.scene.SceneObject#getNormal(apps.lightrays.raytracer.scene.Point)
		 */
		public Vector getNormal(Point intersect)
		{
			return normal;
		}
		
		/* (non-Javadoc)
		 * @see apps.lightrays.raytracer.scene.SceneObject#intersect(apps.lightrays.raytracer.scene.Ray)
		 */
		public RayHitInfo intersect(Ray ray)
		{
			// ( org + t * dir - center ) * normal = 0
			
	    	// cosine of incidence angle
	    	double inc = ray.getDirection().dotproduct(normal);
	    	if (inc >= Vector.EPSILON)
	        	return null;

	    	Vector oc = new Vector(center, ray.origin);

	    	double t = oc.dotproduct(normal);
	    	t /= inc;
	    	if (t > Vector.EPSILON)
	    	{
	    		hit = new RayHitInfo();
	    		hit.distance = t;
	    		
	    		// Find the point of intersection
	    	    Vector tmp = new Vector(ray.getDirection());	    
	    	    tmp.scale(hit.distance);
	    	    Point intersect = new Point(ray.getOrigin());
	    	    intersect.add(tmp.toPoint());			
	    		hit.intersect = intersect;	    			    		
	    		hit.normal = this.normal;
	    		hit.object = this;
	    			        	
	        	return hit;
	    	}
	    	return null;
		}

		/* (non-Javadoc)
		 * @see apps.lightrays.raytracer.scene.SceneObject#createShader(apps.lightrays.raytracer.scene.Ray)
		 */
		public Shader createShader (Ray ray)
		{
			return new PlaneShader(scene, ray, hit, this);
		}
		
		/* (non-Javadoc)
		 * @see apps.lightrays.raytracer.scene.SceneObject#toString()
		 */
		public String toString() {
			return "PLANE";
		}
}
