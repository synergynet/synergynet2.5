package apps.mathpadapp.controllerapp.projectorcontroller;

import java.util.HashMap;

import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;

public interface DataSourceListener {
	
	public void sourceDataUpdated(TableIdentity tableId,	HashMap<UserIdentity, MathToolInitSettings> items);
	public void syncDataReceived(TableIdentity tableId, HashMap<UserIdentity, HashMap<Short, Object>> syncData);
	
}
