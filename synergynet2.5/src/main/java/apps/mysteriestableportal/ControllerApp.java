package apps.mysteriestableportal;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SubAppMenuEventListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.MessageProcessor;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.handlers.NetworkedContentMessageProcessor.NetworkedContentListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.TableMessage;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mysteries.SubAppMenu;
import apps.mysteriestableportal.ControlMenu.ControlMenuListener;
import apps.mysteriestableportal.messages.AnnounceProjectorMessage;
import apps.mysteriestableportal.messages.LockAllMessage;
import apps.mysteriestableportal.messages.SearchProjectorsMessage;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import apps.remotecontrol.networkmanager.managers.TablePortalManager;
import apps.remotecontrol.networkmanager.messages.ConnectTablePortalMessage;
import apps.remotecontrol.networkmanager.messages.PostItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.RequestItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.RequestSyncItemsPortalMessage;
import apps.remotecontrol.networkmanager.messages.TableDiscoveryPortalMessage;
import apps.remotecontrol.networkmanager.messages.UnicastSyncDataPortalMessage;
import apps.remotecontrol.tableportal.TablePortal;
import apps.remotecontrol.tableportal.WorkspaceManager;
import apps.remotecontrol.tableportal.inspectionutility.InspectionUtility;

import com.jme.system.DisplaySystem;

/**
 * The Class ControllerApp.
 */
public class ControllerApp extends DefaultSynergyNetApp {
	
	/**
	 * The listener interface for receiving controlApp events. The class that is
	 * interested in processing a controlApp event implements this interface,
	 * and the object created with that class is registered with a component
	 * using the component's <code>addControlAppListener<code> method. When
	 * the controlApp event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ControlAppEvent
	 */
	class ControlAppListener implements ControlMenuListener {
		
		/*
		 * (non-Javadoc)
		 * @see
		 * apps.mysteriestableportal.ControlMenu.ControlMenuListener#clearLocalTable
		 * ()
		 */
		@Override
		public void clearLocalTable() {
			removeAllItems();
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.mysteriestableportal.ControlMenu.ControlMenuListener#
		 * clearStudentTables()
		 */
		@Override
		public void clearStudentTables() {
			ClearDataDialog clearDataDialog = new ClearDataDialog(manager,
					contentSystem);
			clearDataDialog.getWindow().setVisible(true);
			clearDataDialog.getWindow().centerItem();
			clearDataDialog.getWindow().setAsTopObject();
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.mysteriestableportal.ControlMenu.ControlMenuListener#
		 * createTablePortals()
		 */
		@Override
		public void createTablePortals() {
			int locX = DisplaySystem.getDisplaySystem().getWidth() / 2;
			int locY = DisplaySystem.getDisplaySystem().getHeight() / 2;
			
			for (TableIdentity tableId : RapidNetworkManager
					.getTableCommsClientService().getCurrentlyOnline()) {
				if (!tableId.equals(TableIdentity.getTableIdentity())
						&& !manager.getProjectors().contains(tableId)) {
					if (TablePortalManager.getInstance()
							.getTablePortalForTable(tableId) == null) {
						TablePortal portal = new TablePortal(contentSystem,
								manager, graphManager);
						portal.getWindow().setScaleLimit(0.6f, 2f);
						portal.getWindow().setScale(0.8f);
						TablePortalManager.getInstance().registerTablePortal(
								portal);
						portal.connect(tableId);
						portal.getWindow().setLocation(locX, locY);
						locX += 50;
						locY -= 50;
						if (tableId.toString().equalsIgnoreCase("red")) {
							portal.getWindow().setBackgroundColour(Color.red);
						} else if (tableId.toString().equalsIgnoreCase("green")) {
							portal.getWindow().setBackgroundColour(Color.green);
						} else if (tableId.toString().equalsIgnoreCase("blue")) {
							portal.getWindow().setBackgroundColour(Color.blue);
						} else if (tableId.toString()
								.equalsIgnoreCase("yellow")) {
							portal.getWindow()
									.setBackgroundColour(Color.yellow);
						}
					} else {
						TablePortal portal = TablePortalManager.getInstance()
								.getTablePortalForTable(tableId);
						portal.getWindow().setVisible(true);
						// portal.connect(tableId);
					}
				}
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.mysteriestableportal.ControlMenu.ControlMenuListener#
		 * hideTablePortals()
		 */
		@Override
		public void hideTablePortals() {
			TablePortalManager.getInstance().hideAll();
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.mysteriestableportal.ControlMenu.ControlMenuListener#
		 * lockStudentTables(boolean)
		 */
		@Override
		public void lockStudentTables(boolean lock) {
			try {
				LockAllMessage msg = new LockAllMessage(
						MysteriesPortalClientApp.class);
				msg.enableLock(lock);
				RapidNetworkManager.getTableCommsClientService().sendMessage(
						msg);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see
		 * apps.mysteriestableportal.ControlMenu.ControlMenuListener#sendDesktopData
		 * ()
		 */
		@Override
		public void sendDesktopData() {
			SendDataDialog sendDataDialog = new SendDataDialog(
					ControllerApp.this, mysteryPath);
			sendDataDialog.getWindow().setVisible(true);
			sendDataDialog.getWindow().centerItem();
			sendDataDialog.getWindow().setAsTopObject();
		}

	}

	/**
	 * The Class ControllerMessageProcessor.
	 */
	public class ControllerMessageProcessor implements MessageProcessor {
		
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
			} else if (obj instanceof AnnounceProjectorMessage) {
				manager.registerProjector(((TableMessage) obj).getSender());
			}
			manager.fireMessageReceived(obj);
		}
	}

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(TableCommsClientService.class.getName());

	/** The max scale limit. */
	public static float maxScaleLimit = 3f;

	/** The min scale limit. */
	public static float minScaleLimit = 0.7f;

	/** The content system. */
	private ContentSystem contentSystem;

	/** The controller processor. */
	private ControllerMessageProcessor controllerProcessor;

	/** The graph manager. */
	private GraphManager graphManager;
	
	/** The is menu visible. */
	private boolean isMenuVisible = true;

	/** The items. */
	private List<ContentItem> items = new ArrayList<ContentItem>();

	/** The manager. */
	private NetworkedContentManager manager;
	
	/** The mystery path. */
	protected String mysteryPath = "";

	/**
	 * Instantiates a new controller app.
	 *
	 * @param info
	 *            the info
	 */
	public ControllerApp(ApplicationInfo info) {
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
		
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
		InspectionUtility.getInstance().setContentSystem(contentSystem);
		controllerProcessor = new ControllerMessageProcessor();
		graphManager = new GraphManager(contentSystem);
		SubAppMenu subAppMenu = new SubAppMenu(contentSystem);
		subAppMenu.addSubAppMenuEventListener(new SubAppMenuEventListener() {
			@Override
			public void menuSelected(String filePath, String appName) {
				loadContent(filePath, appName);
			}
		});
		final ControlMenu controlMenu = new ControlMenu(contentSystem,
				subAppMenu);
		controlMenu.addControlMenuListener(new ControlAppListener());

		final SimpleButton menuBtn = (SimpleButton) contentSystem
				.createContentItem(SimpleButton.class);
		menuBtn.setText("Hide Menu");
		menuBtn.setLocation(contentSystem.getScreenWidth() - 55, 20);
		menuBtn.addButtonListener(new SimpleButtonAdapter() {
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				controlMenu.setVisible(!isMenuVisible);
				isMenuVisible = !isMenuVisible;
				if (menuBtn.getText().equals("Hide Menu")) {
					menuBtn.setText("Show Menu");
				} else {
					menuBtn.setText("Hide Menu");
				}
			}
		});
		/*
		 * LoggingFilter logFilter; try { logFilter = new
		 * LoggingFilter(this.getClass());
		 * SynergyNetDesktop.getInstance().getMultiTouchInputComponent
		 * ().addMultiTouchInputFilter(logFilter); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); }
		 */
	}

	/**
	 * Gets the networked content manager.
	 *
	 * @return the networked content manager
	 */
	public NetworkedContentManager getNetworkedContentManager() {
		return manager;
	}
	
	/**
	 * Load content.
	 *
	 * @param filePath
	 *            the file path
	 * @param appName
	 *            the app name
	 */
	protected void loadContent(String filePath, String appName) {
		removeAllItems();
		mysteryPath = filePath;
		if (filePath
				.equals("data/mysteries/dinnerdisaster2/dinnerdisaster2.xml")) {
			items.addAll(new DinnerDisasterBuilder().build(contentSystem));
		} else {
			items.addAll(contentSystem.loadContentItems(filePath));
		}
		for (ContentItem item : items) {
			((OrthoContentItem) item).setScaleLimit(minScaleLimit,
					maxScaleLimit);
			// if(AppConfig.tableType ==
			// AppConfig.TABLE_TYPE_JME_DIRECT_SIMULATOR){
			// item.setAngle(0);
			// }
		}
		if (manager != null) {
			manager.registerContentItems(items);
		}
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
					public void itemsReceived(List<ContentItem> item,
							TableIdentity tableId) {
						
					}
					
					@Override
					public void tableConnected() {
						log.info("Connected");
						List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
						receiverClasses.add(MysteriesPortalClientApp.class);
						if (manager != null) {
							manager.removeNetworkListeners();
							manager.onlineItemsList.clear();
						}
						manager = new NetworkedContentManager(contentSystem,
								RapidNetworkManager
										.getTableCommsClientService(),
								receiverClasses);
						manager.registerContentItems(items);
						WorkspaceManager.getInstance()
								.setNetworkedContentManager(manager);
						TablePortalManager.getInstance()
								.setNetworkedContentManager(manager);
						RapidNetworkManager
								.registerMessageProcessor(controllerProcessor);
						
						try {
							RapidNetworkManager.getTableCommsClientService()
									.sendMessage(
											new SearchProjectorsMessage(
													ProjectorApp.class));
						} catch (IOException e) {
							
							e.printStackTrace();
						}
					}
					
					@Override
					public void tableDisconnected() {
						log.severe("Disconnected");
						if (manager != null) {
							manager.removeNetworkListeners();
						}
					}
					
				});
		if (!RapidNetworkManager.getReceiverClasses().contains(
				MysteriesPortalClientApp.class)) {
			RapidNetworkManager.getReceiverClasses().add(
					MysteriesPortalClientApp.class);
		}
		RapidNetworkManager.setAutoReconnect(true);
		RapidNetworkManager.connect(this);
		
	}

	/**
	 * Removes the all items.
	 */
	public void removeAllItems() {
		if (manager != null) {
			List<ContentItem> itemsToRemote = new ArrayList<ContentItem>(
					manager.getOnlineItems().values());
			manager.unregisterContentItems(itemsToRemote);
			itemsToRemote.clear();
		}
		for (ContentItem item : items) {
			contentSystem.removeContentItem(item);
		}
		items.clear();
	}

	/**
	 * Removes the items.
	 *
	 * @param items
	 *            the items
	 */
	public void removeItems(List<ContentItem> items) {
		if (manager != null) {
			manager.unregisterContentItems(items);
		}
		for (ContentItem item : items) {
			contentSystem.removeContentItem(item);
		}
		items.clear();
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
		TablePortalManager.getInstance().update(tpf);
	}

}
