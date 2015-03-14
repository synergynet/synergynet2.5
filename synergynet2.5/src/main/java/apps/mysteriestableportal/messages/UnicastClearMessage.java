package apps.mysteriestableportal.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class UnicastClearMessage extends UnicastApplicationMessage{
	
	private static final long serialVersionUID = 1L;

	public UnicastClearMessage(){
		super();
	}
	
	public UnicastClearMessage(TableIdentity tableId, Class<?> targetClass){
		super(targetClass);
		this.setRecipient(tableId);
	}
}
