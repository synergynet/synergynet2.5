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
import java.awt.Font;
import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.contentloader.utils.ColorUtil;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.TextLabel;

/**
 * The Class TextLabelRender.
 */
public class TextLabelRender extends FrameRender {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(TextLabelRender.class
			.getName());

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.contentloader.attributesrender
	 * .FrameRender#render(java.util.Map,
	 * synergynetframework.appsystem.contentsystem.items.ContentItem,
	 * synergynetframework.appsystem.contentsystem.ContentSystem)
	 */
	@Override
	protected void render(Map<String, String> itemAttrs,
			ContentItem contentItem, ContentSystem contentsys) {
		super.render(itemAttrs, contentItem, contentsys);
		
		TextLabel item = (TextLabel) contentItem;

		// set text
		String text = itemAttrs.get(AttributeConstants.ITEM_CONTENTS);
		if (text == null) {
			text = "";
			log.warning("The text attribute of " + item.getClass().getName()
					+ " is invalid");
		}
		item.setText(text);
		
		// set the item with Font
		int fontSize;
		String fontSizeString = itemAttrs.get(AttributeConstants.ITEM_FONTSIZE);
		if ((fontSizeString != null) && fontSizeString.matches("\\d*")) {
			fontSize = Integer.parseInt(itemAttrs
					.get(AttributeConstants.ITEM_FONTSIZE));
		} else {
			fontSize = 16;
			log.warning("The font size of " + item.getClass().getName()
					+ " is invalid");
		}
		if (itemAttrs.get(AttributeConstants.ITEM_FONT) != null) {
			item.setFont(new Font(itemAttrs.get(AttributeConstants.ITEM_FONT),
					Font.PLAIN, fontSize));
		} else {
			item.setFont(new Font("Arial", Font.PLAIN, fontSize));
			log.warning("The font attribute of " + item.getClass().getName()
					+ " is invalid");
		}

		// set the item with TextColour
		Color color = ColorUtil.GetColor(itemAttrs
				.get(AttributeConstants.ITEM_TEXTCOLOR));
		if (color != null) {
			item.setTextColour(color);
		} else {
			item.setTextColour(Color.BLACK);
			log.warning("The text color of " + item.getClass().getName()
					+ " is invalid");
		}

		// set auto fit
		boolean autoFit;
		String autoFitString = itemAttrs.get(AttributeConstants.ITEM_AUTOFIT);
		if (autoFitString != null) {
			autoFit = Boolean.parseBoolean(itemAttrs
					.get(AttributeConstants.ITEM_AUTOFIT));
			item.setAutoFitSize(autoFit);
		}
	}
	
}
