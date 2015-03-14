package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

public class DrawString extends DrawData implements Serializable{

	private static final long serialVersionUID = 6939747148375706784L;

	private Point startPoint;
	private String text;
	
	public DrawString(String text, Point startPoint){
		this.startPoint = startPoint;
		this.text = text;
	}
	
	public DrawString(String text, Point startPoint, Color color){
		this(text, startPoint);
		this.color = color;
	}
	
	public Point getStartPoint(){
		return startPoint;
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		graphics.drawString(text, startPoint.x, startPoint.y);
	}
}
