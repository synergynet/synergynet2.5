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
import java.util.ArrayList;
import java.util.List;

import apps.mathpadapp.MathPadResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;


/**
 * The Class LineWidthPanel.
 */
public class LineWidthPanel {
	
	/** The content system. */
	protected ContentSystem contentSystem;
	
	/** The panel. */
	private Window panel;
	
	/** The buttons. */
	private ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
	
	/** The listeners. */
	private transient List<LineWidthPanelListener> listeners = new ArrayList<LineWidthPanelListener>();
	
	/** The current line width. */
	private float currentLineWidth = 1;
	
	/**
	 * Instantiates a new line width panel.
	 *
	 * @param contentSystem the content system
	 * @param noOfWidthLevels the no of width levels
	 * @param widthShift the width shift
	 */
	public LineWidthPanel(ContentSystem contentSystem, int noOfWidthLevels, int widthShift){
		this.contentSystem = contentSystem;
		panel = (Window) contentSystem.createContentItem(Window.class);
		panel.setWidth((noOfWidthLevels * widthShift)+ 2*GraphConfig.CONTROL_PANEL_BORDER_SIZE + 2*panel.getBorderSize());
		int panelHeight = 0;
		for(int i=0; i<noOfWidthLevels; i++) panelHeight+= (i+2)*widthShift + 4*GraphConfig.CONTROL_PANEL_BORDER_SIZE;
		panel.setHeight(panelHeight);
		
		int shiftY = panel.getHeight()/2 - 10;
		
		float startLineWidth = 1;
		for(int i=0; i< noOfWidthLevels; i++){
			final SimpleButton widthBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
			widthBtn.setNote(String.valueOf(startLineWidth));
			widthBtn.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
			widthBtn.setAutoFitSize(false);
			widthBtn.setWidth(widthShift * (i+2));
			widthBtn.setHeight(widthShift * (i+2));
			widthBtn.drawImage(MathPadResources.class.getResource("buttons/lineWidth.jpg"));
			widthBtn.addButtonListener(new SimpleButtonAdapter(){
	
				@Override
				public void buttonReleased(SimpleButton b, long id, float x,
						float y, float pressure) {
					currentLineWidth = Float.parseFloat(b.getNote());
					for(LineWidthPanelListener listener: listeners){
						listener.lineWidthChanged(currentLineWidth);
					}
					for(SimpleButton button: buttons){
						button.drawImage(MathPadResources.class.getResource("buttons/lineWidth.jpg"));
						button.setBorderColour(Color.white);
					}
					b.setBorderColour(GraphConfig.CONTROL_PANEL_BORDER_COLOR);
				}
				
			});
			widthBtn.setLocalLocation(0, shiftY);
			panel.addSubItem(widthBtn);
			buttons.add(widthBtn);
			shiftY-= widthBtn.getHeight() + 4*GraphConfig.CONTROL_PANEL_BORDER_SIZE;
			startLineWidth+= widthShift;
		}

	}

	/**
	 * Sets the line width.
	 *
	 * @param lineWidth the new line width
	 */
	protected void setLineWidth(float lineWidth){
		for(SimpleButton button: buttons){
			button.drawImage(MathPadResources.class.getResource("buttons/lineWidth.jpg"));
			if(lineWidth == Float.parseFloat(button.getNote())){
				currentLineWidth = lineWidth;
				button.setBorderColour(GraphConfig.CONTROL_PANEL_BORDER_COLOR);
			}
			else{
				button.setBorderColour(Color.white);
			}
		}
	}
	
	/**
	 * Gets the content panel.
	 *
	 * @return the content panel
	 */
	protected Window getContentPanel(){
		return panel;
	}
	
	/**
	 * Adds the line width panel listener.
	 *
	 * @param listener the listener
	 */
	public void addLineWidthPanelListener(LineWidthPanelListener listener){
		if(!listeners.contains(listener)) listeners.add(listener);
	}
	
	/**
	 * Removes the line width panel listeners.
	 */
	public void removeLineWidthPanelListeners(){
		listeners.clear();
	}
	
	/**
	 * The listener interface for receiving lineWidthPanel events.
	 * The class that is interested in processing a lineWidthPanel
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addLineWidthPanelListener<code> method. When
	 * the lineWidthPanel event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see LineWidthPanelEvent
	 */
	public interface LineWidthPanelListener{
		
		/**
		 * Line width changed.
		 *
		 * @param lineWidth the line width
		 */
		public void lineWidthChanged(float lineWidth);
	}
	
	/**
	 * Gets the current line width.
	 *
	 * @return the current line width
	 */
	public float getCurrentLineWidth(){
		return currentLineWidth;
	}
	
	/**
	 * Gets the all line widthes.
	 *
	 * @return the all line widthes
	 */
	protected float[] getAllLineWidthes(){
		float[] widthes = new float[buttons.size()];
		for(int i=0; i<buttons.size(); i++) widthes[i] = Float.parseFloat(buttons.get(i).getNote());
		return widthes;
	}

	/**
	 * Fire line width changed.
	 *
	 * @param lineWidth the line width
	 */
	public void fireLineWidthChanged(float lineWidth) {
		for(LineWidthPanelListener listener: listeners){
			listener.lineWidthChanged(lineWidth);
		}
	}
}
