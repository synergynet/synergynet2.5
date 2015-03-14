package apps.lightrays.raytracer.scene;


public class RefractedRay extends Ray {
	
	protected double refr;
	
    public RefractedRay(Scene scene, Point origin, Vector direction, int depth, double refr)
    {
    	super(scene, origin, direction, depth);
    	this.refr = refr;
    }
}
