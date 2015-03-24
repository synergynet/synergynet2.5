package apps.basketapp.messages;

import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;


/**
 * The Class ClearMessage.
 */
public class ClearMessage extends BroadcastApplicationMessage{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new clear message.
	 */
	public ClearMessage(){
		super();
	}
	
	/**
	 * Instantiates a new clear message.
	 *
	 * @param targetClass the target class
	 */
	public ClearMessage(Class<?> targetClass){
		super(targetClass);
	}
}
