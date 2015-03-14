/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.contentloader.attributesrender;

import java.util.Map;
import java.util.logging.Logger;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;

public class OrthoContentItemRender extends ContentItemRender {

	private static final Logger log = Logger.getLogger(OrthoContentItemRender.class.getName());
	
	@Override
	protected void render(Map<String, String> itemAttrs, ContentItem contentItem,
			ContentSystem contentsys) {
		super.render(itemAttrs, contentItem, contentsys);
		 
		OrthoContentItem item = (OrthoContentItem)contentItem;
		
		//set the item with angle
		float angle;
		String angleString =itemAttrs.get(AttributeConstants.ITEM_ANGLE);	
		if (angleString.trim().equals(AttributeConstants.RANDOMANGLE))
			item.rotateRandom();
		else{
			if (angleString!=null && angleString.matches("\\d*.\\d*"))
				angle = Float.parseFloat(itemAttrs.get(AttributeConstants.ITEM_ANGLE));
			else{
				angle = 0;
				log.warning("The angle of "+item.getClass().getName()+" is invalid");
			}
			item.setAngle(angle);  
		
		}
		
		//location
		boolean randomLocation = false;
		float locationX = 0;
		float locationY = 0;
		float locationZ = 0;
		String locationXString = itemAttrs.get(AttributeConstants.ITEM_LOCATION_X);
		if (locationXString!=null && locationXString.matches("\\d*"))
			locationX = Float.parseFloat(locationXString) * (DisplaySystem.getDisplaySystem().getWidth()/1024f);
		else
			randomLocation = true;			
		
		String locationYString = itemAttrs.get(AttributeConstants.ITEM_LOCATION_Y);
		if (locationYString!=null && locationXString.matches("\\d*"))
			locationY = Float.parseFloat(locationYString)  * (DisplaySystem.getDisplaySystem().getHeight()/768f);
		else
			randomLocation = true;
		
		String locationZString = itemAttrs.get(AttributeConstants.ITEM_LOCATION_Z);
		if (locationZString!=null && locationZString.matches("\\d*"))
			locationZ = Float.parseFloat(locationZString);
		else
			randomLocation = true;
		
		if (randomLocation)
			item.placeRandom();
		else{
			item.setLocalLocation(locationX, locationY, locationZ);
		}
		
		//set z-order
		int zorder;
		String zorderString =itemAttrs.get(AttributeConstants.ITEM_SORTORDER);				
		if (zorderString!=null && zorderString.matches("\\d*")){
			zorder = Integer.parseInt(itemAttrs.get(AttributeConstants.ITEM_SORTORDER));
			item.setOrder(zorder);
		}
		else{
			log.warning("The zorder of "+item.getClass().getName()+" is invalid");
		}
		
		//set gesture
		//set rotateTranslateScalable
		boolean rotateTranslateScalable;
		String rotateTranslateScalableString =itemAttrs.get(AttributeConstants.ITEM_ROTATETRANSLATESCALABLE);				
		if (rotateTranslateScalableString!=null){
			rotateTranslateScalable = Boolean.parseBoolean(itemAttrs.get(AttributeConstants.ITEM_ROTATETRANSLATESCALABLE));
			item.setRotateTranslateScalable(rotateTranslateScalable);
		}
		
		//set rotateTranslateScalable
		boolean bringToTopable;
		String bringToTopableString =itemAttrs.get(AttributeConstants.ITEM_BRINGTOTOPABLE);				
		if (bringToTopableString!=null){
			bringToTopable = Boolean.parseBoolean(itemAttrs.get(AttributeConstants.ITEM_BRINGTOTOPABLE));
			item.setBringToTopable(bringToTopable);
		}
		
		//set snapable
		boolean snapable;
		String snapableString =itemAttrs.get(AttributeConstants.ITEM_SNAPABLE);				
		if (snapableString!=null){
			snapable = Boolean.parseBoolean(itemAttrs.get(AttributeConstants.ITEM_SNAPABLE));
			item.setSnapable(snapable);
		}

	}
	
}
