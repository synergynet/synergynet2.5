package apps.userdefinedgestures.transform;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

public class RotateTransformer extends Transformer {

	private static final long serialVersionUID = -1222143255691434371L;
	
	public static final Vector3f XAxis = new Vector3f(1, 0, 0);
	public static final Vector3f YAxis = new Vector3f(0, 1, 0);
	public static final Vector3f ZAxis = new Vector3f(0, 0, 1);
	
	public RotateTransformer(){
		super();
		
	}
	
	public void SetAxis(Vector3f axis){
		
		Quaternion x0 = new Quaternion();
		x0.fromAngleAxis(0, axis);
		this.setRotation(0, 0, x0);
		
		Quaternion x180 = new Quaternion();
		x180.fromAngleAxis(FastMath.DEG_TO_RAD * 180, axis);
		this.setRotation(0, 2, x180);
 
		Quaternion x360 = new Quaternion();
		x360.fromAngleAxis(FastMath.DEG_TO_RAD * 360, axis);
		this.setRotation(0, 4, x360);
	}

}
