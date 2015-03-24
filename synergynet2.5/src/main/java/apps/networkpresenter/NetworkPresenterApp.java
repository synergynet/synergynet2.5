package apps.networkpresenter;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.SketchPad;
import synergynetframework.appsystem.contentsystem.items.VideoPlayer;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messagehandler.DefaultMessageHandler;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.AnnounceTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.EnableFlickMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick.TableInfo;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick.TransferController;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.config.position.PositionConfigPrefsItem;

import com.jme.math.FastMath;
import com.jme.system.DisplaySystem;

import core.ConfigurationSystem;

/**
 * The Class NetworkPresenterApp.
 */
public class NetworkPresenterApp extends DefaultSynergyNetApp {
	
	/** The comms. */
	private TableCommsClientService comms;

	/** The content system. */
	protected ContentSystem contentSystem;

	/** The message handler. */
	protected DefaultMessageHandler messageHandler;

	/** The networked content manager. */
	protected NetworkedContentManager networkedContentManager;

	/** The players. */
	Map<String, VideoPlayer> players = new HashMap<String, VideoPlayer>();
	
	/** The transfer controller. */
	protected TransferController transferController;
	
	/**
	 * Instantiates a new network presenter app.
	 *
	 * @param info
	 *            the info
	 */
	public NetworkPresenterApp(ApplicationInfo info) {
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
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		
		int xstart = 130;
		int gap = 80;
		addVideoButton(
				"apps",
				"buttons/apps.png",
				"software_apps.avi",
				xstart,
				DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 130);
		addVideoButton("lab", "buttons/lab.png", "usability_lab.avi", xstart
				+ gap, DisplaySystem.getDisplaySystem().getRenderer()
				.getHeight() - 130);
		addVideoButton("net", "buttons/net.png", "net_content.avi", xstart
				+ (2 * gap), DisplaySystem.getDisplaySystem().getRenderer()
				.getHeight() - 130);
		addVideoButton("table", "buttons/table.png", "student_table.avi",
				xstart + (3 * gap), DisplaySystem.getDisplaySystem()
						.getRenderer().getHeight() - 130);
		addImagesButton(
				"img",
				"buttons/pics.png",
				xstart + (5 * gap),
				DisplaySystem.getDisplaySystem().getRenderer().getHeight() - 130,
				"1.jpg", "2.jpg", "3.jpg", "4.jpg");
		
	}
	
	/**
	 * Adds the images button.
	 *
	 * @param name
	 *            the name
	 * @param icon
	 *            the icon
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param images
	 *            the images
	 */
	private void addImagesButton(String name, String icon, int i, int j,
			final String... images) {

		final ImageTextLabel button = (ImageTextLabel) contentSystem
				.createContentItem(ImageTextLabel.class);
		button.setAutoFit(false);
		button.setImageInfo(NetworkPresenterApp.class.getResource(icon));
		button.setBorderSize(1);
		button.setBorderColour(Color.white);
		button.setBackgroundColour(Color.black);
		button.setRotateTranslateScalable(false);
		button.setLocation(i, j);
		button.setWidth(64);
		button.setHeight(64);
		button.addItemListener(new ItemEventAdapter() {
			private boolean loaded = false;
			
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				if (loaded) {
					return;
				}
				for (String imgres : images) {
					final ImageTextLabel img = (ImageTextLabel) contentSystem
							.createContentItem(ImageTextLabel.class);
					img.setAutoFit(false);
					img.setImageInfo(NetworkPresenterApp.class
							.getResource(imgres));
					img.setBorderSize(1);
					img.setBorderColour(Color.white);
					img.setBackgroundColour(Color.black);
					img.setWidth(300);
					img.setHeight(200);
					img.setLocation(300, 300);
					img.makeFlickable(0.5f);
					img.setTextColour(Color.white);
					img.setCRLFSeparatedString(imgres);
				}
				SketchPad pad = (SketchPad) contentSystem
						.createContentItem(SketchPad.class);
				pad.setBorderSize(0);
				pad.setWidth(300);
				pad.setHeight(200);
				pad.setSketchArea(new Rectangle(0, 40, 300, 200));
				pad.centerItem();
				pad.fillRectangle(new Rectangle(0, 0, 300, 40), Color.red);
				pad.fillRectangle(new Rectangle(270, 5, 25, 25), Color.black);
				pad.setClearArea(new Rectangle(270, 5, 25, 25));
				pad.setTextColor(Color.black);
				pad.drawString("Sketch Pad", 110, 15);
				pad.setLocation(300, 300);
				pad.makeFlickable(0.5f);
			}
		});
		
	}

	/**
	 * Adds the video button.
	 *
	 * @param name
	 *            the name
	 * @param icon
	 *            the icon
	 * @param video
	 *            the video
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 */
	private void addVideoButton(final String name, final String icon,
			final String video, final int i, final int j) {
		final ImageTextLabel button = (ImageTextLabel) contentSystem
				.createContentItem(ImageTextLabel.class);
		button.setAutoFit(false);
		button.setImageInfo(NetworkPresenterApp.class.getResource(icon));
		button.setBorderSize(1);
		button.setBorderColour(Color.white);
		button.setBackgroundColour(Color.black);
		button.setRotateTranslateScalable(false);
		button.setLocation(i, j);
		button.setWidth(64);
		button.setHeight(64);
		button.addItemListener(new ItemEventAdapter() {
			private boolean buttonOn = false;
			
			public void cursorClicked(ContentItem item, long id, float x,
					float y, float pressure) {
				buttonOn = !buttonOn;
				if (buttonOn) {
					button.setBorderColour(Color.blue);
				} else {
					button.setBorderColour(Color.white);
				}
				setVideoVisibility(name, icon, video, i, j, buttonOn);
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate
	 * ()
	 */
	@Override
	public void onActivate() {
		super.onActivate();
		startAllVisibleVideoPlayers();
		if (networkedContentManager != null) {
			return;
		}
		try {
			if (comms == null) {
				comms = (TableCommsClientService) ServiceManager.getInstance()
						.get(TableCommsClientService.class);
				List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
				receiverClasses.add(NetworkPresenterApp.class);
				networkedContentManager = new NetworkedContentManager(
						contentSystem, comms, receiverClasses);
				transferController = new TransferController(this, comms,
						networkedContentManager);
				networkedContentManager.getNetworkedFlickController()
						.setNetworkFlickEnabled(true);
				messageHandler = new DefaultMessageHandler(contentSystem,
						this.networkedContentManager);
				messageHandler.setTransferController(transferController);
			}
			if (comms != null) {
				comms.register(this, messageHandler);
			}
			if (comms != null) {
				comms.sendMessage(new ApplicationCommsRequest(
						NetworkPresenterApp.class.getName()));
			}
			
			Preferences prefs = ConfigurationSystem
					.getPreferences(PositionConfigPrefsItem.class);
			int location_x = prefs.getInt(
					PositionConfigPrefsItem.PREFS_LOCATION_X, 0);
			int location_y = prefs.getInt(
					PositionConfigPrefsItem.PREFS_LOCATION_Y, 0);
			float angle = prefs
					.getFloat(PositionConfigPrefsItem.PREFS_ANGLE, 0);
			
			if (new PositionConfigPrefsItem().getDeveloperMode()) {
				if (prefs.get(PositionConfigPrefsItem.HORIZONTAL_PLACEMENT,
						"false").equals("true")) {
					if (prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0) != 0) {
						int xPos = (prefs.getInt(
								PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0) - 1)
								% prefs.getInt(
										PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_x = xPos
								* (prefs.getInt(
										PositionConfigPrefsItem.GRID_DISTANCE_X,
										0));
						int yPos = (prefs.getInt(
								PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0) - 1)
								/ prefs.getInt(
										PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_y = yPos
								* (prefs.getInt(
										PositionConfigPrefsItem.GRID_DISTANCE_Y,
										0));
					} else {
						location_x = (prefs.getInt(
								PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0) - 1)
								* (prefs.getInt(
										PositionConfigPrefsItem.GRID_DISTANCE_X,
										0));
						location_y = 0;
					}
				} else {
					if (prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0) != 0) {
						int yPos = (prefs.getInt(
								PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0) - 1)
								% prefs.getInt(
										PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_y = yPos
								* (prefs.getInt(
										PositionConfigPrefsItem.GRID_DISTANCE_Y,
										0));
						int xPos = (prefs.getInt(
								PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0) - 1)
								/ prefs.getInt(
										PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_x = xPos
								* (prefs.getInt(
										PositionConfigPrefsItem.GRID_DISTANCE_X,
										0));
					} else {
						location_y = (prefs.getInt(
								PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0) - 1)
								* (prefs.getInt(
										PositionConfigPrefsItem.GRID_DISTANCE_Y,
										0));
						location_x = 0;
					}
				}
				// angle = 0;
			}
			
			TableInfo tableInfo = new TableInfo(
					TableIdentity.getTableIdentity(), location_x, location_y,
					angle * FastMath.DEG_TO_RAD);
			transferController.setLocalTableInfo(tableInfo);
			for (Class<?> receiverClass : networkedContentManager
					.getReceiverClasses()) {
				if (comms == null) {
					return;
				}
				comms.sendMessage(new AnnounceTableMessage(receiverClass,
						tableInfo));
				comms.sendMessage(new EnableFlickMessage(receiverClass, true));
			}
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (CouldNotStartServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Sets the video visibility.
	 *
	 * @param name
	 *            the name
	 * @param icon
	 *            the icon
	 * @param video
	 *            the video
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @param visible
	 *            the visible
	 */
	protected void setVideoVisibility(String name, String icon, String video,
			int i, int j, boolean visible) {
		VideoPlayer player = players.get(name);
		if (player == null) {
			player = (VideoPlayer) contentSystem
					.createContentItem(VideoPlayer.class);
			player.setVideoURL(NetworkPresenterApp.class.getResource(video));
			player.makeFlickable(0.5f);
			player.setBoundaryEnabled(false);
			player.centerItem();
			players.put(name, player);
		}
		
		if (!player.isVisible() && visible) {
			player.centerItem();
		}

		player.setVisible(visible);
		if (!player.isVisible()) {
			player.stop();
		} else {
			player.setVideoTime(0);
			player.play();
		}
	}
	
	/**
	 * Start all visible video players.
	 */
	private void startAllVisibleVideoPlayers() {
		for (VideoPlayer p : players.values()) {
			if (p.isVisible()) {
				p.play();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp
	 * #stateUpdate(float)
	 */
	@Override
	public void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if (networkedContentManager != null) {
			networkedContentManager.stateUpdate(tpf);
		}
		if (transferController != null) {
			transferController.update();
		}
		if (contentSystem != null) {
			contentSystem.update(tpf);
		}
	}
}
