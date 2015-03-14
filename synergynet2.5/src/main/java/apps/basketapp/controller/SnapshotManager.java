package apps.basketapp.controller;

import java.util.HashMap;
import java.util.Map;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public class SnapshotManager {
	Map<TableIdentity, SnapshotContainer> snapMap = new HashMap<TableIdentity, SnapshotContainer>();
}
