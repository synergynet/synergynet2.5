package apps.basketapp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.listener.SubAppMenuEventListener;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers.ConstructionManager;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import apps.basketapp.BasketAppResources;
import apps.basketapp.client.BasketApp;
import apps.basketapp.controller.ControlMenu.BasketControlMenuListener;
import apps.basketapp.controller.mysteries.TableMystery;
import apps.basketapp.controller.mysteries.TextMystery;
import apps.basketapp.controller.mysteries.VideoMystery;
import apps.basketapp.messages.LockAllMessage;
import apps.basketapp.messages.SwapBasketsMessage;

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
	class ControlAppListener implements BasketControlMenuListener {
		
		/*
		 * (non-Javadoc)
		 * @see apps.basketapp.controller.ControlMenu.BasketControlMenuListener#
		 * captureStudentTables()
		 */
		@Override
		public void captureStudentTables() {
			if (RapidNetworkManager.getTableCommsClientService() == null) {
				return;
			}
			for (TableIdentity tableId : RapidNetworkManager
					.getTableCommsClientService().getCurrentlyOnline()) {
				if (tableId.equals(TableIdentity.getTableIdentity())) {
					continue;
				}
				SnapshotContainer snapContainer = null;
				if (!snapMap.containsKey(tableId)) {
					snapContainer = new SnapshotContainer(contentSystem,
							tableId);
					snapMap.put(tableId, snapContainer);
				} else {
					snapContainer = snapMap.get(tableId);
				}
				snapContainer.getWindow().setVisible(true);
				snapContainer.refresh();
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.basketapp.controller.ControlMenu.BasketControlMenuListener#
		 * clearLocalTable()
		 */
		@Override
		public void clearLocalTable() {
			for (ContentItem item : onlineItems) {
				contentSystem.removeContentItem(item);
			}
			onlineItems.clear();
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.basketapp.controller.ControlMenu.BasketControlMenuListener#
		 * clearStudentTables()
		 */
		@Override
		public void clearStudentTables() {
			ClearDataDialog clearDataDialog = new ClearDataDialog(contentSystem);
			clearDataDialog.getWindow().setVisible(true);
			clearDataDialog.getWindow().centerItem();
			clearDataDialog.getWindow().setAsTopObject();
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.basketapp.controller.ControlMenu.BasketControlMenuListener#
		 * lockStudentTables(boolean)
		 */
		@Override
		public void lockStudentTables(boolean lock) {
			try {
				LockAllMessage msg = new LockAllMessage(BasketApp.class);
				msg.enableLock(lock);
				RapidNetworkManager.getTableCommsClientService().sendMessage(
						msg);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.basketapp.controller.ControlMenu.BasketControlMenuListener#
		 * sendDesktopData()
		 */
		@Override
		public void sendDesktopData() {
			SendDataDialog sendDataDialog = new SendDataDialog(
					ControllerApp.this);
			sendDataDialog.getWindow().setVisible(true);
			sendDataDialog.getWindow().centerItem();
			sendDataDialog.getWindow().setAsTopObject();
		}
		
		/*
		 * (non-Javadoc)
		 * @see apps.basketapp.controller.ControlMenu.BasketControlMenuListener#
		 * swapBaskets()
		 */
		@Override
		public void swapBaskets() {
			if (RapidNetworkManager.getTableCommsClientService() != null) {
				try {
					RapidNetworkManager.getTableCommsClientService()
							.sendMessage(
									new SwapBasketsMessage(BasketApp.class));
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
	}

	/** The content system. */
	private ContentSystem contentSystem;

	/** The online items. */
	private List<ContentItem> onlineItems = new ArrayList<ContentItem>();
	
	/** The snap map. */
	Map<TableIdentity, SnapshotContainer> snapMap = new HashMap<TableIdentity, SnapshotContainer>();
	
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
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		contentSystem.removeAllContentItems();
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		
		ContentSubMenu contentSubMenu = new ContentSubMenu(contentSystem);
		contentSubMenu
				.addSubAppMenuEventListener(new SubAppMenuEventListener() {
					@Override
					public void menuSelected(String filePath, String appName) {
						ControllerApp.this.clearLocalTable();
						
						if (appName
								.equalsIgnoreCase(ContentSubMenu.CONTENT_VIDEO)) {
							onlineItems.addAll(new VideoMystery()
									.getMystery(contentSystem));
						} else if (appName
								.equalsIgnoreCase(ContentSubMenu.CONTENT_TEXT)) {
							onlineItems.addAll(new TextMystery()
									.getMystery(contentSystem));
						} else if (appName
								.equalsIgnoreCase(ContentSubMenu.CONTENT_MAP)) {
							LightImageLabel map = (LightImageLabel) contentSystem
									.createContentItem(LightImageLabel.class);
							// map.enableAspectRatio(false);
							map.setWidth(400);
							map.setHeight(300);
							map.drawImage(BasketAppResources.class
									.getResource("plan.jpg"));
							map.centerItem();
							map.setAsTopObject();
							map.setNote("map");
							onlineItems.add(map);
						} else if (appName
								.equalsIgnoreCase(ContentSubMenu.TABLE_RED)) {
							onlineItems.addAll(new TableMystery().getMystery(
									contentSystem, ContentSubMenu.TABLE_RED));
						} else if (appName
								.equalsIgnoreCase(ContentSubMenu.TABLE_GREEN)) {
							onlineItems.addAll(new TableMystery().getMystery(
									contentSystem, ContentSubMenu.TABLE_GREEN));
						} else if (appName
								.equalsIgnoreCase(ContentSubMenu.TABLE_BLUE)) {
							onlineItems.addAll(new TableMystery().getMystery(
									contentSystem, ContentSubMenu.TABLE_BLUE));
						} else if (appName
								.equalsIgnoreCase(ContentSubMenu.TABLE_YELLOW)) {
							onlineItems.addAll(new TableMystery().getMystery(
									contentSystem, ContentSubMenu.TABLE_YELLOW));
						}
					}
				});

		final ControlMenu controlMenu = new ControlMenu(contentSystem,
				contentSubMenu);
		controlMenu.addControlMenuListener(new ControlAppListener());

	}

	/**
	 * Clear local table.
	 */
	public void clearLocalTable() {
		for (ContentItem item : onlineItems) {
			contentSystem.removeContentItem(item);
		}
		onlineItems.clear();
	}

	/**
	 * Gets the online items.
	 *
	 * @return the online items
	 */
	public List<ContentItem> getOnlineItems() {
		return onlineItems;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate
	 * ()
	 */
	@Override
	public void onActivate() {
		RapidNetworkManager.setAutoReconnect(true);
		RapidNetworkManager.connect(this);
		RapidNetworkManager.getReceiverClasses().clear();
		RapidNetworkManager.getReceiverClasses().add(BasketApp.class);
		RapidNetworkManager.registerConstructionManager(MediaPlayer.class,
				new ConstructionManager() {
					
					@Override
					public HashMap<String, Object> buildConstructionInfo(
							ContentItem item) {
						HashMap<String, Object> info = new HashMap<String, Object>();
						info.put("media_url",
								((MediaPlayer) item).getMediaURL());
						info.put("video_time", ((MediaPlayer) item)
								.getVideoPlayer().getVideoTime());
						return info;
					}
					
					@Override
					public void processConstructionInfo(ContentItem item,
							HashMap<String, Object> info) {
						((MediaPlayer) item).setMediaURL((URL) info
								.get("media_url"));
						((MediaPlayer) item).getVideoPlayer().setVideoTime(
								Double.valueOf(info.get("video_time")
										.toString()));
					}
					
				});
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
	}
}
