package apps.basketapp.controller;

import java.util.HashMap;
import java.util.Map;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

/**
 * The Class SnapshotManager.
 */
public class SnapshotManager {

	/** The snap map. */
	Map<TableIdentity, SnapshotContainer> snapMap = new HashMap<TableIdentity, SnapshotContainer>();
}
