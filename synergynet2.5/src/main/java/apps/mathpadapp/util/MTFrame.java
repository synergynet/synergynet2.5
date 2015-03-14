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
package apps.mathpadapp.util;

import java.awt.Color;
import java.awt.Font;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.conceptmapping.GraphManager;
import apps.mathpadapp.conceptmapping.GraphNode;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

public abstract class  MTFrame extends GraphNode{
	
	protected Window window;
	protected Window topPanel;
	protected TextLabel title;
	protected SimpleButton closeButton;
	protected ContentSystem contentSystem;
	protected SimpleButton linkButton;
	
	public MTFrame(final ContentSystem contentSystem){
		this(contentSystem, null);
	}
	
	public MTFrame(final ContentSystem contentSystem, GraphManager graphManager){
		super(graphManager, (Window) contentSystem.createContentItem(Window.class));
		this.contentSystem = contentSystem;
		final MTFrame instance = this;
		window = (Window) this.getNodeItem();
		window.setWidth(300);
		window.setHeight(200);
		window.setLocalLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);
		
		topPanel = (Window) contentSystem.createContentItem(Window.class);
		topPanel.setBackgroundColour(Color.DARK_GRAY);
		topPanel.setWidth(300);
		topPanel.setHeight(40);
		topPanel.setLocalLocation(0, window.getHeight()/2+ topPanel.getHeight()/2 - topPanel.getBorderSize());
		
		title = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		title.setBackgroundColour(topPanel.getBackgroundColour());
		title.setFont(new Font("TimesRoman", Font.PLAIN,  14));
		title.setTextColour(Color.white);
		title.setText("Title");
		title.setAutoFitSize(false);
		title.setWidth(topPanel.getWidth());
		title.setHeight(topPanel.getHeight());
		title.setLocalLocation(topPanel.getLocalLocation().x,0);
		title.setAlignment(Alignment.LEFT);
		title.setBorderSize(topPanel.getBorderSize());
		topPanel.addSubItem(title);

		closeButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		closeButton.setAutoFitSize(false);
		closeButton.setWidth(25);
		closeButton.setHeight(25);
		closeButton.setBackgroundColour(Color.white);
		closeButton.drawImage(MathPadResources.class.getResource("buttons/close.png"),0,0,25,25);
		closeButton.addButtonListener(new SimpleButtonAdapter(){
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				instance.close();
			}
			
		});
		closeButton.setBorderSize(2);
		closeButton.setLocalLocation(topPanel.getWidth()/2 - closeButton.getWidth()/2 - topPanel.getBorderSize()-3, topPanel.getHeight()/2 - closeButton.getHeight()/2- 6);
		topPanel.addSubItem(closeButton);
		closeButton.setOrder(1000);
		window.addSubItem(topPanel);

		window.setOrder(OrthoBringToTop.bottomMost);
		
		if(graphManager != null){
			linkButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
			linkButton.setAutoFitSize(false);
			linkButton.setWidth(18);
			linkButton.setHeight(18);
			linkButton.setBorderSize(2);
			linkButton.setLocalLocation(topPanel.getWidth()/2 - closeButton.getWidth() - linkButton.getWidth()/2 - topPanel.getBorderSize()-6, topPanel.getHeight()/2 - linkButton.getHeight()/2- 6);
			this.getTopBar().addSubItem(linkButton);
			linkButton.setOrder(1000);
			this.setLinkPoint(linkButton);
		}
	}
	
	public Window getWindow(){
		return window;
	}
	
	public Window getTopBar(){
		return topPanel;
	}
	
	public void setTitle(String text){
		title.setText(" "+text);
	}
	
	public String getTitle(){
		return title.getText();
	}

	public void setHeight(int height) {
		this.window.setHeight(height);
		topPanel.setLocalLocation(0, window.getHeight()/2+ topPanel.getHeight()/2 - topPanel.getBorderSize());
		title.setHeight(topPanel.getHeight());
		title.setLocalLocation(topPanel.getLocalLocation().x,0);
	}
	
	public void setWidth(int width) {
		this.window.setWidth(width);
		topPanel.setWidth(window.getWidth());
		title.setWidth(topPanel.getWidth());
		title.setLocalLocation(topPanel.getLocalLocation().x,0);
		closeButton.setLocalLocation(topPanel.getWidth()/2 - closeButton.getWidth()/2 - topPanel.getBorderSize()-3, topPanel.getHeight()/2 - closeButton.getHeight()/2- 6);
		if(linkButton!= null) linkButton.setLocalLocation(topPanel.getWidth()/2 - closeButton.getWidth() - linkButton.getWidth()/2 - topPanel.getBorderSize()-6, topPanel.getHeight()/2 - linkButton.getHeight()/2- 6);
	}
	
	public void close(){
		if(getGraphManager() != null){
			this.getGraphManager().detachGraphNode(this);
		}
		contentSystem.removeContentItem(this.getWindow());
	}
}



