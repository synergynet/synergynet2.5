package apps.threedbuttonsexperiment.calculator.component;


/**
 * The listener interface for receiving task events.
 * The class that is interested in processing a task
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addTaskListener<code> method. When
 * the task event occurs, that object's appropriate
 * method is invoked.
 *
 * @see TaskEvent
 */
public interface TaskListener {
	
	/**
	 * Task completed.
	 */
	public void taskCompleted();
}
