package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

/**
 * The Class DrawLine.
 */
public class DrawLine extends DrawData implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6939747148375706784L;
	
	/** The end point. */
	private Point startPoint, endPoint;

	/**
	 * Instantiates a new draw line.
	 *
	 * @param cursorId
	 *            the cursor id
	 * @param startPoint
	 *            the start point
	 * @param endPoint
	 *            the end point
	 * @param color
	 *            the color
	 * @param width
	 *            the width
	 */
	public DrawLine(long cursorId, Point startPoint, Point endPoint,
			Color color, float width) {
		super.cursorId = cursorId;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
		this.width = width;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData#
	 * clear(java.awt.Graphics2D)
	 */
	@Override
	public void clear(Graphics2D graphics) {
		super.clear(graphics);
		graphics.drawLine(this.getStartPoint().x, this.getStartPoint().y,
				this.getEndPoint().x, this.getEndPoint().y);
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
		graphics.drawLine(this.getStartPoint().x, this.getStartPoint().y,
				this.getEndPoint().x, this.getEndPoint().y);
	}
	
	/**
	 * Gets the end point.
	 *
	 * @return the end point
	 */
	public Point getEndPoint() {
		return endPoint;
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
