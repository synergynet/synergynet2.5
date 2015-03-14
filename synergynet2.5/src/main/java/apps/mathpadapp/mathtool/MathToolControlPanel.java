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

import java.awt.Color;
import java.util.ArrayList;

import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.mathtool.ColorList.ColorListListener;
import apps.mathpadapp.mathtool.LineWidthPanel.LineWidthPanelListener;
import apps.mathpadapp.mathtool.MathTool.WritingState;
import apps.mathpadapp.util.MTMessageBox;
import apps.mathpadapp.util.MTMessageBox.MessageListener;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.LightImageLabel;
import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.contentsystem.items.OrthoContainer;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;

public class MathToolControlPanel{
	
	protected ContentSystem contentSystem;
	
	private LightImageLabel backImage;
	private ArrayList<SimpleButton> buttons = new ArrayList<SimpleButton>();
	private final int NO_BUTTONS_PER_ROW = 2;
	private OrthoContainer panel;
	private SimpleButton loginBtn, writeBtn, eraseBtn, previousBtn, nextBtn;
	private LineWidthPanel lineWidthPanel;
	private ColorList colorList;
	private WritingState currentWritingState = WritingState.FREE_DRAW;
	private SimpleButton setSolution;
	private MathTool tool;
	private float backupLineWidth = -1;
	
	protected transient ArrayList<ControlPanelListener> listeners = new ArrayList<ControlPanelListener>();
	
	public MathToolControlPanel(final ContentSystem contentSystem, final MathTool tool){
		this.contentSystem = contentSystem;
		this.tool = tool;
		panel = (OrthoContainer) contentSystem.createContentItem(OrthoContainer.class);
		
		backImage = (LightImageLabel)contentSystem.createContentItem(LightImageLabel.class);
		backImage.drawImage(MathPadResources.class.getResource("LeftPanelBackground.jpg"));
		backImage.setAutoFitSize(false);
		backImage.setWidth(NO_BUTTONS_PER_ROW * GraphConfig.CONTROL_PANEL_BUTTON_WIDTH + 4*panel.getBorderSize() + (NO_BUTTONS_PER_ROW-1)* panel.getBorderSize());
		backImage.setHeight(GraphConfig.CONTROL_PANEL_HEIGHT);
		backImage.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
		panel.addSubItem(backImage);
		backImage.setOrder(-1);
		
		loginBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		loginBtn.setAutoFitSize(false);
		loginBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		loginBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		loginBtn.drawImage(MathPadResources.class.getResource("buttons/login.jpg"));
		loginBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				tool.showLoginDialog();
			}
		});
		buttons.add(loginBtn);
		
		writeBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		writeBtn.setAutoFitSize(false);
		writeBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		writeBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		writeBtn.drawImage(MathPadResources.class.getResource("buttons/write_off.jpg"));
		writeBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(backupLineWidth != -1){
					tool.setLineWidth(backupLineWidth);
					lineWidthPanel.fireLineWidthChanged(backupLineWidth);
				}
				currentWritingState = WritingState.FREE_DRAW;
				MathToolControlPanel.this.updateWritingState();
				for(ControlPanelListener listener: listeners)
					listener.writingStateChanged(currentWritingState);
			}
		});
		buttons.add(writeBtn);
		
		eraseBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		eraseBtn.setAutoFitSize(false);
		eraseBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		eraseBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		eraseBtn.drawImage(MathPadResources.class.getResource("buttons/eraser_off.png"));
		eraseBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				currentWritingState = WritingState.ERASER;
				backupLineWidth = tool.getCurrentPad().getLineWidth();
				float maxLineWidth = lineWidthPanel.getAllLineWidthes()[lineWidthPanel.getAllLineWidthes().length -1];
				tool.setLineWidth(maxLineWidth);
				lineWidthPanel.fireLineWidthChanged(maxLineWidth);

				MathToolControlPanel.this.updateWritingState();
				for(ControlPanelListener listener: listeners)
					listener.writingStateChanged(currentWritingState);
			}
		});
		panel.addSubItem(eraseBtn);
		buttons.add(eraseBtn);
		
		final SimpleButton eraseAllBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		eraseAllBtn.setAutoFitSize(false);
		eraseAllBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		eraseAllBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		eraseAllBtn.drawImage(MathPadResources.class.getResource("buttons/eraseAll_off.jpg"));
		eraseAllBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				eraseAllBtn.removeAllImages();
				eraseAllBtn.drawImage(MathPadResources.class.getResource("buttons/eraseAll_on.jpg"));

			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				eraseAllBtn.removeAllImages();
				eraseAllBtn.drawImage(MathPadResources.class.getResource("buttons/eraseAll_off.jpg"));
				
				final MTMessageBox msg = new MTMessageBox(tool, tool.getWindow().getContentSystem());
				msg.setTitle("Clear");
				msg.setMessage("Are you sure you want to \n clear the pad content?");
				msg.addMessageListener(new MessageListener(){
					@Override
					public void buttonClicked(String buttonId) {
					}

					@Override
					public void buttonReleased(String buttonId) {
						if(buttonId.equals("OK")){
							for(ControlPanelListener listener: listeners) listener.padCleared();				
						}
						msg.close();
					}
				});	
			}
		});
		buttons.add(eraseAllBtn);
		
		final SimpleButton newPageBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		newPageBtn.setAutoFitSize(false);
		newPageBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		newPageBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		newPageBtn.drawImage(MathPadResources.class.getResource("buttons/new_page_off.jpg"));
		
		newPageBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				newPageBtn.removeAllImages();
				newPageBtn.drawImage(MathPadResources.class.getResource("buttons/new_page_on.jpg"));
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				newPageBtn.removeAllImages();
				newPageBtn.drawImage(MathPadResources.class.getResource("buttons/new_page_off.jpg"));
				MathPad newPad = tool.addNewPad();
				for(ControlPanelListener listener: listeners) listener.padCreated(newPad);
				updateNextPreviousStatus();
			}
		});
		buttons.add(newPageBtn);
		
		final SimpleButton removePageBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		removePageBtn.setAutoFitSize(false);
		removePageBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		removePageBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		removePageBtn.drawImage(MathPadResources.class.getResource("buttons/delete_page_off.jpg"));
		removePageBtn.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,	float y, float pressure) {
				removePageBtn.removeAllImages();
				removePageBtn.drawImage(MathPadResources.class.getResource("buttons/delete_page_on.jpg"));
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y, float pressure) {
				removePageBtn.removeAllImages();
				removePageBtn.drawImage(MathPadResources.class.getResource("buttons/delete_page_off.jpg"));
				final int removedPadIndex = tool.getCurrentPadIndex();
				if(tool.getAllPads().size() == 1) return;
				final MTMessageBox msg = new MTMessageBox(tool, tool.getWindow().getContentSystem());
				msg.setMessage("Are you sure you want to \n delete the current page?");
				msg.addMessageListener(new MessageListener(){
					@Override
					public void buttonClicked(String buttonId) {
					}

					@Override
					public void buttonReleased(String buttonId) {
						if(buttonId.equals("OK"))
							for(ControlPanelListener listener: listeners) listener.padRemoved(removedPadIndex);
						msg.close();
					}
				});
			}
		});
		buttons.add(removePageBtn);
		
		previousBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		previousBtn.setAutoFitSize(false);
		previousBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		previousBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		previousBtn.drawImage(MathPadResources.class.getResource("buttons/previous_off.jpg"));
		previousBtn.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				previousBtn.removeAllImages();
				previousBtn.drawImage(MathPadResources.class.getResource("buttons/previous_off.jpg"));
				
				int oldPadIndex = tool.getCurrentPadIndex();
				int padIndex = tool.previousPad();
				if(padIndex != oldPadIndex)	
					for(ControlPanelListener listener: listeners) listener.padChanged(padIndex);
			}
			
			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(tool.getCurrentPadIndex()>0){
					previousBtn.removeAllImages();
					previousBtn.drawImage(MathPadResources.class.getResource("buttons/previous_on.jpg"));
				}
			}
		});
		buttons.add(previousBtn);
		
		nextBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		nextBtn.setAutoFitSize(false);
		nextBtn.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		nextBtn.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		nextBtn.drawImage(MathPadResources.class.getResource("buttons/next_off.jpg"));
		nextBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonPressed(SimpleButton b, long id, float x,
					float y, float pressure) {
				if(tool.getCurrentPadIndex()<tool.getAllPads().size()-1){
					nextBtn.removeAllImages();
					nextBtn.drawImage(MathPadResources.class.getResource("buttons/next_on.jpg"));
				}
			}
			
			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				int oldPadIndex = tool.getCurrentPadIndex();
				int padIndex = tool.nextPad();
				if(padIndex != oldPadIndex)	
					for(ControlPanelListener listener: listeners) listener.padChanged(padIndex);
			}
		});
		buttons.add(nextBtn);
		
		setSolution = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		setSolution.setAutoFitSize(false);
		setSolution.setWidth(GraphConfig.CONTROL_PANEL_BUTTON_WIDTH);
		setSolution.setHeight(GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT);
		setSolution.drawImage(MathPadResources.class.getResource("buttons/answer.jpg"));
		setSolution.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,	float y, float pressure) {
				for(ControlPanelListener listener: listeners) listener.answerPadDisplayed();
			}
		});
		buttons.add(setSolution);
	
		colorList = new ColorList(contentSystem);
		colorList.addColorListListener(new ColorListListener(){

			@Override
			public void colorSelected(Color color) {
				for(ControlPanelListener listener: listeners)
					listener.textColorChanged(color);			
			}
			
		});
		panel.addSubItem(colorList.getDropDownList());
		
		// Set line wide panel.
		
		lineWidthPanel = new LineWidthPanel(contentSystem, GraphConfig.NO_OF_LINE_WIDTH_LEVELS, GraphConfig.LINE_WIDTH_SHIFT);
		panel.addSubItem(lineWidthPanel.getContentPanel());
		lineWidthPanel.addLineWidthPanelListener(new LineWidthPanelListener(){

			@Override
			public void lineWidthChanged(float lineWidth) {
				for(ControlPanelListener listener: listeners)
					listener.lineWidthChanged(lineWidth);
			}});
		lineWidthPanel.getContentPanel().setVisible(false);
		updateWritingState();
		updateNextPreviousStatus();
		setLayout();
	}
	
	protected void updateNextPreviousStatus() {
		int padIndex = tool.getCurrentPadIndex();
		previousBtn.removeAllImages();
		if(padIndex == 0)
			previousBtn.drawImage(MathPadResources.class.getResource("buttons/previous_on.jpg"));
		else
			previousBtn.drawImage(MathPadResources.class.getResource("buttons/previous_off.jpg"));
		
		nextBtn.removeAllImages();
		if(padIndex == tool.getAllPads().size()-1)
			nextBtn.drawImage(MathPadResources.class.getResource("buttons/next_on.jpg"));
		else
			nextBtn.drawImage(MathPadResources.class.getResource("buttons/next_off.jpg"));
	}

	public void setLayout(){
		int shiftX=0, shiftY=0;
		int i=0;
		for(SimpleButton button: buttons){
			if(!button.isVisible()) continue;
			button.setBorderSize(GraphConfig.CONTROL_PANEL_BORDER_SIZE);
			button.setBorderColour(GraphConfig.CONTROL_PANEL_BORDER_COLOR);
			panel.addSubItem(button);
			if(i==0){
				// place first button on the top left corder
				shiftX = -backImage.getWidth()/2 + 2*panel.getBorderSize()+ GraphConfig.CONTROL_PANEL_BUTTON_WIDTH/2;
				shiftY = backImage.getHeight()/2 - 2*panel.getBorderSize()- GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT/2;
			}
			else if(i%NO_BUTTONS_PER_ROW==0){
				shiftX = -backImage.getWidth()/2 + 2*panel.getBorderSize()+ GraphConfig.CONTROL_PANEL_BUTTON_WIDTH/2;
				shiftY-= panel.getBorderSize()+ GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT;
			}else{
				shiftX+= GraphConfig.CONTROL_PANEL_BUTTON_WIDTH + panel.getBorderSize();
			}
			button.setLocalLocation(shiftX, shiftY);
			i++;
		}
		
		shiftX = -backImage.getWidth()/2 + 2*panel.getBorderSize()+ colorList.getDropDownList().getWidth()/2;
		shiftY-= panel.getBorderSize()+ GraphConfig.CONTROL_PANEL_BUTTON_HEIGHT;
		colorList.getDropDownList().setLocalLocation(shiftX, shiftY);
		
		shiftY-= GraphConfig.COLOR_LIST_ITEM_HEIGHT + lineWidthPanel.getContentPanel().getHeight()/2- 10;
		lineWidthPanel.getContentPanel().setLocalLocation(0,shiftY);
	}
	
	private void updateWritingState(){
		writeBtn.removeAllImages();
		eraseBtn.removeAllImages();
		if(this.currentWritingState == WritingState.FREE_DRAW){
			writeBtn.drawImage(MathPadResources.class.getResource("buttons/write_on.jpg"));
			eraseBtn.drawImage(MathPadResources.class.getResource("buttons/eraser_off.png"));
			lineWidthPanel.getContentPanel().setVisible(true);
		}else if(this.currentWritingState == WritingState.ERASER){
			writeBtn.drawImage(MathPadResources.class.getResource("buttons/write_off.jpg"));
			eraseBtn.drawImage(MathPadResources.class.getResource("buttons/eraser_on.png"));
			lineWidthPanel.getContentPanel().setVisible(true);
		}
	}
	
	public OrthoContainer getContentPanel(){
		return panel;
	}
	
	public int getWidth(){
		return backImage.getWidth();
	}
	
	public int getHeight(){
		return backImage.getHeight();
	}
	
	public void addControlPanelListener(ControlPanelListener listener){
		if(!listeners.contains(listener)) listeners.add(listener); 
	}
	
	public void removeControlPanelListeners(){
		listeners.clear();
	}
	
	public interface ControlPanelListener{
		public void writingStateChanged(WritingState writingState);
		public void padCleared();
		public void padRemoved(int padIndex);
		public void textColorChanged(Color textColor);
		public void lineWidthChanged(float lineWidth);
		public void padCreated(MathPad newPad);
		public void padChanged(int padIndex);
		public void answerPadDisplayed();
	}

	public void init(MathToolInitSettings settings) {
		if(settings.getWritingState() != null){
			currentWritingState = settings.getWritingState();
			updateWritingState();
		}
		if(!(new Float(settings.getLineWidth()).isNaN()))
			lineWidthPanel.setLineWidth(settings.getLineWidth());
	}
	
	protected void setWritingState(WritingState writingState){
		this.currentWritingState = writingState;
		this.updateWritingState();
	}
	
	protected void setLineWidth(float lineWidth){
		if(!(new Float(lineWidth).isNaN()))
			lineWidthPanel.setLineWidth(lineWidth);
	}

	protected void setTextColor(Color color) {
		if(colorList ==null) return;
		this.colorList.setSelectedColor(color);
	}
	
	public void registerButton(SimpleButton button){
		buttons.add(button);
	}
	
	public void unregisterButton(SimpleButton button){
		buttons.remove(button);
	}
	
	public SimpleButton getSolutionButton(){
		return setSolution;
	}
	
	public ContentItem getLoginButton() {
		return loginBtn;
	}
}
