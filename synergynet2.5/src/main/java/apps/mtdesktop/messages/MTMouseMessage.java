package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class MTMouseMessage  extends UnicastApplicationMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = -201244581876093018L;
	private float angle;
	private float scale;

	public MTMouseMessage(){
		super();
	}
	
	public MTMouseMessage(Class<?> targetClass, TableIdentity targetTableId, float angle, float scale){
		super(targetClass);
		this.setRecipient(targetTableId);
		this.setAngle(angle);
		this.setScale(scale);
	}
	
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public void setScale(float scale){
		this.scale = scale;
	}
	
	public float getScale(){
		return scale;
	}
}
