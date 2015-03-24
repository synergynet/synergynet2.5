package apps.mysteriestableportal;

// import java.io.FileNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Frame;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import apps.mysteriestableportal.messages.AnnounceTableMessage;
import apps.mysteriestableportal.messages.ClearMessage;
import apps.mysteriestableportal.messages.LockAllMessage;
import apps.mysteriestableportal.messages.TableDiscoveryMessage;
import apps.mysteriestableportal.messages.UnicastClearMessage;
import apps.mysteriestableportal.messages.UnicastMysteryPathMessage;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import apps.remotecontrol.networkmanager.messages.ConnectTablePortalMessage;
import apps.remotecontrol.networkmanager.messages.LockTableMessage;
import apps.remotecontrol.networkmanager.messages.PostItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.RequestItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.RequestSyncItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.TableDiscoveryPortalMessage;
import apps.remotecontrol.networkmanager.messages.UnicastSyncDataPortalMessage;
import apps.remotecontrol.tableportal.WorkspaceManager;
import core.SynergyNetDesktop;

/**
 * The Class MysteriesPortalClientApp.
 */
public class MysteriesPortalClientApp extends DefaultSynergyNetApp {
	
	/**
	 * The Class ClientMessageProcessor.
	 */
	public class ClientMessageProcessor implements MessageProcessor {

		/*
		 * (non-Javadoc)
		 * @see
		 * synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers
		 * .MessageProcessor#process(java.lang.Object)
		 */
		@Override
		public void process(Object obj) {
			if (obj instanceof TableDiscoveryPortalMessage) {
				manager.postAliveMessage(((TableMessage) obj).getSender());
			} else if (obj instanceof ConnectTablePortalMessage) {
				manager.handleConnectionRequest(
						((TableMessage) obj).getSender(),
						((ConnectTablePortalMessage) obj).isConnect());
			} else if (obj instanceof PostItemsPortalMessage) {
				PostItemsPortalMessage msg = (PostItemsPortalMessage) obj;
				manager.processReceivedItems(msg);
			} else if (obj instanceof UnicastSyncDataPortalMessage) {
				manager.syncContent(((UnicastSyncDataPortalMessage) obj)
						.getItems());
			} else if (obj instanceof RequestItemsPortalMessage) {
				RequestItemsPortalMessage msg = (RequestItemsPortalMessage) obj;
				manager.postItemsToTable(msg.getItemNames(), msg.getSender(),
						msg.getTargetTableId(), msg.deleteItems());
			} else if (obj instanceof RequestSyncItemsPortalMessage) {
				manager.updateSyncData();
			} else if (obj instanceof TableDiscoveryMessage) {
				manager.postAliveMessage(((TableDiscoveryMessage) obj)
						.getSender());
			} else if ((obj instanceof ClearMessage)
					|| (obj instanceof UnicastClearMessage)) {
				reset();
			} else if (obj instanceof UnicastMysteryPathMessage) {
				reset();
				mysteryPath = ((UnicastMysteryPathMessage) obj)
						.getMysteryPath();
			} else if (obj instanceof LockTableMessage) {
				lockImage.setVisible(((LockTableMessage) obj).isLocked());
				manager.processTableMode(((LockTableMessage) obj).isLocked());
			} else if (obj instanceof LockAllMessage) {
				lockImage.setVisible(((LockAllMessage) obj).isLocked());
				manager.processTableMode(((LockAllMessage) obj).isLocked());
			}
		}

		/**
		 * Reset.
		 */
		private void reset() {
			contentSystem.removeAllContentItems();
			lockImage = null;
			if (manager != null) {
				List<ContentItem> itemsToRemove = new ArrayList<ContentItem>(
						manager.getOnlineItems().values());
				manager.unregisterContentItems(new ArrayList<ContentItem>(items));
				itemsToRemove.clear();
			}
			items.clear();
			if (stateRecorder != null) {
				stateRecorder.reset();
			}
			mysteryPath = null;
			createLockImage();
			mcontroller.createButtons();
		}

	}

	/** The client processor. */
	private ClientMessageProcessor clientProcessor;

	/** The content system. */
	private ContentSystem contentSystem;

	/** The items. */
	private List<ContentItem> items = new ArrayList<ContentItem>();

	/** The lock image. */
	private Frame lockImage;

	/** The logging. */
	private MysteriesLogging logging;

	/** The manager. */
	private NetworkedContentManager manager;

	/** The mcontroller. */
	private HoldTopRightConfirmVisualExit mcontroller;

	/** The mystery path. */
	private String mysteryPath = null;

	/** The restore. */
	private boolean restore = true;

	/** The state recorder. */
	private StateRecorder stateRecorder;
	
	/**
	 * Instantiates a new mysteries portal client app.
	 *
	 * @param info
	 *            the info
	 */
	public MysteriesPortalClientApp(ApplicationInfo info) {
		super(info);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent
	 * ()
	 */
	@Override
	public void addContent() {
		try {
			logging = new MysteriesLogging(this.getClass());
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
		clientProcessor = new ClientMessageProcessor();
		stateRecorder = new StateRecorder(this);
		if (restore) {
			items.addAll(stateRecorder.loadMysteryState());
			logging.registerItemsForLogging(items);
		}
		mcontroller = new HoldTopRightConfirmVisualExit(this);
		setMenuController(mcontroller);
		
		createLockImage();
	}

	/**
	 * Creates the lock image.
	 */
	public void createLockImage() {
		if (lockImage == null) {
			lockImage = (Frame) contentSystem.createContentItem(Frame.class);
			lockImage.setWidth(80);
			lockImage.setHeight(100);
			lockImage.drawImage(MysteriesPortalClientApp.class
					.getResource("lock.png"));
			lockImage.setOrder(99999);
			lockImage.setRotateTranslateScalable(false);
			lockImage.setLocalLocation(100, 600);
		}
		lockImage.setVisible(!SynergyNetDesktop.getInstance()
				.getMultiTouchInputComponent().isMultiTouchInputEnabled());
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate
	 * ()
	 */
	@Override
	public void onActivate() {
		RapidNetworkManager
				.addNetworkedContentListener(new NetworkedContentListener() {
					
					@Override
					public void itemsReceived(List<ContentItem> items,
							TableIdentity tableId) {
						
						for (ContentItem item : items) {
							item.setName(UUID.randomUUID().toString());
							if (!item.getId().endsWith("Title")) {
								((OrthoContentItem) item)
										.setRotateTranslateScalable(true);
							}
							((OrthoContentItem) item).setScaleLimit(
									ControllerApp.minScaleLimit,
									ControllerApp.maxScaleLimit);
							logging.registerItemForLogging(item);
						}
						
						MysteriesPortalClientApp.this.items.addAll(items);
						if (manager != null) {
							manager.registerContentItems(items);
						}
						if ((stateRecorder != null) && (mysteryPath != null)) {
							stateRecorder.registerMysteryContentItems(
									mysteryPath, items);
						}
					}
					
					@Override
					public void tableConnected() {
						List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
						receiverClasses.add(ControllerApp.class);
						if (manager != null) {
							manager.onlineItemsList.clear();
							manager.getReceiverClasses().clear();
						}
						contentSystem = ContentSystem
								.getContentSystemForSynergyNetApp(MysteriesPortalClientApp.this);
						manager = new NetworkedContentManager(contentSystem,
								RapidNetworkManager
										.getTableCommsClientService(),
								receiverClasses);
						manager.registerContentItems(items);
						WorkspaceManager.getInstance()
								.setNetworkedContentManager(manager);
						RapidNetworkManager
								.registerMessageProcessor(clientProcessor);
						try {
							RapidNetworkManager.getTableCommsClientService()
									.sendMessage(
											new AnnounceTableMessage(
													ControllerApp.class));
						} catch (IOException e) {
							
							e.printStackTrace();
						}
					}
					
					@Override
					public void tableDisconnected() {
						
					}
				});
		RapidNetworkManager.setAutoReconnect(true);
		RapidNetworkManager.connect(this);
		RapidNetworkManager.getReceiverClasses().clear();
		// SynergyNet.getReceiverClasses().add(ControllerApp.class);
		
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if (contentSystem != null) {
			contentSystem.update(tpf);
		}
		if (manager != null) {
			manager.update(tpf);
		}
	}
}
