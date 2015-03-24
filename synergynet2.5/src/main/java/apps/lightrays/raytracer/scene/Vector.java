package apps.lightrays.raytracer.scene;

/**
 * The Class Vector.
 */
public class Vector {

	/** The Constant EPSILON. */
	public static final double EPSILON = 0.00001;

	/** The x. */
	public double x;

	/** The y. */
	public double y;

	/** The z. */
	public double z;

	/**
	 * Instantiates a new vector.
	 */
	public Vector() {
		set(0.0, 0.0, 0.0);
	}
	
	/**
	 * Instantiates a new vector.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public Vector(double x, double y, double z) {
		set(x, y, z);
	}

	/**
	 * Instantiates a new vector.
	 *
	 * @param p
	 *            the p
	 * @param origin
	 *            the origin
	 */
	public Vector(Point p, Point origin) {
		set(p.x - origin.x, p.y - origin.y, p.z - origin.z);
	}

	/**
	 * Instantiates a new vector.
	 *
	 * @param v
	 *            the v
	 */
	public Vector(Vector v) {
		set(v.x, v.y, v.z);
	}

	/**
	 * Adds the.
	 *
	 * @param v
	 *            the v
	 */
	public void add(Vector v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}

	/**
	 * Adds the new.
	 *
	 * @param v
	 *            the v
	 * @return the vector
	 */
	public Vector addNew(Vector v) {
		return new Vector(x + v.x, y + v.y, z + v.z);
	}

	/**
	 * Cross product.
	 *
	 * @param v
	 *            the v
	 */
	public void crossProduct(Vector v) {
		double xh = (y * v.z) - (v.y * z);
		double yh = (z * v.x) - (v.z * x);
		double zh = (x * v.y) - (v.x * y);
		x = xh;
		y = yh;
		z = zh;
	}

	/**
	 * Cross product new.
	 *
	 * @param v
	 *            the v
	 * @return the vector
	 */
	public Vector crossProductNew(Vector v) {
		double xh = (y * v.z) - (v.y * z);
		double yh = (z * v.x) - (v.z * x);
		double zh = (x * v.y) - (v.x * y);
		return new Vector(xh, yh, zh);
	}

	/**
	 * Dotproduct.
	 *
	 * @param v
	 *            the v
	 * @return the double
	 */
	public double dotproduct(Vector v) {
		return (v.x * x) + (v.y * y) + (v.z * z);
	}

	/**
	 * Length.
	 *
	 * @return the double
	 */
	public double length() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
	}

	/**
	 * Multiply new.
	 *
	 * @param a
	 *            the a
	 * @return the vector
	 */
	public Vector multiplyNew(double a) {
		return new Vector(x * a, y * a, z * a);
	}
	
	/**
	 * Nonzero.
	 *
	 * @return true, if successful
	 */
	public boolean nonzero() {
		return (this.x != 0) || (this.y != 0) || (this.z != 0);
	}

	/**
	 * Normalise.
	 */
	public void normalise() {
		double f = 1.0d / length();
		if (f > EPSILON) {
			scale(f);
		} else {
			set(0.0, 0.0, 0.0);
		}
	}

	/**
	 * Scale.
	 *
	 * @param a
	 *            the a
	 */
	public void scale(double a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;
	}

	/**
	 * Sets the.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Subtract.
	 *
	 * @param v
	 *            the v
	 */
	public void subtract(Vector v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}
	
	/**
	 * Subtract new.
	 *
	 * @param v
	 *            the v
	 * @return the vector
	 */
	public Vector subtractNew(Vector v) {
		return new Vector(x - v.x, y - v.y, z - v.z);
	}

	/**
	 * To point.
	 *
	 * @return the point
	 */
	public Point toPoint() {
		return new Point(x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return toPoint().toString();
	}

	/**
	 * Unary minus.
	 */
	public void unaryMinus() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}
}
