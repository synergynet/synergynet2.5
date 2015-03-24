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

/**
 * The Class Keyboard.
 */
public class Keyboard extends QuadContentItem implements
		IKeyboardImplementation {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8945096690371189741L;

	/** The border colour. */
	protected Color borderColour = Color.white;

	/** The border size. */
	protected int borderSize = 4;

	/** The keyboard image resource. */
	protected URL keyboardImageResource;

	/** The key definitions. */
	protected List<Key> keyDefinitions;

	/** The listeners. */
	protected transient List<MTKeyListener> listeners = new ArrayList<MTKeyListener>();
	
	/** The pixels per unit. */
	protected float pixelsPerUnit = 2f;
	
	/**
	 * Instantiates a new keyboard.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public Keyboard(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	/**
	 * Adds the key listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addKeyListener(MTKeyListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	/**
	 * Fire key pressed.
	 *
	 * @param evt
	 *            the evt
	 */
	public void fireKeyPressed(KeyEvent evt) {
		for (MTKeyListener l : listeners) {
			l.keyPressedEvent(evt);
		}
	}
	
	/**
	 * Fire key released.
	 *
	 * @param evt
	 *            the evt
	 */
	public void fireKeyReleased(KeyEvent evt) {
		for (MTKeyListener l : listeners) {
			l.keyReleasedEvent(evt);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IKeyboardImplementation#getCurrentKeysPressed()
	 */
	public ArrayList<Key> getCurrentKeysPressed() {
		return new ArrayList<Key>(
				((IKeyboardImplementation) this.contentItemImplementation)
						.getCurrentKeysPressed());
	}

	/**
	 * Gets the keyboard image resource.
	 *
	 * @return the keyboard image resource
	 */
	public URL getKeyboardImageResource() {
		return keyboardImageResource;
	}

	/**
	 * Gets the key definitions.
	 *
	 * @return the key definitions
	 */
	public List<Key> getKeyDefinitions() {
		return keyDefinitions;
	}

	/**
	 * Gets the pixels per unit.
	 *
	 * @return the pixels per unit
	 */
	public float getPixelsPerUnit() {
		return pixelsPerUnit;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IKeyboardImplementation#isKeyPressed(int)
	 */
	public boolean isKeyPressed(int vk) {
		return ((IKeyboardImplementation) this.contentItemImplementation)
				.getCurrentKeysPressed().contains(vk);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IKeyboardImplementation#setKeyboardImageResource(java.net.URL)
	 */
	public void setKeyboardImageResource(URL keyboardImageResource) {
		this.keyboardImageResource = keyboardImageResource;
		((IKeyboardImplementation) this.contentItemImplementation)
				.setKeyboardImageResource(keyboardImageResource);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IKeyboardImplementation#setKeyDefinitions(java.util.List)
	 */
	public void setKeyDefinitions(List<Key> keyDefinitions) {
		this.keyDefinitions = keyDefinitions;
		((IKeyboardImplementation) this.contentItemImplementation)
				.setKeyDefinitions(keyDefinitions);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IKeyboardImplementation#setPixelsPerUnit(float)
	 */
	public void setPixelsPerUnit(float pixelsPerUnit) {
		this.pixelsPerUnit = pixelsPerUnit;
		((IKeyboardImplementation) this.contentItemImplementation)
				.setPixelsPerUnit(pixelsPerUnit);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IKeyboardImplementation#setRotateTranslateScalable(boolean, boolean)
	 */
	public void setRotateTranslateScalable(boolean isEnabled, boolean overRide) {
		((IKeyboardImplementation) this.contentItemImplementation)
				.setRotateTranslateScalable(isEnabled, overRide);
	}
}
