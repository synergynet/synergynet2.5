package apps.lightrays.raytracer.scene;

import java.util.ArrayList;
import java.util.List;


public class Scene {
	
	private Light light;
	
	private int max_depth = 4;
	
	private List<SceneObject> objs = new ArrayList<SceneObject>();
	private Lighting lights;
	private Colour ambient_colour;
	
	public Scene() {		
	}

	public void addSceneObject(SceneObject obj) {
		obj.setScene(this);
		objs.add(obj);
	}
	
	public List<SceneObject> getSceneObjects() {		
		return objs;
	}
	
	public void setLight(Point lightpoint) {	
	    Point center = new Point(lightpoint);		
		light = new Light(center);
		addSceneObject(light);		
	}
	
	public void setMaxDepth(int max_depth) {
		this.max_depth = max_depth;
	}
	
	public int getMaxDepth() {
		return max_depth;
	}

	public Colour getAmbientColour() {
		return ambient_colour;
	}

	public void setAmbientColour(Colour ambient_colour) {
		this.ambient_colour = ambient_colour;
	}
	
	public String toString() {
		return "scene[" + light + " " + max_depth + ", " + ambient_colour + "," + objs + ", " + lights +  "]";
	}

	public void setLighting(Lighting lighting) {
		this.lights = lighting;
		
	}
	
	public Lighting getLighting() {
		return lights;
	}
}
