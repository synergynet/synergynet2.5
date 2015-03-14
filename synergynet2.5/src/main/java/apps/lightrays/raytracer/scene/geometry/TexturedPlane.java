package apps.lightrays.raytracer.scene.geometry;

import apps.lightrays.raytracer.scene.Colour;
import apps.lightrays.raytracer.scene.OpticalProperties;
import apps.lightrays.raytracer.scene.Point;
import apps.lightrays.raytracer.scene.Vector;

public class TexturedPlane extends Plane {
		
		// the tile
		int pixels[];
		int width;
		int height;
		
		double  cos_inv;
		
		public TexturedPlane (Point center, Vector perpendicular, OpticalProperties prop, int pixels [], int width, int height)
		{
			super(center, perpendicular, prop);
			this.width = width;
			this.height = height;
			this.pixels = pixels;
		}
		
		public Colour getPixel(Point p)
		{
			// texture mapping
			// convert to local plane coordinates
			int x = (int)Math.ceil (p.x * cos_inv);
			int y = (int)Math.ceil (p.y);
			// divide them modulo tile size
			x %= width;
			if (x < 0)
				x += width;
			y %= height;
			if (y < 0)
				y += height;
			// find the corresponding pixel in the tile
			int rgb = pixels [y * width + x];
			return new Colour(rgb);
		}
		
		public String toString() {
			return "TEXTUREDPLANE";
		}
}
