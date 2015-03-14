package synergynetframework.mtinput.exceptions;

public class MultiTouchInputException extends Exception {
	private static final long serialVersionUID = -1890884133795191123L;
	private Throwable[] causes;

	public MultiTouchInputException(Throwable... causes) {
		this.setCause(causes);
	}

	public void setCause(Throwable[] causes) {
		this.causes = causes;
	}

	public Throwable[] getCauses() {
		return causes;
	}
	
	public Throwable getRootCause() {
		if(causes != null && causes.length > 0) return causes[0];
		return null;
	}
}
