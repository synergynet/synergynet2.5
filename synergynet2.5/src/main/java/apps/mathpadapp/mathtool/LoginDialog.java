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
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import common.CommonResources;

import apps.conceptmap.GraphConfig;
import apps.mathpadapp.MathPadResources;
import apps.mathpadapp.mathtool.MathTool.MathToolListener;
import apps.mathpadapp.util.MTDialog;
import apps.mathpadapp.util.MTMessageBox;
import apps.mathpadapp.util.MTMessageBox.MessageListener;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.Keyboard;
import synergynetframework.appsystem.contentsystem.items.LineItem;
import synergynetframework.appsystem.contentsystem.items.MathPad;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.contentsystem.items.listener.OrthoControlPointRotateTranslateScaleAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;

public class LoginDialog extends MTDialog{
	
	protected Keyboard keyboard;
	protected LineItem line;
	protected boolean isKeyboradOn = false;	
	protected MathPad mathTool;

	public LoginDialog(final ContentSystem contentSystem, final MathTool parentTool){
		super(parentTool, contentSystem);
		this.setTitle("Login");
		this.setModal(true);
		final TextLabel userName = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		userName.setBorderSize(1);
		userName.setBorderColour(Color.black);
		userName.setAutoFitSize(false);
		userName.setWidth(170);
		userName.setHeight(30);
		userName.setBackgroundColour(Color.white);
		userName.setLocalLocation(10, 40);
		
		final TextLabel userNameLabel = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		userNameLabel.setBorderSize(0);
		userNameLabel.setBackgroundColour(this.getWindow().getBackgroundColour());
		userNameLabel.setText("Username");
		userNameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		userNameLabel.setLocalLocation(-110, 40);
		
		SimpleButton userNameBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		userNameBtn.setBorderSize(1);
		userNameBtn.setBorderColour(Color.black);
		userNameBtn.setAutoFitSize(false);
		userNameBtn.setWidth(30);
		userNameBtn.setHeight(30);
		userNameBtn.drawImage(MathPadResources.class.getResource("buttons/text_off.jpg"));
		userNameBtn.setLocalLocation(115, 40);
		userNameBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (!isKeyboradOn){
					showKeyborad(userName);
					isKeyboradOn = true;
				}
				else{
					hideKeyborad();
					isKeyboradOn= false;
				}
			}
		});

		
		final TextLabel password = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		password.setBorderSize(1);
		password.setBorderColour(Color.black);
		password.setAutoFitSize(false);
		password.setWidth(170);
		password.setHeight(30);
		password.setBackgroundColour(Color.white);
		password.setLocalLocation(10, -10);
		
		final TextLabel passwordLabel = (TextLabel) contentSystem.createContentItem(TextLabel.class);
		passwordLabel.setBorderSize(0);
		passwordLabel.setBackgroundColour(this.getWindow().getBackgroundColour());
		passwordLabel.setText("Password");
		passwordLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		passwordLabel.setLocalLocation(-110, -10);
		
		SimpleButton passwordBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		passwordBtn.setBorderSize(1);
		passwordBtn.setBorderColour(Color.black);
		passwordBtn.setAutoFitSize(false);
		passwordBtn.setWidth(30);
		passwordBtn.setHeight(30);
		passwordBtn.drawImage(MathPadResources.class.getResource("buttons/text_off.jpg"));
		passwordBtn.setLocalLocation(115, -10);
		passwordBtn.addButtonListener(new SimpleButtonAdapter(){

			@Override
			public void buttonReleased(SimpleButton b, long id, float x,
					float y, float pressure) {
				if (!isKeyboradOn){
					showKeyborad(password);
					isKeyboradOn = true;
				}
				else{
					hideKeyborad();
					isKeyboradOn= false;
				}
			}
		});
		
		final SimpleButton okBtn = (SimpleButton) contentSystem.createContentItem(SimpleButton.class);
		okBtn.setBorderSize(1);
		okBtn.setBorderColour(Color.black);
		okBtn.setBackgroundColour(Color.LIGHT_GRAY);
		okBtn.setTextColour(Color.black);
		okBtn.setAutoFitSize(false);
		okBtn.setWidth(50);
		okBtn.setHeight(30);
		okBtn.setText("Login");
		okBtn.setLocalLocation(0, -55);
		okBtn.addButtonListener(new SimpleButtonAdapter(){
			@Override
			public void buttonReleased(SimpleButton b, long id, float x, float y,
					float pressure) {
				okBtn.setBackgroundColour(Color.LIGHT_GRAY);
				okBtn.setTextColour(Color.black);
				
				if(userName.getText() != null && !userName.getText().trim().equals("")){
					if(parentTool != null){	
						for(MathToolListener listener :parentTool.mathToolListeners){
								listener.userLogin(userName.getText().trim(), password.getText().trim());
						}
					}
				}else{
					final MTMessageBox msg = new MTMessageBox(parentTool, contentSystem);
					msg.setTitle("Login Error");
					msg.setMessage("Invalid Username");
					msg.getCancelButton().setVisible(false);
					msg.getOkButton().setLocalLocation(0, msg.getOkButton().getLocalLocation().y);
					msg.addMessageListener(new MessageListener(){
						@Override
						public void buttonClicked(String buttonId) {
						}

						@Override
						public void buttonReleased(String buttonId) {
							msg.close();
						}
					});
				}
				LoginDialog.this.setVisible(false);
			}

			@Override
			public void buttonPressed(SimpleButton b, long id, float x, float y,
					float pressure) {
				okBtn.setBackgroundColour(Color.DARK_GRAY);
				okBtn.setTextColour(Color.white);
			}
		});
		
		this.getWindow().addSubItem(userName);
		this.getWindow().addSubItem(userNameLabel);
		this.getWindow().addSubItem(userNameBtn);
		this.getWindow().addSubItem(password);
		this.getWindow().addSubItem(passwordLabel);
		this.getWindow().addSubItem(passwordBtn);
		this.getWindow().addSubItem(okBtn);
		/*
		this.getWindow().addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				if(isKeyboradOn && keyboard != null){
					keyboard.setLocalLocation(LoginDialog.this.getWindow().getLocalLocation().getX(), LoginDialog.this.getWindow().getLocalLocation().getY() - LoginDialog.this.getWindow().getHeight()+20);
					updateLine();
				}
			}
			
		});
		*/
		this.setHeight(160);
		
		if(closeButton != null){
			this.closeButton.removeButtonListeners();
			this.closeButton.addButtonListener(new SimpleButtonAdapter(){
			public void buttonReleased(SimpleButton b, long id, float x, float y,float pressure) {
				LoginDialog.this.setVisible(false);
			}			
		});
		}
	}
	

	private void showKeyborad(final TextLabel edittedTextLabel){
		if (keyboard!= null) return;
		keyboard = (Keyboard)contentSystem.createContentItem(Keyboard.class);
		keyboard.setAngle(LoginDialog.this.getWindow().getAngle());
		keyboard.setLocalLocation(LoginDialog.this.getWindow().getLocalLocation().getX(), LoginDialog.this.getWindow().getLocalLocation().getY() - LoginDialog.this.getWindow().getHeight()-30);
		keyboard.setScale(0.95f);
		keyboard.setKeyboardImageResource(GraphConfig.nodeKeyboardImageResource);
		keyboard.setKeyDefinitions(this.getKeyDefs());
		keyboard.setBringToTopable(false);
		keyboard.setOrder(9999);
		
		line = (LineItem)contentSystem.createContentItem(LineItem.class);
		line.setSourceItem(LoginDialog.this.getWindow());
		line.setSourceLocation(LoginDialog.this.getWindow().getLocalLocation());
		line.setTargetItem(keyboard);
		line.setTargetLocation(keyboard.getLocalLocation());
		line.setArrowMode(LineItem.NO_ARROWS);
		line.setLineMode(LineItem.SEGMENT_LINE);

		keyboard.addKeyListener(new MTKeyListener(){

			@Override
			public void keyReleasedEvent(KeyEvent evt) {
				String text = edittedTextLabel.getText();
				if(text == null) text ="";
				if(evt.getKeyChar() == KeyEvent.VK_ENTER){
					edittedTextLabel.setText(text + evt.getKeyChar());
				}
				else if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE){
					if(text.length() >0){
						text = text.substring(0,text.length()-1);
						edittedTextLabel.setText(text);
					}
				}
				else if(evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK){
					text = text+evt.getKeyChar();
					edittedTextLabel.setText(text);
				}
			}

			@Override
			public void keyPressedEvent(KeyEvent evt) {
				
			}});
		
		keyboard.addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				updateLine();
			}
			
		});
		
		this.getWindow().addOrthoControlPointRotateTranslateScaleListener(new OrthoControlPointRotateTranslateScaleAdapter(){

			@Override
			public void itemTranslated(ContentItem item, float newLocationX,
					float newLocationY, float oldLocationX, float oldLocationY) {
				updateLine();
			}
			
		});
	}
	
	private void hideKeyborad(){
		if (this.keyboard!=null){
			this.contentSystem.removeContentItem(keyboard);
			keyboard = null;
		}
		
		if (this.line!=null){
			this.contentSystem.removeContentItem(line);
			line = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Key> getKeyDefs() {
		try {
			ObjectInputStream ois = new ObjectInputStream(CommonResources.class.getResourceAsStream("keyboard.def"));
			return (List<Key>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void updateLine(){
		if (line!=null && keyboard!=null){
			line.setSourceLocation(LoginDialog.this.getWindow().getLocalLocation());
			line.setTargetLocation(keyboard.getLocalLocation());
		}
	}
	
	@Override
	public void setVisible(boolean isVisible){
		super.setVisible(isVisible);
		this.getWindow().setBringToTopable(!isVisible);
		if(!isVisible)	hideKeyborad();
	}
	
	public Window getWindow(){
		return window;
	}
	
	class WriteAction implements SimpleButtonListener{

		@Override
		public void buttonClicked(SimpleButton b, long id, float x, float y,
				float pressure) {

		}

		@Override
		public void buttonDragged(SimpleButton b, long id, float x, float y,
				float pressure) {
			 
			
		}

		@Override
		public void buttonPressed(SimpleButton b, long id, float x, float y,
				float pressure) {
			 
			
		}

		@Override
		public void buttonReleased(SimpleButton b, long id, float x, float y,
				float pressure) {
			 
			
		}
		
	}
}


