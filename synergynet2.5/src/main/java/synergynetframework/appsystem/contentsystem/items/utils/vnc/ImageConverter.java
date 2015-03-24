package synergynetframework.appsystem.contentsystem.items.utils.vnc;

// import java.awt.image.ImageObserver;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
// import java.awt.Graphics;
// import java.awt.image.DataBufferByte;
// import java.awt.image.WritableRaster;
// import java.awt.GraphicsConfiguration;
// import java.awt.GraphicsDevice;
// import java.awt.GraphicsEnvironment;
import java.awt.image.PixelGrabber;

// import java.util.ArrayList;

/**
 * The Class ImageConverter.
 */
public class ImageConverter extends Component {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Convert int array to byte array.
	 *
	 * @param integers
	 *            the integers
	 * @return the byte[]
	 */
	private static byte[] convertIntArrayToByteArray(int[] integers) {
		byte[] bytes = new byte[integers.length * 4];
		for (int index = 0; index < integers.length; index++) {
			byte[] integerBytes = convertIntToByteArray(integers[index]);
			bytes[index * 4] = integerBytes[0];
			bytes[1 + (index * 4)] = integerBytes[1];
			bytes[2 + (index * 4)] = integerBytes[2];
			bytes[3 + (index * 4)] = integerBytes[3];
			
		}
		return bytes;
		
	}
	
	/**
	 * Convert int to byte array.
	 *
	 * @param integer
	 *            the integer
	 * @return the byte[]
	 */
	private static byte[] convertIntToByteArray(int integer) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (integer >> 24);
		bytes[1] = (byte) ((integer << 8) >> 24);
		bytes[2] = (byte) ((integer << 16) >> 24);
		bytes[3] = (byte) ((integer << 24) >> 24);
		return bytes;
		
	}
	
	// private BufferedImage convert() {
	// /* * Have to wait for image to load. */
	// try
	// {
	// this.mediaTracker.waitForID(0);
	//
	// }catch(InterruptedException e) { }
	// System.out.println("-1");
	// GraphicsConfiguration graphicsConfig =
	// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	// BufferedImage bimage =
	// graphicsConfig.createCompatibleImage(this.image.getWidth(null),this.image.getHeight(null));
	// System.out.println("-2");
	// Graphics g = bimage.getGraphics();
	// g.drawImage(image, 0, 0, null);
	// return bimage;
	//
	// }
	
	/**
	 * Convert to bytes.
	 *
	 * @param image
	 *            the image
	 * @return the byte[]
	 */
	public static byte[] convertToBytes(Image image) {
		// ImageConverter converter = new ImageConverter(image);
		// BufferedImage bufferedImage = converter.convert();
		PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0,
				image.getWidth(null), image.getHeight(null), true);
		try {
			if (pixelGrabber.grabPixels()) {
				Object pixels = pixelGrabber.getPixels();
				if (pixels instanceof byte[]) {
					return (byte[]) pixels;
					
				}
				return convertIntArrayToByteArray((int[]) pixels);
			}
			
		} catch (InterruptedException e) {
		}
		return null;
		
	}
	
	// private static int convertByteArrayToInt(byte[] bytes) {
	//
	// return (bytes[0] << 32) | (bytes[1] << 24) | (bytes[2] << 16) | (bytes[3]
	// << 8) | bytes[4];
	//
	// }
	
	/**
	 * To buffered image.
	 *
	 * @param src
	 *            the src
	 * @return the buffered image
	 */
	public static BufferedImage toBufferedImage(Image src) {
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		int type = BufferedImage.TYPE_INT_RGB; // other options
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return dest;
	}
	
	// private static int[] convertByteArrayToIntArray(byte[] bytes) {
	// ArrayList<Integer> integers = new ArrayList<Integer>();
	// for (int index = 0; index < bytes.length; index += 4)
	// {
	// byte[] fourBytes = new byte[4];
	// fourBytes[0] = bytes[index];
	// fourBytes[1] = bytes[index+1];
	// fourBytes[2] = bytes[index+2];
	// fourBytes[3] = bytes[index+3];
	// int integer = convertByteArrayToInt(fourBytes);
	// integers.add(new Integer(integer));
	//
	// }
	// int[] ints = new int[bytes.length/4];
	// for (int index = 0; index < integers.size() ; index++)
	// {
	// ints[index] = ((Integer)integers.get(index)).intValue();
	//
	// }
	// return ints;
	//
	// }
	
	/** The media tracker. */
	private MediaTracker mediaTracker;
	
	// private Image image;
	/**
	 * Instantiates a new image converter.
	 *
	 * @param image
	 *            the image
	 */
	private ImageConverter(Image image) {
		super();
		this.mediaTracker = new MediaTracker(this);
		this.mediaTracker.addImage(image, 0);
		// this.image = image;
	}

}