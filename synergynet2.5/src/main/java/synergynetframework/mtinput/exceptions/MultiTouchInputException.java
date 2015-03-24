package synergynetframework.mtinput.exceptions;


/**
 * The Class MultiTouchInputException.
 */
public class MultiTouchInputException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1890884133795191123L;
	
	/** The causes. */
	private Throwable[] causes;

	/**
	 * Instantiates a new multi touch input exception.
	 *
	 * @param causes the causes
	 */
	public MultiTouchInputException(Throwable... causes) {
		this.setCause(causes);
	}

	/**
	 * Sets the cause.
	 *
	 * @param causes the new cause
	 */
	public void setCause(Throwable[] causes) {
		this.causes = causes;
	}

	/**
	 * Gets the causes.
	 *
	 * @return the causes
	 */
	public Throwable[] getCauses() {
		return causes;
	}
	
	/**
	 * Gets the root cause.
	 *
	 * @return the root cause
	 */
	public Throwable getRootCause() {
		if(causes != null && causes.length > 0) return causes[0];
		return null;
	}
}
