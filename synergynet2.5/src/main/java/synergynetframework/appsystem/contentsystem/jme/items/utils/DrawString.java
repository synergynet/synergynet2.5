package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

/**
 * The Class DrawString.
 */
public class DrawString extends DrawData implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6939747148375706784L;
	
	/** The start point. */
	private Point startPoint;

	/** The text. */
	private String text;

	/**
	 * Instantiates a new draw string.
	 *
	 * @param text
	 *            the text
	 * @param startPoint
	 *            the start point
	 */
	public DrawString(String text, Point startPoint) {
		this.startPoint = startPoint;
		this.text = text;
	}

	/**
	 * Instantiates a new draw string.
	 *
	 * @param text
	 *            the text
	 * @param startPoint
	 *            the start point
	 * @param color
	 *            the color
	 */
	public DrawString(String text, Point startPoint, Color color) {
		this(text, startPoint);
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData#
	 * draw(java.awt.Graphics2D)
	 */
	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		graphics.drawString(text, startPoint.x, startPoint.y);
	}
	
	/**
	 * Gets the start point.
	 *
	 * @return the start point
	 */
	public Point getStartPoint() {
		return startPoint;
	}
}
