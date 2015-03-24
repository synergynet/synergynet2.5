package apps.userdefinedgestures.transform;

import com.jme.animation.SpatialTransformer;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

/**
 * The Class Transformer.
 */
public class Transformer extends SpatialTransformer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1871143255691434371L;

	/** The target object. */
	protected Spatial targetObject;

	/**
	 * Instantiates a new transformer.
	 */
	public Transformer() {
		super(1);
		Spatial targetObject = new Box();
		targetObject.setLocalTranslation(0, 15, 0);
		this.setObject(targetObject);
		this.interpolateMissing();
		this.setSpeed(0.7f);
		this.setRepeatType(1);
	}

	/**
	 * Sets the object.
	 *
	 * @param objChange
	 *            the new object
	 */
	public void setObject(Spatial objChange) {
		super.setObject(objChange, 0, -1);
		this.targetObject = objChange;
		this.setPosition(0, 0, objChange.getLocalTranslation());
		this.setPosition(0, 2, objChange.getLocalTranslation());
		this.setPosition(0, 4, objChange.getLocalTranslation());
	}
	
}
