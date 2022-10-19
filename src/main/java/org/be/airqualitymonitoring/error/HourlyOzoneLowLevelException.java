package org.be.airqualitymonitoring.error;

public class HourlyOzoneLowLevelException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6152165131185406203L;

	public HourlyOzoneLowLevelException() {
		super();
	}

	public HourlyOzoneLowLevelException(String message) {
		super(message);
	}

	public HourlyOzoneLowLevelException(String message, Throwable cause) {
		super(message, cause);
	}

	public HourlyOzoneLowLevelException(Throwable cause) {
		super(cause);
	}

	protected HourlyOzoneLowLevelException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
