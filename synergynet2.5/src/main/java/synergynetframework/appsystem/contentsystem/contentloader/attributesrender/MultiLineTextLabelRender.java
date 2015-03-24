/* Copyright (c) 2008 University of Durham, England
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

package synergynetframework.appsystem.contentsystem.contentloader.attributesrender;

import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.MultiLineTextLabel;


/**
 * The Class MultiLineTextLabelRender.
 */
public class MultiLineTextLabelRender extends TextLabelRender {
	
	/** The Constant log. */
	private static final Logger log = Logger.getLogger(MultiLineTextLabelRender.class.getName());
	
	/* (non-Javadoc)
	 * @see synergynetframework.appsystem.contentsystem.contentloader.attributesrender.TextLabelRender#render(java.util.Map, synergynetframework.appsystem.contentsystem.items.ContentItem, synergynetframework.appsystem.contentsystem.ContentSystem)
	 */
	@Override
	protected void render(Map<String, String> itemAttrs,
			ContentItem contentItem, ContentSystem contentsys) {
		super.render(itemAttrs, contentItem, contentsys);	
		
		MultiLineTextLabel item = (MultiLineTextLabel)contentItem;

		//auto separate the lines 
		int charsPerLine;
		String charsPerLineString =itemAttrs.get(AttributeConstants.ITEM_CHARSPERLINE);				
		if (charsPerLineString!=null && charsPerLineString.matches("\\d*"))
			charsPerLine = Integer.parseInt(itemAttrs.get(AttributeConstants.ITEM_CHARSPERLINE));
		else{
			charsPerLine = 20;
			log.warning("The charsPerline attribute of "+item.getClass().getName()+" is invalid");
		}
		item.setLines(item.getText(), charsPerLine);
		
		//separate the lines by insert "\n"
		boolean autoSeperateMultiLines;
		String autoSeperateMultiLinesString =itemAttrs.get(AttributeConstants.ITEM_AUTOSEPERATEMULTILINES);				
		if (autoSeperateMultiLinesString!=null){
			autoSeperateMultiLines = Boolean.parseBoolean(itemAttrs.get(AttributeConstants.ITEM_AUTOSEPERATEMULTILINES));
			if (!autoSeperateMultiLines)
				item.setCRLFSeparatedString(item.getText());
		}

	}
}
