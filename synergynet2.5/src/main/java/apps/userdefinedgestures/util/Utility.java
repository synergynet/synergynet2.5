package apps.userdefinedgestures.util;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Spatial;

public class Utility {

	public enum ViewPort{
		TOPDOWN, FRONT
	}
	
	public static final void setViewPort(Camera cam, ViewPort viewPort){
		
		if (viewPort==ViewPort.FRONT)
			cam.setLocation(new Vector3f(0f, 10f, 50f));
		else
			cam.setLocation(new Vector3f(0f, 70f, 40f));
		cam.lookAt(new Vector3f(0, 10, 0), new Vector3f( 0, 1, 0 ));
		cam.update();	
	}
	
	public static final void renderTargetObject(Spatial targetObject){
		
	}
}
