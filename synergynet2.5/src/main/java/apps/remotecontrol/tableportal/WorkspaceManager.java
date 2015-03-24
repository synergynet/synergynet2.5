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
package apps.remotecontrol.tableportal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import synergynetframework.appsystem.contentsystem.items.OrthoContentItem;
import synergynetframework.appsystem.contentsystem.items.listener.ScreenCursorListener;
import synergynetframework.appsystem.contentsystem.items.utils.Location;
import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import synergynetframework.jme.mtinputbridge.MultiTouchInputFilterManager;
import synergynetframework.jme.pickingsystem.IJMEMultiTouchPicker;
import synergynetframework.jme.pickingsystem.PickSystemException;
import synergynetframework.jme.pickingsystem.data.PickRequest;
import synergynetframework.jme.pickingsystem.data.PickResultData;
import apps.remotecontrol.networkmanager.managers.NetworkedContentManager;
import apps.remotecontrol.networkmanager.managers.TablePortalManager;

import com.jme.math.Vector2f;
import com.jme.scene.Spatial;

/**
 * The Class WorkspaceManager.
 */
public class WorkspaceManager {

	/** The instance. */
	private static WorkspaceManager instance = new WorkspaceManager();

	/**
	 * Gets the single instance of WorkspaceManager.
	 *
	 * @return single instance of WorkspaceManager
	 */
	public static WorkspaceManager getInstance() {
		synchronized (TablePortalManager.class) {
			if (instance == null) {
				instance = new WorkspaceManager();
			}
			return instance;
		}
	}

	/** The inspected portals. */
	public List<TablePortal> inspectedPortals = new ArrayList<TablePortal>();

	/** The item loc. */
	public HashMap<String, Location> itemLoc = new HashMap<String, Location>();
	
	/** The network manager. */
	private NetworkedContentManager networkManager;

	/** The pick system. */
	private IJMEMultiTouchPicker pickSystem;

	/**
	 * Instantiates a new workspace manager.
	 */
	private WorkspaceManager() {
		this.pickSystem = MultiTouchInputFilterManager.getInstance()
				.getPickingSystem();
	}

	/**
	 * Adds the workspace listener.
	 *
	 * @param item
	 *            the item
	 */
	public void addWorkspaceListener(ContentItem item) {
		((OrthoContentItem) item)
				.addScreenCursorListener(new ScreenCursorListener() {
					
					@Override
					public void screenCursorChanged(ContentItem item, long id,
							float x, float y, float pressure) {
					}
					
					@Override
					public void screenCursorClicked(ContentItem item, long id,
							float x, float y, float pressure) {
					}
					
					@Override
					public void screenCursorPressed(ContentItem item, long id,
							float x, float y, float pressure) {
					}
					
					@Override
					public void screenCursorReleased(ContentItem item, long id,
							float x, float y, float pressure) {
						TablePortal sourcePortal = getSourceTablePortal(item);
						TablePortal targetPortal = getTargetTablePortal(item,
								id, x, y);
						TableIdentity sourceTableId, targetTableId;
						
						if (sourcePortal != null) {
							sourceTableId = sourcePortal.getTableId();
						} else {
							sourceTableId = TableIdentity.getTableIdentity();
						}
						
						if (targetPortal != null) {
							targetTableId = targetPortal.getTableId();
						} else {
							targetTableId = TableIdentity.getTableIdentity();
						}
						if ((sourceTableId != null) && (targetTableId != null)) {
							networkManager.transferContentItem(item,
									sourceTableId, targetTableId, new Location(
											x, y, 0));
						}
					}
				});
	}

	/**
	 * Disable inspection for portal.
	 *
	 * @param tablePortal
	 *            the table portal
	 */
	public void disableInspectionForPortal(TablePortal tablePortal) {
		inspectedPortals.remove(tablePortal);
		System.out.println("inspection disabled");
	}

	/**
	 * Enable inspection for portal.
	 *
	 * @param tablePortal
	 *            the table portal
	 */
	public void enableInspectionForPortal(TablePortal tablePortal) {
		if (!inspectedPortals.contains(tablePortal)) {
			inspectedPortals.add(tablePortal);
		}
		System.out.println("inspection enabled");
	}

	/**
	 * Gets the picked spatials.
	 *
	 * @param id
	 *            the id
	 * @param position
	 *            the position
	 * @return the picked spatials
	 */
	private List<Spatial> getPickedSpatials(long id, Vector2f position) {
		PickRequest req = new PickRequest(id, position);
		List<PickResultData> pickResults;
		List<Spatial> pickedSpatials = new ArrayList<Spatial>();
		try {
			pickResults = pickSystem.doPick(req);
			for (PickResultData pr : pickResults) {
				pickedSpatials.add(pr.getPickedSpatial());
			}
		} catch (PickSystemException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return pickedSpatials;
	}

	/**
	 * Gets the source table portal.
	 *
	 * @param item
	 *            the item
	 * @return the source table portal
	 */
	public TablePortal getSourceTablePortal(ContentItem item) {
		if (!item.getName().contains("@")) {
			return null;
		}
		String[] strs = item.getName().split("@");
		if (strs.length < 3) {
			return null;
		}
		return TablePortalManager.getInstance().getTablePortal(strs[0]);
	}

	/**
	 * Gets the target table portal.
	 *
	 * @param item
	 *            the item
	 * @param id
	 *            the id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return the target table portal
	 */
	private TablePortal getTargetTablePortal(ContentItem item, long id,
			float x, float y) {
		List<Spatial> spatials = getPickedSpatials(id, new Vector2f(x, y));
		TablePortal sourcePortal = getSourceTablePortal(item);
		for (Spatial spatial : spatials) {
			if ((sourcePortal != null)
					&& sourcePortal.getWindow().getBackgroundFrame().getName()
							.equals(spatial.getName())) {
				return sourcePortal;
			}
			for (TablePortal portal : TablePortalManager.getInstance()
					.getTablePortals()) {
				if (portal.getWindow().getBackgroundFrame().getName()
						.equals(spatial.getName())) {
					return portal;
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks if is inspection enabled for portal.
	 *
	 * @param tablePortal
	 *            the table portal
	 * @return true, if is inspection enabled for portal
	 */
	public boolean isInspectionEnabledForPortal(TablePortal tablePortal) {
		return inspectedPortals.contains(tablePortal);
	}
	
	/**
	 * Sets the networked content manager.
	 *
	 * @param networkManager
	 *            the new networked content manager
	 */
	public void setNetworkedContentManager(
			NetworkedContentManager networkManager) {
		this.networkManager = networkManager;
	}

	/**
	 * Unregister content item.
	 *
	 * @param item
	 *            the item
	 */
	public void unregisterContentItem(ContentItem item) {
		((OrthoContentItem) item).removeScreenCursorListeners();
	}
	
}
