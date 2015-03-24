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

package synergynetframework.appsystem.contentsystem.items.listener;

import java.awt.event.ItemEvent;

import synergynetframework.appsystem.contentsystem.items.ContentItem;

/**
 * The listener interface for receiving item events. The class that is
 * interested in processing a item event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addItemListener<code> method. When
 * the item event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ItemEvent
 */
public interface ItemListener {

	/**
	 * Cursor changed.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void cursorChanged(ContentItem item, long id, float x, float y,
			float pressure);

	/**
	 * Cursor clicked.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void cursorClicked(ContentItem item, long id, float x, float y,
			float pressure);

	/**
	 * Cursor double clicked.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void cursorDoubleClicked(ContentItem item, long id, float x,
			float y, float pressure);

	/**
	 * Cursor long held.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void cursorLongHeld(ContentItem item, long id, float x, float y,
			float pressure);

	/**
	 * Cursor pressed.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void cursorPressed(ContentItem item, long id, float x, float y,
			float pressure);

	/**
	 * Cursor released.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void cursorReleased(ContentItem item, long id, float x, float y,
			float pressure);

	/**
	 * Cursor right clicked.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param pressure
	 *            the pressure
	 */
	public void cursorRightClicked(ContentItem item, long id, float x, float y,
			float pressure);
}
