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

import java.awt.Color;
import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.contentloader.utils.ColorUtil;
import synergynetframework.appsystem.contentsystem.items.ContentItem;

/**
 * The Class ContentItemRender.
 */
public class ContentItemRender {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ContentItemRender.class
			.getName());
	
	/**
	 * Render.
	 *
	 * @param itemAttrs
	 *            the item attrs
	 * @param item
	 *            the item
	 * @param contentsys
	 *            the contentsys
	 */
	protected void render(Map<String, String> itemAttrs, ContentItem item,
			ContentSystem contentsys) {

		// set the item with id
		// set the item with scale
		String idString = itemAttrs.get(AttributeConstants.ITEM_ID);
		if (idString != null) {
			item.setId(idString);
		} else {
			log.warning("The scale of " + item.getClass().getName()
					+ " is invalid");
		}

		// set the item with scale
		float scale;
		String scaleString = itemAttrs.get(AttributeConstants.ITEM_SCALE);
		if ((scaleString != null) && scaleString.matches("\\d*.\\d*")) {
			scale = Float.parseFloat(itemAttrs
					.get(AttributeConstants.ITEM_SCALE));
		} else {
			scale = 1f;
			log.warning("The scale of " + item.getClass().getName()
					+ " is invalid");
		}
		item.setScale(scale);

		// set the item with BgColour
		Color color = ColorUtil.GetColor(itemAttrs
				.get(AttributeConstants.ITEM_BACKGROUNDCOLOR));
		if (color != null) {
			item.setBackgroundColour(color);
		} else {
			item.setBackgroundColour(Color.WHITE);
			log.warning("The background colour of " + item.getClass().getName()
					+ " is invalid");
		}
		// set the item with BorderColour
		color = ColorUtil.GetColor(itemAttrs
				.get(AttributeConstants.ITEM_BORDERCOLOR));
		if (color != null) {
			item.setBorderColour(color);
		} else {
			item.setBorderColour(Color.BLACK);
			log.warning("The border colour of " + item.getClass().getName()
					+ " is invalid");
		}
		
		// set the item with BorderSize
		int borderSize;
		String borderSizeString = itemAttrs
				.get(AttributeConstants.ITEM_BORDERSIZE);
		if ((borderSizeString != null) && borderSizeString.matches("\\d*")) {
			borderSize = Integer.parseInt(itemAttrs
					.get(AttributeConstants.ITEM_BORDERSIZE));
		} else {
			borderSize = 10;
			log.warning("The border size of " + item.getClass().getName()
					+ " is invalid");
		}
		item.setBorderSize(borderSize);

		// set isBoundaryenabled
		boolean boundaryEnabled;
		String boundaryEnabledString = itemAttrs
				.get(AttributeConstants.ITEM_ISBOUNDARYENABLED);
		if (boundaryEnabledString != null) {
			boundaryEnabled = Boolean.parseBoolean(itemAttrs
					.get(AttributeConstants.ITEM_ISBOUNDARYENABLED));
			if (!boundaryEnabled) {
				item.setBoundaryEnabled(boundaryEnabled);
			}
		}

		// set isVisiable
		boolean isVisiable;
		String isVisiableString = itemAttrs
				.get(AttributeConstants.ITEM_ISVISIABLE);
		if (isVisiableString != null) {
			isVisiable = Boolean.parseBoolean(itemAttrs
					.get(AttributeConstants.ITEM_ISBOUNDARYENABLED));
			if (!isVisiable) {
				item.setVisible(isVisiable);
			}
		}
		
	}

	/**
	 * Render attributes.
	 *
	 * @param items
	 *            the items
	 * @param item
	 *            the item
	 * @param contentSys
	 *            the content sys
	 */
	public void renderAttributes(Map<String, String> items, ContentItem item,
			ContentSystem contentSys) {
		render(items, item, contentSys);
	}
	
}
