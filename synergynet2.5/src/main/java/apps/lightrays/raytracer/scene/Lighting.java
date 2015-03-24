package apps.lightrays.raytracer.scene;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * The Class Lighting.
 */
public class Lighting {

	/** The lights. */
	List<Light> lights;

	
	/**
	 * Instantiates a new lighting.
	 */
	public Lighting() {
		lights = new ArrayList<Light>();
	}
	
	/**
	 * Adds the light.
	 *
	 * @param light the light
	 */
	public void addLight(Light light) {
		this.lights.add(light);
		updateRelativeBrightness();
	}
	
	/**
	 * Update relative brightness.
	 */
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
	
	/**
	 * Adds the lights to scene.
	 *
	 * @param s the s
	 */
	public void addLightsToScene(Scene s) {
		Light l;
		for(int i = 0; i < lights.size(); i++) {
			l = (Light)lights.get(i);
			s.addSceneObject(l);
		}
	}
	
	/**
	 * Gets the default light.
	 *
	 * @param lightpoint the lightpoint
	 * @return the default light
	 */
	public static Light getDefaultLight(Point lightpoint) {
	    Point center = new Point(lightpoint);		
		return new Light(center);
	}

	/**
	 * Gets the first light.
	 *
	 * @return the first light
	 */
	public Light getFirstLight() {
		return (Light)lights.get(0);
		
	}
	
	/**
	 * Gets the iterator.
	 *
	 * @return the iterator
	 */
	public Iterator<Light> getIterator() {
		return lights.iterator();
	}
}
