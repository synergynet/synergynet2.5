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

package synergynetframework.appsystem.services.net.networkedcontentmanager.synchroniseddatarender;

import java.util.Map;

import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteController;
import synergynetframework.appsystem.contentsystem.items.innernotecontroller.InnerNoteEditor;
import synergynetframework.appsystem.services.net.networkedcontentmanager.utils.RemoteDesktop;


/**
 * The Class SynchronisedDataRender.
 */
public class SynchronisedDataRender {
	
	/**
	 * Render.
	 *
	 * @param item the item
	 * @param itemAttrs the item attrs
	 * @param innerNoteController the inner note controller
	 */
	public static void render(OrthoContentItem item, Map<String, String> itemAttrs, InnerNoteController innerNoteController){
		
		InnerNoteEditor innerNoteEditor =null;
		if (innerNoteController.getNodeEditor((QuadContentItem)item)!= null)	
			innerNoteEditor = innerNoteController.getNodeEditor((QuadContentItem)item);
		//set the item with scale
		float scale;
		String scaleString =itemAttrs.get(AttributeConstants.ITEM_SCALE);		
		if (scaleString!=null && scaleString.matches("\\d*.\\d*")){
			scale = Float.parseFloat(itemAttrs.get(AttributeConstants.ITEM_SCALE));
			if(innerNoteEditor!=null)
				innerNoteEditor.getNoteNode().setScale(scale);
			item.setScale(scale);
		}
		
		//set the item with angle
		float angle;
		String angleString =itemAttrs.get(AttributeConstants.ITEM_ANGLE);	
		if (angleString!=null && angleString.matches("\\d*.\\d*")){
			angle = Float.parseFloat(itemAttrs.get(AttributeConstants.ITEM_ANGLE));
			item.setAngle(angle);  
			if(innerNoteEditor!=null)
				innerNoteEditor.getNoteNode().setAngle(angle);
		}
		
		//location
		float locationX = 0;
		float locationY = 0;
		String locationXString = itemAttrs.get(AttributeConstants.ITEM_LOCATION_X);
		if (locationXString!=null){
			locationX = Float.parseFloat(locationXString);	
			item.setLocalLocation(locationX, item.getLocalLocation().getY());
			if(innerNoteEditor!=null)
			innerNoteEditor.getNoteNode().setLocalLocation(locationX, item.getLocalLocation().getY());
		}
					
		String locationYString = itemAttrs.get(AttributeConstants.ITEM_LOCATION_Y);
		if (locationYString!=null){
			locationY = Float.parseFloat(locationYString);
			item.setLocalLocation(item.getLocalLocation().getX(), locationY);
			if(innerNoteEditor!=null)
			innerNoteEditor.setLocation(item.getLocalLocation().getX(), locationY);
		}
		
		
		//set z-order
		//int zorder;
		String zorderString =itemAttrs.get(AttributeConstants.ITEM_SORTORDER);				
		if (zorderString!=null && zorderString.matches("\\d*")){
			//zorder = Integer.parseInt(itemAttrs.get(AttributeConstants.ITEM_SORTORDER));
			//item.setOrder(zorder);
			if (item!=null)
			{
				
				if(innerNoteEditor!=null)
					innerNoteEditor.setAsTopObject();
				else
					item.setAsTopObject();
			}
		}
		
	}
	
	/**
	 * Render remote desktop.
	 *
	 * @param desktopItem the desktop item
	 * @param item the item
	 * @param itemAttrs the item attrs
	 * @param innerNoteController the inner note controller
	 */
	public static void renderRemoteDesktop(RemoteDesktop desktopItem, OrthoContentItem item, Map<String, String> itemAttrs, InnerNoteController innerNoteController) {
		if(desktopItem == null) return;
		InnerNoteEditor innerNoteEditor =null;
		if (innerNoteController.getNodeEditor((QuadContentItem)item)!= null){	
			innerNoteEditor = innerNoteController.getNodeEditor((QuadContentItem)item);
		}else{
			desktopItem.removeNoteContainerForItem(item);
		}
		//set the item with scale
		float scale;
		String scaleString =itemAttrs.get(AttributeConstants.ITEM_SCALE);		
		if (scaleString!=null && scaleString.matches("\\d*.\\d*")){
			scale = Float.parseFloat(itemAttrs.get(AttributeConstants.ITEM_SCALE));
			if(innerNoteEditor!=null)
				innerNoteEditor.getNoteNode().setScale(scale);
			item.setScale(scale);
		}
		
		//set the item with angle
		float angle;
		String angleString =itemAttrs.get(AttributeConstants.ITEM_ANGLE);	
		if (angleString!=null && angleString.matches("\\d*.\\d*")){
			angle = Float.parseFloat(itemAttrs.get(AttributeConstants.ITEM_ANGLE));
			item.setAngle(angle);  
			if(innerNoteEditor!=null)
				innerNoteEditor.getNoteNode().setAngle(angle);
		}
		
		//location
		float locationX = 0;
		float locationY = 0;
		String locationXString = itemAttrs.get(AttributeConstants.ITEM_LOCATION_X);
		if (locationXString!=null){
			locationX = Float.parseFloat(locationXString) ;
			item.setLocalLocation(locationX, item.getLocalLocation().getY());
			if(innerNoteEditor!=null)
			innerNoteEditor.getNoteNode().setLocalLocation(locationX, item.getLocalLocation().getY());
		}
					
		String locationYString = itemAttrs.get(AttributeConstants.ITEM_LOCATION_Y);
		if (locationYString!=null){
			locationY = Float.parseFloat(locationYString) ;
			item.setLocalLocation(item.getLocalLocation().getX(), locationY);
			if(innerNoteEditor!=null)
			innerNoteEditor.setLocation(item.getLocalLocation().getX(), locationY);
		}
		
		//set z-order
		//int zorder;
		String zorderString =itemAttrs.get(AttributeConstants.ITEM_SORTORDER);				
		if (zorderString!=null && zorderString.matches("\\d*")){
			if (item!=null){				
				if(innerNoteEditor!=null)
					innerNoteEditor.setAsTopObject();
				else{
					desktopItem.getDesktopWindow().setTopItem(item);
				}
			}
		}		
	}
	
	/**
	 * Render note.
	 *
	 * @param item the item
	 * @param itemAttrs the item attrs
	 * @param innerNoteController the inner note controller
	 */
	public static void renderNote(OrthoContentItem item, Map<String, String> itemAttrs, InnerNoteController innerNoteController){
		if (itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE)!=null){
			item.setNote(itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE));
			if (innerNoteController.getNodeEditor((QuadContentItem)item)!= null)
				innerNoteController.getNodeEditor((QuadContentItem)item).setNote(itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE));

		}
		
		if (itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE_ON)!=null){
			boolean noteLabelOn = Boolean.parseBoolean(itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE_ON));
			if (!noteLabelOn){
				innerNoteController.removeNoteEditor((QuadContentItem)item);
			}
			else{
				innerNoteController.addNoteEditor((QuadContentItem)item);
				innerNoteController.getNodeEditor((QuadContentItem)item).setAsTopObject();
			}
		}
	}
	
	/**
	 * Render remote desktop note.
	 *
	 * @param desktopItem the desktop item
	 * @param item the item
	 * @param itemAttrs the item attrs
	 * @param innerNoteController the inner note controller
	 */
	public static void renderRemoteDesktopNote(RemoteDesktop desktopItem, OrthoContentItem item, Map<String, String> itemAttrs, InnerNoteController innerNoteController){
		
		if (itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE)!=null){
			item.setNote(itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE));
			if (innerNoteController.getNodeEditor((QuadContentItem)item)!= null)
				innerNoteController.getNodeEditor((QuadContentItem)item).setNote(itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE));

		}
		
		if (itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE_ON)!=null){
			boolean noteLabelOn = Boolean.parseBoolean(itemAttrs.get(AttributeConstants.ITEM_INNER_NOTE_ON));
			if (!noteLabelOn){
				innerNoteController.removeNoteEditor((QuadContentItem)item);
			}
			else{
				innerNoteController.addNoteEditor((QuadContentItem)item);
				OrthoContainer noteContainer = innerNoteController.getNodeEditor((QuadContentItem)item).getNoteNode();
				if(noteContainer.getParent() == null || !item.getParent().contains(noteContainer)){
					String tableId = item.getName().substring(0, item.getName().indexOf("@"));
					noteContainer.setName(tableId + "@" + noteContainer.getName());
					noteContainer.setRotateTranslateScalable(false);
					desktopItem.addNoteContainerForItem(item, noteContainer);
					desktopItem.getDesktopWindow().setTopItem(noteContainer);
				}
			}
		}
	}
}
