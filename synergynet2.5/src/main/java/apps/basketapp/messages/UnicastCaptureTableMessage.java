package apps.basketapp.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;


/**
 * The Class UnicastCaptureTableMessage.
 */
public class UnicastCaptureTableMessage extends UnicastApplicationMessage{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new unicast capture table message.
	 */
	public UnicastCaptureTableMessage(){
		super();
	}
	
	/**
	 * Instantiates a new unicast capture table message.
	 *
	 * @param tableId the table id
	 * @param targetClass the target class
	 */
	public UnicastCaptureTableMessage(TableIdentity tableId, Class<?> targetClass){
		super(targetClass);
		this.setRecipient(tableId);
	}
}
