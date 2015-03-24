package apps.threedbuttonsexperiment.logger;

/**
 * The listener interface for receiving log events. The class that is interested
 * in processing a log event implements this interface, and the object created
 * with that class is registered with a component using the component's
 * <code>addLogListener<code> method. When
 * the log event occurs, that object's appropriate
 * method is invoked.
 *
 * @see LogEvent
 */
public interface LogListener {

	/**
	 * Delete key pressed.
	 */
	public void deleteKeyPressed();

	/**
	 * Key pressed.
	 *
	 * @param key
	 *            the key
	 */
	public void keyPressed(String key);

	/**
	 * Submit key pressed.
	 */
	public void submitKeyPressed();
}
