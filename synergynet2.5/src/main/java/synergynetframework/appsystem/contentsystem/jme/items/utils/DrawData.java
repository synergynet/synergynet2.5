package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

/**
 * The Class DrawData.
 */
public abstract class DrawData implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2957295980875244718L;
	
	/** The color. */
	protected Color color;

	/** The cursor id. */
	protected long cursorId;

	/** The width. */
	protected float width;
	
	/**
	 * Clear.
	 *
	 * @param graphics
	 *            the graphics
	 */
	public void clear(Graphics2D graphics) {
	}

	/**
	 * Draw.
	 *
	 * @param graphics
	 *            the graphics
	 */
	public void draw(Graphics2D graphics) {
		if (color != null) {
			graphics.setColor(color);
		}
		if (width != 0) {
			graphics.setStroke(new BasicStroke(width));
		}
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the cursor id.
	 *
	 * @return the cursor id
	 */
	public long getCursorId() {
		return cursorId;
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Sets the color.
	 *
	 * @param color
	 *            the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Sets the cursor id.
	 *
	 * @param cursorId
	 *            the new cursor id
	 */
	public void setCursorId(long cursorId) {
		this.cursorId = cursorId;
	}

	/**
	 * Sets the width.
	 *
	 * @param width
	 *            the new width
	 */
	public void setWidth(float width) {
		this.width = width;
	}
}
