package apps.mtdesktop.desktop.mtmousetest;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.JLabel;

/**
 * The Class ImageLabel.
 */
public class ImageLabel extends JLabel implements MouseMotionListener,
		MouseListener {

	/** The width. */
	private final static int WIDTH = 1024;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1622760377813315620L;

	/** The angle. */
	private double angle = 0;

	// private double scale = 1.0;
	/** The at. */
	private AffineTransform at;

	/** The bi. */
	private BufferedImage bi;

	/** The height. */
	int height = 1024;

	/** The image height. */
	int imageHeight = height / 4;

	/** The image width. */
	int imageWidth = WIDTH / 4;

	/** The is dragged. */
	private boolean isDragged = false;

	/**
	 * Instantiates a new image label.
	 *
	 * @param imageResource
	 *            the image resource
	 */
	public ImageLabel(URL imageResource) {
		super();
		this.setBounds(0, 0, WIDTH, height);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		Image image = getToolkit().getImage(imageResource);
		
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 1);
		try {
			mt.waitForAll();
		} catch (Exception e) {
			System.out.println("Exception while loading image.");
		}
		
		bi = new BufferedImage(imageWidth, imageHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D big = bi.createGraphics();
		big.drawImage(image, 0, 0, imageWidth, imageHeight, this);
		
		at = AffineTransform.getScaleInstance(
				(double) imageWidth / bi.getWidth(),
				(double) imageHeight / bi.getHeight());
	}
	
	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public double getAngle() {
		return angle;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		Component c = e.getComponent();
		c.setLocation((c.getX() + e.getX()) - (imageWidth / 2) - 30,
				(c.getY() + e.getY()) - (imageHeight / 2) - 30);
		c.repaint();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		isDragged = true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		isDragged = false;
		
	}
	
	/**
	 * Notify change.
	 *
	 * @param angle
	 *            the angle
	 * @param scale
	 *            the scale
	 */
	public void notifyChange(double angle, double scale) {
		if (isDragged) {
			this.setAngle(angle);
			this.setScale(scale);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.rotate(angle, bi.getWidth() / 2, bi.getHeight() / 2);
		g2D.drawImage(bi, at, this);
	}
	
	/**
	 * Sets the angle.
	 *
	 * @param angle
	 *            the new angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
		this.repaint();
	}
	
	/**
	 * Sets the scale.
	 *
	 * @param scale
	 *            the new scale
	 */
	public void setScale(double scale) {
		// this.scale = scale;
		at = AffineTransform.getScaleInstance(
				imageWidth * scale / bi.getWidth(),
				imageHeight * scale / bi.getHeight());
	}
}
