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

package apps.twentyfourpoint.utils;

import java.awt.Color;

import apps.twentyfourpoint.TwentyFourPointApp;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;

public class NOAnswerConfirmDialogue {

	protected Window window;
	protected MultiLineTextLabel label;
	protected SimpleButton okButton;
	protected SimpleButton cancelButton;
	protected ContentSystem contentSystem;
	protected TwentyFourPointApp app;
	protected MultiLineTextLabel noAnswerLabel;
	
	public NOAnswerConfirmDialogue(ContentSystem contentSystem, TwentyFourPointApp app, MultiLineTextLabel noAnswerLabel){
		this.contentSystem = contentSystem;
		this.app = app;
		this.noAnswerLabel = noAnswerLabel;
		loadContent();
	}
	
	protected void loadContent(){
		window = (Window)contentSystem.createContentItem(Window.class);
		
		label = (MultiLineTextLabel)contentSystem.createContentItem(MultiLineTextLabel.class);
		label.setBorderSize(0);
		label.setLocalLocation(0, 50);
		label.setTextColour(Color.black);
		label.setLines("Are you sure there is no answer?", 100);
		
		okButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		okButton.setLocalLocation(-100, -50);
		okButton.setText("YES");
		okButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				app.checkNoAnswer();
				
			}
		});
		
		cancelButton = (SimpleButton)contentSystem.createContentItem(SimpleButton.class);
		cancelButton.setLocalLocation(100, -50);
		cancelButton.setText("No");
		cancelButton.addItemListener(new ItemEventAdapter(){
			public void cursorClicked(ContentItem item, long id, float x, float y,
					float pressure) {
				noAnswerLabel.setLocalLocation(900, 670);
				app.removeConfirmDialogue();
			}
		});
				
		window.addSubItem(label);
		window.addSubItem(okButton);
		window.addSubItem(cancelButton);
		window.setBringToTopable(false);
		window.setRotateTranslateScalable(false);
		
		window.setLocalLocation(512, 384);
		window.setWidth(400);
		window.setHeight(200);	
		window.setOrder(9999999);	
	
	}
	
	public void clearContent(){
		contentSystem.removeContentItem(window);
		contentSystem.removeContentItem(label);
		
		window = null;
		label = null;
		okButton = null;
		cancelButton = null;
		noAnswerLabel = null;
	}
}
