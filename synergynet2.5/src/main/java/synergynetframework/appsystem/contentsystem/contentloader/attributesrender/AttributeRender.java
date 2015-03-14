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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.contentloader.config.AttributeConstants;
import synergynetframework.appsystem.contentsystem.items.ContentItem;

public class AttributeRender {

	public static final String CONTENT_ITEM_RENDER_FOLDER = "synergynetframework.appsystem.contentsystem.contentloader.attributesrender";
	private static final Logger log = Logger.getLogger(AttributeRender.class.getName());	
	
	public static void render(Map<ContentItem, Map<String, String>> items, ContentSystem contentSystem) {
		
		String contentItemTypeName;
		String type;
		Object obj=null;
		
		for (ContentItem item: items.keySet()){
			
			type = items.get(item).get(AttributeConstants.ITEM_CONTENTITEMTYPE)+"Render";
			contentItemTypeName = CONTENT_ITEM_RENDER_FOLDER+"."+type;	
					
			try {	
				Class<?> renderType = Class.forName(contentItemTypeName);	
				Constructor<?> con = renderType.getConstructor();
				obj = con.newInstance();
				((ContentItemRender)obj).render(items.get(item), item, contentSystem);
						
			}catch (SecurityException e) {
				log.severe(e.toString());
			} catch (NoSuchMethodException e) {
				log.severe(e.toString());
			} catch (IllegalArgumentException e) {
				log.severe(e.toString());
			} catch (InstantiationException e) {
				log.severe(e.toString());
			} catch (IllegalAccessException e) {
				log.severe(e.toString());
			} catch (InvocationTargetException e) {
				log.severe(e.toString());
			} catch (ClassNotFoundException e) {
				log.severe(e.toString());
			}
			
		}
	}
}
