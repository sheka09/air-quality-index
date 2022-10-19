package org.be.airqualitymonitoring.error;

public class BreakpointAqiNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6044067635842293238L;

	public BreakpointAqiNotFoundException() {
		super();
	}

	public BreakpointAqiNotFoundException(String message) {
		super(message);
	}

	public BreakpointAqiNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BreakpointAqiNotFoundException(Throwable cause) {
		super(cause);
	}

	protected BreakpointAqiNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
