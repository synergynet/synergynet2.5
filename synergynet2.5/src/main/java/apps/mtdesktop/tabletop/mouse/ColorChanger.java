package apps.mtdesktop.tabletop.mouse;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The Class ColorChanger.
 */
public class ColorChanger {
	
	/** The Constant ALPHA. */
	public static final int ALPHA = 0;
	
	/** The Constant BLUE. */
	public static final int BLUE = 3;
	
	/** The Constant BRIGHTNESS. */
	public static final int BRIGHTNESS = 2;
	
	/** The Constant GREEN. */
	public static final int GREEN = 2;
	
	/** The Constant HUE. */
	public static final int HUE = 0;
	
	/** The Constant RED. */
	public static final int RED = 1;
	
	/** The Constant SATURATION. */
	public static final int SATURATION = 1;
	
	/** The Constant TRANSPARENT. */
	public static final int TRANSPARENT = 0;
	
	/**
	 * Change color.
	 *
	 * @param image
	 *            the image
	 * @param mask
	 *            the mask
	 * @param replacement
	 *            the replacement
	 * @return the buffered image
	 */
	public static BufferedImage changeColor(BufferedImage image, Color mask,
			Color replacement) {
		BufferedImage destImage = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = destImage.createGraphics();
		g.drawImage(image, null, 0, 0);
		g.dispose();
		
		for (int i = 0; i < destImage.getWidth(); i++) {
			for (int j = 0; j < destImage.getHeight(); j++) {
				
				int destRGB = destImage.getRGB(i, j);
				if (matches(mask.getRGB(), destRGB)) {
					int rgbnew = getNewPixelRGB(replacement.getRGB(), destRGB);
					destImage.setRGB(i, j, rgbnew);
				}
			}
		}
		
		return destImage;
	}
	
	/**
	 * Gets the HSB array.
	 *
	 * @param rgb
	 *            the rgb
	 * @return the HSB array
	 */
	private static float[] getHSBArray(int rgb) {
		int[] rgbArr = getRGBArray(rgb);
		return Color.RGBtoHSB(rgbArr[RED], rgbArr[GREEN], rgbArr[BLUE], null);
	}
	
	/**
	 * Gets the new pixel rgb.
	 *
	 * @param replacement
	 *            the replacement
	 * @param destRGB
	 *            the dest rgb
	 * @return the new pixel rgb
	 */
	private static int getNewPixelRGB(int replacement, int destRGB) {
		float[] destHSB = getHSBArray(destRGB);
		float[] replHSB = getHSBArray(replacement);
		
		int rgbnew = Color.HSBtoRGB(replHSB[HUE], replHSB[SATURATION],
				destHSB[BRIGHTNESS]);
		return rgbnew;
	}
	
	/**
	 * Gets the RGB array.
	 *
	 * @param rgb
	 *            the rgb
	 * @return the RGB array
	 */
	private static int[] getRGBArray(int rgb) {
		return new int[] { (rgb >> 24) & 0xff, (rgb >> 16) & 0xff,
				(rgb >> 8) & 0xff, rgb & 0xff };
	}
	
	/**
	 * Matches.
	 *
	 * @param maskRGB
	 *            the mask rgb
	 * @param destRGB
	 *            the dest rgb
	 * @return true, if successful
	 */
	private static boolean matches(int maskRGB, int destRGB) {
		float[] hsbMask = getHSBArray(maskRGB);
		float[] hsbDest = getHSBArray(destRGB);
		
		if ((hsbMask[HUE] == hsbDest[HUE])
				&& (hsbMask[SATURATION] == hsbDest[SATURATION])
				&& (getRGBArray(destRGB)[ALPHA] != TRANSPARENT)) {
			
			return true;
		}
		return false;
	}
}