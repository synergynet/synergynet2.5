/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.ISimpleButton;
import synergynetframework.appsystem.contentsystem.items.listener.ItemListener;
import synergynetframework.appsystem.contentsystem.items.listener.SimpleButtonListener;

/**
 * The Class SimpleButton.
 */
public class SimpleButton extends TextLabel implements ItemListener,
		ISimpleButton {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2022191445971266719L;

	/** The listeners. */
	protected transient List<SimpleButtonListener> listeners = new ArrayList<SimpleButtonListener>();
	
	/**
	 * Instantiates a new simple button.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param name
	 *            the name
	 */
	public SimpleButton(ContentSystem contentSystem, String name) {
		super(contentSystem, name);
	}
	
	/**
	 * Adds the button listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addButtonListener(SimpleButtonListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	/**
	 * Fire button clicked.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireButtonClicked(long id, float x, float y, float pressure) {
		if (this.isRotateTranslateScaleEnabled()) {
			for (SimpleButtonListener l : listeners) {
				l.buttonClicked(this, id, x, y, pressure);
			}
		}
	}
	
	/**
	 * Fire button dragged.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireButtonDragged(long id, float x, float y, float pressure) {
		if (this.isRotateTranslateScaleEnabled()) {
			for (SimpleButtonListener l : listeners) {
				l.buttonDragged(this, id, x, y, pressure);
			}
		}
	}
	
	/**
	 * Fire button pressed.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireButtonPressed(long id, float x, float y, float pressure) {
		if (this.isRotateTranslateScaleEnabled()) {
			for (SimpleButtonListener l : listeners) {
				l.buttonPressed(this, id, x, y, pressure);
			}
		}
	}
	
	/**
	 * Fire button released.
	 *
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void fireButtonReleased(long id, float x, float y, float pressure) {
		if (this.isRotateTranslateScaleEnabled()) {
			for (SimpleButtonListener l : listeners) {
				l.buttonReleased(this, id, x, y, pressure);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * fireCursorChanged(long, float, float, float)
	 */
	@Override
	public void fireCursorChanged(long id, float x, float y, float pressure) {
		super.fireCursorChanged(id, x, y, pressure);
		this.fireButtonDragged(id, x, y, pressure);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * fireCursorClicked(long, float, float, float)
	 */
	@Override
	public void fireCursorClicked(long id, float x, float y, float pressure) {
		super.fireCursorClicked(id, x, y, pressure);
		this.fireButtonClicked(id, x, y, pressure);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * fireCursorPressed(long, float, float, float)
	 */
	@Override
	public void fireCursorPressed(long id, float x, float y, float pressure) {
		super.fireCursorPressed(id, x, y, pressure);
		this.fireButtonPressed(id, x, y, pressure);
	}
	
	/*
	 * (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.items.OrthoContentItem#
	 * fireCursorReleased(long, float, float, float)
	 */
	@Override
	public void fireCursorReleased(long id, float x, float y, float pressure) {
		super.fireCursorReleased(id, x, y, pressure);
		this.fireButtonReleased(id, x, y, pressure);
	}
	
	/**
	 * Removes the button listeners.
	 */
	public void removeButtonListeners() {
		listeners.clear();
	}
}
