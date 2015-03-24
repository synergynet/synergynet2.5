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

package synergynetframework.appsystem.contentsystem.contentloader.attributesloader;

import java.util.Map;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;


/**
 * The Class AttributeLoaderUtil.
 */
public class AttributeLoaderUtil {
	
	/**
	 * Load item attributes.
	 *
	 * @param itemAttrs the item attrs
	 * @param attrs the attrs
	 */
	public static void loadItemAttributes(Map<String, String> itemAttrs, NamedNodeMap attrs){
		
		Node attrNode = attrs.getNamedItem(AttributeConstants.ITEM_SCALE);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_SCALE, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_ANGLE);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_ANGLE, attrNode.getNodeValue());
				
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_BACKGROUNDCOLOR);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_BACKGROUNDCOLOR, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_BORDERCOLOR);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_BORDERCOLOR, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_BORDERSIZE);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_BORDERSIZE, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_ISVISIABLE);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_ISVISIABLE, attrNode.getNodeValue());
	
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_SORTORDER);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_SORTORDER, attrNode.getNodeValue());
			
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_HEIGHT);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_HEIGHT, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_WIDTH);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_WIDTH, attrNode.getNodeValue());
		
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_TEXT);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_TEXT, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_TEXTCOLOR);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_TEXTCOLOR, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_FONT);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_FONT, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_FONTSIZE);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_FONTSIZE, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_AUTOFIT);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_AUTOFIT, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_CHARSPERLINE);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_CHARSPERLINE, attrNode.getNodeValue());
		
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_IMAGE_X);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_IMAGE_X, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_IMAGE_Y);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_IMAGE_Y, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_IMAGE_WIDTH);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_IMAGE_WIDTH, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_IMAGE_HEIGHT);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_IMAGE_HEIGHT, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_IMAGE_PATH);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_IMAGE_PATH, attrNode.getNodeValue());
		
		attrNode = attrs.getNamedItem(AttributeConstants.ITEM_IMAGE_AUTORATIO);
		if (attrNode!=null && !attrNode.getNodeValue().trim().isEmpty())
			itemAttrs.put(AttributeConstants.ITEM_IMAGE_AUTORATIO, attrNode.getNodeValue());
		
		
	}


}
