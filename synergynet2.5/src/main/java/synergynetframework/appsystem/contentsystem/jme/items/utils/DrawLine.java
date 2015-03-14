package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public class DrawLine extends DrawData implements Serializable{

	private static final long serialVersionUID = 6939747148375706784L;

	private Point startPoint, endPoint;
	
	public DrawLine(long cursorId, Point startPoint, Point endPoint, Color color, float width){
		super.cursorId = cursorId;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
		this.width = width;
	}
	
	public Point getStartPoint(){
		return startPoint;
	}
	
	public Point getEndPoint(){
		return endPoint;
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		graphics.drawLine(this.getStartPoint().x, this.getStartPoint().y, this.getEndPoint().x, this.getEndPoint().y);
	}
	
	@Override
	public void clear(Graphics2D graphics) {
		super.clear(graphics);
		graphics.drawLine(this.getStartPoint().x, this.getStartPoint().y, this.getEndPoint().x, this.getEndPoint().y);
	}
}
