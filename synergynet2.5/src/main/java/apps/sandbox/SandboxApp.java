/*
 * Copyright (c) 2008 University of Durham, England
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

package apps.sandbox;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import com.jme.math.FastMath;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

import common.CommonResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.ImageTextLabel;
import synergynetframework.appsystem.contentsystem.items.Keyboard;
import synergynetframework.appsystem.contentsystem.items.ObjShape;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.RoundImageLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel;
import synergynetframework.appsystem.contentsystem.items.TextLabel.Alignment;
import synergynetframework.appsystem.contentsystem.items.listener.ItemEventAdapter;
import synergynetframework.appsystem.table.SynergyNetAppUtils;
import synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp;
import synergynetframework.appsystem.table.appregistry.ApplicationInfo;
import synergynetframework.appsystem.table.appregistry.menucontrol.HoldTopRightConfirmVisualExit;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;


/**
 * The Class SandboxApp.
 */
public class SandboxApp extends DefaultSynergyNetApp {
	
	/** The colours. */
	private Color[] colours = {Color.magenta, Color.red, Color.blue, Color.green, Color.yellow, Color.pink, Color.orange, Color.CYAN, new Color(255, 69, 0, 255), new Color(255, 0, 255, 255)};
	
	/** The current count. */
	private int currentCount = 0;
	
	/** The button on. */
	private boolean buttonOn = false;
	
	/** The content system. */
	private ContentSystem contentSystem;
	
	/**
	 * Instantiates a new sandbox app.
	 *
	 * @param info the info
	 */
	public SandboxApp(ApplicationInfo info) {
		super(info);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#addContent()
	 */
	@Override
	public void addContent() {
		SynergyNetAppUtils.addTableOverlay(this);
		contentSystem = ContentSystem.getContentSystemForSynergyNetApp(this);	
		this.setMenuController(new HoldTopRightConfirmVisualExit(this));		
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.SynergyNetApp#onActivate()
	 */
	@Override
	public void onActivate() {
		contentSystem.removeAllContentItems();		
    	addKeyboard();	
		for (int i = 0; i < FastMath.nextRandomInt(5, 15); i++)addOctagon();
		for (int i = 0; i < FastMath.nextRandomInt(5, 15); i++)addSquare();
		for (int i = 0; i < FastMath.nextRandomInt(5, 15); i++)addCircle();	
	};
	
	/**
	 * Adds the octagon.
	 */
	private void addOctagon(){
		ObjShape shape = (ObjShape)this.contentSystem.createContentItem(ObjShape.class);
		shape.setShapeGeometry(SandboxApp.class.getResource("octagonshape.obj"));	
		Color colour = getNextColour();
		shape.setSolidColour(new ColorRGBA(colour.getRed(), colour.getGreen(), colour.getBlue(), colour.getAlpha()));
		positionRandomly(shape);		
	}
	
	/**
	 * Adds the square.
	 */
	private void addSquare() {
		ImageTextLabel shape = (ImageTextLabel)this.contentSystem.createContentItem(ImageTextLabel.class);
		shape.setBackgroundColour(getNextColour());
		shape.setAutoFit(false);
		shape.setWidth(200);
		shape.setHeight(200);
		shape.setBorderSize(0);
		positionRandomly(shape);
	}
		
	/**
	 * Adds the circle.
	 */
	private void addCircle() {
		RoundImageLabel shape = (RoundImageLabel)this.contentSystem.createContentItem(RoundImageLabel.class);
		shape.setBackgroundColour(getNextColour());
		shape.setBorderSize(0);
		shape.setRadius(100);
		positionRandomly(shape);
	}	
	
	/**
	 * Position randomly.
	 *
	 * @param shape the shape
	 */
	private void positionRandomly(OrthoContentItem shape){
		shape.makeFlickable(3f);
		shape.setAngle((FastMath.nextRandomInt(1, 200)/100f) * FastMath.PI);
		shape.setScale((FastMath.nextRandomInt(50, 200)/100f));
		shape.setScaleLimit(0.5f, 2f);
		shape.placeRandom();
	}
	
	/**
	 * Gets the next colour.
	 *
	 * @return the next colour
	 */
	private Color getNextColour(){
		currentCount++;
		if (currentCount == colours.length)currentCount = 0;
		return colours[currentCount];
	}	
	
	/**
	 * Adds the keyboard.
	 */
	private void addKeyboard(){
		
		final ImageTextLabel button = (ImageTextLabel) contentSystem.createContentItem(ImageTextLabel.class);
		button.setAsTopObject();
		button.setBringToTopable(false);
		
		final TextLabel answerText = (TextLabel)this.contentSystem.createContentItem(TextLabel.class);
		
		answerText.setAsTopObject();
		answerText.setBringToTopable(false);
		answerText.setText(" ");
		answerText.setAlignment(Alignment.LEFT);
		answerText.setFont(new Font("Arial", Font.PLAIN,30));
		answerText.setBackgroundColour(Color.black);
		answerText.setTextColour(Color.white);
		answerText.setBorderColour(Color.gray);
		answerText.setBorderSize(2);		
		answerText.setRotateTranslateScalable(false);	

		final Keyboard keyboard = (Keyboard)contentSystem.createContentItem(Keyboard.class);
		keyboard.setScaleLimit(0.5f, 3f);
		keyboard.setKeyboardImageResource(SandboxApp.class.getResource("keyboard-inactive.jpg"));
		keyboard.setKeyDefinitions(this.getKeyDefs());
		keyboard.centerItem();
		keyboard.makeFlickable(3f);
		keyboard.addKeyListener(new MTKeyListener(){
			boolean caps = false;			
			@Override
			public void keyReleasedEvent(KeyEvent evt) {				
				if (buttonOn){					
					if(evt.getKeyChar() == KeyEvent.VK_CAPS_LOCK){
						caps = !caps;
						if (caps){
							button.setBorderColour(Color.white);
							answerText.setBorderColour(Color.white);
						}else{
							button.setBorderColour(Color.gray);
							answerText.setBorderColour(Color.gray);
						}
					}						
					String text = answerText.getText();
					if(text == null) text ="";
					if(evt.getKeyChar() == KeyEvent.VK_BACK_SPACE){
						if(text.length() >0 && !text.equals(" "))text = text.substring(0,text.length()-1);
					}else if(evt.getKeyCode() == 521){
						text = text + "+";
					}else if(evt.getKeyChar() == KeyEvent.VK_STOP){
						text = text + ".";
					}else if(evt.getKeyChar() != KeyEvent.VK_CAPS_LOCK){
						text = text+evt.getKeyChar();
					}	
					
					if(evt.getKeyChar() != KeyEvent.VK_SHIFT){
						answerText.setText(text);						
					}					
				}
			}

			@Override
			public void keyPressedEvent(KeyEvent evt) {}
		});			
		
		button.setAutoFit(false);
		button.setBorderColour(Color.gray);
		button.setBorderSize(2);		
		button.setBackgroundColour(Color.darkGray);
		button.setRotateTranslateScalable(false);
		button.setWidth(answerText.getHeight());
		button.setHeight(answerText.getHeight());
		button.addItemListener(new ItemEventAdapter() {			
			public void cursorClicked(ContentItem item, long id, float x, float y, float pressure) {
				buttonOn = !buttonOn;
				if(buttonOn){
					button.setBackgroundColour(Color.gray);
					keyboard.makeUnflickable();
					keyboard.setRotateTranslateScalable(false, true);
					keyboard.setKeyboardImageResource(SandboxApp.class.getResource("keyboard-active.jpg"));
					button.setBorderColour(Color.white);
				}else{
					button.setBackgroundColour(Color.darkGray);
					keyboard.setRotateTranslateScalable(true, true);
					keyboard.makeFlickable(3f);
					keyboard.setKeyboardImageResource(SandboxApp.class.getResource("keyboard-inactive.jpg"));
					button.setBorderColour(Color.gray);
				}
			}
		});

		answerText.setAutoFit(false);			
		answerText.setHeight(button.getHeight());
		answerText.setWidth(DisplaySystem.getDisplaySystem().getRenderer().getWidth()-button.getWidth());
		answerText.setLocation(answerText.getWidth()/2, answerText.getHeight()/2);
		answerText.setBorderColour(Color.gray);
		button.setLocation(answerText.getWidth() + button.getWidth()/2, button.getHeight()/2);
	}

	/**
	 * Gets the key defs.
	 *
	 * @return the key defs
	 */
	@SuppressWarnings("unchecked")
	private List<Key> getKeyDefs() {
		try {
			ObjectInputStream ois = new ObjectInputStream(CommonResources.class.getResourceAsStream("keyboard.def"));
			return (List<Key>)ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
        }
		return null;
	}

	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.table.appdefinitions.DefaultSynergyNetApp#stateUpdate(float)
	 */
	@Override
	protected void stateUpdate(float tpf) {
		super.stateUpdate(tpf);
		if(contentSystem != null) contentSystem.update(tpf);
	}

}
