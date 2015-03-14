package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;

public abstract class ContentMessage extends UnicastApplicationMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2918061014330431267L;

	private String contentId;

	public ContentMessage(){
		super();
	}
	
	public ContentMessage(Class<?> targetClass, TableIdentity targetTableId, String contentId){
		super(targetClass);
		this.setRecipient(targetTableId);
		this.setContentId(contentId);
	}
	
	public void setContentId(String contentId){
		this.contentId = contentId;
	}
	
	public String getContentId(){
		return contentId;
	}
}

