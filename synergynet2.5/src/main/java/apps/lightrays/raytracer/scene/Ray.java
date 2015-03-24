package apps.lightrays.raytracer.scene;

import java.util.Iterator;

import apps.lightrays.raytracer.shaders.Shader;



/**
 * The Class Ray.
 */
public class Ray {
	
	/** The Constant FAR_AWAY. */
	protected static final double FAR_AWAY = 1000000.0;

	/** The scene. */
	protected Scene scene;
	
	/** The origin. */
	public Point origin;
	
	/** The direction. */
	protected Vector direction;
	
	/** The depth. */
	protected int depth;
	//protected SceneObject intersected_object;	
	//protected double distance = FAR_AWAY;
	/** The closest_hit. */
	protected RayHitInfo closest_hit = new RayHitInfo();

	/**
	 * Instantiates a new ray.
	 *
	 * @param scene the scene
	 * @param origin the origin
	 * @param direction the direction
	 * @param depth the depth
	 */
	public Ray(Scene scene, Point origin, Vector direction, int depth) {
		this.scene = scene;
		this.origin = origin;
		this.direction = direction;
		this.direction.normalise();
		this.depth = depth;		
		closest_hit.distance = FAR_AWAY;
	}

	/**
	 * Gets the depth.
	 *
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public Vector getDirection() {
		return direction;
	}

	/**
	 * Gets the origin.
	 *
	 * @return the origin
	 */
	public Point getOrigin() {
		return origin;
	}
	
	/**
	 * Process hit.
	 *
	 * @param hit the hit
	 */
	protected void processHit(RayHitInfo hit) {
		if(hit.distance < closest_hit.distance) {
			this.closest_hit = hit;
		}
	}
		
	/**
	 * Fire.
	 *
	 * @param c the c
	 */
	public void fire(Colour c) {
		// Go through all objects and try to intersect with them
		Iterator<SceneObject> i = scene.getSceneObjects().iterator();
		SceneObject obj;
		while(i.hasNext()) {
			obj = (SceneObject) i.next();
			RayHitInfo hit = obj.intersect(this);
			if(hit != null)
				this.processHit(hit);
		}
		shade(c);		

	}
	
	/**
	 * Shade.
	 *
	 * @param c the c
	 */
	void shade (Colour c) {
	    if (closest_hit == null || closest_hit.object == null || depth > scene.getMaxDepth()) {
	        c.set(0, 0, 0);
	        return;
	    }
	
	    if (closest_hit.object.getOpticProperties().luminous) {
			// It's the light source
	        c.set(closest_hit.object.getOpticProperties().colour);
	        return;
	    }
	
		// Ask the object for its shader
		Shader shader = closest_hit.object.createShader(this);
	    shader.getColour(c);
	}	

}
