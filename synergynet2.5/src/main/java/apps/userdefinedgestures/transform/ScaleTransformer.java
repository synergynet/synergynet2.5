package apps.userdefinedgestures.transform;

import com.jme.math.Vector3f;

/**
 * The Class ScaleTransformer.
 */
public class ScaleTransformer extends Transformer {
	
	/** The Constant DOWN. */
	public static final Vector3f DOWN = new Vector3f(0.3f, 0.3f, 0.3f);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1212343255691884371L;

	/** The Constant UP. */
	public static final Vector3f UP = new Vector3f(2.5f, 2.5f, 2.5f);

	/**
	 * Instantiates a new scale transformer.
	 */
	public ScaleTransformer() {
		super();

	}

	/**
	 * Sets the direction.
	 *
	 * @param direction
	 *            the direction
	 */
	public void SetDirection(Vector3f direction) {

		this.setScale(0, 0, new Vector3f(1, 1, 1));
		this.setScale(
				0,
				2,
				new Vector3f(1, 1, 1).add(direction.subtract(
						new Vector3f(1, 1, 1)).divide(2)));
		this.setScale(0, 4, direction);
		
	}
}
