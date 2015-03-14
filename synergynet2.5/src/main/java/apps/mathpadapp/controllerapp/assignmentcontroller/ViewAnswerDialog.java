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
package apps.mathpadapp.controllerapp.assignmentcontroller;

import java.awt.Color;
import java.awt.Font;

import com.jme.system.DisplaySystem;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.SketchPad;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.jme.cursorsystem.elements.twod.OrthoBringToTop;

public class ViewAnswerDialog {
	
	private Window window;
	private Window topPanel;
	private SketchPad sketchPad;
	
	public ViewAnswerDialog(final ContentSystem contentSystem){
		window = (Window) contentSystem.createContentItem(Window.class);
		window.setWidth(300);
		window.setHeight(200);
		window.setLocalLocation(DisplaySystem.getDisplaySystem().getWidth()/2, DisplaySystem.getDisplaySystem().getHeight()/2);

		sketchPad = (SketchPad) contentSystem.createContentItem(SketchPad.class);
		sketchPad.setWidth(280);
		sketchPad.setHeight(180);
		sketchPad.setBorderSize(1);
		sketchPad.setBorderColour(Color.black);
		window.addSubItem(sketchPad);
		
		topPanel = (Window) contentSystem.createContentItem(Window.class);
		topPanel.setBackgroundColour(Color.DARK_GRAY);
		topPanel.setWidth(300);
		topPanel.setHeight(30);
		topPanel.setLocalLocation(0, window.getHeight()/2+ topPanel.getHeight()/2 - topPanel.getBorderSize());
		
		TextLabel title = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		title.setBorderSize(0);
		title.setBackgroundColour(topPanel.getBackgroundColour());
		title.setFont(new Font("TimesRoman", Font.PLAIN,  12));
		title.setTextColour(Color.white);
		title.setText("View Answer");
		title.setLocalLocation(-topPanel.getWidth()/2 + title.getWidth()/2 + topPanel.getBorderSize()+3, topPanel.getHeight()/2 - title.getHeight()/2- 4);
		topPanel.addSubItem(title);

		SimpleButton closeButton = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		closeButton.setAutoFitSize(false);
		closeButton.setWidth(18);
		closeButton.setHeight(18);
		closeButton.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				contentSystem.removeContentItem(window);
			}
			
		});
		closeButton.setBorderSize(2);
		closeButton.setLocalLocation(topPanel.getWidth()/2 - closeButton.getWidth()/2 - topPanel.getBorderSize()-3, topPanel.getHeight()/2 - closeButton.getHeight()/2- 6);
		topPanel.addSubItem(closeButton);
		window.addSubItem(topPanel);

		window.setOrder(OrthoBringToTop.bottomMost);
		
	}
	
	public Window getWindow(){
		return window;
	}

	public SketchPad getSketchPad(){
		return sketchPad;
	}
}

