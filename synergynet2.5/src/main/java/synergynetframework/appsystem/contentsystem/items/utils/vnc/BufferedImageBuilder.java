package synergynetframework.appsystem.contentsystem.items.utils.vnc;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

// import java.awt.image.ImageObserver;

/**
 * The Class BufferedImageBuilder.
 *
 * @author Anthony Eden
 */
public class BufferedImageBuilder {
	
	/**
	 * The Class ImageLoadStatus.
	 */
	class ImageLoadStatus {
		
		/** The height done. */
		public boolean heightDone = false;
		
		/** The width done. */
		public boolean widthDone = false;
	}
	
	/** The Constant DEFAULT_IMAGE_TYPE. */
	private static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;
	
	/**
	 * Buffer image.
	 *
	 * @param image
	 *            the image
	 * @return the buffered image
	 */
	public BufferedImage bufferImage(Image image) {
		return bufferImage(image, DEFAULT_IMAGE_TYPE);
	}
	
	// private void waitForImage(BufferedImage bufferedImage) {
	// final ImageLoadStatus imageLoadStatus = new ImageLoadStatus();
	// bufferedImage.getHeight(new ImageObserver() {
	// public boolean imageUpdate(Image img, int infoflags, int x, int y, int
	// width, int height) {
	// if (infoflags == ALLBITS) {
	// imageLoadStatus.heightDone = true;
	// return true;
	// }
	// return false;
	// }
	// });
	// bufferedImage.getWidth(new ImageObserver() {
	// public boolean imageUpdate(Image img, int infoflags, int x, int y, int
	// width, int height) {
	// if (infoflags == ALLBITS) {
	// imageLoadStatus.widthDone = true;
	// return true;
	// }
	// return false;
	// }
	// });
	// while (!imageLoadStatus.widthDone && !imageLoadStatus.heightDone) {
	// try {
	// Thread.sleep(300);
	// } catch (InterruptedException e) {
	//
	// }
	// }
	// }
	
	/**
	 * Buffer image.
	 *
	 * @param image
	 *            the image
	 * @param type
	 *            the type
	 * @return the buffered image
	 */
	public BufferedImage bufferImage(Image image, int type) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), type);
		Graphics2D g = bufferedImage.createGraphics();
		g.drawImage(image, null, null);
		// waitForImage(bufferedImage);
		return bufferedImage;
	}
	
}
