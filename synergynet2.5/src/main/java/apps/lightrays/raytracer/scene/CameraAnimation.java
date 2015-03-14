package apps.lightrays.raytracer.scene;


public class CameraAnimation {

	public Vector startEyePosition;
	public Vector endEyePosition;
	public int frames;
	
	public CameraAnimation() {
		
	}
	
	
	public Vector getCameraPositionForFrame(int frame) {
		Vector v = new Vector();
		
		
		double dx = endEyePosition.x - startEyePosition.x;
		double dy = endEyePosition.y - startEyePosition.y;
		double dz = endEyePosition.z - startEyePosition.z;
		
		double f = ((double) frame / (double)frames);
		
		dx *= f;
		dy *= f;
		dz *= f;

		v.x = startEyePosition.x + dx;
		v.y = startEyePosition.y + dy;
		v.z = startEyePosition.z + dz;
		
		return v;		
	}
	
}
