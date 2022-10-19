package org.be.airqualitymonitoring.error;

import java.lang.Exception;

public class PollutantNotFoundException extends Exception {


	private static final long serialVersionUID = 1058987879321912752L;
	
	public PollutantNotFoundException() {
		super();
	}

	public PollutantNotFoundException(String message) {
		super(message);
	}

	public PollutantNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PollutantNotFoundException(Throwable cause) {
		super(cause);
	}

	protected PollutantNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
