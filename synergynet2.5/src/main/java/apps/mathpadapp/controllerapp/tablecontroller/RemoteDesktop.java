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

package apps.mathpadapp.controllerapp.tablecontroller;

import java.util.ArrayList;
import java.util.List;

import synergynetframework.appsystem.contentsystem.ContentSystem;
import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.Window;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mathpadapp.conceptmapping.GraphLink;
import apps.mathpadapp.conceptmapping.GraphNode;
import apps.mathpadapp.networkmanager.managers.ControllerManager;
import apps.mathpadapp.networkmanager.managers.NetworkedContentManager;
import apps.mathpadapp.networkmanager.managers.remotedesktopmanager.RemoteDesktopManager;
import apps.mathpadapp.networkmanager.managers.syncmanager.SyncRenderer;

import com.jme.system.DisplaySystem;

/**
 * The Class RemoteDesktop.
 */
public abstract class RemoteDesktop extends GraphNode {

	/** The desktop window. */
	protected Window desktopWindow;

	/** The network manager. */
	protected NetworkedContentManager networkManager;

	/** The online items. */
	protected List<ContentItem> onlineItems = new ArrayList<ContentItem>();

	/** The remote desktop manager. */
	protected RemoteDesktopManager remoteDesktopManager;

	/** The sync renderer. */
	protected SyncRenderer syncRenderer;

	/** The table id. */
	protected TableIdentity tableId;

	/**
	 * Instantiates a new remote desktop.
	 *
	 * @param contentSystem
	 *            the content system
	 * @param networkManager
	 *            the network manager
	 */
	public RemoteDesktop(final ContentSystem contentSystem,
			final NetworkedContentManager networkManager) {
		super(((ControllerManager) networkManager).getGraphManager(),
				(Window) contentSystem.createContentItem(Window.class));
		desktopWindow = (Window) this.getNodeItem();
		this.networkManager = networkManager;
		ControllerManager controllerManager = (ControllerManager) networkManager;
		this.createSyncRenderer();
		this.remoteDesktopManager = controllerManager.getRemoteDesktopManager();
		this.contentSystem = networkManager.getContentSystem();

		desktopWindow.setWidth((DisplaySystem.getDisplaySystem().getWidth()));
		desktopWindow.setHeight((DisplaySystem.getDisplaySystem().getHeight()));

		desktopWindow.setLocalLocation(DisplaySystem.getDisplaySystem()
				.getWidth() / 2,
				DisplaySystem.getDisplaySystem().getHeight() / 2);
		desktopWindow.setOrder(-1);

		desktopWindow.setLocalLocation(0, 0);
		this.addConceptMapListener(new ConceptMapListener() {
			@Override
			public void nodeConnected(GraphLink link) {
				// if(!(link.getTargetNode() instanceof ProjectorNode)){
				// RemoteDesktop.this.graphManager.detachGraphLink(link);
				// return;
				// }
				// link.setArrowMode(LineItem.ARROW_TO_TARGET);
			}
			
			@Override
			public void nodeDisconnected(GraphLink link) {
			}
		});
	}

	/**
	 * Adds the online item.
	 *
	 * @param item
	 *            the item
	 */
	public void addOnlineItem(ContentItem item) {
		this.getDesktopWindow().addSubItem(item);
		if (item instanceof OrthoContentItem) {
			((OrthoContentItem) item).setLocation(((OrthoContentItem) item)
					.getLocalLocation());
			((OrthoContentItem) item).setRotateTranslateScalable(false);
		}

	}
	
	/**
	 * Adds the online items.
	 *
	 * @param items
	 *            the items
	 */
	public void addOnlineItems(List<ContentItem> items) {
		for (ContentItem item : items) {
			this.getDesktopWindow().addSubItem(item);
			if (item instanceof OrthoContentItem) {
				((OrthoContentItem) item).setRotateTranslateScalable(false);
			}
		}
	}
	
	/**
	 * Creates the sync renderer.
	 */
	public abstract void createSyncRenderer();

	/**
	 * Gets the desktop window.
	 *
	 * @return the desktop window
	 */
	public Window getDesktopWindow() {
		return desktopWindow;
	}

	/**
	 * Gets the online items.
	 *
	 * @return the online items
	 */
	public List<ContentItem> getOnlineItems() {
		return onlineItems;
	}

	/**
	 * Gets the sync renderer.
	 *
	 * @return the sync renderer
	 */
	public SyncRenderer getSyncRenderer() {
		return syncRenderer;
	}

	/**
	 * Gets the table id.
	 *
	 * @return the table id
	 */
	public TableIdentity getTableId() {
		return tableId;
	}
	
	/**
	 * Removes the online item.
	 *
	 * @param item
	 *            the item
	 */
	public void removeOnlineItem(ContentItem item) {
		this.desktopWindow.removeSubItem(item);
		onlineItems.remove(item);
	}

	/**
	 * Removes the online items.
	 */
	public void removeOnlineItems() {
		for (ContentItem item : onlineItems) {
			this.desktopWindow.removeSubItem(item);
		}
		onlineItems.clear();
	}

	/**
	 * Sets the sync renderer.
	 *
	 * @param syncRenderer
	 *            the new sync renderer
	 */
	public void setSyncRenderer(SyncRenderer syncRenderer) {
		this.syncRenderer = syncRenderer;
	}
	
	/**
	 * Sets the table id.
	 *
	 * @param tableId
	 *            the new table id
	 */
	public void setTableId(TableIdentity tableId) {
		this.tableId = tableId;
	}

	/**
	 * Update node.
	 */
	public void updateNode() {
		this.updateConnectionPoints();
	}
}
