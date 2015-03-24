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

package apps.projectmanagement.component.workflowchart.core.graphcomponents.nodes;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import common.CommonResources;

import apps.projectmanagement.GraphConfig;
import apps.projectmanagement.component.workflowchart.core.GraphManager;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.Keyboard;
import synergynetframework.appsystem.contentsystem.items.SimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonAdapter;
import synergynetframework.jme.gfx.twod.keyboard.Key;
import synergynetframework.jme.gfx.twod.keyboard.MTKeyListener;



/**
 * The Class KeyboardNode.
 */
public class KeyboardNode extends QuadNode{

	/** The listeners. */
	protected transient List<KeyboardListener> listeners = new ArrayList<KeyboardListener>();

	/** The keyboard. */
	protected Keyboard keyboard;
	
	/**
	 * Instantiates a new keyboard node.
	 *
	 * @param contentSystem the content system
	 * @param gManager the g manager
	 */
	public KeyboardNode(ContentSystem contentSystem, GraphManager gManager) {
		super(contentSystem, gManager);
		keyboard = (Keyboard)contentSystem.createContentItem(Keyboard.class);
		keyboard.setKeyboardImageResource(GraphConfig.nodeKeyboardImageResource);
		keyboard.setKeyDefinitions(this.getKeyDefs());
		keyboard.addKeyListener(new MTKeyListener(){
			@Override
			public void keyPressedEvent(KeyEvent evt) {
				fireKeyPressed(evt);
			}

			@Override
			public void keyReleasedEvent(KeyEvent evt) {
				fireKeyReleased(evt);
			}});
		super.setNodeContent(keyboard);
		
		closePoint.removeButtonListeners();
		closePoint.addButtonListener(new SimpleButtonAdapter() {
			
			public void buttonClicked(SimpleButton b, long id, float x,	float y, float pressure) {
				KeyboardNode.this.remove();
				graphManager.detachGraphNode(KeyboardNode.this);
			}
		});
	}

	
	/**
	 * Fire key pressed.
	 *
	 * @param evt the evt
	 */
	public void fireKeyPressed(KeyEvent evt) {
		for(KeyboardListener l : listeners) {
			l.keyPressed(evt);
		}		
	}
	
	/**
	 * Fire key released.
	 *
	 * @param evt the evt
	 */
	public void fireKeyReleased(KeyEvent evt) {
		for(KeyboardListener l : listeners) {
			l.keyReleased(evt);
		}		
	}
	
	/**
	 * Adds the key listener.
	 *
	 * @param l the l
	 */
	public void addKeyListener(KeyboardListener l) {
		if(!listeners.contains(l)) listeners.add(l);
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
	
	/**
	 * The listener interface for receiving keyboard events.
	 * The class that is interested in processing a keyboard
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addKeyboardListener<code> method. When
	 * the keyboard event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see KeyboardEvent
	 */
	public interface KeyboardListener {
		
		/**
		 * Key pressed.
		 *
		 * @param evt the evt
		 */
		public void keyPressed(KeyEvent evt);
		
		/**
		 * Key released.
		 *
		 * @param evt the evt
		 */
		public void keyReleased(KeyEvent evt);
	}
	
}
