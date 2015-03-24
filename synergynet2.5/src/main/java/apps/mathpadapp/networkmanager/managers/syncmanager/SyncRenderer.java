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

package apps.mathpadapp.networkmanager.managers.syncmanager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;


/**
 * The Class SyncRenderer.
 */
public class SyncRenderer {
	
	/** The Constant SYNC_LOCATION_X. */
	public static final short SYNC_LOCATION_X = 0;
	
	/** The Constant SYNC_LOCATION_Y. */
	public static final short SYNC_LOCATION_Y = 1;
	
	/** The Constant SYNC_ANGLE. */
	public static final short SYNC_ANGLE = 2;
	
	/** The Constant SYNC_SCALE. */
	public static final short SYNC_SCALE = 3;
	
	/** The Constant SYNC_ORDER. */
	public static final short SYNC_ORDER = 4;
	
	/** The allowed sync. */
	protected List<Short> allowedSync = new ArrayList<Short>(); 
	
	/**
	 * Instantiates a new sync renderer.
	 */
	public SyncRenderer(){
		Field[] fields = SyncRenderer.class.getFields();
		for(Field field: fields){
			try {
				if(field.getName().startsWith("SYNC")){
					if(!allowedSync.contains(field.getShort(null))) allowedSync.add(field.getShort(null));
				}
			} catch (IllegalArgumentException e) {
				 
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				 
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Render sync data.
	 *
	 * @param item the item
	 * @param syncParameter the sync parameter
	 * @param syncValue the sync value
	 */
	public void renderSyncData(ContentItem item, Short syncParameter, Object syncValue){
		
	}
	
	/**
	 * Render sync data.
	 *
	 * @param item the item
	 * @param syncData the sync data
	 */
	public void renderSyncData(ContentItem item, HashMap<Short, Object> syncData){
		for(short paramName: syncData.keySet()){
			if(!allowedSync.contains(paramName)) continue;
			if(paramName == SYNC_LOCATION_X)
				((OrthoContentItem)item).setLocation((Float)syncData.get(paramName), ((OrthoContentItem)item).getLocation().y);
			else if(paramName == SYNC_LOCATION_Y)
				((OrthoContentItem)item).setLocation(((OrthoContentItem)item).getLocation().x, (Float)syncData.get(paramName));
			else if(paramName == SYNC_ANGLE)
				item.setAngle((Float)syncData.get(paramName));
			else if(paramName == SYNC_SCALE)
				item.setScale((Float)syncData.get(paramName));
			else if(paramName == SYNC_ORDER && item instanceof OrthoContentItem){
				OrthoContentItem orthoItem = (OrthoContentItem) item;
				int order = (Integer)syncData.get(paramName);
				if(order == OrthoBringToTop.topMost){
					if(orthoItem.getParent() == null) orthoItem.setAsTopObject();
					else orthoItem.getParent().setTopItem(orthoItem);
				}
				else orthoItem.setOrder(order);
			}
		}		
	}
	
	
	/**
	 * Disable sync parameter.
	 *
	 * @param syncParameterId the sync parameter id
	 */
	public void disableSyncParameter(Short syncParameterId){
		if(allowedSync.contains(syncParameterId)) allowedSync.remove(syncParameterId);
	}
	
	/**
	 * Enable sync parameter.
	 *
	 * @param syncParameterId the sync parameter id
	 */
	public void enableSyncParameter(Short syncParameterId){
		if(!allowedSync.contains(syncParameterId)) allowedSync.add(syncParameterId);
	}
}
