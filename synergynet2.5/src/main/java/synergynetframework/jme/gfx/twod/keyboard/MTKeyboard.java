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

package synergynetframework.jme.gfx.twod.keyboard;

import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import com.jme.scene.Spatial;
import com.jmex.awt.swingui.ImageGraphics;

import synergynetframework.jme.gfx.twod.DrawableSpatialImage;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.jme.gfx.twod.utils.GraphicsImageQuad;


/**
 * The Class MTKeyboard.
 */
public class MTKeyboard extends GraphicsImageQuad implements DrawableSpatialImage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6323388632774505481L;
	
	/** The gfx. */
	private ImageGraphics gfx;
	
	/** The img width. */
	private int imgWidth;
	
	/** The img height. */
	private int imgHeight;
	
	/** The keyboard image. */
	private Image keyboardImage;

	/** The current keys pressed. */
	protected Map<Integer,Key> currentKeysPressed = new HashMap<Integer,Key>();
	
	/** The listeners. */
	protected List<MTKeyListener> listeners = new ArrayList<MTKeyListener>();
	
	/** The keys. */
	protected List<Key> keys;
	
	/** The caps lock on. */
	protected boolean capsLockOn;
	
	/** The fake component. */
	protected JFrame fakeComponent;

	/**
	 * Instantiates a new MT keyboard.
	 *
	 * @param name the name
	 * @param keyboardImage the keyboard image
	 * @param keys the keys
	 * @param width the width
	 * @param height the height
	 * @param imgWidth the img width
	 * @param imgHeight the img height
	 */
	public MTKeyboard(String name, Image keyboardImage, List<Key> keys, float width, float height, int imgWidth, int imgHeight) {
		super(name, width, height, imgWidth, imgHeight);
		fakeComponent = new JFrame();
		fakeComponent.setSize(10,10);
		fakeComponent.setVisible(false);
		fakeComponent.setUndecorated(true);
		
		this.keyboardImage = keyboardImage;
		this.keys = keys;
		gfx = getImageGraphics();
		gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		this.imgWidth = imgWidth;
		this.imgHeight = imgHeight;
		draw();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#draw()
	 */
	public void draw() {
		gfx.drawImage(keyboardImage, 0, 0, imgWidth, imgHeight, null);
		updateGraphics();
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#getSpatial()
	 */
	public Spatial getSpatial() {
		return this;
	}

	/**
	 * Adds the key listener.
	 *
	 * @param listener the listener
	 */
	public void addKeyListener(MTKeyListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorDragged(long, int, int)
	 */
	@Override
	public void cursorDragged(long id, int x, int y) {
	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorPressed(long, int, int)
	 */
	@Override
	public void cursorPressed(long cursorID, int x, int y) {
		Key k = getKeyAtLocation(x, y);
		if(k != null) {
			
			char keyChar = k.getKeyChar(upperCaseModeOn());				
			KeyEvent event = new KeyEvent(fakeComponent, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), getModifiers(), k.key, keyChar);
			for(MTKeyListener l : listeners) {
				l.keyPressedEvent(event);
			}
			this.currentKeysPressed.put(k.key, k);
		}

	}

	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorReleased(long, int, int)
	 */
	@Override
	public void cursorReleased(long cursorID, int x, int y) {
		Key k = getKeyAtLocation(x, y);
		if(k != null) {
			
			if(k.key == KeyEvent.VK_CAPS_LOCK) {
				toggleCapsLock();
			}
			
			char keyChar = k.getKeyChar(upperCaseModeOn());
			KeyEvent event = new KeyEvent(fakeComponent, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), getModifiers(), k.key, keyChar);
			
			for(MTKeyListener l : listeners) {
				l.keyReleasedEvent(event);
			}
			this.currentKeysPressed.remove(k.key);
		}
	}
	
	/* (non-Javadoc)
	 * @see synergynetframework.jme.gfx.twod.DrawableSpatialImage#cursorClicked(long, int, int)
	 */
	@Override
	public void cursorClicked(long cursorID, int x, int y) {}

	
	/**
	 * Checks if is key pressed.
	 *
	 * @param vk the vk
	 * @return true, if is key pressed
	 */
	public boolean isKeyPressed(int vk) {
		return currentKeysPressed.keySet().contains(vk);
	}

	/**
	 * Upper case mode on.
	 *
	 * @return true, if successful
	 */
	private boolean upperCaseModeOn() {
		// xor of shift pressed and caps lock on
		return isKeyPressed(KeyEvent.VK_SHIFT) ^ capsLockOn;
	}
	
	/**
	 * Toggle caps lock.
	 */
	private void toggleCapsLock() {
		capsLockOn = !capsLockOn;
	}

	/**
	 * Gets the key at location.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the key at location
	 */
	private Key getKeyAtLocation(int x, int y) {
		for(Key k : keys) {
			if(k.area.contains(x, y)) {
				return k;
			}
		}
		return null;
	}



	/**
	 * Gets the modifiers.
	 *
	 * @return the modifiers
	 */
	private int getModifiers() {
		int modifiers = 0;
		if(isKeyPressed(KeyEvent.VK_SHIFT)){
			modifiers &= KeyEvent.VK_SHIFT;
		}
		return modifiers;
	}
}
