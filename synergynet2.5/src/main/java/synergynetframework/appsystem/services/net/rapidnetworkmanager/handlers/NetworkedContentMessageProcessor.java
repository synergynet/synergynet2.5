package synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers.ConstructionManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.BroadcastItemConstructionMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.BroadcastItemsMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.PostItemConstructionMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.PostItemsMessage;

import com.sun.media.Log;

/**
 * The Class NetworkedContentMessageProcessor.
 */
public class NetworkedContentMessageProcessor implements MessageProcessor {

	/**
	 * The listener interface for receiving networkedContent events. The class
	 * that is interested in processing a networkedContent event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addNetworkedContentListener<code> method. When
	 * the networkedContent event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see NetworkedContentEvent
	 */
	public interface NetworkedContentListener {

		/**
		 * Items received.
		 *
		 * @param item
		 *            the item
		 * @param tableId
		 *            the table id
		 */
		public void itemsReceived(List<ContentItem> item, TableIdentity tableId);

		/**
		 * Table connected.
		 */
		public void tableConnected();

		/**
		 * Table disconnected.
		 */
		public void tableDisconnected();
	}

	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The listeners. */
	protected List<NetworkedContentListener> listeners = new ArrayList<NetworkedContentListener>();

	/**
	 * Instantiates a new networked content message processor.
	 *
	 * @param contentSystem
	 *            the content system
	 */
	public NetworkedContentMessageProcessor(ContentSystem contentSystem) {
		this.contentSystem = contentSystem;
	}

	/**
	 * Adds the networked content listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addNetworkedContentListener(NetworkedContentListener listener) {
		listeners.add(listener);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers
	 * .MessageProcessor#process(java.lang.Object)
	 */
	@Override
	public void process(Object obj) {
		if (obj instanceof PostItemsMessage) {
			for (ContentItem item : ((PostItemsMessage) obj).getItems()) {
				registerContentItem(item);
			}
			for (NetworkedContentListener listener : listeners) {
				listener.itemsReceived(((PostItemsMessage) obj).getItems(),
						((PostItemsMessage) obj).getSender());
			}
		} else if (obj instanceof PostItemConstructionMessage) {
			HashMap<ContentItem, HashMap<String, Object>> constructionMap = ((PostItemConstructionMessage) obj)
					.getConstructionInfo();
			List<ContentItem> constructedItems = restoreItemsFromConstructionInfo(constructionMap);
			if (constructedItems != null) {
				for (NetworkedContentListener listener : listeners) {
					listener.itemsReceived(constructedItems,
							((PostItemConstructionMessage) obj).getSender());
				}
			}
		} else if (obj instanceof BroadcastItemsMessage) {
			for (ContentItem item : ((BroadcastItemsMessage) obj).getItems()) {
				registerContentItem(item);
			}
			for (NetworkedContentListener listener : listeners) {
				listener.itemsReceived(
						((BroadcastItemsMessage) obj).getItems(),
						((BroadcastItemsMessage) obj).getSender());
			}
		} else if (obj instanceof BroadcastItemConstructionMessage) {
			HashMap<ContentItem, HashMap<String, Object>> constructionMap = ((BroadcastItemConstructionMessage) obj)
					.getConstructionInfo();
			List<ContentItem> constructedItems = restoreItemsFromConstructionInfo(constructionMap);
			if (constructedItems != null) {
				for (NetworkedContentListener listener : listeners) {
					listener.itemsReceived(constructedItems,
							((PostItemConstructionMessage) obj).getSender());
				}
			}
		}
	}
	
	/**
	 * Register content item.
	 *
	 * @param item
	 *            the item
	 */
	private void registerContentItem(ContentItem item) {
		if (contentSystem.getAllContentItems().containsKey(item.getName())) {
			contentSystem.removeContentItem(item);
		}
		contentSystem.addContentItem(item);
	}

	/**
	 * Removes the networked content listeners.
	 */
	public void removeNetworkedContentListeners() {
		listeners.clear();
	}

	/**
	 * Restore items from construction info.
	 *
	 * @param constructionMap
	 *            the construction map
	 * @return the list
	 */
	private List<ContentItem> restoreItemsFromConstructionInfo(
			HashMap<ContentItem, HashMap<String, Object>> constructionMap) {
		List<ContentItem> constructedItems = new ArrayList<ContentItem>();
		for (ContentItem sentItem : constructionMap.keySet()) {
			ContentItem constructedItem = contentSystem
					.createContentItem(sentItem.getClass());
			constructedItem.setName(sentItem.getName());
			constructedItem.setLocation(sentItem.getLocalLocation());
			constructedItem.setScale(sentItem.getScale());
			constructedItem.setAngle(sentItem.getAngle());
			constructedItems.add(constructedItem);
			ConstructionManager constructManager = RapidNetworkManager
					.getConstructionManagers().get(sentItem.getClass());
			if (constructManager == null) {
				Log.error("No construction manager was found for the item:"
						+ sentItem.getClass().getName());
				return null;
			}
			constructManager.processConstructionInfo(constructedItem,
					constructionMap.get(sentItem));
			sentItem.getClass().cast(constructedItem).init();
		}
		return constructedItems;
	}
}
