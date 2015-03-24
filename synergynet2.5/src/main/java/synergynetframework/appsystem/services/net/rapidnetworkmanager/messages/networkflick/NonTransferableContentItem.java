package synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick;

import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


/**
 * The Class NonTransferableContentItem.
 */
public class NonTransferableContentItem extends TransferableContentItem{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8608957157880717753L;
	
	/** The construct info. */
	protected HashMap<String, Object> constructInfo;
	
	/**
	 * Instantiates a new non transferable content item.
	 *
	 * @param targetClass the target class
	 * @param item the item
	 * @param targetTableId the target table id
	 */
	public NonTransferableContentItem(Class<?> targetClass, ContentItem item, TableIdentity targetTableId) {
		super(targetClass, item, targetTableId);
	}
	
	/**
	 * Gets the construction info.
	 *
	 * @return the construction info
	 */
	public HashMap<String, Object> getConstructionInfo(){
		return constructInfo;
	}

	/**
	 * Sets the construction info.
	 *
	 * @param constructInfo the construct info
	 */
	public void setConstructionInfo(HashMap<String, Object> constructInfo){
		this.constructInfo = constructInfo;
	}
}
