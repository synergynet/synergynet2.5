package apps.mysteriestableportal.messages;


import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;


/**
 * The Class LockAllMessage.
 */
public class LockAllMessage extends BroadcastApplicationMessage{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The is locked. */
	protected boolean isLocked = false;

	/**
	 * Instantiates a new lock all message.
	 */
	public LockAllMessage(){
		super();
	}
	
	/**
	 * Instantiates a new lock all message.
	 *
	 * @param targetClass the target class
	 */
	public LockAllMessage(Class<?> targetClass){
		super(targetClass);
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