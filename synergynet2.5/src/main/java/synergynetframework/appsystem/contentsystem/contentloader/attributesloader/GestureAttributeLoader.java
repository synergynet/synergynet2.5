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

package synergynetframework.appsystem.contentsystem.contentloader.attributesloader;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;

/**
 * The Class GestureAttributeLoader.
 */
public class GestureAttributeLoader extends AttributesLoader {
	
	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(GestureAttributeLoader.class.getName());
	
	/*
	 * (non-Javadoc)
	 * @see
	 * synergynetframework.appsystem.contentsystem.contentloader.attributesloader
	 * .AttributesLoader#loadAttributes(java.util.Map, java.lang.String)
	 */
	public void loadAttributes(Map<String, Map<String, String>> items,
			String filePath) {
		
		try {
			this.getAttriNotes(filePath, "contentitems_gesture_schema");
		} catch (XPathExpressionException e) {
			log.severe(e.toString());
		} catch (ParserConfigurationException e) {
			log.severe(e.toString());
		} catch (SAXException e) {
			log.severe(e.toString());
		} catch (IOException e) {
			log.severe(e.toString());
		}
		
		Map<String, String> itemAttributes;
		
		for (int i = 0; i < list.getLength(); i++) {
			
			attributeNodeMap = list.item(i).getAttributes();
			itemAttributes = items.get(attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_ID).getNodeValue()
					.trim());
			
			String manipulate = "";
			if (attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_MANIPULATE) != null) {
				manipulate = attributeNodeMap
						.getNamedItem(AttributeConstants.ITEM_MANIPULATE)
						.getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_MANIPULATE,
						manipulate);
			}
			
			String isBoundaryEnabled = "";
			if (attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_ISBOUNDARYENABLED) != null) {
				isBoundaryEnabled = attributeNodeMap
						.getNamedItem(AttributeConstants.ITEM_ISBOUNDARYENABLED)
						.getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_ISBOUNDARYENABLED,
						isBoundaryEnabled);
			}
			
			String rotateTranslateScalable = "";
			if (attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_ROTATETRANSLATESCALABLE) != null) {
				rotateTranslateScalable = attributeNodeMap
						.getNamedItem(
								AttributeConstants.ITEM_ROTATETRANSLATESCALABLE)
						.getNodeValue().trim();
				itemAttributes.put(
						AttributeConstants.ITEM_ROTATETRANSLATESCALABLE,
						rotateTranslateScalable);
			}
			
			String bringToTopable = "";
			if (attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_BRINGTOTOPABLE) != null) {
				bringToTopable = attributeNodeMap
						.getNamedItem(AttributeConstants.ITEM_BRINGTOTOPABLE)
						.getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_BRINGTOTOPABLE,
						bringToTopable);
			}
			
			String snapable = "";
			if (attributeNodeMap.getNamedItem(AttributeConstants.ITEM_SNAPABLE) != null) {
				snapable = attributeNodeMap
						.getNamedItem(AttributeConstants.ITEM_SNAPABLE)
						.getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_SNAPABLE, snapable);
			}
			
			String flickable = "";
			if (attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_FLICKABLE) != null) {
				flickable = attributeNodeMap
						.getNamedItem(AttributeConstants.ITEM_FLICKABLE)
						.getNodeValue().trim();
				itemAttributes
						.put(AttributeConstants.ITEM_FLICKABLE, flickable);
			}
			
			String spinnable = "";
			if (attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_SPINNABLE) != null) {
				spinnable = attributeNodeMap
						.getNamedItem(AttributeConstants.ITEM_SPINNABLE)
						.getNodeValue().trim();
				itemAttributes
						.put(AttributeConstants.ITEM_SPINNABLE, spinnable);
			}
			
			String scaleMotionable = "";
			if (attributeNodeMap
					.getNamedItem(AttributeConstants.ITEM_SCALEMOTIONABLE) != null) {
				scaleMotionable = attributeNodeMap
						.getNamedItem(AttributeConstants.ITEM_SCALEMOTIONABLE)
						.getNodeValue().trim();
				itemAttributes.put(AttributeConstants.ITEM_SCALEMOTIONABLE,
						scaleMotionable);
			}
			
		}
	}
}
