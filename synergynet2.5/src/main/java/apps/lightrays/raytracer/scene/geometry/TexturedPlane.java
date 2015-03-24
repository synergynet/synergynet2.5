package apps.lightrays.raytracer.scene.geometry;

import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.OpticalProperties;
import apps.lightrays.raytracer.scene.Point;
import apps.lightrays.raytracer.scene.Vector;

/**
 * The Class TexturedPlane.
 */
public class TexturedPlane extends Plane {
	
	/** The cos_inv. */
	double cos_inv;
	
	/** The height. */
	int height;
	
	// the tile
	/** The pixels. */
	int pixels[];
	
	/** The width. */
	int width;
	
	/**
	 * Instantiates a new textured plane.
	 *
	 * @param center
	 *            the center
	 * @param perpendicular
	 *            the perpendicular
	 * @param prop
	 *            the prop
	 * @param pixels
	 *            the pixels
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public TexturedPlane(Point center, Vector perpendicular,
			OpticalProperties prop, int pixels[], int width, int height) {
		super(center, perpendicular, prop);
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * apps.lightrays.raytracer.scene.geometry.Plane#getPixel(apps.lightrays
	 * .raytracer.scene.Point)
	 */
	public Colour getPixel(Point p) {
		// texture mapping
		// convert to local plane coordinates
		int x = (int) Math.ceil(p.x * cos_inv);
		int y = (int) Math.ceil(p.y);
		// divide them modulo tile size
		x %= width;
		if (x < 0) {
			x += width;
		}
		y %= height;
		if (y < 0) {
			y += height;
		}
		// find the corresponding pixel in the tile
		int rgb = pixels[(y * width) + x];
		return new Colour(rgb);
	}
	
	/*
	 * (non-Javadoc)
	 * @see apps.lightrays.raytracer.scene.geometry.Plane#toString()
	 */
	public String toString() {
		return "TEXTUREDPLANE";
	}
}
