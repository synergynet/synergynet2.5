package apps.mathpadapp.controllerapp.projectorcontroller;

import java.util.HashMap;

import synergynetframework.appsystem.services.net.localpresence.TableIdentity;
import apps.mathpadapp.mathtool.MathToolInitSettings;
import apps.mathpadapp.networkmanager.utils.UserIdentity;

/**
 * The listener interface for receiving dataSource events. The class that is
 * interested in processing a dataSource event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addDataSourceListener<code> method. When
 * the dataSource event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DataSourceEvent
 */
public interface DataSourceListener {

	/**
	 * Invoked when source data update occurs.
	 *
	 * @param tableId
	 *            the table id
	 * @param items
	 *            the items
	 */
	public void sourceDataUpdated(TableIdentity tableId,
			HashMap<UserIdentity, MathToolInitSettings> items);

	/**
	 * Sync data received.
	 *
	 * @param tableId
	 *            the table id
	 * @param syncData
	 *            the sync data
	 */
	public void syncDataReceived(TableIdentity tableId,
			HashMap<UserIdentity, HashMap<Short, Object>> syncData);

}
