package synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers;

import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.items.ContentItem;


/**
 * The Interface ConstructionManager.
 */
public interface ConstructionManager{

	
	/**
	 * Builds the construction info.
	 *
	 * @param item the item
	 * @return the hash map
	 */
	public HashMap<String, Object> buildConstructionInfo(ContentItem item);
	
	/**
	 * Process construction info.
	 *
	 * @param item the item
	 * @param info the info
	 */
	public abstract void processConstructionInfo(ContentItem item, HashMap<String, Object> info);
}
