package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;

/**
 * The Class DrawFillRectangle.
 */
public class DrawFillRectangle extends DrawData implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6939747148375706784L;
	
	/** The rect. */
	protected Rectangle rect;

	/**
	 * Instantiates a new draw fill rectangle.
	 *
	 * @param rect
	 *            the rect
	 */
	public DrawFillRectangle(Rectangle rect) {
		this.rect = rect;
	}

	/**
	 * Instantiates a new draw fill rectangle.
	 *
	 * @param rect
	 *            the rect
	 * @param color
	 *            the color
	 */
	public DrawFillRectangle(Rectangle rect, Color color) {
		this(rect);
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
		graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
	
	/**
	 * Gets the rectangle.
	 *
	 * @return the rectangle
	 */
	public Rectangle getRectangle() {
		return rect;
	}
}
