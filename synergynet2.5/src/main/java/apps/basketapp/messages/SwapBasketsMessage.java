package apps.basketapp.messages;

import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;


/**
 * The Class SwapBasketsMessage.
 */
public class SwapBasketsMessage extends BroadcastApplicationMessage{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new swap baskets message.
	 */
	public SwapBasketsMessage(){
		super();
	}
	
	/**
	 * Instantiates a new swap baskets message.
	 *
	 * @param targetClass the target class
	 */
	public SwapBasketsMessage(Class<?> targetClass){
		super(targetClass);
	}
}
