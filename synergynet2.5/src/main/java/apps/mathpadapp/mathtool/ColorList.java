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

package apps.mathpadapp.mathtool;

import java.awt.Color;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import apps.mathpadapp.MathPadResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener;

public class ColorList {
	
	protected ArrayList<String> colors = new ArrayList<String>();
	protected DropDownList colorList;
	protected ColorListAction colorListAction;
	
	protected transient List<ColorListListener> listeners = new ArrayList<ColorListListener>();
	
	public ColorList(ContentSystem contentSystem){
		// Set color list
		colors.add("black");
		colors.add("red");
		colors.add("green");
		colors.add("blue");
		colors.add("gray");
		colors.add("orange");
		colors.add("pink");
		colors.add("yellow");
		colors.add("white");
		
		colorList = (DropDownList) contentSystem.createContentItem(DropDownList.class);
		colorList.setWidth(GraphConfig.CONTROL_PANEL_WIDTH);
		colorList.setBorderColour(GraphConfig.CONTROL_PANEL_BORDER_COLOR);
		colorList.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		colorList.setItemHeight(GraphConfig.COLOR_LIST_ITEM_HEIGHT);
		for(String colorName: colors){	
			URL colorImageURL = MathPadResources.class.getResource("colors/"+colorName+".jpg");
			if(colorImageURL != null)
				colorList.addListItem(colorImageURL, colorName);
		}
		colorListAction = new ColorListAction();
		colorList.addDropDownListListener(colorListAction);
		
		this.setSelectedColor(Color.black);
	}
	
	protected void setSelectedColor(Color color) {
		if(colorList ==null) return;
		for(int i=0; i<colors.size(); i++){
			Field field;
			try {
				field = Class.forName("java.awt.Color").getField(colors.get(i));
				if(color.equals((Color)field.get(null))){
					colorList.removeDropDownListListeners();
					colorList.setSelectedItem(colorList.getListItems().get(i));
					colorList.addDropDownListListener(colorListAction);
				}
			} catch (SecurityException e) {
				 
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				 
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				 
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				 
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				 
				e.printStackTrace();
			}
		}
	}
	
	class ColorListAction implements DropDownListListener{
		@Override
		public void itemSelected(DropDownListItem item) {
			Color selectedColor = null;
			try {
				Field field = Class.forName("java.awt.Color").getField(item.getValue());
				selectedColor = (Color) field.get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(selectedColor != null){
				for(ColorListListener listener: listeners)
					listener.colorSelected(selectedColor);
			}
		}
	}
	
	public void addColorListListener(ColorListListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	public void removeColorListListener(ColorListListener listener){
		listeners.remove(listener);
	}
	
	public void removeColorListListeners(){
		listeners.clear();
	}
	public interface ColorListListener{
		public void colorSelected(Color color);
	}
	
	public DropDownList getDropDownList(){
		return colorList;
	}
}
