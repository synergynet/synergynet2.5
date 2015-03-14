package apps.lightrays.raytracer.scene;


public class Vector {
	
	public static final double EPSILON = 0.00001;
	
	public double x;
	public double y;
	public double z;
	
	public Vector() {
		set(0.0, 0.0, 0.0);
	}

	public Vector(double x, double y, double z) {
		set(x, y, z);
	}
	
	public Vector(Vector v) {
		set(v.x, v.y, v.z);
	}
	
	public Vector(Point p, Point origin) {
		set(p.x - origin.x, p.y - origin.y, p.z - origin.z);
	}
	
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double length() {
		return Math.sqrt((x * x) + (y * y) + (z * z));
	}
	
	public void add(Vector v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}
	
	public void scale(double a) {
		this.x *= a;
		this.y *= a;
		this.z *= a;
	}
	
	public Vector multiplyNew(double a) {
		return new Vector(x*a, y*a, z*a);
	}
	
	public Vector addNew(Vector v) {
		return new Vector(x+v.x, y+v.y, z+v.z);
	}
	
	public Vector subtractNew(Vector v) {
		return new Vector(x-v.x, y-v.y, z-v.z);
	}	
	
	public void subtract(Vector v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}
	
	public void unaryMinus() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}
	
	public double dotproduct(Vector v) {
		return (v.x * x) + (v.y * y) + (v.z * z);
	}
	
	public void crossProduct(Vector v) {
		double xh = y * v.z - v.y * z;
		double yh = z * v.x - v.z * x;
		double zh = x * v.y - v.x * y;
		x = xh;
		y = yh;
		z = zh;		
	}

	public Vector crossProductNew(Vector v) {
		double xh = y * v.z - v.y * z;
		double yh = z * v.x - v.z * x;
		double zh = x * v.y - v.x * y;
		return new Vector(xh, yh, zh);
	}	
	
	public void normalise() {
		double f = 1.0d/length();
		if(f > EPSILON)
			scale(f);
		else
			set(0.0, 0.0, 0.0);
	}
	
	public boolean nonzero() {
		return this.x != 0 || this.y != 0 || this.z != 0;
	}
	
	public Point toPoint() {
		return new Point(x, y, z);
	}
	
	public String toString() {
		return toPoint().toString();
	}
}
