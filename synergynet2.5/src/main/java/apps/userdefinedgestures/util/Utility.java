package apps.userdefinedgestures.util;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;


/**
 * The Class Utility.
 */
public class Utility {

	/**
	 * The Enum ViewPort.
	 */
	public enum ViewPort{
		
		/** The topdown. */
		TOPDOWN, 
 /** The front. */
 FRONT
	}
	
	/**
	 * Sets the view port.
	 *
	 * @param cam the cam
	 * @param viewPort the view port
	 */
	public static final void setViewPort(Camera cam, ViewPort viewPort){
		
		if (viewPort==ViewPort.FRONT)
			cam.setLocation(new Vector3f(0f, 10f, 50f));
		else
			cam.setLocation(new Vector3f(0f, 70f, 40f));
		cam.lookAt(new Vector3f(0, 10, 0), new Vector3f( 0, 1, 0 ));
		cam.update();	
	}
	
	/**
	 * Render target object.
	 *
	 * @param targetObject the target object
	 */
	public static final void renderTargetObject(Spatial targetObject){
		
	}
}
