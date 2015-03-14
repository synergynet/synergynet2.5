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

package synergynetframework.appsystem.contentsystem.items;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IKeyboardImplementation;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;

public class Keyboard extends QuadContentItem implements IKeyboardImplementation{

	protected URL keyboardImageResource;
	protected List<Key> keyDefinitions;
	protected float pixelsPerUnit = 2f;
	protected Color borderColour = Color.white;
	protected int borderSize = 4;
	
	private static final long serialVersionUID = 8945096690371189741L;

	protected transient List<MTKeyListener> listeners = new ArrayList<MTKeyListener>();

	public Keyboard(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	public void fireKeyPressed(KeyEvent evt) {
		for(MTKeyListener l : listeners) {
			l.keyPressedEvent(evt);
		}		
	}
	
	public void fireKeyReleased(KeyEvent evt) {
		for(MTKeyListener l : listeners) {
			l.keyReleasedEvent(evt);
		}		
	}

		
	public void addKeyListener(MTKeyListener l) {
		if(!listeners.contains(l)) listeners.add(l);
	}
	
	public void setRotateTranslateScalable(boolean isEnabled, boolean overRide){
		((IKeyboardImplementation)this.contentItemImplementation).setRotateTranslateScalable(isEnabled, overRide);
	}
	
	public void setKeyboardImageResource(URL keyboardImageResource){
		this.keyboardImageResource = keyboardImageResource;
		((IKeyboardImplementation)this.contentItemImplementation).setKeyboardImageResource(keyboardImageResource);
	}
	
	public void setKeyDefinitions(List<Key> keyDefinitions){
		this.keyDefinitions = keyDefinitions;
		((IKeyboardImplementation)this.contentItemImplementation).setKeyDefinitions(keyDefinitions);
	}
	
	public List<Key> getKeyDefinitions(){
		return keyDefinitions;
	}
	
	public URL getKeyboardImageResource(){
		return keyboardImageResource;
	}
	
	public void setPixelsPerUnit(float pixelsPerUnit){
		this.pixelsPerUnit = pixelsPerUnit;
		((IKeyboardImplementation)this.contentItemImplementation).setPixelsPerUnit(pixelsPerUnit);
	}
	
	public float getPixelsPerUnit(){
		return pixelsPerUnit;
	}
	
	public boolean isKeyPressed(int vk) {
		return ((IKeyboardImplementation)this.contentItemImplementation).getCurrentKeysPressed().contains(vk);
	}
	
	public ArrayList<Key> getCurrentKeysPressed() {
		return new ArrayList<Key>(((IKeyboardImplementation)this.contentItemImplementation).getCurrentKeysPressed());
	}
}
