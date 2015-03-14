package apps.userdefinedgestures.transform;

import com.jme.math.Vector3f;

public class ScaleTransformer extends Transformer {

	private static final long serialVersionUID = -1212343255691884371L;
	
	public static final Vector3f UP = new Vector3f(2.5f, 2.5f, 2.5f);
	public static final Vector3f DOWN =new Vector3f(0.3f, 0.3f, 0.3f);
	
	public ScaleTransformer(){
		super();
		
	}
	
	public void SetDirection(Vector3f direction){
		
		this.setScale(0, 0, new Vector3f(1, 1, 1));
		this.setScale(0, 2, new Vector3f(1, 1, 1).add(direction.subtract(new Vector3f(1, 1, 1)).divide(2)));
		this.setScale(0, 4, direction);
	
	}
}
