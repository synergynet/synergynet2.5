package apps.lightrays.raytracer.scene;



/**
 * The Class ShadowRay.
 */
public class ShadowRay extends Ray
{
	
	/** The attenuation. */
	public double attenuation;
	
	/** The cosine. */
	public double cosine;
	
	/** The light_brightness. */
	public double light_brightness;
	
	/**
	 * Instantiates a new shadow ray.
	 *
	 * @param scene the scene
	 * @param origin the origin
	 * @param dir the dir
	 * @param depth the depth
	 */
	public ShadowRay(Scene scene, Point origin, Vector dir, int depth)
	{
		super(scene, origin, dir, depth);
		this.attenuation = 1.0;
	}
	
	/**
	 * Attenuation.
	 *
	 * @return the double
	 */
	public final double attenuation(){
		return attenuation;
	}
	
	/* (non-Javadoc)
	 * @see apps.lightrays.raytracer.scene.Ray#processHit(apps.lightrays.raytracer.scene.RayHitInfo)
	 */
	protected void processHit(RayHitInfo hit) {
		if(hit.distance < closest_hit.distance && hit.distance > Vector.EPSILON) {
        	if (hit.object.getOpticProperties().transparency != 0.0) {
        		// pass through but record attenuation
            	attenuation *= hit.object.getOpticProperties().transparency;
        	}else{
        		if(hit.distance < closest_hit.distance)
        			this.closest_hit = hit;
	        }
		}
	}	

	/* (non-Javadoc)
	 * @see apps.lightrays.raytracer.scene.Ray#shade(apps.lightrays.raytracer.scene.Colour)
	 */
	public void shade(Colour c)
	{
		if (closest_hit.object != null && closest_hit.object.getOpticProperties().luminous){
			c.set(closest_hit.object.getOpticProperties().colour);
		}else{
	        c.set(0, 0, 0);
		}
	}
}