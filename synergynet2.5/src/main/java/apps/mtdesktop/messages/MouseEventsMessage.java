package apps.mtdesktop.messages;

import java.util.List;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.objectmessaging.messages.UDPMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;
import apps.mtdesktop.messages.util.MouseEventInfo;

/**
 * The Class MouseEventsMessage.
 */
public class MouseEventsMessage extends UnicastApplicationMessage implements
		UDPMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 832254747031925652L;

	/** The mouse events. */
	private List<MouseEventInfo> mouseEvents;

	/**
	 * Instantiates a new mouse events message.
	 */
	public MouseEventsMessage() {
		super();
	}

	/**
	 * Instantiates a new mouse events message.
	 *
	 * @param targetClass
	 *            the target class
	 * @param targetTableId
	 *            the target table id
	 * @param events
	 *            the events
	 */
	public MouseEventsMessage(Class<?> targetClass,
			TableIdentity targetTableId, List<MouseEventInfo> events) {
		super(targetClass);
		this.setRecipient(targetTableId);
		mouseEvents = events;
	}

	/**
	 * Gets the mouse events.
	 *
	 * @return the mouse events
	 */
	public List<MouseEventInfo> getMouseEvents() {
		return mouseEvents;
	}
	
	/**
	 * Sets the mouse events.
	 *
	 * @param mouseEvents
	 *            the new mouse events
	 */
	public void setMouseEvents(List<MouseEventInfo> mouseEvents) {
		this.mouseEvents = mouseEvents;
	}
	
}
