package synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers;

import java.util.HashMap;

import synergynetframework.appsystem.contentsystem.items.ContentItem;

public interface ConstructionManager{

	
	public HashMap<String, Object> buildConstructionInfo(ContentItem item);
	public abstract void processConstructionInfo(ContentItem item, HashMap<String, Object> info);
}
