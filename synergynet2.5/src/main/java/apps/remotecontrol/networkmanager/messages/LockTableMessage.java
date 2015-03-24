package apps.remotecontrol.networkmanager.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;


/**
 * The Class LockTableMessage.
 */
public class LockTableMessage extends UnicastApplicationMessage{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3611717853521792797L;
	
	/** The is locked. */
	protected boolean isLocked = false;
	
	/**
	 * Instantiates a new lock table message.
	 */
	public LockTableMessage(){
		super();
	}
	
	/**
	 * Instantiates a new lock table message.
	 *
	 * @param targetClass the target class
	 * @param targetTableId the target table id
	 */
	public LockTableMessage(Class<?> targetClass, TableIdentity targetTableId){
		super(targetClass);
		this.setRecipient(targetTableId);
	}
	
	/**
	 * Enable lock.
	 *
	 * @param isLocked the is locked
	 */
	public void enableLock(boolean isLocked){
		this.isLocked = isLocked;
	}
	
	/**
	 * Checks if is locked.
	 *
	 * @return true, if is locked
	 */
	public boolean isLocked(){
		return this.isLocked;
	}
}
