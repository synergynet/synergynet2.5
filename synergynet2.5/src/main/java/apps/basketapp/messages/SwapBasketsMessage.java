package apps.basketapp.messages;

import synergynetframework.appsystem.services.net.tablecomms.messages.application.BroadcastApplicationMessage;

public class SwapBasketsMessage extends BroadcastApplicationMessage{
	
	private static final long serialVersionUID = 1L;

	public SwapBasketsMessage(){
		super();
	}
	
	public SwapBasketsMessage(Class<?> targetClass){
		super(targetClass);
	}
}
