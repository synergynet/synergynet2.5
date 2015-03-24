package apps.lightrays.raytracer.scene;

/**
 * The Class Point.
 */
public class Point {

	/** The x. */
	public double x;

	/** The y. */
	public double y;

	/** The z. */
	public double z;

	/**
	 * Instantiates a new point.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Instantiates a new point.
	 *
	 * @param p
	 *            the p
	 */
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

	/**
	 * Adds the.
	 *
	 * @param p
	 *            the p
	 */
	public void add(Point p) {
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "x:" + x + " y:" + y + " z:" + z;
	}
}
