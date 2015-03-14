package apps.basketapp.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class UnicastCaptureTableMessage extends UnicastApplicationMessage{
	
	private static final long serialVersionUID = 1L;

	public UnicastCaptureTableMessage(){
		super();
	}
	
	public UnicastCaptureTableMessage(TableIdentity tableId, Class<?> targetClass){
		super(targetClass);
		this.setRecipient(tableId);
	}
}
