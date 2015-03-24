package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.tablecomms.messages.application.UnicastApplicationMessage;


/**
 * The Class ContentMessage.
 */
public abstract class ContentMessage extends UnicastApplicationMessage{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2918061014330431267L;

	/** The content id. */
	private String contentId;

	/**
	 * Instantiates a new content message.
	 */
	public ContentMessage(){
		super();
	}
	
	/**
	 * Instantiates a new content message.
	 *
	 * @param targetClass the target class
	 * @param targetTableId the target table id
	 * @param contentId the content id
	 */
	public ContentMessage(Class<?> targetClass, TableIdentity targetTableId, String contentId){
		super(targetClass);
		this.setRecipient(targetTableId);
		this.setContentId(contentId);
	}
	
	/**
	 * Sets the content id.
	 *
	 * @param contentId the new content id
	 */
	public void setContentId(String contentId){
		this.contentId = contentId;
	}
	
	/**
	 * Gets the content id.
	 *
	 * @return the content id
	 */
	public String getContentId(){
		return contentId;
	}
}

