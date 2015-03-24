package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;


/**
 * The Class MTMouseMessage.
 */
public class MTMouseMessage  extends UnicastApplicationMessage{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -201244581876093018L;
	
	/** The angle. */
	private float angle;
	
	/** The scale. */
	private float scale;

	/**
	 * Instantiates a new MT mouse message.
	 */
	public MTMouseMessage(){
		super();
	}
	
	/**
	 * Instantiates a new MT mouse message.
	 *
	 * @param targetClass the target class
	 * @param targetTableId the target table id
	 * @param angle the angle
	 * @param scale the scale
	 */
	public MTMouseMessage(Class<?> targetClass, TableIdentity targetTableId, float angle, float scale){
		super(targetClass);
		this.setRecipient(targetTableId);
		this.setAngle(angle);
		this.setScale(scale);
	}
	
	/**
	 * Sets the angle.
	 *
	 * @param angle the new angle
	 */
	public void setAngle(float angle){
		this.angle = angle;
	}
	
	/**
	 * Gets the angle.
	 *
	 * @return the angle
	 */
	public float getAngle(){
		return angle;
	}
	
	/**
	 * Sets the scale.
	 *
	 * @param scale the new scale
	 */
	public void setScale(float scale){
		this.scale = scale;
	}
	
	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public float getScale(){
		return scale;
	}
}
