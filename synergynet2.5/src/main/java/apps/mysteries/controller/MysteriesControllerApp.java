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

package apps.mysteries.controller;

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

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.items.BackgroundController;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEventListener;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
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
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsApplicationListener;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.config.position.PositionConfigPrefsItem;

import apps.conceptmap.GraphConfig;
import apps.conceptmap.utility.GraphManager;
import apps.control.controlmenu.ControlMenu;
import apps.mysteries.SubAppMenu;
import apps.mysteries.client.MysteryApp;
import apps.mysteries.projector.MysteryProjectorApp;

import com.jme.math.FastMath;
import com.jme.scene.Spatial;

import core.ConfigurationSystem;

public class MysteriesControllerApp extends DefaultSynergyNetApp implements NetworkedContentListener{

	protected TableCommsClientService comms;
	protected TableCommsApplicationListener messageProcessor;
	protected DefaultMessageHandler messageHandler;
	protected ContentSystem contentSystem;
	protected GraphManager graphManager;
	protected NetworkedContentManager networkedContentManager;
	protected TransferController transferController;

	protected BackgroundController backgroundController;
	protected SubAppMenu subAppMenu;

	protected String currentSubApp = "";
	protected InnerNoteController innerNoteController = new InnerNoteController();

	protected File restoreFolder;
	protected File exitSettingsFile;
	public static boolean restore = true;
	protected boolean isLogEnabled = false;

	public MysteriesControllerApp(ApplicationInfo info) {
		super(info);

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
		GraphConfig.arrowMode = LineItem.ARROW_TO_TARGET;
		graphManager = new GraphManager(contentSystem);

		SynergyNetAppUtils.addTableOverlay(this);

		backgroundController = (BackgroundController)contentSystem.createContentItem(BackgroundController.class);
		backgroundController.setOrder(Integer.MIN_VALUE);

		subAppMenu = new SubAppMenu(contentSystem);
		subAppMenu.addSubAppMenuEventListener(new SubAppMenuEventListener(){
			@Override
			public void menuSelected(String filePath, String appName) {
				loadContent(filePath, appName);
			}
		});

	}

	@Override
	public void onActivate() {

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
			this.networkedContentManager.setGraphManager(graphManager);
			this.networkedContentManager.addNetworkedContentListener(this);
			this.transferController = new TransferController(this, comms, networkedContentManager);

			ArrayList<Class<?>> controllerClasses = new ArrayList<Class<?>>();
			ArrayList<Class<?>> remoteDesktopClasses = new ArrayList<Class<?>>();
			ArrayList<Class<?>> projectorClasses = new ArrayList<Class<?>>();

			controllerClasses.add(this.getClass());
			projectorClasses.add(MysteryProjectorApp.class);
			remoteDesktopClasses.add(MysteryApp.class);

			this.networkedContentManager.createRemoteDesktopController(controllerClasses, remoteDesktopClasses);
			this.networkedContentManager.createProjectorController(controllerClasses, projectorClasses);
			@SuppressWarnings("unused")
			ControlMenu controlMenu = new ControlMenu(contentSystem, this.networkedContentManager, subAppMenu, backgroundController);


			if(messageProcessor == null) {
				messageProcessor = new DefaultMessageHandler(this.contentSystem, this.networkedContentManager);
				((DefaultMessageHandler)messageProcessor).setTransferController(transferController);
			}

			try {
				if(comms != null) {
					comms.register(this, messageProcessor);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(comms != null) comms.getCurrentlyOnline();

			final Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);
		    int location_x = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_X, 0);
		    int location_y = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_Y, 0);
		    float angle = prefs.getFloat(PositionConfigPrefsItem.PREFS_ANGLE, 0);
			try {
				TableInfo tableInfo = new TableInfo(TableIdentity.getTableIdentity(),location_x, location_y, angle * FastMath.DEG_TO_RAD);
				transferController.setLocalTableInfo(tableInfo);
				for(Class<?> receiverClass: networkedContentManager.getReceiverClasses()){
					comms.sendMessage( new AnnounceTableMessage(receiverClass, tableInfo));
					System.out.println("client sent: AnnounceTableMessage");
				}
			} catch (IOException e) {
				 
				e.printStackTrace();
			}
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

	@SuppressWarnings("unused")
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

	public void loadContent(String filePath, String name){

		this.removeAllItems();
		networkedContentManager.loadLocalContent(filePath, name);

		this.contentLoaded();
	}

	@Override
	public void contentLoaded() {
		addSynchronisedDateListeners();
		this.innerNoteController.addNoteController(networkedContentManager.getOnlineItems().values());
	}

	public void removeAllItems(){
		this.innerNoteController.removeAllNoteEditors();
	}

	@Override
	public void renderSynchronisedDate(ContentItem item,
			Map<String, String> itemAttrs) {
		SynchronisedDataRender.render((OrthoContentItem)item, itemAttrs, this.innerNoteController);
		SynchronisedDataRender.renderNote((OrthoContentItem)item, itemAttrs, this.innerNoteController);

	}

	@Override
	public void remoteContentLoaded(RemoteDesktop remoteDesktop) {
		 

	}


	@Override
	public void renderRemoteDesktop(RemoteDesktop remoteDesktop,
			OrthoContentItem item, Map<String, String> itemAttrs) {
		SynchronisedDataRender.renderRemoteDesktop(remoteDesktop, (OrthoContentItem)item, itemAttrs, this.innerNoteController);
		SynchronisedDataRender.renderRemoteDesktopNote(remoteDesktop, (OrthoContentItem)item, itemAttrs, this.innerNoteController);
	}

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

	@Override
	public void channelSwitched() {}


	@Override
	public void contentItemLoaded(ContentItem item) {
		this.innerNoteController.addNoteController(item);
		addItemListeners(item);
	}



}
