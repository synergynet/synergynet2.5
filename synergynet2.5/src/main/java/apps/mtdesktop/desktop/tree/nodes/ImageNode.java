package apps.mtdesktop.desktop.tree.nodes;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The Class ImageNode.
 */
public class ImageNode extends AssetNode {
	
	/** The icon height. */
	private static int ICON_HEIGHT = 30;

	/** The icon width. */
	private static int ICON_WIDTH = 40;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7063668231614709939L;

	/** The img. */
	protected Image img;

	/**
	 * Instantiates a new image node.
	 *
	 * @param assetFile
	 *            the asset file
	 */
	public ImageNode(File assetFile) {
		super(assetFile);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Instantiates a new image node.
	 *
	 * @param img
	 *            the img
	 */
	public ImageNode(Image img) {
		super(null);
		this.img = img;
	}

	/*
	 * (non-Javadoc)
	 * @see apps.mtdesktop.desktop.tree.nodes.AssetNode#getIcon()
	 */
	public Icon getIcon() {
		try {
			ImageIcon imgIcon = null;
			if (assetFile != null) {
				imgIcon = new ImageIcon(assetFile.toURI().toURL());
			} else if (img != null) {
				imgIcon = new ImageIcon(img);
			}
			BufferedImage bi = new BufferedImage(ICON_WIDTH, ICON_HEIGHT,
					BufferedImage.TYPE_INT_ARGB);
			Graphics g = bi.createGraphics();
			g.drawImage(imgIcon.getImage(), 0, 0, ICON_WIDTH, ICON_HEIGHT, null);
			return new ImageIcon(bi);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public Image getImage() {
		return img;
	}

	/**
	 * Sets the image.
	 *
	 * @param img
	 *            the new image
	 */
	public void setImage(Image img) {
		this.img = img;
	}
}