package apps.userdefinedgestures.transform;

import com.jme.math.Vector3f;


/**
 * The Class TranslationTransformer.
 */
public class TranslationTransformer extends Transformer {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1233343255691434371L;
	
	/** The Constant RIGHT. */
	public static final Vector3f RIGHT = new Vector3f(30, 0, 0);
	
	/** The Constant UP. */
	public static final Vector3f UP = new Vector3f(0, 30, 0);
	
	/** The Constant INSIDE. */
	public static final Vector3f INSIDE = new Vector3f(0, 0, -30);
	
	/** The Constant TOPRIGHTINSIDE. */
	public static final Vector3f TOPRIGHTINSIDE = new Vector3f(10, 12, -40);
	
	/** The Constant TOPRIGHTOUTSIDE. */
	public static final Vector3f TOPRIGHTOUTSIDE = new Vector3f(10, 12, 40);

	
	/**
	 * Instantiates a new translation transformer.
	 */
	public TranslationTransformer(){
		super();

	}
	
	/**
	 * Sets the target position.
	 *
	 * @param targetPosition the target position
	 */
	public void SetTargetPosition(Vector3f targetPosition){
		this.setPosition(0, 0, this.targetObject.getLocalTranslation());
		this.setPosition(0, 2, this.targetObject.getLocalTranslation().add(targetPosition.divide(2)));
		this.setPosition(0, 4, this.targetObject.getLocalTranslation().add(targetPosition));
	}

}
