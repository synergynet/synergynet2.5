package apps.lightrays.raytracer.scene;

import apps.lightrays.raytracer.scene.geometry.Sphere;

public class Light extends Sphere {
	
	private double brightness = 1.0;
	private double relative_brightness = 1.0;
	private OpticalProperties optics;
	
	public Light() {
		super();
	}
	
	public Light(Point center) {
		super();
		/*
		Colour c = new Colour (1.1, 1.1, 0.9);
	    double refr = 0;
		double transp = 0;
		double refl = 0.5;
		double diffusion = 0.5;
		double radius = 80;
		*/
		//optics = new OpticalProperties(c, refr, transp, refl, diffusion, true);
		
		set(center, radius, optics);
	}
	
	public void setBrightness(double b) {
		this.brightness = b;
	}
	
	public double getBrightness() {
		return brightness;
	}
	
	public double getRelativeBrightness() {
		return relative_brightness;
	}
	
	public void setRelativeBrightness(double b) {
		this.relative_brightness = b;
	}
	
	public Colour getColour() {
		return optics.colour;
	}
	
	public String toString() {
		return super.toString();
	}
}
