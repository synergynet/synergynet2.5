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

package synergynetframework.jme.cursorsystem.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import synergynetframework.jme.cursorsystem.MultiTouchElement;
import synergynetframework.jme.cursorsystem.cursordata.ScreenCursor;
import synergynetframework.mtinput.events.MultiTouchCursorEvent;

import com.jme.scene.Spatial;

/**
 * The Class MultiTouchButton.
 */
public class MultiTouchButton extends MultiTouchElement {
	
	/**
	 * The listener interface for receiving multiTouchButton events. The class
	 * that is interested in processing a multiTouchButton event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addMultiTouchButtonListener<code> method. When
	 * the multiTouchButton event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MultiTouchButtonEvent
	 */
	public interface MultiTouchButtonListener {

		/**
		 * Button clicked.
		 *
		 * @param button
		 *            the button
		 * @param c
		 *            the c
		 * @param event
		 *            the event
		 */
		public void buttonClicked(MultiTouchButton button, ScreenCursor c,
				MultiTouchCursorEvent event);

		/**
		 * Button dragged.
		 *
		 * @param button
		 *            the button
		 * @param c
		 *            the c
		 * @param event
		 *            the event
		 */
		public void buttonDragged(MultiTouchButton button, ScreenCursor c,
				MultiTouchCursorEvent event);

		/**
		 * Button pressed.
		 *
		 * @param button
		 *            the button
		 * @param c
		 *            the c
		 * @param event
		 *            the event
		 */
		public void buttonPressed(MultiTouchButton button, ScreenCursor c,
				MultiTouchCursorEvent event);

		/**
		 * Button released.
		 *
		 * @param button
		 *            the button
		 * @param c
		 *            the c
		 * @param event
		 *            the event
		 */
		public void buttonReleased(MultiTouchButton button, ScreenCursor c,
				MultiTouchCursorEvent event);
	}

	/** The last changed event. */
	protected MultiTouchCursorEvent lastChangedEvent;

	/** The last clicked event. */
	protected MultiTouchCursorEvent lastClickedEvent;

	/** The last pressed event. */
	protected MultiTouchCursorEvent lastPressedEvent;

	/** The last released event. */
	protected MultiTouchCursorEvent lastReleasedEvent;

	/** The listeners. */
	protected List<MultiTouchButtonListener> listeners = new ArrayList<MultiTouchButtonListener>();

	/** The props. */
	private Properties props = new Properties();

	/**
	 * Instantiates a new multi touch button.
	 *
	 * @param s
	 *            the s
	 */
	public MultiTouchButton(Spatial s) {
		super(s);
	}
	
	/**
	 * Adds the button listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addButtonListener(MultiTouchButtonListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorChanged(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(ScreenCursor c, MultiTouchCursorEvent event) {
		this.lastChangedEvent = event;
		for (MultiTouchButtonListener l : listeners) {
			l.buttonDragged(this, c, event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorClicked(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(ScreenCursor c, MultiTouchCursorEvent event) {
		this.lastClickedEvent = event;
		for (MultiTouchButtonListener l : listeners) {
			l.buttonClicked(this, c, event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorPressed(
	 * synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(ScreenCursor c, MultiTouchCursorEvent event) {
		this.lastPressedEvent = event;
		for (MultiTouchButtonListener l : listeners) {
			l.buttonPressed(this, c, event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.jme.cursorsystem.MultiTouchElement#cursorReleased
	 * (synergynetframework.jme.cursorsystem.cursordata.ScreenCursor,
	 * synergynetframework.mtinput.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(ScreenCursor c, MultiTouchCursorEvent event) {
		this.lastReleasedEvent = event;
		for (MultiTouchButtonListener l : listeners) {
			l.buttonReleased(this, c, event);
		}
	}
	
	/**
	 * Gets the drag distance from cursor pressed event.
	 *
	 * @return the drag distance from cursor pressed event
	 */
	public double getDragDistanceFromCursorPressedEvent() {
		if ((this.lastChangedEvent != null) && (this.lastPressedEvent != null)) {
			return lastChangedEvent.getPosition().distance(
					lastPressedEvent.getPosition());
		}

		return Double.NaN;
	}
	
	/**
	 * Gets the last changed event.
	 *
	 * @return the last changed event
	 */
	public MultiTouchCursorEvent getLastChangedEvent() {
		return lastChangedEvent;
	}
	
	/**
	 * Gets the last clicked event.
	 *
	 * @return the last clicked event
	 */
	public MultiTouchCursorEvent getLastClickedEvent() {
		return lastClickedEvent;
	}
	
	/**
	 * Gets the last pressed event.
	 *
	 * @return the last pressed event
	 */
	public MultiTouchCursorEvent getLastPressedEvent() {
		return lastPressedEvent;
	}
	
	/**
	 * Gets the last released event.
	 *
	 * @return the last released event
	 */
	public MultiTouchCursorEvent getLastReleasedEvent() {
		return lastReleasedEvent;
	}
	
	/**
	 * Gets the property.
	 *
	 * @param key
	 *            the key
	 * @return the property
	 */
	public Object getProperty(String key) {
		return props.get(key);
	}
	
	/**
	 * Removes the button listener.
	 *
	 * @param l
	 *            the l
	 */
	public void removeButtonListener(MultiTouchButtonListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Sets the last changed event.
	 *
	 * @param lastChangedEvent
	 *            the new last changed event
	 */
	public void setLastChangedEvent(MultiTouchCursorEvent lastChangedEvent) {
		this.lastChangedEvent = lastChangedEvent;
	}

	/**
	 * Sets the last clicked event.
	 *
	 * @param lastClickedEvent
	 *            the new last clicked event
	 */
	public void setLastClickedEvent(MultiTouchCursorEvent lastClickedEvent) {
		this.lastClickedEvent = lastClickedEvent;
	}
	
	/**
	 * Sets the last pressed event.
	 *
	 * @param lastPressedEvent
	 *            the new last pressed event
	 */
	public void setLastPressedEvent(MultiTouchCursorEvent lastPressedEvent) {
		this.lastPressedEvent = lastPressedEvent;
	}

	/**
	 * Sets the last released event.
	 *
	 * @param lastReleasedEvent
	 *            the new last released event
	 */
	public void setLastReleasedEvent(MultiTouchCursorEvent lastReleasedEvent) {
		this.lastReleasedEvent = lastReleasedEvent;
	}

	/**
	 * Sets the property.
	 *
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void setProperty(String key, Object value) {
		props.put(key, value);
	}
}
