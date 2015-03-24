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

package synergynetframework.jme.gfx;

import java.util.HashMap;
import java.util.Map;

import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;

import com.jme.scene.shape.Disk;
import com.jme.system.DisplaySystem;


/**
 * The Class TableOverlayNode.
 */
public class TableOverlayNode extends Node implements IMultiTouchEventListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6173073460723651499L;
	
	/** The Constant MAX_FINGERS. */
	private static final int MAX_FINGERS = 40;
	
	/** The disk radius. */
	private static float diskRadius = 10f; 

	/** The display width. */
	private int displayWidth;
	
	/** The display height. */
	private int displayHeight;

	/** The disks. */
	private Map<Integer, Disk> disks = new HashMap<Integer,Disk>();
	
	/** The disk index in use. */
	private Map<Long,Integer> diskIndexInUse = new HashMap<Long,Integer>();

	/**
	 * Instantiates a new table overlay node.
	 *
	 * @param zdepth the zdepth
	 */
	public TableOverlayNode(float zdepth) {
		super();

		this.displayWidth = DisplaySystem.getDisplaySystem().getWidth();
		this.displayHeight = DisplaySystem.getDisplaySystem().getHeight();

		for(int i = 0; i < MAX_FINGERS; i++) {
			Disk d = new Disk("cursor_" + i, 8, 8, diskRadius);
			d.setZOrder(999999);
			d.setSolidColor(ColorRGBA.randomColor());
			d.setLocalTranslation(-diskRadius, 100, zdepth);
			disks.put(i, d);
			attachChild(d);
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorPressed(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorPressed(MultiTouchCursorEvent event) {
		for(int i = 0; i < MAX_FINGERS; i++) {
			if(!diskIndexInUse.values().contains(i)) {
				long id = event.getCursorID();
				Disk d = disks.get(i);
				d.setLocalTranslation(event.getPosition().x * displayWidth, event.getPosition().y * displayHeight, 0f);
				diskIndexInUse.put(id, i);
				d.updateGeometricState(0f, true);			
				return;
			}
		}
	}


	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorChanged(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorChanged(MultiTouchCursorEvent event) {
		long id = event.getCursorID();
		if(diskIndexInUse.keySet().contains(id)) {
			int index = diskIndexInUse.get(id);
			Disk d = disks.get(index);
			if(d != null) {
				d.setLocalTranslation(event.getPosition().x * displayWidth, event.getPosition().y * displayHeight, 0f);
				d.updateGeometricState(0f, true);
			}
		}
	}

	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorClicked(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorClicked(MultiTouchCursorEvent event) {
	}



	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#cursorReleased(synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	public void cursorReleased(MultiTouchCursorEvent event) {
		long id = event.getCursorID();
		
		int index = diskIndexInUse.get(id);
		Disk d = disks.get(index);
		if(d != null) {
			d.setLocalTranslation(-diskRadius, -diskRadius, 0f);
			diskIndexInUse.remove(id);
			d.updateGeometricState(0f, true);
		}
	}


	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectAdded(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectAdded(MultiTouchObjectEvent event) {}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectChanged(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectChanged(MultiTouchObjectEvent event) {}
	
	/* (non-Javadoc)
	 * @see synergynetframework.mtinput.IMultiTouchEventListener#objectRemoved(synergynetframework.mtinput.events.MultiTouchObjectEvent)
	 */
	public void objectRemoved(MultiTouchObjectEvent event) {}

}
