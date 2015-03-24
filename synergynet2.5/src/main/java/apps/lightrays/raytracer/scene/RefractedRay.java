package apps.lightrays.raytracer.scene;



/**
 * The Class RefractedRay.
 */
public class RefractedRay extends Ray {
	
	/** The refr. */
	protected double refr;
	
    /**
     * Instantiates a new refracted ray.
     *
     * @param scene the scene
     * @param origin the origin
     * @param direction the direction
     * @param depth the depth
     * @param refr the refr
     */
    public RefractedRay(Scene scene, Point origin, Vector direction, int depth, double refr)
    {
    	super(scene, origin, direction, depth);
    	this.refr = refr;
    }
}
