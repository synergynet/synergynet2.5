package synergynetframework.appsystem.services.net.rapidnetworkmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.exceptions.ServiceNotRunningException;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.netservicediscovery.NetworkServiceDiscoveryService;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers.ConstructionManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.DefaultMessageHandler;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkFlickMessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.BroadcastItemConstructionMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.BroadcastItemsMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.PostItemConstructionMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.PostItemsMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.AnnounceTableMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.EnableFlickMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.messages.networkflick.UnregisterTableMessage;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.utils.networkflick.TableInfo;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.utils.networkflick.TransferController;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;

/**
 * The Class RapidNetworkManager.
 */
public class RapidNetworkManager {
	
	/**
	 * The Enum SyncType.
	 */
	public enum SyncType {
		/** The bidirectional sync. */
		BIDIRECTIONAL_SYNC, /** The unidirectional sync. */
		UNIDIRECTIONAL_SYNC
	}

	/** The app. */
	protected static DefaultSynergyNetApp app;

	/** The auto reconnect. */
	protected static boolean autoReconnect = false;

	/** The comms. */
	protected static TableCommsClientService comms;

	/** The construction managers. */
	protected static HashMap<Class<? extends ContentItem>, ConstructionManager> constructionManagers = new HashMap<Class<? extends ContentItem>, ConstructionManager>();

	/** The content listeners. */
	protected static List<NetworkedContentListener> contentListeners = new ArrayList<NetworkedContentListener>();

	/** The is connected. */
	public static boolean isConnected = false;

	/** The is flick enabled. */
	protected static boolean isFlickEnabled = false;

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(RapidNetworkManager.class.getName());

	/** The message handler. */
	protected static DefaultMessageHandler messageHandler = new DefaultMessageHandler();

	/** The nfmp. */
	protected static NetworkFlickMessageProcessor nfmp;

	/** The nsds. */
	protected static NetworkServiceDiscoveryService nsds = null;

	/** The processor. */
	protected static NetworkedContentMessageProcessor processor;

	/** The receiver classes. */
	protected static List<Class<?>> receiverClasses = new ArrayList<Class<?>>();

	/** The reconnect time. */
	protected static int reconnectTime = 1000;

	/** The table info. */
	protected static TableInfo tableInfo = new TableInfo(
			TableIdentity.getTableIdentity(), 0, 0, 0);

	/** The transfer controller. */
	protected static TransferController transferController;;
	
	/**
	 * Adds the networked content listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public static void addNetworkedContentListener(
			NetworkedContentListener listener) {
		contentListeners.add(listener);
	}
	
	/**
	 * Broadcast item.
	 *
	 * @param item
	 *            the item
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void broadcastItem(ContentItem item) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		broadcastItems(items);
	}
	
	/**
	 * Broadcast items.
	 *
	 * @param items
	 *            the items
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void broadcastItems(List<ContentItem> items)
			throws IOException {
		if (comms == null) {
			throw new IOException("Table is not connected");
		}
		BroadcastItemsMessage msgBindableItems = null;
		BroadcastItemConstructionMessage msgNonBindableItems = null;
		List<ContentItem> bindableItems = new ArrayList<ContentItem>();
		List<ContentItem> nonBindableItems = new ArrayList<ContentItem>();
		for (ContentItem item : items) {
			if (constructionManagers.containsKey(item.getClass())) {
				nonBindableItems.add(item);
			} else {
				bindableItems.add(item);
			}
		}
		if (!bindableItems.isEmpty()) {
			msgBindableItems = new BroadcastItemsMessage();
			msgBindableItems.setItems(bindableItems);
		}
		if (!nonBindableItems.isEmpty()) {
			HashMap<ContentItem, HashMap<String, Object>> constructInfo = new HashMap<ContentItem, HashMap<String, Object>>();
			for (ContentItem item : nonBindableItems) {
				ConstructionManager contruct = constructionManagers.get(item
						.getClass());
				constructInfo.put(item, contruct.buildConstructionInfo(item));
			}
			msgNonBindableItems = new BroadcastItemConstructionMessage();
			msgNonBindableItems.setConstructionInfo(constructInfo);
		}

		for (Class<?> targetClass : receiverClasses) {
			if (msgBindableItems != null) {
				msgBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgBindableItems);
				log.info("Broadcast bindable items");
			} else if (msgNonBindableItems != null) {
				msgNonBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgNonBindableItems);
				log.info("Broadcast non-bindable items");
			}
		}
	}

	/**
	 * Connect.
	 *
	 * @param app
	 *            the app
	 */
	public static void connect(final DefaultSynergyNetApp app) {
		RapidNetworkManager.connect(app, true);
	}
	
	/**
	 * Connect.
	 *
	 * @param app
	 *            the app
	 * @param contentSystemExists
	 *            the content system exists
	 */
	public static void connect(final DefaultSynergyNetApp app,
			final boolean contentSystemExists) {
		RapidNetworkManager.app = app;
		// if(!receiverClasses.contains(app.getClass()))
		// receiverClasses.add(app.getClass());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (comms == null) {
						comms = (TableCommsClientService) ServiceManager
								.getInstance().get(
										TableCommsClientService.class);
					}
					if ((comms != null) && RapidNetworkManager.autoReconnect) {
						while (!comms.isClientConnected()) {
							Thread.sleep(reconnectTime);
							if (nsds == null) {
								nsds = (NetworkServiceDiscoveryService) ServiceManager
										.getInstance()
										.get(NetworkServiceDiscoveryService.class);
							} else {
								nsds.shutdown();
							}
							nsds.start();
							comms.start();
						}
						if (nsds != null) {
							nsds.shutdown();
						}
						if (processor != null) {
							processor.removeNetworkedContentListeners();
						}
						if (contentSystemExists) {
							processor = new NetworkedContentMessageProcessor(
									ContentSystem
											.getContentSystemForSynergyNetApp(app));
						} else {
							processor = new NetworkedContentMessageProcessor(
									null);
						}
						messageHandler.registerMessageProcessor(processor);
						processor
								.addNetworkedContentListener(new NetworkedContentListener() {
									
									@Override
									public void itemsReceived(
											List<ContentItem> item,
											TableIdentity tableId) {
										for (NetworkedContentListener listener : contentListeners) {
											listener.itemsReceived(item,
													tableId);
										}
									}
									
									@Override
									public void tableConnected() {
										
									}
									
									@Override
									public void tableDisconnected() {
										
									}
									
								});
						comms.register(app, messageHandler);
						comms.register("connection_listener",
								new TableCommsApplicationListener() {
									@Override
									public void messageReceived(Object obj) {
										
									}
									
									@Override
									public void tableDisconnected() {
										if (comms != null) {
											try {
												comms.stop();
											} catch (ServiceNotRunningException e) {
												
												e.printStackTrace();
											}
										}
										comms = null;
										isConnected = false;
										for (NetworkedContentListener listener : contentListeners) {
											listener.tableDisconnected();
										}
										if (RapidNetworkManager.autoReconnect) {
											connect(app, contentSystemExists);
										}
									}
									
								});
						isConnected = true;
						for (NetworkedContentListener listener : contentListeners) {
							listener.tableConnected();
						}
					}
				} catch (CouldNotStartServiceException e1) {
					log.warning(e1.toString());
				} catch (IOException e) {
					log.warning(e.toString());
				} catch (InterruptedException e) {
					log.warning(e.toString());
				}
			}
		}).start();

		log.info("Connected to server");
	}
	
	/**
	 * Enable network flick.
	 *
	 * @param isFlickEnabled
	 *            the is flick enabled
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void enableNetworkFlick(boolean isFlickEnabled)
			throws IOException {
		if (comms == null) {
			throw new IOException("Table is not connected");
		}
		if (isFlickEnabled) {
			RapidNetworkManager.isFlickEnabled = isFlickEnabled;
			if (transferController == null) {
				transferController = new TransferController(app);
			}
			transferController.enableNetworkFlick(isFlickEnabled);
			if (nfmp == null) {
				nfmp = new NetworkFlickMessageProcessor(transferController);
			}
			RapidNetworkManager.registerMessageProcessor(nfmp);
			transferController.setLocalTableInfo(tableInfo);
			for (Class<?> receiverClass : RapidNetworkManager
					.getReceiverClasses()) {
				comms.sendMessage(new AnnounceTableMessage(receiverClass,
						tableInfo));
				comms.sendMessage(new EnableFlickMessage(receiverClass, true));
			}
		} else {
			for (Class<?> receiverClass : RapidNetworkManager
					.getReceiverClasses()) {
				if (transferController != null) {
					transferController.enableNetworkFlick(isFlickEnabled);
				}
				comms.sendMessage(new UnregisterTableMessage(receiverClass));
				comms.sendMessage(new EnableFlickMessage(receiverClass, false));
			}
		}

		log.info("Enable network flick");
	}
	
	/**
	 * Gets the construction managers.
	 *
	 * @return the construction managers
	 */
	public static HashMap<Class<? extends ContentItem>, ConstructionManager> getConstructionManagers() {
		return constructionManagers;
	}
	
	/**
	 * Gets the receiver classes.
	 *
	 * @return the receiver classes
	 */
	public static List<Class<?>> getReceiverClasses() {
		return receiverClasses;
	}

	/**
	 * Gets the table comms client service.
	 *
	 * @return the table comms client service
	 */
	public static TableCommsClientService getTableCommsClientService() {
		return comms;
	}
	
	/**
	 * Post item.
	 *
	 * @param item
	 *            the item
	 * @param tableIdentity
	 *            the table identity
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void postItem(ContentItem item, TableIdentity tableIdentity)
			throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
		postItems(items, tableIdentity);

	}

	/**
	 * Post items.
	 *
	 * @param items
	 *            the items
	 * @param tableIdentity
	 *            the table identity
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void postItems(List<ContentItem> items,
			TableIdentity tableIdentity) throws IOException {
		if (comms == null) {
			throw new IOException("Table is not connected");
		}
		PostItemsMessage msgBindableItems = null;
		PostItemConstructionMessage msgNonBindableItems = null;
		List<ContentItem> bindableItems = new ArrayList<ContentItem>();
		List<ContentItem> nonBindableItems = new ArrayList<ContentItem>();
		for (ContentItem item : items) {
			if (constructionManagers.containsKey(item.getClass())) {
				nonBindableItems.add(item);
			} else {
				bindableItems.add(item);
			}
		}
		if (!bindableItems.isEmpty()) {
			msgBindableItems = new PostItemsMessage();
			msgBindableItems.setRecipient(tableIdentity);
			msgBindableItems.setItems(bindableItems);
		}
		if (!nonBindableItems.isEmpty()) {
			HashMap<ContentItem, HashMap<String, Object>> constructInfo = new HashMap<ContentItem, HashMap<String, Object>>();
			for (ContentItem item : nonBindableItems) {
				ConstructionManager contruct = constructionManagers.get(item
						.getClass());
				constructInfo.put(item, contruct.buildConstructionInfo(item));
			}
			msgNonBindableItems = new PostItemConstructionMessage();
			msgNonBindableItems.setRecipient(tableIdentity);
			msgNonBindableItems.setConstructionInfo(constructInfo);
		}

		for (Class<?> targetClass : receiverClasses) {
			if (msgBindableItems != null) {
				msgBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgBindableItems);
				log.info("Post bindable items to table-"
						+ tableIdentity.toString());
			} else if (msgNonBindableItems != null) {
				msgNonBindableItems.setTargetClassName(targetClass.getName());
				comms.sendMessage(msgNonBindableItems);
				log.info("Post non-bindable items to table-"
						+ tableIdentity.toString());
			}
		}
	}

	/**
	 * Register construction manager.
	 *
	 * @param itemClass
	 *            the item class
	 * @param constructManager
	 *            the construct manager
	 */
	public static void registerConstructionManager(
			Class<? extends ContentItem> itemClass,
			ConstructionManager constructManager) {
		constructionManagers.put(itemClass, constructManager);
		log.info("Register construction manager");
	}

	/**
	 * Register message processor.
	 *
	 * @param processor
	 *            the processor
	 */
	public static void registerMessageProcessor(MessageProcessor processor) {
		if (!messageHandler.getMessageProcessors().contains(processor)) {
			messageHandler.registerMessageProcessor(processor);
		}
	}

	/**
	 * Removes the message processor.
	 *
	 * @param processor
	 *            the processor
	 */
	public static void removeMessageProcessor(MessageProcessor processor) {
		messageHandler.removeMessageProcessor(processor);
	}

	/**
	 * Removes the message processors.
	 */
	public static void removeMessageProcessors() {

		messageHandler.removeMessageProcessors();
	}
	
	/**
	 * Removes the networked content listeners.
	 */
	public static void removeNetworkedContentListeners() {
		contentListeners.clear();
	}

	/**
	 * Sets the auto connect time.
	 *
	 * @param reconnectTime
	 *            the new auto connect time
	 */
	public static void setAutoConnectTime(int reconnectTime) {
		RapidNetworkManager.reconnectTime = reconnectTime;
	}
	
	/**
	 * Sets the auto reconnect.
	 *
	 * @param autoReconnect
	 *            the new auto reconnect
	 */
	public static void setAutoReconnect(boolean autoReconnect) {
		RapidNetworkManager.autoReconnect = autoReconnect;
	}

	/**
	 * Sets the table orientation.
	 *
	 * @param angle
	 *            the new table orientation
	 */
	public static void setTableOrientation(float angle) {
		tableInfo.setAngle(angle);
		try {
			RapidNetworkManager
					.enableNetworkFlick(RapidNetworkManager.isFlickEnabled);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}

	/**
	 * Sets the table position.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public static void setTablePosition(int x, int y) {
		tableInfo.setTablePosition(x, y);
		try {
			RapidNetworkManager
					.enableNetworkFlick(RapidNetworkManager.isFlickEnabled);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}

	/**
	 * Share item.
	 *
	 * @param item
	 *            the item
	 * @param tableIdentity
	 *            the table identity
	 * @param syncType
	 *            the sync type
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void shareItem(ContentItem item, TableIdentity tableIdentity,
			SyncType syncType) throws IOException {
		if (comms == null) {
			throw new IOException("Table is not connected");
		}
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
	}

	/**
	 * Unshare item.
	 *
	 * @param item
	 *            the item
	 * @param tableIdentity
	 *            the table identity
	 * @param syncType
	 *            the sync type
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void unshareItem(ContentItem item,
			TableIdentity tableIdentity, SyncType syncType) throws IOException {
		List<ContentItem> items = new ArrayList<ContentItem>();
		items.add(item);
	}
	
	/**
	 * Update.
	 */
	public static void update() {
		if (transferController != null) {
			transferController.update();
		}
	}
}
