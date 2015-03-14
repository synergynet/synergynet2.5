package synergynetframework.appsystem.contentsystem.jme.items.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

public abstract class DrawData implements Serializable{

	private static final long serialVersionUID = -2957295980875244718L;

	protected long cursorId;
	protected Color color;
	protected float width; 
	
	public void setCursorId(long cursorId){
		this.cursorId = cursorId;
	}
	
	public long getCursorId(){
		return cursorId;
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public void setWidth(float width){
		this.width = width;
	}
	
	public Color getColor(){
		return color;
	}
	
	public float getWidth(){
		return width;
	}
	
	public  void draw(Graphics2D graphics){
		if(color != null) graphics.setColor(color);
		if(width != 0) graphics.setStroke(new BasicStroke(width));
	}
	
	public  void clear(Graphics2D graphics){}
}
