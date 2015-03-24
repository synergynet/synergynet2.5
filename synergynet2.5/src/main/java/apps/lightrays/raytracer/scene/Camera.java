package apps.lightrays.raytracer.scene;

import java.awt.Dimension;

/**
 * The Class Camera.
 */
public class Camera {

	/** The Constant FRONT. */
	private static final Vector FRONT = new Vector(0, 0, 1);

	/** The Constant LEFT. */
	private static final Vector LEFT = new Vector(-1, 0, 0);

	/** The Constant UP. */
	private static final Vector UP = new Vector(0, 1, 0);

	/** The right with scale. */
	private Vector _dir, upWithScale, rightWithScale;

	/** The eye position. */
	private Vector eyePosition;

	/** The fov. */
	private double fov;

	/** The front. */
	private Vector front;

	/** The left. */
	private Vector left;

	/** The lookat_target. */
	private Vector lookat_target;

	/** The up. */
	private Vector up;

	/** The viewport_xsize. */
	private double viewport_xsize;

	/** The viewport_ysize. */
	private double viewport_ysize;

	/**
	 * Instantiates a new camera.
	 */
	public Camera() {
		this(new Dimension(640, 480));
	}

	/**
	 * Instantiates a new camera.
	 *
	 * @param viewport_size
	 *            the viewport_size
	 */
	public Camera(Dimension viewport_size) {

		eyePosition = new Vector(0, -5, 1);

		up = new Vector(UP);
		front = new Vector(FRONT);
		left = new Vector(LEFT);

		fov = 40;
		viewport_xsize = viewport_size.getWidth();
		viewport_ysize = viewport_size.getHeight();
		
		updateVectors();
		
	}

	/**
	 * Generate ray.
	 *
	 * @param s
	 *            the s
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the ray
	 */
	public final Ray generateRay(Scene s, double x, double y) {
		double u, v;
		// double mi_x, mi_y;

		// 1. Convert integer image coordinates into values in the range [-0.5,
		// 0.5]
		u = (x - (viewport_xsize / 2.0)) / viewport_xsize;
		v = ((viewport_ysize - y - 1) - (viewport_ysize / 2.0))
				/ viewport_ysize;
		
		Vector dv = upWithScale.multiplyNew(v);
		Vector du = rightWithScale.multiplyNew(u);
		Vector dir = dv.addNew(du).addNew(_dir);
		
		// 3. Build up and return a ray with origin in the eye position and with
		// calculated direction
		Ray ray;

		ray = new Ray(s, eyePosition.toPoint(), dir, 0);

		return ray;
	}

	/**
	 * Generate supersampled rays.
	 *
	 * @param s
	 *            the s
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param side
	 *            the side
	 * @param f
	 *            the f
	 * @return the ray[][]
	 */
	public final Ray[][] generateSupersampledRays(Scene s, int x, int y,
			int side, double f) {
		Ray[][] r = new Ray[(side * 2) + 1][(side * 2) + 1];

		for (int i = -side; i <= side; i++) {
			for (int j = -side; j <= side; j++) {
				int x_index = side + i;
				int y_index = side + j;
				r[x_index][y_index] = generateRay(s, x + (i / f), y + (j / f));
			}
		}

		return r;
	}
	
	/**
	 * Sets the look at.
	 *
	 * @param focusedPosition
	 *            the new look at
	 */
	public void setLookAt(Vector focusedPosition) {
		this.lookat_target = new Vector(focusedPosition);
		front = focusedPosition.subtractNew(eyePosition);
		left = front.crossProductNew(UP);
		// should we do something with up here?
		updateVectors();
	}
	
	/**
	 * Sets the viewpoint.
	 *
	 * @param eyePosition
	 *            the new viewpoint
	 */
	public void setViewpoint(Vector eyePosition) {
		this.eyePosition = eyePosition;
		// updateVectors(); // now done in setLookAt
		if (lookat_target != null) {
			setLookAt(lookat_target);
		}
	}
	
	/**
	 * Sets the viewport size.
	 *
	 * @param size
	 *            the new viewport size
	 */
	public void setViewportSize(Dimension size) {
		viewport_xsize = size.getWidth();
		viewport_ysize = size.getHeight();
		updateVectors();
	}
	
	/**
	 * Update vectors.
	 */
	public void updateVectors() {
		up.normalise();
		left.normalise();
		front.normalise();

		double fovFactor = viewport_xsize / viewport_ysize;

		_dir = front.multiplyNew(0.5);
		upWithScale = up.multiplyNew(Math.tan(Math.toRadians(fov / 2)));
		rightWithScale = left.multiplyNew(-fovFactor
				* Math.tan(Math.toRadians(fov / 2)));
	}
	
}
