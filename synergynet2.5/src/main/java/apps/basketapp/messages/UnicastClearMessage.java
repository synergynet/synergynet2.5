package apps.basketapp.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

/**
 * The Class UnicastClearMessage.
 */
public class UnicastClearMessage extends UnicastApplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new unicast clear message.
	 */
	public UnicastClearMessage() {
		super();
	}

	/**
	 * Instantiates a new unicast clear message.
	 *
	 * @param tableId
	 *            the table id
	 * @param targetClass
	 *            the target class
	 */
	public UnicastClearMessage(TableIdentity tableId, Class<?> targetClass) {
		super(targetClass);
		this.setRecipient(tableId);
	}
}
