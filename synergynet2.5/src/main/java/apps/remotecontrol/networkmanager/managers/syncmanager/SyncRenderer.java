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

import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.SketchPad;
import synergynetframework.appsystem.contentsystem.jme.items.utils.DrawData;


/**
 * The Class SyncRenderer.
 */
public class SyncRenderer {
	
	/** The data masks. */
	protected Map<Short, Float> dataMasks = new HashMap<Short, Float>();
	
	/**
	 * Render sync data.
	 *
	 * @param item the item
	 * @param itemAttrs the item attrs
	 */
	@SuppressWarnings("unchecked")
	public void renderSyncData( OrthoContentItem item, Map<Short, Object> itemAttrs) {
		//set the item with scale
		float scale;
		if (itemAttrs.containsKey(SyncManager.SYNC_ITEM_SCALE)){
			if(dataMasks.containsKey(SyncManager.SYNC_ITEM_SCALE)){
				scale = dataMasks.get(SyncManager.SYNC_ITEM_SCALE);
			}else{
				scale = Float.parseFloat(itemAttrs.get(SyncManager.SYNC_ITEM_SCALE).toString());
			}
			item.setScale(scale);
		}
		
		//set the item with angle
		float angle;
		if (itemAttrs.containsKey(SyncManager.SYNC_ITEM_ANGLE)){
			if(dataMasks.containsKey(SyncManager.SYNC_ITEM_ANGLE)){
				angle = dataMasks.get(SyncManager.SYNC_ITEM_ANGLE);
			}else{
				angle = Float.parseFloat(itemAttrs.get(SyncManager.SYNC_ITEM_ANGLE).toString());
			}
			item.setAngle(angle);  
		}
		
		//location
		float locationX = 0;
		float locationY = 0;
		if (itemAttrs.containsKey(SyncManager.SYNC_ITEM_LOCATION_X)){
			locationX = Float.parseFloat(itemAttrs.get(SyncManager.SYNC_ITEM_LOCATION_X).toString()) ;
			item.setLocation(locationX, item.getLocation().getY());
		}
					
		if (itemAttrs.containsKey(SyncManager.SYNC_ITEM_LOCATION_Y)){
			locationY = Float.parseFloat(itemAttrs.get(SyncManager.SYNC_ITEM_LOCATION_Y).toString());
			item.setLocation(item.getLocation().getX(), locationY);
		}
		
		//set z-order
		if (itemAttrs.containsKey(SyncManager.SYNC_ITEM_SORTORDER)){
			if (item!=null && item.getParent() != null){				
				item.getParent().setTopItem(item);
			}else if(item != null && item.getParent() == null){
				item.setAsTopObject();
			}
		}
		
		//draw data
		if(item instanceof SketchPad){
			if(itemAttrs.containsKey(SyncManager.SYNC_ITEM_DRAW_DATA)){
				List<DrawData> drawData = (ArrayList<DrawData>)itemAttrs.get(SyncManager.SYNC_ITEM_DRAW_DATA);
				((SketchPad)item).draw(drawData);
			}
			if(itemAttrs.containsKey(SyncManager.SYNC_ITEM_CLEAR_PAD))
				((SketchPad)item).clearAll();
		}
		
	}
	
	/**
	 * Sets the data mask.
	 *
	 * @param attribute the attribute
	 * @param value the value
	 */
	public void setDataMask(Short attribute, float value){
		dataMasks.put(attribute, value);
		System.out.println("Mask : "+attribute+ " is applied");
	}
	
	/**
	 * Removes the data mask.
	 *
	 * @param attribute the attribute
	 */
	public void removeDataMask(Short attribute){
		if(dataMasks.containsKey(attribute)) dataMasks.remove(attribute);
	}
	
	/**
	 * Gets the mask value.
	 *
	 * @param attribute the attribute
	 * @return the mask value
	 */
	public float getMaskValue(Short attribute){
		if(dataMasks.containsKey(attribute)) return dataMasks.get(attribute);
		else return -1.0f;
	}
	
	/**
	 * Reset data masks.
	 */
	public void resetDataMasks(){
		dataMasks.clear();
	}
}
