package apps.lightrays.raytracer.scene;

/**
 * The Class CameraAnimation.
 */
public class CameraAnimation {
	
	/** The end eye position. */
	public Vector endEyePosition;

	/** The frames. */
	public int frames;

	/** The start eye position. */
	public Vector startEyePosition;

	/**
	 * Instantiates a new camera animation.
	 */
	public CameraAnimation() {

	}
	
	/**
	 * Gets the camera position for frame.
	 *
	 * @param frame
	 *            the frame
	 * @return the camera position for frame
	 */
	public Vector getCameraPositionForFrame(int frame) {
		Vector v = new Vector();
		
		double dx = endEyePosition.x - startEyePosition.x;
		double dy = endEyePosition.y - startEyePosition.y;
		double dz = endEyePosition.z - startEyePosition.z;

		double f = ((double) frame / (double) frames);

		dx *= f;
		dy *= f;
		dz *= f;
		
		v.x = startEyePosition.x + dx;
		v.y = startEyePosition.y + dy;
		v.z = startEyePosition.z + dz;

		return v;
	}

}
