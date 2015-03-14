package apps.remotecontrol.networkmanager.managers.trackmanager;

import java.util.UUID;

import apps.remotecontrol.tableportal.TablePortal;

import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Disk;

import synergynetframework.appsystem.contentsystem.items.ContentItem;

public class TrackListener {
	private TablePortal portal;
	
	public TrackListener(TablePortal portal){
		this.portal = portal;
	}
	
	public void itemMoved(ContentItem item, int x, int y){
		Disk d = new Disk(UUID.randomUUID().toString(), 8, 8, 5f);
		d.setZOrder(999999);
		d.setSolidColor(ColorRGBA.red);
		d.setLocalTranslation(x, y, 0);
		portal.getDisplayPanel().getWindow().getNode().attachChild(d);
	}
}
