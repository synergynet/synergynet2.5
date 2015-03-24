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

package apps.rapidnetworking;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.prefs.Preferences;

import common.CommonResources;
import core.ConfigurationSystem;



import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MediaPlayer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.SketchPad;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.RapidNetworkManager;
import synergynetframework.appsystem.services.net.rapidnetworkmanager.constructionmanagers.ConstructionManager;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.config.position.PositionConfigPrefsItem;


/**
 * The Class RapidNetworkingApp.
 */
public class RapidNetworkingApp extends DefaultSynergyNetApp{


	/**
	 * Instantiates a new rapid networking app.
	 *
	 * @param info the info
	 */
	public RapidNetworkingApp(ApplicationInfo info) {
		super(info);
		// TODO Auto-generated constructor stub
	}

	/** The content system. */
	private ContentSystem contentSystem;


	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);	
		setMenuController(new HoldTopRightConfirmVisualExit(this));
		
		final MediaPlayer video = (MediaPlayer)contentSystem.createContentItem(MediaPlayer.class);
		video.setMediaURL(CommonResources.class.getResource("smallvid.mp4"));
		video.centerItem();
		video.getVideoPlayer().setBoundaryEnabled(false);
		video.makeFlickable(0.5f);
		
		RapidNetworkManager.registerConstructionManager(MediaPlayer.class, new ConstructionManager(){

			@Override
			public HashMap<String, Object> buildConstructionInfo(ContentItem item) {
				HashMap<String, Object> info = new HashMap<String, Object>();
				info.put("media_url", ((MediaPlayer)item).getMediaURL());
				info.put("video_time", ((MediaPlayer)item).getVideoPlayer().getVideoTime());
				return info;
			}

			@Override
			public void processConstructionInfo(ContentItem item, HashMap<String, Object> info) {
				((MediaPlayer)item).setMediaURL((URL)info.get("media_url"));
				((MediaPlayer)item).getVideoPlayer().setVideoTime(Double.valueOf(info.get("video_time").toString()));
			}
	
		});
		
		final SketchPad pad = (SketchPad) this.contentSystem.createContentItem(SketchPad.class);
		pad.setBorderSize(0);
		pad.setWidth(300);
		pad.setHeight(200);
		pad.setSketchArea(new Rectangle(0,40,300,200));
		pad.centerItem();
		pad.fillRectangle(new Rectangle(0,0,300,40), Color.red);
		pad.fillRectangle(new Rectangle(270,5,25,25), Color.black);
		pad.setClearArea(new Rectangle(270,5,25,25));
		pad.setTextColor(Color.black);
		pad.drawString("Sketch Pad", 110, 15);
		

		SimpleButton broadcastBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		broadcastBtn.setText("boradcast pad");
		broadcastBtn.setLocation(60,20);
		broadcastBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
					
					try {
						RapidNetworkManager.broadcastItem(pad);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
			}
		
		});

		SimpleButton shareButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		shareButton.setText("share pad");
		shareButton.setLocation(180,20);
		shareButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(RapidNetworkManager.getTableCommsClientService() != null){
					for(TableIdentity tableId: RapidNetworkManager.getTableCommsClientService().getCurrentlyOnline())
						try {
							RapidNetworkManager.shareItem(pad, tableId, RapidNetworkManager.SyncType.BIDIRECTIONAL_SYNC);
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		
		});
		
		SimpleButton unshareButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		unshareButton.setText("unshare pad");
		unshareButton.setLocation(300,20);
		unshareButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(RapidNetworkManager.getTableCommsClientService() != null){
					for(TableIdentity tableId: RapidNetworkManager.getTableCommsClientService().getCurrentlyOnline())
						try {
							RapidNetworkManager.unshareItem(pad, tableId, RapidNetworkManager.SyncType.BIDIRECTIONAL_SYNC);
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
		
		});
		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
		RapidNetworkManager.getReceiverClasses().add(RapidNetworkingApp.class);
		RapidNetworkManager.setAutoReconnect(true);
		RapidNetworkManager.connect(this);
		while(!RapidNetworkManager.isConnected) {}
		try {
			Preferences prefs = ConfigurationSystem.getPreferences(PositionConfigPrefsItem.class);
			int location_x = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_X, 0);
			int location_y = prefs.getInt(PositionConfigPrefsItem.PREFS_LOCATION_Y, 0);
			float angle = prefs.getFloat(PositionConfigPrefsItem.PREFS_ANGLE, 0);
			
			if (new PositionConfigPrefsItem().getDeveloperMode()){
				if (prefs.get(PositionConfigPrefsItem.HORIZONTAL_PLACEMENT, "false").equals("true")){
					if(prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0) != 0){
						int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_x = xPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
						int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_X, 0);
						location_y = yPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
					}else{
						location_x = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
						location_y = 0;
					}
				}else{
					if(prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0) != 0){
						int yPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) % prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_y = yPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
						int xPos = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) / prefs.getInt(PositionConfigPrefsItem.GRID_LIMIT_Y, 0);
						location_x = xPos * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_X, 0));
					}else{
						location_y = (prefs.getInt(PositionConfigPrefsItem.CURRENT_CONNECTIONS, 0)-1) * (prefs.getInt(PositionConfigPrefsItem.GRID_DISTANCE_Y, 0));
						location_x = 0;
					}
				}
				angle = 0;
			}
			RapidNetworkManager.setTablePosition(location_x, location_y);
			RapidNetworkManager.setTableOrientation(angle);
			RapidNetworkManager.enableNetworkFlick(true);
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		
	}
	
		
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
		RapidNetworkManager.update();
	}


}
