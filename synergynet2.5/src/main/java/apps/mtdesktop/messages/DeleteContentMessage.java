package apps.mtdesktop.messages;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


/**
 * The Class DeleteContentMessage.
 */
public class DeleteContentMessage extends ContentMessage{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2918061014330431267L;
	
	/**
	 * Instantiates a new delete content message.
	 */
	public DeleteContentMessage(){
		super();
	}
	
	/**
	 * Instantiates a new delete content message.
	 *
	 * @param targetClass the target class
	 * @param targetTableId the target table id
	 * @param contentId the content id
	 */
	public DeleteContentMessage(Class<?> targetClass, TableIdentity targetTableId, String contentId){
		super(targetClass, targetTableId, contentId);
	}
}

