package apps.lightrays.raytracer.scene.geometry;


/**
 * The Class Roots.
 */
public class Roots {
	
	/** The num. */
	public int num;
    
    /** The r0. */
    public double r0;
    
    /** The r1. */
    public double r1;
    
    /** The r2. */
    public double r2;
    
    /** The r3. */
    public double r3;
    
    /**
     * Gets the max root.
     *
     * @return the max root
     */
    public double getMaxRoot() {
        if( num == 0 ) return Double.NaN;
        
        double r = r0;
        if( num >= 2 && r < r1 ) r = r1;  
        if( num >= 3 && r < r2 ) r = r2; 
        if( num >= 4 && r < r3 ) r = r3;
        return r;    	
    }
    
    /**
     * Gets the min positive root.
     *
     * @param r the r
     * @return the min positive root
     */
    public double getMinPositiveRoot(double r) {
        boolean okay = false;
        
        if( num >= 1 && 0.0 <= r0 && r0 < r ) { okay = true; r = r0; } 
        if( num >= 2 && 0.0 <= r1 && r1 < r ) { okay = true; r = r1; }
        if( num >= 3 && 0.0 <= r2 && r2 < r ) { okay = true; r = r2; }      
        if( num >= 4 && 0.0 <= r3 && r3 < r ) { okay = true; r = r3; }
        if(okay)
        	return r;
        else
        	return Double.NaN;    	
    }
}
