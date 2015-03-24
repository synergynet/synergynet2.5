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
 * Main layer in producing multi-touch capable content for an application. This
 * avoids application developers having to deal with the intricacies of managing
 * multi-touch interactions for graphical compoments.
 */
public abstract class ContentSystem {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ContentSystem.class
			.getName());
	
	/** The sys. */
	private static Map<String, ContentSystem> sys = new HashMap<String, ContentSystem>();

	// TODO refactor to remove JME implementation
	/**
	 * Gets the content system for synergy net app.
	 *
	 * @param app
	 *            the app
	 * @return the content system for synergy net app
	 */
	public static ContentSystem getContentSystemForSynergyNetApp(
			SynergyNetApp app) {
		String name = app.getInfo().getApplicationName();
		if (sys.containsKey(name)) {
			return sys.get(name);
		}
		JMEContentSystem qc = new JMEContentSystem(app.getOrthoNode());
		log.info("JME ContentSystem is created.");
		sys.put(name, qc);
		return qc;
	}

	/** The content items. */
	protected Map<String, ContentItem> contentItems = new ConcurrentHashMap<String, ContentItem>();

	/** The implementation item factory. */
	protected IImplementationItemFactory implementationItemFactory;

	/** The updateable items. */
	protected List<Updateable> updateableItems = new ArrayList<Updateable>();

	/**
	 * Adds the content item.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public abstract void addContentItem(ContentItem contentItem);
	
	/**
	 * Adds the updateable listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addUpdateableListener(Updateable l) {
		this.updateableItems.add(l);
	}

	/**
	 * Creates the content item.
	 *
	 * @param contentItemType
	 *            the content item type
	 * @return the content item
	 */
	public abstract ContentItem createContentItem(
			Class<? extends ContentItem> contentItemType);
	
	/**
	 * Create a content item with a specific name. The content item will be a
	 * class that extends ContentItem. You may use the default set of
	 * ContentItem classes found in synergynet.contentsystem.items or use your
	 * own.
	 *
	 * @param contentItemType
	 *            the content item type
	 * @param itemName
	 *            the item name
	 * @return the content item
	 */
	public abstract ContentItem createContentItem(
			Class<? extends ContentItem> contentItemType, String itemName);
	
	/**
	 * Generate unique name.
	 *
	 * @return the string
	 */
	public String generateUniqueName() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Gets the all content items.
	 *
	 * @return the all content items
	 */
	public Map<String, ContentItem> getAllContentItems() {
		return contentItems;
	}

	/**
	 * Gets the content item.
	 *
	 * @param name
	 *            the name
	 * @return the content item
	 */
	public ContentItem getContentItem(String name) {
		return contentItems.get(name);
	}

	/**
	 * Gets the content items count.
	 *
	 * @return the content items count
	 */
	public int getContentItemsCount() {
		return contentItems.size();
	}

	/**
	 * Gets the implementation item factory.
	 *
	 * @return the implementation item factory
	 */
	public IImplementationItemFactory getImplementationItemFactory() {
		return implementationItemFactory;
	}

	/**
	 * Gets the screen height.
	 *
	 * @return the screen height
	 */
	public abstract int getScreenHeight();

	/**
	 * Gets the screen width.
	 *
	 * @return the screen width
	 */
	public abstract int getScreenWidth();
	
	/**
	 * Checks if is top level container.
	 *
	 * @param implementationObject
	 *            the implementation object
	 * @return true, if is top level container
	 */
	public abstract boolean isTopLevelContainer(Object implementationObject);
	
	/**
	 * Load content items.
	 *
	 * @param xmlFilePath
	 *            the xml file path
	 * @return the sets the
	 */
	public Set<ContentItem> loadContentItems(String xmlFilePath) {
		IContentLoader contentLoader = new XMLContentLoader();
		return contentLoader.loadContent(xmlFilePath, this);
	}

	/**
	 * Removes the all content items.
	 */
	public abstract void removeAllContentItems();

	/**
	 * Removes the content item.
	 *
	 * @param contentItem
	 *            the content item
	 */
	public abstract void removeContentItem(ContentItem contentItem);

	/**
	 * Removes the content item.
	 *
	 * @param contentItem
	 *            the content item
	 * @param releaseTextures
	 *            the release textures
	 */
	public abstract void removeContentItem(ContentItem contentItem,
			boolean releaseTextures);

	/**
	 * Removes the updateable listener.
	 *
	 * @param l
	 *            the l
	 */
	public void removeUpdateableListener(Updateable l) {
		this.updateableItems.remove(l);
	}
	
	/**
	 * Sets the item name.
	 *
	 * @param item
	 *            the item
	 * @param nameName
	 *            the name name
	 */
	public abstract void setItemName(ContentItem item, String nameName);

	/**
	 * Update.
	 *
	 * @param tpf
	 *            the tpf
	 */
	public abstract void update(float tpf);
}
