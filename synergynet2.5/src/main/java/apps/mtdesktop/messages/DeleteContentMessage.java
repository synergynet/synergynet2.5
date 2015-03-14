package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class DeleteContentMessage extends ContentMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2918061014330431267L;
	public DeleteContentMessage(){
		super();
	}
	
	public DeleteContentMessage(Class<?> targetClass, TableIdentity targetTableId, String contentId){
		super(targetClass, targetTableId, contentId);
	}
}

