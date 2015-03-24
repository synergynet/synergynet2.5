package apps.mtdesktop.messages.util;

import java.io.Serializable;


/**
 * The Class MouseEventInfo.
 */
public class MouseEventInfo implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	protected int id;
	
	/** The x. */
	protected float x;
	
	/** The y. */
	protected float y;
	
	/**
	 * Instantiates a new mouse event info.
	 */
	public MouseEventInfo(){
		
	}
	
	/**
	 * Instantiates a new mouse event info.
	 *
	 * @param id the id
	 * @param x the x
	 * @param y the y
	 */
	public MouseEventInfo(int id, float x, float y){
		this.setMouseEventInfo(id, x, y);
	}
	
	/**
	 * Sets the mouse event info.
	 *
	 * @param id the id
	 * @param x the x
	 * @param y the y
	 */
	public void setMouseEventInfo(int id, float x, float y){
		this.id= id;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setID(int id){
		this.id = id;
	}
	
	/**
	 * Sets the x.
	 *
	 * @param x the new x
	 */
	public void setX(float x){
		this.x = x;
	}
	
	/**
	 * Sets the y.
	 *
	 * @param y the new y
	 */
	public void setY(float y){
		this.y = y;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID(){
		return id;
	}
	
	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX(){
		return x;
	}
	
	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public float getY(){
		return y;
	}
}
