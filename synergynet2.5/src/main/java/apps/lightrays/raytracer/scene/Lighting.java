package apps.lightrays.raytracer.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Lighting {

	List<Light> lights;

	
	public Lighting() {
		lights = new ArrayList<Light>();
	}
	
	public void addLight(Light light) {
		this.lights.add(light);
		updateRelativeBrightness();
	}
	
	private void updateRelativeBrightness() {
		int num_lights = lights.size();
		double total_brightness = 0.0;
		Light l;
		for(int i = 0; i < num_lights; i++) {
			l = (Light)lights.get(i);
			total_brightness += l.getBrightness();
		}
		
		for(int i = 0; i < num_lights; i++) {
			l = (Light)lights.get(i);
			l.setRelativeBrightness(l.getBrightness()/total_brightness);
		}		
	}
	
	public void addLightsToScene(Scene s) {
		Light l;
		for(int i = 0; i < lights.size(); i++) {
			l = (Light)lights.get(i);
			s.addSceneObject(l);
		}
	}
	
	public static Light getDefaultLight(Point lightpoint) {
	    Point center = new Point(lightpoint);		
		return new Light(center);
	}

	public Light getFirstLight() {
		return (Light)lights.get(0);
		
	}
	
	public Iterator<Light> getIterator() {
		return lights.iterator();
	}
}
