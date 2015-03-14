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

package synergynetframework.appsystem.contentsystem.jme.items.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.IImplementationItemFactory;
import synergynetframework.appsystem.contentsystem.items.implementation.interfaces.IContentItemImplementation;

public class JMEImplementationItemFactory implements IImplementationItemFactory {

	private static final long serialVersionUID = 3143730708904582391L;

	public static final String JME_IMPLEMENTATION_ITEM_FOLDER = "synergynetframework.appsystem.contentsystem.jme.items";
	
	private static final Logger log = Logger.getLogger(JMEImplementationItemFactory.class.getName());
	
	@Override
	public IContentItemImplementation create(ContentItem contentItem) {
		
		String contentItemTypePath = contentItem.getClass().getName();
		String[] contentItemTypePathParts = contentItemTypePath.split("\\.");
		String contentItemTypeName = contentItemTypePathParts[contentItemTypePathParts.length-1];
		
		Class<?> jMEcontentItemType =null;
		String itemFolder = contentItem.getClass().getPackage().getName();
		String jMEcontentItemTypeName = itemFolder+".JME"+contentItemTypeName;
		
		try {
			jMEcontentItemType = Class.forName(jMEcontentItemTypeName);
		} catch (ClassNotFoundException e1) {
			jMEcontentItemTypeName = JMEImplementationItemFactory.JME_IMPLEMENTATION_ITEM_FOLDER+".JME"+contentItemTypeName;
			try {
				jMEcontentItemType = Class.forName(jMEcontentItemTypeName);
			} catch (ClassNotFoundException e) {
				log.severe("Class-jMEcontentItemTypeName has not been found.\n"+e.toString());
				//e.printStackTrace();
			}
		}
				
		try {			
			Constructor<?> con = jMEcontentItemType.getConstructor(ContentItem.class);
			Object obj = con.newInstance(contentItem);
			log.info("JME implementation of the ContentItem-"+jMEcontentItemTypeName+" is created.");
			return (IContentItemImplementation)obj;		
		} catch (SecurityException e) {
			log.severe("SecurityException when creating JME implementation object of the contentitem.\n"+e.toString());
		} catch (NoSuchMethodException e) {
			log.severe("NoSuchMethodException when creating JME implementation object of the contentitem.\n"+e.toString());
		} catch (IllegalArgumentException e) {
			log.severe("IllegalArgumentException when creating JME implementation object of the contentitem.\n"+e.toString());
		} catch (InstantiationException e) {
			log.severe("InstantiationException when creating JME implementation object of the contentitem.\n"+e.toString());
		} catch (IllegalAccessException e) {
			log.severe("IllegalAccessException when creating JME implementation object of the contentitem.\n"+e.toString());
		} catch (InvocationTargetException e) {
			log.severe("InvocationTargetException when creating JME implementation object of the contentitem.\n"+e.toString());
		}		
		return null;
	}

}
