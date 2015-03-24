package apps.mtdesktop.messages;

import java.awt.event.KeyEvent;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

/**
 * The Class KeyEventMessage.
 */
public class KeyEventMessage extends UnicastApplicationMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 832254747031925652L;

	/** The key event. */
	private KeyEvent keyEvent;

	/**
	 * Instantiates a new key event message.
	 */
	public KeyEventMessage() {
		super();
	}

	/**
	 * Instantiates a new key event message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param targetTableId
	 *            the target table id
	 * @param evt
	 *            the evt
	 */
	public KeyEventMessage(Class<?> targetClass, TableIdentity targetTableId,
			KeyEvent evt) {
		super(targetClass);
		this.setRecipient(targetTableId);
		keyEvent = evt;
	}

	/**
	 * Gets the key event.
	 *
	 * @return the key event
	 */
	public KeyEvent getKeyEvent() {
		return keyEvent;
	}
	
	/**
	 * Sets the key event.
	 *
	 * @param keyEvent
	 *            the new key event
	 */
	public void setKeyEvent(KeyEvent keyEvent) {
		this.keyEvent = keyEvent;
	}
	
}
