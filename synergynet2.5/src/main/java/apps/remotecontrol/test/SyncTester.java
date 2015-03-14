package apps.remotecontrol.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;

import synergynetframework.appsystem.contentsystem.items.ContentItem;

public class SyncTester {
	
	protected List<ContentItem> items = new ArrayList<ContentItem>();
	protected float defaultAnimationDelay = 300f;
	protected float animationDelay = defaultAnimationDelay;
	protected NetworkedContentManager networkManager;
	boolean enableZoom = false, enableRotate = true, enableMove = true;
	
	protected float moveShift = 300;
	
	protected HashMap<ContentItem, Float> map = new HashMap<ContentItem, Float>();
	protected HashMap<ContentItem, Boolean> zoommap = new HashMap<ContentItem, Boolean>();
	protected HashMap<ContentItem, Boolean> movemap = new HashMap<ContentItem, Boolean>();

	public SyncTester(){}
	
	public SyncTester(NetworkedContentManager networkManager) {
		this.networkManager = networkManager;
	}
	
	public void setNetworkedContentManager(NetworkedContentManager networkManager){
		this.networkManager = networkManager;
	}

	public void setItems(List<ContentItem> items){
		this.items = items;
	}
	
	public void update(float interpolation){
		if(animationDelay <= 0){
			for(ContentItem item: items){
				// auto scale
				if(!zoommap.containsKey(item)) zoommap.put(item, true);
				if(enableZoom){
					float scale = item.getScale();
					if(scale<3 && zoommap.get(item)){
						scale+=0.1f;
						item.setScale(scale);
						if(scale >=3) zoommap.put(item, false);
					}else{
						scale-=0.1f;
						item.setScale(scale);
						if(scale <=0.5) zoommap.put(item, true);
					}
					if(networkManager != null) networkManager.getSyncManager().fireItemScaled(item);
				}
				
				if(enableRotate){
					// auto rotate
					float angle = item.getAngle();
					angle+=0.1f;
					item.setAngle(angle);
					if(networkManager != null) networkManager.getSyncManager().fireItemRotated(item);
				}
				
				if(enableMove){
					float xdash = item.getLocalLocation().x;
					if(!map.containsKey(item)) map.put(item, xdash);
					float xoriginal = map.get(item);
					if(!movemap.containsKey(item)) movemap.put(item, true);

					if(xdash - xoriginal<moveShift && movemap.get(item)){
						xdash+=4.0f;
						item.setLocalLocation(xdash, item.getLocalLocation().y);
						if(xdash - xoriginal >= moveShift) movemap.put(item, false);
					}else{
						xdash-=4.0f;
						item.setLocalLocation(xdash, item.getLocalLocation().y);
						if(xoriginal - xdash >= moveShift) movemap.put(item, true);
					}
					if(networkManager != null) networkManager.getSyncManager().fireItemMoved(item);
				}
				
			}
			animationDelay = defaultAnimationDelay;
		}
		animationDelay-=interpolation*2;
	}
}
