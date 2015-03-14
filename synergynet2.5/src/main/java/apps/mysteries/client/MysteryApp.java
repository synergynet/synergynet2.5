/*

 * Copyright (c) 2009 University of Durham, England
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'SynergyNet' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mysteries.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

import apps.mysteries.SubAppMenu;
import apps.mysteries.controller.MysteriesControllerApp;

import com.jme.math.FastMath;
import com.jme.scene.Spatial;

import core.ConfigurationSystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.items.BackgroundController;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ListContainer;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SubAppMenuEventListener;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messagehandler.DefaultMessageHandler;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messages.networkedflick.AnnounceTableMessage;
import synergynetframework.appsystem.services.net.networkedcontentmanager.synchroniseddatarender.SynchronisedDataRender;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.RemoteDesktop;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick.TableInfo;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.networkedflick.TransferController;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.config.position.PositionConfigPrefsItem;

public class MysteryApp extends DefaultSynergyNetApp implements NetworkedContentListener {

	public final String SUB_APP_LONDONFIRE = "LONDON FIRE";
	public final String SUB_APP_TIPPINGWAITRESSES = "TIPPING WAITRESSES";
	public final String SUB_APP_ROBERTDIXON = "ROBERT DIXON";
	public final String SUB_APP_DINNERDISASTER = "DINNER DISASTER";
	public final String SUB_APP_WALTZER = "WALTZER";
	public final String SUB_APP_SNEAKYSYDNEY = "SNEAKY SYDNEY";
	public final String SUB_APP_SCHOOLTRIP = "SCHOOL TRIP";

	private TableCommsClientService comms;
	protected ContentSystem contentSystem;
	protected DefaultMessageHandler messageHandler;
	protected NetworkedContentManager networkedContentManager;
	protected TransferController transferController;
	protected SubAppMenu subAppMenu;
	protected ListContainer menu;

	protected InnerNoteController innerNoteController;

	private File restoreFolder;
	private File exitSettingsFile;
	public static boolean restore = true;
	protected String currentSubApp = "";
	private boolean isLogEnabled = false;

	protected MultiLineTextLabel channeLabel;

	public MysteryApp(ApplicationInfo info) {
		super(info);

		innerNoteController = new InnerNoteController();

		restoreFolder = new File(getApplicationDataDirectory(), "restore");
		exitSettingsFile = new File(getApplicationDataDirectory(), "safeExitSettings.properties");
		checkLastExitSettings();

		/*
		if(restore){
			for(String mysteryID: mysteryIDToXMLPath.keySet())
				mysteriesToRestore.add(mysteryID);
		}
		 */

	}

	@Override
	public void addContent() {

		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		SynergyNetAppUtils.addTableOverlay(this);

		BackgroundController backgroundController;
		backgroundController = (BackgroundController)contentSystem.createContentItem(BackgroundController.class);
		backgroundController.setOrder(Integer.MIN_VALUE);

		subAppMenu = new SubAppMenu(contentSystem);
		subAppMenu.addSubAppMenuEventListener(new SubAppMenuEventListener(){
			@Override
			public void menuSelected(String filePath, String appName) {
				loadContent(filePath, appName);
			}
		});



		SimpleButton backToMainMenuButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		backToMainMenuButton.setAutoFitSize(false);
		backToMainMenuButton.setText("BACK TO MAIN MENU");
		backToMainMenuButton.setBackgroundColour(Color.lightGray);
		backToMainMenuButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				setSafeExit();
				exitApp();
			}
		});

		SimpleButton closeMenuButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		closeMenuButton.setAutoFitSize(false);
		closeMenuButton.setText("HIDE MENU");
		closeMenuButton.setBackgroundColour(Color.lightGray);
		closeMenuButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonClicked(SimpleButton b, long id, float x, float y, float pressure) {
				menu.setVisible(false);
			}
		});

		menu = subAppMenu.getSubAppMenu();
		menu.addSubItem(backToMainMenuButton);
		menu.addSubItem(closeMenuButton);
		menu.setLocalLocation(200, 200);
		menu.setBackgroundColour(Color.blue);

		backgroundController.addItemListener(new ItemEventAdapter(){

			boolean topRightCornerSelected = false;

			@Override
			public void cursorPressed(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorPressed(item, id, x, y, pressure);
				Rectangle rightTopCorner = new Rectangle(contentSystem.getScreenWidth()-30, 0, contentSystem.getScreenWidth(), 30);
				if (rightTopCorner.contains(x, y)) topRightCornerSelected = true;
			}

			@Override
			public void cursorReleased(ContentItem item, long id, float x,
					float y, float pressure) {
				super.cursorReleased(item, id, x, y, pressure);
				Rectangle rightTopCorner = new Rectangle(contentSystem.getScreenWidth()-30, contentSystem.getScreenWidth(), 30, 30);
				if (rightTopCorner.contains(x, y)) topRightCornerSelected = false;

			}



			@Override
			public void cursorLongHeld(ContentItem b, long id, float x, float y, float pressure) {
				Rectangle rightBottomCorner = new Rectangle(0, contentSystem.getScreenHeight()-30, 30, contentSystem.getScreenHeight());
				if (!rightBottomCorner.contains(x, y)) return;
				if (!topRightCornerSelected) return;
				if (!networkedContentManager.isMenuEnabled()){

					menu.setVisible(false);
					return;
				}
				if (menu.isVisible()){
					menu.setVisible(false);
				}
				else{
					menu.setVisible(true);
					menu.setLocalLocation(x, contentSystem.getScreenHeight()-y);
				}
			}
		});

		currentSubApp=this.SUB_APP_LONDONFIRE;

		channeLabel = (MultiLineTextLabel)this.contentSystem.createContentItem(MultiLineTextLabel.class);
		channeLabel.setBackgroundColour(Color.black);
		channeLabel.setBorderSize(0);
		channeLabel.setBorderColour(Color.red);
		channeLabel.setTextColour(Color.red);
		channeLabel.setLines(String.valueOf("Local Desktop"), 1);
		channeLabel.setFont(new Font("Arial", Font.PLAIN,30));
		channeLabel.setLocalLocation(100, 700);
		channeLabel.setBringToTopable(false);
		channeLabel.setRotateTranslateScalable(false);

		channeLabel.setVisible(false);
	}

	@Override
	public void onActivate() {
		if (networkedContentManager!=null) return;

		if(comms == null) {

			try {
				comms = (TableCommsClientService) ServiceManager.getInstance().get(TableCommsClientService.class);
			} catch (CouldNotStartServiceException e1) {
				e1.printStackTrace();
			}

			List<Class<?>> receiverClasses = new ArrayList<Class<?>>();
			receiverClasses.add(MysteriesControllerApp.class);
			receiverClasses.add(MysteryApp.class);
			this.networkedContentManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
			this.networkedContentManager.addNetworkedContentListener(this);
			this.networkedContentManager.enableMenuController(true, menu);
			this.networkedContentManager.setbackupListEnabled(true);

			ArrayList<Class<?>> controllerClasses = new ArrayList<Class<?>>();
			ArrayList<Class<?>> remoteDesktopClasses = new ArrayList<Class<?>>();
			controllerClasses.add(MysteriesControllerApp.class);
			remoteDesktopClasses.add(this.getClass());

			this.networkedContentManager.createRemoteDesktopController(controllerClasses, remoteDesktopClasses);
			this.transferController = new TransferController(this, comms, networkedContentManager);

			messageHandler = new DefaultMessageHandler(contentSystem, this.networkedContentManager);
			((DefaultMessageHandler)messageHandler).setTransferController(transferController);

			try {
				if(comms != null) {
					comms.register(this, messageHandler);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if(comms != null) comms.sendMessage(new ApplicationCommsRequest(MysteryApp.class.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		final Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);
		int location_x = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_X, 0);
		int location_y = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_Y, 0);
		float angle = prefs.getFloat(PositionConfigPrefsItem.PREFS_ANGLE, 0);
		try {
			TableInfo tableInfo = new TableInfo(TableIdentity.getTableIdentity(),location_x, location_y, angle * FastMath.DEG_TO_RAD);
			transferController.setLocalTableInfo(tableInfo);
			for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
				if(comms == null) return;
				comms.sendMessage( new AnnounceTableMessage(receiverClass, tableInfo));
				System.out.println("client sent: AnnounceTableMessage");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void stateUpdate(float tpf) {

		if(networkedContentManager!= null) networkedContentManager.stateUpdate(tpf);
		if(transferController != null) transferController.update();
		if(contentSystem != null) contentSystem.update(tpf);
		if(currentSubApp != null && isLogEnabled){
			try {
				logContentState(currentSubApp);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onDeactivate() {}


	public void loadContent(String filePath, String name){

		this.removeAllItems();
		networkedContentManager.loadLocalContent(filePath, name);

		this.contentLoaded();
	}

	@Override
	public void renderSynchronisedDate(ContentItem item,
			Map<String, String> itemAttrs) {
		SynchronisedDataRender.render((OrthoContentItem)item, itemAttrs, this.innerNoteController);
		SynchronisedDataRender.renderNote((OrthoContentItem)item, itemAttrs, this.innerNoteController);
	}

	@Override
	public void contentLoaded() {
		//addSynchronisedDateListeners();
		//this.innerNoteController.addNoteController(networkedContentManager.getOnlineItems().values());
	}

	public void removeAllItems(){
		//this.innerNoteController.removeAllNoteEditors();
	}

	@SuppressWarnings("unused")
	private void addSynchronisedDateListeners(){

		//add note listener
		this.innerNoteController.addInnerNoteEventListener(new InnerNoteEventListener(){

			@Override
			public void noteBringToTop(ContentItem edittedItem) {
				this.registerItem(edittedItem);
				networkedContentManager.getSychronisedData().get(edittedItem.getName()).put(AttributeConstants.ITEM_SORTORDER, String.valueOf(1000));
			}

			@Override
			public void noteChanged(ContentItem item, String text) {
				this.registerItem(item);
				networkedContentManager.getSychronisedData().get(item.getName()).put(AttributeConstants.ITEM_INNER_NOTE, text);
			}

			@Override
			public void noteLabelOn(ContentItem item, boolean noteLabelOn) {
				this.registerItem(item);
				networkedContentManager.getSychronisedData().get(item.getName()).put(AttributeConstants.ITEM_INNER_NOTE_ON, String.valueOf(noteLabelOn));
			}

			@Override
			public void noteRotated(ContentItem edittedItem, float newAngle,
					float oldAngle) {
				this.registerItem(edittedItem);
				networkedContentManager.getSychronisedData().get(edittedItem.getName()).put(AttributeConstants.ITEM_ANGLE, String.valueOf(newAngle));

			}

			@Override
			public void noteScaled(ContentItem edittedItem,
					float newScaleFactor, float oldScaleFactor) {
				this.registerItem(edittedItem);
				networkedContentManager.getSychronisedData().get(edittedItem.getName()).put(AttributeConstants.ITEM_SCALE, String.valueOf(newScaleFactor));

			}

			@Override
			public void noteTranslated(ContentItem edittedItem,
					float newLocationX, float newLocationY, float oldLocationX,
					float oldLocationY) {
				this.registerItem(edittedItem);
				networkedContentManager.getSychronisedData().get(edittedItem.getName()).put(AttributeConstants.ITEM_LOCATION_X, String.valueOf(newLocationX));
				networkedContentManager.getSychronisedData().get(edittedItem.getName()).put(AttributeConstants.ITEM_LOCATION_Y, String.valueOf(newLocationY));

			}

			private void registerItem(ContentItem item){
				if (networkedContentManager.getSychronisedData().get(item.getName())==null){
					networkedContentManager.getSychronisedData().put(item.getName(), new HashMap<String, String>());
				}
			}

		});

		for (ContentItem item:networkedContentManager.getOnlineItems().values()) addItemListeners(item);

	}

	private void addItemListeners(ContentItem item){
		OrthoContentItem orthoItem = (OrthoContentItem)item;
		orthoItem.addBringToTopListener(new BringToTopListener(){

			@Override
			public void itemBringToToped(ContentItem item) {
				this.registerItem(item);
				/*
				((OrthoContentItem)item).setAsTopObject();
				if (innerNoteController.getNodeEditor((QuadContentItem)item)!= null){
					InnerNoteEditor innerNoteEditor = innerNoteController.getNodeEditor((QuadContentItem)item);
					innerNoteEditor.setAsTopObject();
				}*/
				networkedContentManager.getSychronisedData().get(item.getName()).put(AttributeConstants.ITEM_SORTORDER, String.valueOf(1000));

			}

			private void registerItem(ContentItem item){
				if (networkedContentManager.getSychronisedData().get(item.getName())==null){
					networkedContentManager.getSychronisedData().put(item.getName(), new HashMap<String, String>());
				}
			}

		});
		orthoItem.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

			@Override
			public void itemRotated(ContentItem item, float newAngle,
					float oldAngle) {
				this.registerItem(item);
				networkedContentManager.getSychronisedData().get(item.getName()).put(AttributeConstants.ITEM_ANGLE, String.valueOf(newAngle));
			}

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,
					float oldScaleFactor) {
				this.registerItem(item);
				networkedContentManager.getSychronisedData().get(item.getName()).put(AttributeConstants.ITEM_SCALE, String.valueOf(newScaleFactor));

			}

			@Override
			public void itemTranslated(ContentItem item,
					float newLocationX, float newLocationY,
					float oldLocationX, float oldLocationY) {
				this.registerItem(item);
				networkedContentManager.getSychronisedData().get(item.getName()).put(AttributeConstants.ITEM_LOCATION_X, String.valueOf(newLocationX));
				networkedContentManager.getSychronisedData().get(item.getName()).put(AttributeConstants.ITEM_LOCATION_Y, String.valueOf(newLocationY));

			}

			private void registerItem(ContentItem item){
				if (networkedContentManager.getSychronisedData().get(item.getName())==null){
					networkedContentManager.getSychronisedData().put(item.getName(), new HashMap<String, String>());
				}
			}

		});
	}

	private void logContentState(String mysteryID) throws FileNotFoundException {
		if(!restoreFolder.exists()) restoreFolder.mkdir();
		File restoreFile = new File(restoreFolder, mysteryID);
		PrintWriter pw = new PrintWriter(new FileOutputStream(restoreFile));
		pw.println("# Last state for app ID: " + mysteryID);
		pw.println("# Storing started at " + new Date().toString());
		pw.println("# Format is as follows:");
		pw.println("# content Item name, location x, location y, location z, scale x, scale y, scale z, rotation x, rotation y, rotation z, rotation w, zOrder");
		for(ContentItem item: contentSystem.getAllContentItems().values()) {

			Spatial spatial = (Spatial)item.getImplementationObject();
			pw.print(spatial.getName() + ",");
			pw.print(spatial.getLocalTranslation().x + ",");
			pw.print(spatial.getLocalTranslation().y + ",");
			pw.print(spatial.getLocalTranslation().z + ",");
			pw.print(spatial.getLocalScale().x + ",");
			pw.print(spatial.getLocalScale().y + ",");
			pw.print(spatial.getLocalScale().z + ",");
			pw.print(spatial.getLocalRotation().x + ",");
			pw.print(spatial.getLocalRotation().y + ",");
			pw.print(spatial.getLocalRotation().z + ",");
			pw.print(spatial.getLocalRotation().w + ",");
			pw.println(spatial.getZOrder());
		}
		pw.close();
	}

	private void checkLastExitSettings(){
		try{
			if(!exitSettingsFile.exists()) exitSettingsFile.createNewFile();
			FileInputStream is = new FileInputStream(exitSettingsFile);
			Properties properties = new Properties();
			properties.load(is);
			String isRestore = properties.getProperty("restore");
			is.close();
			if(isRestore != null && isRestore.equals("1"))
				MysteryApp.restore = true;
			else {
				MysteryApp.restore = false;
				properties.setProperty("restore", "1");
				FileOutputStream os = new FileOutputStream(exitSettingsFile);
				properties.store(os, null);
				os.close();
			}
		}
		catch(IOException exp){
			exp.printStackTrace();
		}
	}

	private void setSafeExit(){
		try{
			if(!exitSettingsFile.exists()) exitSettingsFile.createNewFile();
			FileOutputStream os = new FileOutputStream(exitSettingsFile);
			Properties properties = new Properties();
			properties.setProperty("restore", "0");
			properties.store(os, "Safe exit on "+ new Date());
			os.close();
		}
		catch(IOException exp){
			exp.printStackTrace();
		}
	}

	@Override
	public void channelSwitched() {
		this.channeLabel.setLines(this.networkedContentManager.getCurrentScreenChannel(), 1);

	}

	@Override
	public void remoteContentLoaded(RemoteDesktop remoteDesktop) {}


	@Override
	public void renderRemoteDesktop(RemoteDesktop remoteDesktop, OrthoContentItem item, Map<String, String> map) {}

	@Override
	public void contentItemLoaded(ContentItem item) {
		this.innerNoteController.addNoteController(item);
		addItemListeners(item);
	}

}
