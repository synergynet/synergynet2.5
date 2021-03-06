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

import synergynetframework.appsystem.contentsystem.items.ContentItem;

/**
 * The Class ItemEventAdapter.
 */
public class ItemEventAdapter implements ItemListener {
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorChanged
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorChanged(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorClicked(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorDoubleClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorDoubleClicked(ContentItem item, long id, float x,
			float y, float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorLongHeld
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorLongHeld(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorPressed
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorPressed(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorReleased
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorReleased(ContentItem item, long id, float x, float y,
			float pressure) {
	}
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.items.listener.ItemListener
	 * #cursorRightClicked
	 * (synergynetframework.appsystem.contentsystem.items.ContentItem, long,
	 * float, float, float)
	 */
	@Override
	public void cursorRightClicked(ContentItem item, long id, float x, float y,
			float pressure) {
	}

}
