/*
 * Copyright (c) 2009 University of Durham, England
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

package synergynetframework.appsystem.contentsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


import synergynetframework.appsystem.contentsystem.contentloader.IContentLoader;
import synergynetframework.appsystem.contentsystem.contentloader.XMLContentLoader;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.implementation.IImplementationItemFactory;
import synergynetframework.appsystem.contentsystem.jme.JMEContentSystem;
import synergynetframework.appsystem.table.appdefinitions.SynergyNetApp;

/**
 * Main layer in producing multi-touch capable content for
 * an application. This avoids application developers having to
 * deal with the intricacies of managing multi-touch interactions
 * for graphical compoments.
 *   
 */
public abstract class ContentSystem {
	
	private static final Logger log = Logger.getLogger(ContentSystem.class.getName());	
	
	protected Map<String, ContentItem> contentItems = new ConcurrentHashMap<String, ContentItem>();
	protected IImplementationItemFactory implementationItemFactory;
	protected List<Updateable> updateableItems = new ArrayList<Updateable>();
	
	private static Map<String,ContentSystem> sys = new HashMap<String,ContentSystem>();
	
	//TODO refactor to remove JME implementation
	public static ContentSystem getContentSystemForSynergyNetApp(SynergyNetApp app) {
		String name = app.getInfo().getApplicationName();
		if(sys.containsKey(name)) return sys.get(name);
		JMEContentSystem qc = new JMEContentSystem(app.getOrthoNode());
		log.info("JME ContentSystem is created.");
		sys.put(name, qc);
		return qc;
	}
	
	public String generateUniqueName() {
		return UUID.randomUUID().toString();
	}
	

	/**
	 * Create a content item with a specific name. The content item will be a class that
	 * extends ContentItem. You may use the default set of ContentItem classes
	 * found in synergynet.contentsystem.items or use your own.
	 * 
	 * @param contentItemType
	 * @param itemName
	 * @return
	 */
	public abstract ContentItem createContentItem(Class <? extends ContentItem> contentItemType, String itemName);
	public abstract ContentItem createContentItem(Class <? extends ContentItem> contentItemType);

	public Set<ContentItem> loadContentItems(String xmlFilePath){
		IContentLoader contentLoader = new XMLContentLoader();
		return contentLoader.loadContent(xmlFilePath, this);
	}
	
	
	public abstract void removeContentItem(ContentItem contentItem);
	public abstract void removeContentItem(ContentItem contentItem, boolean releaseTextures);
	public abstract void removeAllContentItems();
	public abstract void update(float tpf);
	
	public Map<String, ContentItem> getAllContentItems(){
		return contentItems;
	}
	
	public ContentItem getContentItem(String name){
		return contentItems.get(name);
	}
	
	public int getContentItemsCount(){
		return contentItems.size();
	}

	public IImplementationItemFactory getImplementationItemFactory() {
		return implementationItemFactory;
	}

	public abstract void addContentItem(ContentItem contentItem);
	public abstract void setItemName(ContentItem item, String nameName);
	public abstract boolean isTopLevelContainer(Object implementationObject);
	public abstract int getScreenWidth();
	public abstract int getScreenHeight();

	public void addUpdateableListener(Updateable l){
		this.updateableItems.add(l);
	}
	public void removeUpdateableListener(Updateable l){
		this.updateableItems.remove(l);
	}
}
