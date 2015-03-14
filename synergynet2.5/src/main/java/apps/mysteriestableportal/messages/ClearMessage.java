package apps.mysteriestableportal.messages;

import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;

public class ClearMessage extends BroadcastApplicationMessage{
	
	private static final long serialVersionUID = 1L;

	public ClearMessage(){
		super();
	}
	
	public ClearMessage(Class<?> targetClass){
		super(targetClass);
	}
}
