package apps.lightrays.raytracer.scene;

import apps.lightrays.raytracer.scene.geometry.Sphere;

/**
 * The Class Light.
 */
public class Light extends Sphere {

	/** The brightness. */
	private double brightness = 1.0;

	/** The optics. */
	private OpticalProperties optics;

	/** The relative_brightness. */
	private double relative_brightness = 1.0;

	/**
	 * Instantiates a new light.
	 */
	public Light() {
		super();
	}

	/**
	 * Instantiates a new light.
	 *
	 * @param center
	 *            the center
	 */
	public Light(Point center) {
		super();
		/*
		 * Colour c = new Colour (1.1, 1.1, 0.9); double refr = 0; double transp
		 * = 0; double refl = 0.5; double diffusion = 0.5; double radius = 80;
		 */
		// optics = new OpticalProperties(c, refr, transp, refl, diffusion,
		// true);

		set(center, radius, optics);
	}

	/**
	 * Gets the brightness.
	 *
	 * @return the brightness
	 */
	public double getBrightness() {
		return brightness;
	}

	/**
	 * Gets the colour.
	 *
	 * @return the colour
	 */
	public Colour getColour() {
		return optics.colour;
	}

	/**
	 * Gets the relative brightness.
	 *
	 * @return the relative brightness
	 */
	public double getRelativeBrightness() {
		return relative_brightness;
	}

	/**
	 * Sets the brightness.
	 *
	 * @param b
	 *            the new brightness
	 */
	public void setBrightness(double b) {
		this.brightness = b;
	}

	/**
	 * Sets the relative brightness.
	 *
	 * @param b
	 *            the new relative brightness
	 */
	public void setRelativeBrightness(double b) {
		this.relative_brightness = b;
	}

	/*
	 * (non-Javadoc)
	 * @see apps.lightrays.raytracer.scene.geometry.Sphere#toString()
	 */
	public String toString() {
		return super.toString();
	}
}
