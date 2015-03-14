package apps.lightrays.raytracer.scene;


public class ShadowRay extends Ray
{
	public double attenuation;
	public double cosine;
	public double light_brightness;
	
	public ShadowRay(Scene scene, Point origin, Vector dir, int depth)
	{
		super(scene, origin, dir, depth);
		this.attenuation = 1.0;
	}
	
	public final double attenuation(){
		return attenuation;
	}
	
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

	public void shade(Colour c)
	{
		if (closest_hit.object != null && closest_hit.object.getOpticProperties().luminous){
			c.set(closest_hit.object.getOpticProperties().colour);
		}else{
	        c.set(0, 0, 0);
		}
	}
}