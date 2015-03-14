package apps.mysteriestableportal.messages;


import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;

public class LockAllMessage extends BroadcastApplicationMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean isLocked = false;

	public LockAllMessage(){
		super();
	}
	
	public LockAllMessage(Class<?> targetClass){
		super(targetClass);
	}
	
	public void enableLock(boolean isLocked){
		this.isLocked = isLocked;
	}
	
	public boolean isLocked(){
		return this.isLocked;
	}
}