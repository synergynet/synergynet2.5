package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.SketchPad.DrawListener;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;


/**
 * The Interface ISketchPadImplementation.
 */
public interface ISketchPadImplementation extends IFrameImplementation {
	
	/**
	 * Sets the sketch area.
	 *
	 * @param rectangle the new sketch area
	 */
	public void setSketchArea(Rectangle rectangle);
	
	/**
	 * Clear all.
	 */
	public void clearAll();
	
	/**
	 * Clear.
	 *
	 * @param drawData the draw data
	 */
	public void clear(DrawData drawData);
	
	/**
	 * Sets the background colour.
	 *
	 * @param color the new background colour
	 */
	public void setBackgroundColour(Color color);
	
	/**
	 * Gets the background colour.
	 *
	 * @return the background colour
	 */
	public Color getBackgroundColour();
	
	/**
	 * Sets the text color.
	 *
	 * @param color the new text color
	 */
	public void setTextColor(Color color);
	
	/**
	 * Gets the text color.
	 *
	 * @return the text color
	 */
	public Color getTextColor();
	
	/**
	 * Sets the line width.
	 *
	 * @param width the new line width
	 */
	public void setLineWidth(float width);
	
	/**
	 * Gets the line width.
	 *
	 * @return the line width
	 */
	public float getLineWidth();
	
	/**
	 * Gets the sketch area.
	 *
	 * @return the sketch area
	 */
	public Rectangle getSketchArea();
	
	/**
	 * Gets the draw data.
	 *
	 * @return the draw data
	 */
	public List<DrawData> getDrawData();
	
	/**
	 * Draw.
	 *
	 * @param drawData the draw data
	 */
	public void draw(DrawData drawData);
	
	/**
	 * Draw line.
	 *
	 * @param cursorId the cursor id
	 * @param startPoint the start point
	 * @param endPoint the end point
	 * @param color the color
	 * @param width the width
	 */
	public void drawLine(long cursorId, Point startPoint, Point endPoint, Color color, float width);
	
	/**
	 * Draw string.
	 *
	 * @param string the string
	 * @param x the x
	 * @param y the y
	 */
	public void drawString(String string, int x, int y);
	
	/**
	 * Fill rectangle.
	 *
	 * @param rectangle the rectangle
	 * @param color the color
	 */
	public void fillRectangle(Rectangle rectangle, Color color);
	
	/**
	 * Draw.
	 *
	 * @param drawData the draw data
	 */
	public void draw(List<DrawData> drawData);
	
	/**
	 * Adds the draw listener.
	 *
	 * @param listener the listener
	 */
	public void addDrawListener(DrawListener listener);
	
	/**
	 * Removes the draw listener.
	 *
	 * @param listener the listener
	 */
	public void removeDrawListener(DrawListener listener);
	
	/**
	 * Removes the draw listeners.
	 */
	public void removeDrawListeners();
	
	/**
	 * Sets the clear area.
	 *
	 * @param rectangle the new clear area
	 */
	public void setClearArea(Rectangle rectangle);
	
	/**
	 * Sets the draw enabled.
	 *
	 * @param isWriteEnabled the new draw enabled
	 */
	public void setDrawEnabled(boolean isWriteEnabled);
	
	/**
	 * Checks if is draw enabled.
	 *
	 * @return true, if is draw enabled
	 */
	public boolean isDrawEnabled();
	
	/**
	 * Gets the clear area.
	 *
	 * @return the clear area
	 */
	public Rectangle getClearArea();
}
