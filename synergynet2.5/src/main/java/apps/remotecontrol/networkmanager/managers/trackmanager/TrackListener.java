package apps.remotecontrol.networkmanager.managers.trackmanager;

import java.util.UUID;

import synergynetframework.appsystem.contentsystem.items.ContentItem;
import apps.remotecontrol.tableportal.TablePortal;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Disk;

/**
 * The listener interface for receiving track events. The class that is
 * interested in processing a track event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addTrackListener<code> method. When
 * the track event occurs, that object's appropriate
 * method is invoked.
 *
 * @see TrackEvent
 */
public class TrackListener {

	/** The portal. */
	private TablePortal portal;

	/**
	 * Instantiates a new track listener.
	 *
	 * @param portal
	 *            the portal
	 */
	public TrackListener(TablePortal portal) {
		this.portal = portal;
	}

	/**
	 * Item moved.
	 *
	 * @param item
	 *            the item
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void itemMoved(ContentItem item, int x, int y) {
		Disk d = new Disk(UUID.randomUUID().toString(), 8, 8, 5f);
		d.setZOrder(999999);
		d.setSolidColor(ColorRGBA.red);
		d.setLocalTranslation(x, y, 0);
		portal.getDisplayPanel().getWindow().getNode().attachChild(d);
	}
}
