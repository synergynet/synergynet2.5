package apps.mtdesktop.messages.util;

import java.io.Serializable;

public class MouseEventInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int id;
	protected float x;
	protected float y;
	
	public MouseEventInfo(){
		
	}
	
	public MouseEventInfo(int id, float x, float y){
		this.setMouseEventInfo(id, x, y);
	}
	
	public void setMouseEventInfo(int id, float x, float y){
		this.id= id;
		this.x = x;
		this.y = y;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public int getID(){
		return id;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
}
