package synergynetframework.appsystem.contentsystem.items.implementation.interfaces;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.SketchPad.DrawListener;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;

public interface ISketchPadImplementation extends IFrameImplementation {
	public void setSketchArea(Rectangle rectangle);
	public void clearAll();
	public void clear(DrawData drawData);
	public void setBackgroundColour(Color color);
	public Color getBackgroundColour();
	public void setTextColor(Color color);
	public Color getTextColor();
	public void setLineWidth(float width);
	public float getLineWidth();
	public Rectangle getSketchArea();
	public List<DrawData> getDrawData();
	public void draw(DrawData drawData);
	public void drawLine(long cursorId, Point startPoint, Point endPoint, Color color, float width);
	public void drawString(String string, int x, int y);
	public void fillRectangle(Rectangle rectangle, Color color);
	public void draw(List<DrawData> drawData);
	public void addDrawListener(DrawListener listener);
	public void removeDrawListener(DrawListener listener);
	public void removeDrawListeners();
	public void setClearArea(Rectangle rectangle);
	public void setDrawEnabled(boolean isWriteEnabled);
	public boolean isDrawEnabled();
	public Rectangle getClearArea();
}
