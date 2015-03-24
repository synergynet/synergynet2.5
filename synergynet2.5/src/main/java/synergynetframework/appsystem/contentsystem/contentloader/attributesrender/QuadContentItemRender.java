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

package synergynetframework.appsystem.contentsystem.contentloader.attributesrender;

import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.QuadContentItem;

/**
 * The Class QuadContentItemRender.
 */
public class QuadContentItemRender extends OrthoContentItemRender {
	
	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(QuadContentItemRender.class.getName());

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.contentloader.attributesrender
	 * .OrthoContentItemRender#render(java.util.Map,
	 * synergynetframework.appsystem.contentsystem.items.ContentItem,
	 * synergynetframework.appsystem.contentsystem.ContentSystem)
	 */
	@Override
	protected void render(Map<String, String> itemAttrs,
			ContentItem contentItem, ContentSystem contentsys) {
		super.render(itemAttrs, contentItem, contentsys);

		QuadContentItem item = (QuadContentItem) contentItem;

		// set the item with width
		int width;
		String widthString = itemAttrs.get(AttributeConstants.ITEM_WIDTH);
		if ((widthString != null) && widthString.matches("\\d*")) {
			width = Integer.parseInt(itemAttrs
					.get(AttributeConstants.ITEM_WIDTH));
		} else {
			width = 150;
			log.warning("The width of " + item.getClass().getName()
					+ " is invalid");
		}
		item.setWidth(width);

		// set the item with height
		int height;
		String heightString = itemAttrs.get(AttributeConstants.ITEM_HEIGHT);
		if ((heightString != null) && heightString.matches("\\d*")) {
			height = Integer.parseInt(itemAttrs
					.get(AttributeConstants.ITEM_HEIGHT));
		} else {
			height = 100;
			log.warning("The height of " + item.getClass().getName()
					+ " is invalid");
		}
		item.setHeight(height);

	}

}
