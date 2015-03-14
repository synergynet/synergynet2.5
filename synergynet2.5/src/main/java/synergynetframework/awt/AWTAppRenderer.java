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

package synergynetframework.awt;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import synergynetframework.mtinput.Blob;
import synergynetframework.mtinput.IMultiTouchEventListener;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;
import synergynetframework.mtinput.events.MultiTouchObjectEvent;

/**
 * Utility base class for building AWT based multi-touch applications.
 * Listens for multi-touch input, and keeps a track of 'blobs'.  Blobs
 * are stored in the coordinate space determined by the height and width,
 * set via setSize.
 * 
 * @author dcs0ah1
 *
 */

public abstract class AWTAppRenderer implements IMultiTouchEventListener {
	
	protected Map<Long,Blob> blobs = new HashMap<Long,Blob>();
	
	protected int height;
	protected int width;
	
	public abstract void render(Graphics2D g2d);

	public void cursorChanged(MultiTouchCursorEvent event) {
		Blob b = blobs.get(event.getCursorID());
		b.x = (int) (event.getPosition().x * width);
		b.y = (int) (event.getPosition().y * height);
		b.pressure = event.getPressure();
	}

	public void cursorPressed(MultiTouchCursorEvent event) {
		Blob b = new Blob(event.getCursorID(), (int) (event.getPosition().x * width),(int) (event.getPosition().y * height));
		synchronized(blobs) {
			blobs.put(b.id, b);
		}
	}

	public void cursorReleased(MultiTouchCursorEvent event) {
		synchronized(blobs) {
			blobs.remove(event.getCursorID());
		}
	}
	
	/**
	 * Sets the size of the coordinate space that blobs will be reported in.  This should typically
	 * match the AWT component that will draw the blobs.
	 * @param d
	 */
	public void setSize(Dimension d) {
		this.width = d.width;
		this.height = d.height;
	}
	
	/**
	 * Get the current blob map.
	 * @return
	 */
	public Map<Long,Blob> getBlobs() {
		return blobs;
	}

	// 
	
	public void cursorClicked(MultiTouchCursorEvent event) {}
	public void objectAdded(MultiTouchObjectEvent event) {}
	public void objectChanged(MultiTouchObjectEvent event) {}
	public void objectRemoved(MultiTouchObjectEvent event) {}


}
