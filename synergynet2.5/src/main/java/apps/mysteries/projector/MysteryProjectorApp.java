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

package apps.mysteries.projector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import apps.mysteries.controller.MysteriesControllerApp;

import com.jme.scene.Spatial;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEditor;
import synergynetframework.appsystem.services.ServiceManager;
import synergynetframework.appsystem.services.exceptions.CouldNotStartServiceException;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener;
import synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentManager;
import synergynetframework.appsystem.services.net.networkedcontentmanager.messagehandler.DefaultMessageHandler;
import synergynetframework.appsystem.services.net.networkedcontentmanager.synchroniseddatarender.SynchronisedDataRender;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.RemoteDesktop;
import synergynetframework.appsystem.services.net.tablecomms.client.TableCommsClientService;
import synergynetframework.appsystem.services.net.tablecomms.messages.control.fromclient.ApplicationCommsRequest;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;


/**
 * The Class MysteryProjectorApp.
 */
public class MysteryProjectorApp extends DefaultSynergyNetApp implements NetworkedContentListener {

	
	/** The comms. */
	private TableCommsClientService comms;
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The message handler. */
	protected DefaultMessageHandler messageHandler;
	
	/** The networked content manager. */
	protected NetworkedContentManager networkedContentManager;
	
	/** The inner note controller. */
	protected InnerNoteController innerNoteController;
	
	/** The restore folder. */
	private File restoreFolder;
	
	/** The exit settings file. */
	private File exitSettingsFile;
	
	/** The restore. */
	public static boolean restore = true;
	
	/** The current sub app. */
	protected String currentSubApp = "";
	
	/** The is log enabled. */
	private boolean isLogEnabled = false;
	

	/**
	 * Instantiates a new mystery projector app.
	 *
	 * @param info the info
	 */
	public MysteryProjectorApp(ApplicationInfo info) {
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

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);
		SynergyNetAppUtils.addTableOverlay(this);

	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
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
			receiverClasses.add(MysteryProjectorApp.class);
			this.networkedContentManager = new NetworkedContentManager(contentSystem, comms, receiverClasses);
			this.networkedContentManager.addNetworkedContentListener(this);
			
			ArrayList<Class<?>> controllerClasses = new ArrayList<Class<?>>();
			ArrayList<Class<?>> projectorClasses = new ArrayList<Class<?>>();
			controllerClasses.add(MysteriesControllerApp.class);
			projectorClasses.add(this.getClass());
			
			this.networkedContentManager.createProjectorController(controllerClasses, projectorClasses);
			messageHandler = new DefaultMessageHandler(contentSystem, this.networkedContentManager);
			try {
				if(comms != null) comms.register(this, messageHandler);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			if(comms != null) comms.sendMessage(new ApplicationCommsRequest(MysteryProjectorApp.class.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
	public void stateUpdate(float tpf) {
		
		
		
		if(networkedContentManager!= null) networkedContentManager.stateUpdate(tpf);	
		if(contentSystem != null) contentSystem.update(tpf);
		if(currentSubApp != null && isLogEnabled){
			try {
				logContentState(currentSubApp);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onDeactivate()
	 */
	@Override
	public void onDeactivate() {}

	
	/**
	 * Load content.
	 *
	 * @param filePath the file path
	 * @param name the name
	 */
	public void loadContent(String filePath, String name){
		
		this.removeAllItems();
		networkedContentManager.loadLocalContent(filePath, name);		
		
		this.contentLoaded();		
	}
	
	
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener#contentLoaded()
	 */
	@Override
	public void contentLoaded() {}
	
	/**
	 * Removes the all items.
	 */
	public void removeAllItems(){	
		this.innerNoteController.removeAllNoteEditors();		
		networkedContentManager.removeAllItems();
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener#renderSynchronisedDate(synergynetframework.appsystem.contentsystem.items.ContentItem, java.util.Map)
	 */
	@Override
	public void renderSynchronisedDate(ContentItem item,
			Map<String, String> itemAttrs) {
		SynchronisedDataRender.render((OrthoContentItem)item, itemAttrs, this.innerNoteController);
		SynchronisedDataRender.renderNote((OrthoContentItem)item, itemAttrs, this.innerNoteController);
		if (innerNoteController.getNodeEditor((QuadContentItem)item)!= null){	
			InnerNoteEditor innerNoteEditor = innerNoteController.getNodeEditor((QuadContentItem)item);
			innerNoteEditor.removeInnerNoteEventListeners();
			innerNoteEditor.getNoteNode().setRotateTranslateScalable(false);
			innerNoteEditor.getNoteNode().removeItemListerners();
		}
	}
	
	/**
	 * Log content state.
	 *
	 * @param mysteryID the mystery id
	 * @throws FileNotFoundException the file not found exception
	 */
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
	
	/**
	 * Check last exit settings.
	 */
	private void checkLastExitSettings(){
		try{
			if(!exitSettingsFile.exists()) exitSettingsFile.createNewFile();
			FileInputStream is = new FileInputStream(exitSettingsFile);
			Properties properties = new Properties();
			properties.load(is);
			String isRestore = properties.getProperty("restore");
			is.close();
			if(isRestore != null && isRestore.equals("1"))	
				MysteryProjectorApp.restore = true;
			else {
				MysteryProjectorApp.restore = false;
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
	/*
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
	*/

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener#channelSwitched()
	 */
	@Override
	public void channelSwitched() {
	
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener#contentItemLoaded(synergynetframework.appsystem.contentsystem.items.ContentItem)
	 */
	@Override
	public void contentItemLoaded(ContentItem item) {
		
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener#remoteContentLoaded(synergynetframework.appsystem.services.net.networkedcontentmanager.utils.RemoteDesktop)
	 */
	@Override
	public void remoteContentLoaded(RemoteDesktop remoteDesktop) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.services.net.networkedcontentmanager.NetworkedContentListener#renderRemoteDesktop(synergynetframework.appsystem.services.net.networkedcontentmanager.utils.RemoteDesktop, synergynetframework.appsystem.contentsystem.items.OrthoContentItem, java.util.Map)
	 */
	@Override
	public void renderRemoteDesktop(RemoteDesktop remoteDesktop,
			OrthoContentItem item, Map<String, String> map) {
		
	}



	
}
