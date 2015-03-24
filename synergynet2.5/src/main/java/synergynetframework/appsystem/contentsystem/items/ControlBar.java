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

import java.io.Serializable;
import java.net.URL;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IControlBarImplementation;
import synergynetframework.appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.ControlBarMoverListener;
import controlbar.ControlbarResources;

/**
 * The Class ControlBar.
 */
public class ControlBar extends OrthoContainer implements Serializable,
		IControlBarImplementation {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -777819286170256309L;
	
	/** The bar resource. */
	protected URL barResource = ControlbarResources.class
			.getResource("bar.png");

	/** The control bar length. */
	protected float controlBarLength = 150;

	/** The control bar mover enabled. */
	protected boolean controlBarMoverEnabled = true;

	/** The control bar width. */
	protected float controlBarWidth = 5;

	/** The current position. */
	protected float currentPosition = 0;

	/** The cursor resource. */
	protected URL cursorResource = ControlbarResources.class
			.getResource("cursor.png");

	/** The finished bar resource. */
	protected URL finishedBarResource = ControlbarResources.class
			.getResource("finishedbar.png");

	/**
	 * Instantiates a new control bar.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public ControlBar(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .
	 * IControlBarImplementation#addControlBarMoverListener(synergynetframework.
	 * appsystem.contentsystem.jme.items.utils.controlbar.ControlBarMover.
	 * ControlBarMoverListener)
	 */
	@Override
	public void addControlBarMoverListener(ControlBarMoverListener l) {
		((IControlBarImplementation) this.contentItemImplementation)
				.addControlBarMoverListener(l);
	}
	
	/**
	 * Gets the bar image resource.
	 *
	 * @return the bar image resource
	 */
	public URL getBarImageResource() {
		return barResource;
	}
	
	/**
	 * Gets the control bar length.
	 *
	 * @return the control bar length
	 */
	public float getControlBarLength() {
		return controlBarLength;
	}
	
	/**
	 * Gets the control bar width.
	 *
	 * @return the control bar width
	 */
	public float getControlBarWidth() {
		return controlBarWidth;
	}
	
	/**
	 * Gets the current position.
	 *
	 * @return the current position
	 */
	public float getCurrentPosition() {
		return currentPosition;
	}
	
	/**
	 * Gets the cursor image resource.
	 *
	 * @return the cursor image resource
	 */
	public URL getCursorImageResource() {
		return cursorResource;
	}
	
	/**
	 * Gets the finished bar image resource.
	 *
	 * @return the finished bar image resource
	 */
	public URL getFinishedBarImageResource() {
		return finishedBarResource;
	}
	
	/**
	 * Checks if is control bar mover enabled.
	 *
	 * @return true, if is control bar mover enabled
	 */
	public boolean isControlBarMoverEnabled() {
		return controlBarMoverEnabled;
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#setBarImageResource(java.net.URL)
	 */
	@Override
	public void setBarImageResource(URL imageResource) {
		this.barResource = imageResource;
		((IControlBarImplementation) this.contentItemImplementation)
				.setBarImageResource(barResource);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#setControlBarLength(float)
	 */
	public void setControlBarLength(float controlBarLength) {
		this.controlBarLength = controlBarLength;
		((IControlBarImplementation) this.contentItemImplementation)
				.setControlBarLength(controlBarLength);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#setControlBarMoverEnabled(boolean)
	 */
	public void setControlBarMoverEnabled(boolean controlBarMoverEnabled) {
		this.controlBarMoverEnabled = controlBarMoverEnabled;
		((IControlBarImplementation) this.contentItemImplementation)
				.setControlBarMoverEnabled(controlBarMoverEnabled);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#setControlBarWidth(float)
	 */
	@Override
	public void setControlBarWidth(float controlBarWidth) {
		this.controlBarWidth = controlBarWidth;
		((IControlBarImplementation) this.contentItemImplementation)
				.setControlBarLength(controlBarLength);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#setCurrentPosition(float)
	 */
	public void setCurrentPosition(float currentPosition) {
		if (currentPosition == this.currentPosition) {
			return;
		}
		this.currentPosition = currentPosition;
		((IControlBarImplementation) this.contentItemImplementation)
				.setCurrentPosition(currentPosition);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#setCursorImageResource(java.net.URL)
	 */
	@Override
	public void setCursorImageResource(URL imageResource) {
		this.cursorResource = imageResource;
		((IControlBarImplementation) this.contentItemImplementation)
				.setCursorImageResource(cursorResource);
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#setFinishedBarImageResource(java.net.URL)
	 */
	@Override
	public void setFinishedBarImageResource(URL imageResource) {
		this.finishedBarResource = imageResource;
		((IControlBarImplementation) this.contentItemImplementation)
				.setFinishedBarImageResource(finishedBarResource);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.implementation.interfaces
	 * .IControlBarImplementation#updateControlBar()
	 */
	@Override
	public void updateControlBar() {
		((IControlBarImplementation) this.contentItemImplementation)
				.updateControlBar();

	}
}
