package synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick;

import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class NonTransferableContentItem extends TransferableContentItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8608957157880717753L;
	protected HashMap<String, Object> constructInfo;
	
	public NonTransferableContentItem(Class<?> targetClass, ContentItem item, TableIdentity targetTableId) {
		super(targetClass, item, targetTableId);
	}
	
	public HashMap<String, Object> getConstructionInfo(){
		return constructInfo;
	}

	public void setConstructionInfo(HashMap<String, Object> constructInfo){
		this.constructInfo = constructInfo;
	}
}
