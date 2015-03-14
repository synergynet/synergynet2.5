package apps.lightrays.raytracer.scene;

public class Point {
	public double x;
	public double y;
	public double z;
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}
	
	public void add(Point p) {
		this.x += p.x;
		this.y += p.y;
		this.z += p.z;
	}
	
	public String toString() {
		return "x:" + x + " y:" + y + " z:" + z;
	}
}
