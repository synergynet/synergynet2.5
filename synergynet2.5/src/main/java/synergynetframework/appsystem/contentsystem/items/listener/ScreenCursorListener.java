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

package synergynetframework.appsystem.contentsystem.items.listener;

import synergynetframework.appsystem.contentsystem.items.ContentItem;

/**
 * The listener interface for receiving screenCursor events. The class that is
 * interested in processing a screenCursor event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addScreenCursorListener<code> method. When
 * the screenCursor event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ScreenCursorEvent
 */
public interface ScreenCursorListener {

	/**
	 * Screen cursor changed.
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
	public void screenCursorChanged(ContentItem item, long id, float x,
			float y, float pressure);

	/**
	 * Screen cursor clicked.
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
	public void screenCursorClicked(ContentItem item, long id, float x,
			float y, float pressure);

	/**
	 * Screen cursor pressed.
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
	public void screenCursorPressed(ContentItem item, long id, float x,
			float y, float pressure);

	/**
	 * Screen cursor released.
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
	public void screenCursorReleased(ContentItem item, long id, float x,
			float y, float pressure);
}
