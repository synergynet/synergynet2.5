package apps.remotecontrol.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;

/**
 * The Class SyncTester.
 */
public class SyncTester {
	
	/** The default animation delay. */
	protected static final float DEFAULT_ANIMATION_DELAY = 300f;

	/** The animation delay. */
	protected float animationDelay = DEFAULT_ANIMATION_DELAY;

	/** The enable move. */
	boolean enableZoom = false, enableRotate = true, enableMove = true;

	/** The items. */
	protected List<ContentItem> items = new ArrayList<ContentItem>();

	/** The map. */
	protected HashMap<ContentItem, Float> map = new HashMap<ContentItem, Float>();

	/** The movemap. */
	protected HashMap<ContentItem, Boolean> movemap = new HashMap<ContentItem, Boolean>();

	/** The move shift. */
	protected float moveShift = 300;

	/** The network manager. */
	protected NetworkedContentManager networkManager;

	/** The zoommap. */
	protected HashMap<ContentItem, Boolean> zoommap = new HashMap<ContentItem, Boolean>();
	
	/**
	 * Instantiates a new sync tester.
	 */
	public SyncTester() {
	}

	/**
	 * Instantiates a new sync tester.
	 *
	 * @param networkManager
	 *            the network manager
	 */
	public SyncTester(NetworkedContentManager networkManager) {
		this.networkManager = networkManager;
	}

	/**
	 * Sets the items.
	 *
	 * @param items
	 *            the new items
	 */
	public void setItems(List<ContentItem> items) {
		this.items = items;
	}
	
	/**
	 * Sets the networked content manager.
	 *
	 * @param networkManager
	 *            the new networked content manager
	 */
	public void setNetworkedContentManager(
			NetworkedContentManager networkManager) {
		this.networkManager = networkManager;
	}

	/**
	 * Update.
	 *
	 * @param interpolation
	 *            the interpolation
	 */
	public void update(float interpolation) {
		if (animationDelay <= 0) {
			for (ContentItem item : items) {
				// auto scale
				if (!zoommap.containsKey(item)) {
					zoommap.put(item, true);
				}
				if (enableZoom) {
					float scale = item.getScale();
					if ((scale < 3) && zoommap.get(item)) {
						scale += 0.1f;
						item.setScale(scale);
						if (scale >= 3) {
							zoommap.put(item, false);
						}
					} else {
						scale -= 0.1f;
						item.setScale(scale);
						if (scale <= 0.5) {
							zoommap.put(item, true);
						}
					}
					if (networkManager != null) {
						networkManager.getSyncManager().fireItemScaled(item);
					}
				}

				if (enableRotate) {
					// auto rotate
					float angle = item.getAngle();
					angle += 0.1f;
					item.setAngle(angle);
					if (networkManager != null) {
						networkManager.getSyncManager().fireItemRotated(item);
					}
				}

				if (enableMove) {
					float xdash = item.getLocalLocation().x;
					if (!map.containsKey(item)) {
						map.put(item, xdash);
					}
					float xoriginal = map.get(item);
					if (!movemap.containsKey(item)) {
						movemap.put(item, true);
					}
					
					if (((xdash - xoriginal) < moveShift) && movemap.get(item)) {
						xdash += 4.0f;
						item.setLocalLocation(xdash, item.getLocalLocation().y);
						if ((xdash - xoriginal) >= moveShift) {
							movemap.put(item, false);
						}
					} else {
						xdash -= 4.0f;
						item.setLocalLocation(xdash, item.getLocalLocation().y);
						if ((xoriginal - xdash) >= moveShift) {
							movemap.put(item, true);
						}
					}
					if (networkManager != null) {
						networkManager.getSyncManager().fireItemMoved(item);
					}
				}

			}
			animationDelay = DEFAULT_ANIMATION_DELAY;
		}
		animationDelay -= interpolation * 2;
	}
}
