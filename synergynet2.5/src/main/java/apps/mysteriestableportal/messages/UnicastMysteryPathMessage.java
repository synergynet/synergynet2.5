package apps.mysteriestableportal.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

/**
 * The Class UnicastMysteryPathMessage.
 */
public class UnicastMysteryPathMessage extends UnicastApplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The mystery path. */
	private String mysteryPath;

	/**
	 * Instantiates a new unicast mystery path message.
	 */
	public UnicastMysteryPathMessage() {
		super();
	}

	/**
	 * Instantiates a new unicast mystery path message.
	 *
	 * @param tableId
	 *            the table id
	 * @param targetClass
	 *            the target class
	 * @param mysteryPath
	 *            the mystery path
	 */
	public UnicastMysteryPathMessage(TableIdentity tableId,
			Class<?> targetClass, String mysteryPath) {
		super(targetClass);
		this.setRecipient(tableId);
		this.mysteryPath = mysteryPath;
	}

	/**
	 * Gets the mystery path.
	 *
	 * @return the mystery path
	 */
	public String getMysteryPath() {
		return mysteryPath;
	}

	/**
	 * Sets the mystery path.
	 *
	 * @param mysteryPath
	 *            the new mystery path
	 */
	public void setMysteryPath(String mysteryPath) {
		this.mysteryPath = mysteryPath;
	}
}
