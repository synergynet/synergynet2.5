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

package apps.remotecontrol.networkmanager.managers.syncmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import apps.remotecontrol.networkmanager.messages.UnicastSyncDataPortalMessage;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SketchPad;
import synergynetframework.appsystem.contentsystem.items.SketchPad.DrawListener;
import synergynetframework.appsystem.contentsystem.items.listener.BringToTopListener;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;


/**
 * The Class SyncManager.
 */
public class SyncManager{

	/** The Constant SYNC_ITEM_ANGLE. */
	public static final short SYNC_ITEM_ANGLE = 1;
	
	/** The Constant SYNC_ITEM_SCALE. */
	public static final short SYNC_ITEM_SCALE = 2;
	
	/** The Constant SYNC_ITEM_LOCATION_X. */
	public static final short SYNC_ITEM_LOCATION_X = 3;
	
	/** The Constant SYNC_ITEM_LOCATION_Y. */
	public static final short SYNC_ITEM_LOCATION_Y = 4;
	
	/** The Constant SYNC_ITEM_LOCATION_Z. */
	public static final short SYNC_ITEM_LOCATION_Z = 5;
	
	/** The Constant SYNC_ITEM_SORTORDER. */
	public static final short SYNC_ITEM_SORTORDER = 6;
	
	/** The Constant SYNC_ITEM_DRAW_DATA. */
	public static final short SYNC_ITEM_DRAW_DATA = 7;
	
	/** The Constant SYNC_ITEM_CLEAR_PAD. */
	public static final short SYNC_ITEM_CLEAR_PAD = 8;
	
	/** The network manager. */
	protected NetworkedContentManager networkManager;
	
	/** The sync data. */
	protected Map<String, Map<Short, Object>> syncData = new HashMap<String, Map<Short, Object>>();
	
	/** The sync tables. */
	protected List<TableIdentity> syncTables = new ArrayList<TableIdentity>();

	/**
	 * Instantiates a new sync manager.
	 *
	 * @param networkManager the network manager
	 */
	public SyncManager(NetworkedContentManager networkManager){
		this.networkManager = networkManager;
	}
	
	/**
	 * Register sync table.
	 *
	 * @param tableId the table id
	 */
	public void registerSyncTable(TableIdentity tableId){
		if(!syncTables.contains(tableId)) syncTables.add(tableId);
	}
	
	/**
	 * Unregister sync table.
	 *
	 * @param tableId the table id
	 */
	public void unregisterSyncTable(TableIdentity tableId){
		syncTables.remove(tableId);
	}
	
	/**
	 * Adds the sync listeners.
	 *
	 * @param item the item
	 */
	public void addSyncListeners(ContentItem item){

		OrthoContentItem orthoItem = (OrthoContentItem)item;
		orthoItem.addBringToTopListener(new BringToTopListener(){
			@Override
			public void itemBringToToped(ContentItem item) {
				this.registerItem(item);
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SORTORDER, String.valueOf(1000));
				
			}
			
			private void registerItem(ContentItem item){
				if (syncData.get(item.getName())==null){
					syncData.put(item.getName(), new HashMap<Short, Object>());
				}
			}
			
		});
		orthoItem.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleListener(){

			@Override
			public void itemRotated(ContentItem item, float newAngle,
					float oldAngle) {
				SyncManager.this.registerItem(item);
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_ANGLE, newAngle);	
			}

			@Override
			public void itemScaled(ContentItem item, float newScaleFactor,
					float oldScaleFactor) {
				SyncManager.this.registerItem(item);
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SCALE, newScaleFactor);		

			}

			@Override
			public void itemTranslated(ContentItem item,
					float newLocationX, float newLocationY,
					float oldLocationX, float oldLocationY) {
				SyncManager.this.registerItem(item);
				Location loc = new Location(newLocationX, newLocationY,0);
				if(((OrthoContentItem)item).getParent() != null) loc = ((OrthoContentItem)item).getLocation();
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_X, loc.x);		
				syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_Y, loc.y);

			}
		});
		
		if(orthoItem instanceof SketchPad){
			final SketchPad pad = ((SketchPad)orthoItem);
			pad.addDrawListener(new DrawListener(){

				@SuppressWarnings("unchecked")
				@Override
				public void itemDrawn(DrawData drawData) {
					SyncManager.this.registerItem(pad);
					if(!syncData.get(pad.getName()).containsKey(SyncManager.SYNC_ITEM_DRAW_DATA))
						syncData.get(pad.getName()).put(SyncManager.SYNC_ITEM_DRAW_DATA, new ArrayList<DrawData>());
					((ArrayList<DrawData>)syncData.get(pad.getName()).get(SyncManager.SYNC_ITEM_DRAW_DATA)).add(drawData);
				}

				@Override
				public void padCleared() {
					SyncManager.this.registerItem(pad);
					syncData.get(pad.getName()).put(SyncManager.SYNC_ITEM_CLEAR_PAD, "clear");
				}
			});
		}
	}
	
	/**
	 * Register item.
	 *
	 * @param item the item
	 */
	private void registerItem(ContentItem item){
		if (syncData.get(item.getName())==null){
			syncData.put(item.getName(), new HashMap<Short, Object>());
		}
	}
	
	/**
	 * Unregister content item.
	 *
	 * @param item the item
	 */
	public void unregisterContentItem(ContentItem item) {
		((OrthoContentItem)item).removeBringToTopListeners();
		((OrthoContentItem)item).removeOrthoControlPointRotateTranslateScaleListeners();
	}
	
	/**
	 * Update.
	 */
	public void update() {
		if(!syncTables.isEmpty() && !syncData.isEmpty()){
			for(TableIdentity tableId: syncTables){
				for(Class<?> receiverClass: networkManager.getReceiverClasses()){
					UnicastSyncDataPortalMessage msg = new UnicastSyncDataPortalMessage(receiverClass, tableId, syncData);
					networkManager.sendMessage(msg);
				}
			}
			syncData.clear();
		}
	}

	/**
	 * Update sync data.
	 */
	public void updateSyncData() {
		for(ContentItem item: networkManager.onlineItemsList.values()){
			this.registerItem(item);
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_ANGLE, String.valueOf(item.getAngle()));
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SCALE, String.valueOf(item.getScale()));
			Location loc = ((OrthoContentItem)item).getLocation();
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_X, String.valueOf(loc.x));		
			syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_Y, String.valueOf(loc.y));

		}
	}


	// Testing methods
	
	/**
	 * Fire item scaled.
	 *
	 * @param item the item
	 */
	public void fireItemScaled(ContentItem item){
		SyncManager.this.registerItem(item);
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_SCALE, String.valueOf(item.getScale()));		
		
	}

	/**
	 * Fire item rotated.
	 *
	 * @param item the item
	 */
	public void fireItemRotated(ContentItem item) {
		SyncManager.this.registerItem(item);
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_ANGLE, String.valueOf(item.getAngle()));	
	}

	/**
	 * Fire item moved.
	 *
	 * @param item the item
	 */
	public void fireItemMoved(ContentItem item) {
		SyncManager.this.registerItem(item);
		Location loc = new Location(item.getLocalLocation().x, item.getLocalLocation().y,0);
		if(((OrthoContentItem)item).getParent() != null) loc = ((OrthoContentItem)item).getLocation();
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_X, String.valueOf(loc.x));		
		syncData.get(item.getName()).put(SyncManager.SYNC_ITEM_LOCATION_Y, String.valueOf(loc.y));

	}


}
