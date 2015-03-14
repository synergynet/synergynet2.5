package apps.lightrays.raytracer.scene;

import java.awt.Dimension;


public class Camera {
	
	private static final Vector UP = new Vector(0, 1, 0);
	private static final Vector FRONT = new Vector(0, 0, 1);
	private static final Vector LEFT = new Vector(-1, 0, 0);
	
	private Vector up;
	private Vector front;
	private Vector left;
	private Vector eyePosition;
	private double fov;
	
	private double viewport_xsize;
	private double viewport_ysize;
	
	private Vector _dir, upWithScale, rightWithScale;
	private Vector lookat_target;
	
	public Camera() {
		this(new Dimension(640,480));
	}
	
	public Camera(Dimension viewport_size) {
		
		eyePosition = new Vector(0,-5,1);
		
		up = new Vector(UP);
		front=new Vector(FRONT);
		left=new Vector(LEFT);
		
		fov = 40;
		viewport_xsize = viewport_size.getWidth();
		viewport_ysize = viewport_size.getHeight();

		updateVectors();		
		
	}
	
	public void setViewportSize(Dimension size) {
		viewport_xsize = size.getWidth();
		viewport_ysize = size.getHeight();
		updateVectors();
	}
	
	public void setViewpoint(Vector eyePosition)
	{
		this.eyePosition = eyePosition;
		//updateVectors(); // now done in setLookAt
		if(lookat_target != null)
			setLookAt(lookat_target);
	}	
	
	public void setLookAt(Vector focusedPosition)
	{
		this.lookat_target = new Vector(focusedPosition);
		front = focusedPosition.subtractNew(eyePosition);		
		left = front.crossProductNew(UP);
		// should we do something with up here?
		updateVectors();
	}	
	
	public void updateVectors()
	{
		up.normalise();
		left.normalise();
		front.normalise();
		
		double fovFactor = viewport_xsize/viewport_ysize;
		
		_dir = front.multiplyNew(0.5);		
		upWithScale = up.multiplyNew(Math.tan(Math.toRadians(fov/2)));
		rightWithScale = left.multiplyNew(-fovFactor*Math.tan(Math.toRadians(fov/2)));
	}	
	
	public final Ray generateRay(Scene s, double x, double y)
	{	
		double u, v;
		//double mi_x, mi_y;
		
		// 1. Convert integer image coordinates into values in the range [-0.5, 0.5]
		u = (x - viewport_xsize/2.0) / viewport_xsize;
		v = ((viewport_ysize - y - 1) -  viewport_ysize/2.0) / viewport_ysize;
		
		
		Vector dv = upWithScale.multiplyNew(v);
		Vector du = rightWithScale.multiplyNew(u);
		Vector dir = dv.addNew(du).addNew(_dir);

		
		// 3. Build up and return a ray with origin in the eye position and with calculated direction
		Ray ray;
		
		ray = new Ray(s, eyePosition.toPoint(), dir, 0);
		
		return ray;
	}	
	
	public final Ray[][] generateSupersampledRays(Scene s, int x, int y, int side, double f) {
		Ray[][] r = new Ray[side*2+1][side*2+1];
		
		for(int i = -side; i <= side; i++) {
			for(int j = -side; j <=side; j++) {
				int x_index = side + i;
				int y_index = side + j;
				r[x_index][y_index] = generateRay(s, x + i/f, y + j/f);
			}
		}
		
		return r;
	}

	
}
