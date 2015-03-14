package apps.mtdesktop.messages;

import java.util.List;

import apps.mtdesktop.messages.util.MouseEventInfo;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.objectmessaging.messages.UDPMessage;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class MouseEventsMessage extends UnicastApplicationMessage implements UDPMessage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 832254747031925652L;
	private List<MouseEventInfo> mouseEvents;
	
	public MouseEventsMessage(){
		super();
	}
	
	public MouseEventsMessage(Class<?> targetClass, TableIdentity targetTableId, List<MouseEventInfo> events){
		super(targetClass);
		this.setRecipient(targetTableId);
		mouseEvents = events;
	}
	
	public void setMouseEvents(List<MouseEventInfo> mouseEvents){
		this.mouseEvents = mouseEvents;
	}

	public List<MouseEventInfo> getMouseEvents(){
		return mouseEvents;
	}

}
