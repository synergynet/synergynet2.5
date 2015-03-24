package apps.lightrays.raytracer;

/**
 * The listener interface for receiving rayTracer events. The class that is
 * interested in processing a rayTracer event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addRayTracerListener<code> method. When
 * the rayTracer event occurs, that object's appropriate
 * method is invoked.
 *
 * @see RayTracerEvent
 */
public interface RayTracerListener {

	/**
	 * Trace aborted.
	 */
	public void traceAborted();

	/**
	 * Traced line.
	 *
	 * @param line_completed
	 *            the line_completed
	 */
	public void tracedLine(int line_completed);

	/**
	 * Trace finished.
	 */
	public void traceFinished();

	/**
	 * Trace started.
	 */
	public void traceStarted();
}
