package apps.mysteriestableportal.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public class UnicastMysteryPathMessage extends UnicastApplicationMessage{
	
	private static final long serialVersionUID = 1L;

	private String mysteryPath;
	
	public UnicastMysteryPathMessage(){
		super();
	}
	
	public UnicastMysteryPathMessage(TableIdentity tableId, Class<?> targetClass, String mysteryPath){
		super(targetClass);
		this.setRecipient(tableId);
		this.mysteryPath = mysteryPath;
	}
	
	public void setMysteryPath(String mysteryPath){
		this.mysteryPath = mysteryPath;
	}
	
	public String getMysteryPath(){
		return mysteryPath;
	}
}
