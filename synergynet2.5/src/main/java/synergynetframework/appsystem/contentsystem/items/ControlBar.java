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

import java.io.Serializable;
import java.net.URL;

import controlbar.ControlbarResources;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation;
import synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener;

public class ControlBar extends OrthoContainer implements Serializable, IControlBarImplementation{
	
	private static final long serialVersionUID = -777819286170256309L;

	protected float controlBarLength = 150;
	protected float controlBarWidth =5;
	protected float currentPosition =0;
	protected boolean controlBarMoverEnabled = true;
	protected URL barResource = ControlbarResources.class.getResource("bar.png");
	protected URL finishedBarResource = ControlbarResources.class.getResource("finishedbar.png");
	protected URL cursorResource = ControlbarResources.class.getResource("cursor.png");
	
	public ControlBar(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}

	public float getControlBarLength() {
		return controlBarLength;
	}

	public void setControlBarLength(float controlBarLength) {
		this.controlBarLength = controlBarLength;
		((IControlBarImplementation)this.contentItemImplementation).setControlBarLength(controlBarLength);
	}

	public float getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(float currentPosition) {
		if (currentPosition==this.currentPosition) return;
		this.currentPosition = currentPosition;
		((IControlBarImplementation)this.contentItemImplementation).setCurrentPosition(currentPosition);
	}
	

	public float getControlBarWidth() {
		return controlBarWidth;
	}

	@Override
	public void addControlBarMoverListener(ControlBarMoverListener l) {		
		((IControlBarImplementation)this.contentItemImplementation).addControlBarMoverListener(l);
	}

	public boolean isControlBarMoverEnabled() {
		return controlBarMoverEnabled;
	}

	public void setControlBarMoverEnabled(boolean controlBarMoverEnabled) {
		this.controlBarMoverEnabled = controlBarMoverEnabled;
		((IControlBarImplementation)this.contentItemImplementation).setControlBarMoverEnabled(controlBarMoverEnabled);
	}

	@Override
	public void updateControlBar() {
		((IControlBarImplementation)this.contentItemImplementation).updateControlBar();
		
	}

	@Override
	public void setControlBarWidth(float controlBarWidth) {
		this.controlBarWidth = controlBarWidth;
		((IControlBarImplementation)this.contentItemImplementation).setControlBarLength(controlBarLength);
	}

	@Override
	public void setFinishedBarImageResource(URL imageResource) {
		this.finishedBarResource = imageResource;
		((IControlBarImplementation)this.contentItemImplementation).setFinishedBarImageResource(finishedBarResource);
	}
	
	public URL getFinishedBarImageResource(){
		return finishedBarResource;
	}

	@Override
	public void setBarImageResource(URL imageResource) {
		this.barResource = imageResource;
		((IControlBarImplementation)this.contentItemImplementation).setBarImageResource(barResource);
	}
	
	public URL getBarImageResource(){
		return barResource;
	}

	@Override
	public void setCursorImageResource(URL imageResource) {
		this.cursorResource = imageResource;
		((IControlBarImplementation)this.contentItemImplementation).setCursorImageResource(cursorResource);
	}
	
	public URL getCursorImageResource(){
		return cursorResource;
	}
}
