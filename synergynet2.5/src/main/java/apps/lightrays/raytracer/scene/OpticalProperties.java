package apps.lightrays.raytracer.scene;

/**
 * The Class OpticalProperties.
 */
public class OpticalProperties {
	
	/** The colour. */
	public Colour colour;

	/** The diffusion. */
	public double diffusion;

	/** The luminous. */
	public boolean luminous;

	/** The reflectiveness. */
	public double reflectiveness;

	/** The refractiveness. */
	public double refractiveness;

	/** The transparency. */
	public double transparency;
	
	/**
	 * Instantiates a new optical properties.
	 */
	public OpticalProperties() {

	}

	/**
	 * Instantiates a new optical properties.
	 *
	 * @param c
	 *            the c
	 * @param refra
	 *            the refra
	 * @param transp
	 *            the transp
	 * @param refle
	 *            the refle
	 * @param diffu
	 *            the diffu
	 * @param lumin
	 *            the lumin
	 */
	public OpticalProperties(Colour c, double refra, double transp,
			double refle, double diffu, boolean lumin) {
		set(c, refra, transp, refle, diffu, lumin);
	}

	/**
	 * Sets the.
	 *
	 * @param c
	 *            the c
	 * @param refra
	 *            the refra
	 * @param transp
	 *            the transp
	 * @param refle
	 *            the refle
	 * @param diffu
	 *            the diffu
	 * @param lumin
	 *            the lumin
	 */
	public void set(Colour c, double refra, double transp, double refle,
			double diffu, boolean lumin) {
		this.colour = c;
		this.refractiveness = refra;
		this.transparency = transp;
		this.reflectiveness = refle;
		this.diffusion = diffu;
		this.luminous = lumin;
	}

	/**
	 * Sets the colour.
	 *
	 * @param c
	 *            the new colour
	 */
	public void setColour(Colour c) {
		this.colour = c;
	}
	
	/**
	 * Sets the values.
	 *
	 * @param refra
	 *            the refra
	 * @param transp
	 *            the transp
	 * @param refle
	 *            the refle
	 * @param diffu
	 *            the diffu
	 * @param lumin
	 *            the lumin
	 */
	public void setValues(double refra, double transp, double refle,
			double diffu, boolean lumin) {
		this.refractiveness = refra;
		this.transparency = transp;
		this.reflectiveness = refle;
		this.diffusion = diffu;
		this.luminous = lumin;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "optics[" + colour + "refr:" + refractiveness + ",transp:"
				+ transparency + ",refl:" + reflectiveness + ",dif:"
				+ diffusion + " lumin:" + luminous + "]";
	}
}
