package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;

public class DrawFillRectangle extends DrawData implements Serializable{

	private static final long serialVersionUID = 6939747148375706784L;

	protected Rectangle rect;
	
	public DrawFillRectangle(Rectangle rect, Color color){
		this(rect);
		this.color = color;
	}
	
	public DrawFillRectangle(Rectangle rect){
		this.rect = rect;
	}
	
	public Rectangle getRectangle(){
		return rect;
	}

	@Override
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
		graphics.fillRect(rect.x, rect.y, rect.width, rect.height);
	}
}
