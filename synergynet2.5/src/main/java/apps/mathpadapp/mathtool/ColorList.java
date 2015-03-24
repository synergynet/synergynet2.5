/*
 * Copyright (c) 2009 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergyNet' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package apps.mathpadapp.mathtool;

import java.awt.Color;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.DropDownList;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListItem;
import synergynetframework.appsystem.contentsystem.items.DropDownList.DropDownListListener;
import apps.mathpadapp.MathPadResources;

/**
 * The Class ColorList.
 */
public class ColorList {

	/**
	 * The Class ColorListAction.
	 */
	class ColorListAction implements DropDownListListener {

		/*
		 * (non-Javadoc)
		 * @see synergynetframework.appsystem.contentsystem.items.DropDownList.
		 * DropDownListListener
		 * #itemSelected(synergynetframework.appsystem.contentsystem
		 * .items.DropDownList.DropDownListItem)
		 */
		@Override
		public void itemSelected(DropDownListItem item) {
			Color selectedColor = null;
			try {
				Field field = Class.forName("java.awt.Color").getField(
						item.getValue());
				selectedColor = (Color) field.get(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (selectedColor != null) {
				for (ColorListListener listener : listeners) {
					listener.colorSelected(selectedColor);
				}
			}
		}
	}

	/**
	 * The listener interface for receiving colorList events. The class that is
	 * interested in processing a colorList event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addColorListListener<code> method. When
	 * the colorList event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ColorListEvent
	 */
	public interface ColorListListener {

		/**
		 * Color selected.
		 *
		 * @param color
		 *            the color
		 */
		public void colorSelected(Color color);
	}

	/** The color list. */
	protected DropDownList colorList;

	/** The color list action. */
	protected ColorListAction colorListAction;

	/** The colors. */
	protected ArrayList<String> colors = new ArrayList<String>();

	/** The listeners. */
	protected transient List<ColorListListener> listeners = new ArrayList<ColorListListener>();

	/**
	 * Instantiates a new color list.
	 *
	 * @param contentSystem
	 *            the content system
	 */
	public ColorList(ContentSystem contentSystem) {
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

		colorList = (DropDownList) contentSystem
				.createContentItem(DropDownList.class);
		colorList.setWidth(GraphConfig.CONTROL_PANEL_WIDTH);
		colorList.setBorderColour(GraphConfig.CONTROL_PANEL_BORDER_COLOR);
		colorList.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		colorList.setItemHeight(GraphConfig.COLOR_LIST_ITEM_HEIGHT);
		for (String colorName : colors) {
			URL colorImageURL = MathPadResources.class.getResource("colors/"
					+ colorName + ".jpg");
			if (colorImageURL != null) {
				colorList.addListItem(colorImageURL, colorName);
			}
		}
		colorListAction = new ColorListAction();
		colorList.addDropDownListListener(colorListAction);

		this.setSelectedColor(Color.black);
	}

	/**
	 * Adds the color list listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addColorListListener(ColorListListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Gets the drop down list.
	 *
	 * @return the drop down list
	 */
	public DropDownList getDropDownList() {
		return colorList;
	}

	/**
	 * Removes the color list listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void removeColorListListener(ColorListListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes the color list listeners.
	 */
	public void removeColorListListeners() {
		listeners.clear();
	}

	/**
	 * Sets the selected color.
	 *
	 * @param color
	 *            the new selected color
	 */
	protected void setSelectedColor(Color color) {
		if (colorList == null) {
			return;
		}
		for (int i = 0; i < colors.size(); i++) {
			Field field;
			try {
				field = Class.forName("java.awt.Color").getField(colors.get(i));
				if (color.equals(field.get(null))) {
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
}
