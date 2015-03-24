package apps.threedmanipulationexperiment.listener;


/**
 * The listener interface for receiving tool events.
 * The class that is interested in processing a tool
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addToolListener<code> method. When
 * the tool event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ToolEvent
 */
public interface ToolListener {

	/**
	 * Dispose tool.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void disposeTool(float x, float y);

}
