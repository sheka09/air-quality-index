package org.be.airqualitymonitoring.error;

import java.lang.Exception;


public class DateNotValidException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3336055714320685403L;
	
	
	public DateNotValidException() {
		super();
	}

	public DateNotValidException(String message) {
		super(message);
	}

	public DateNotValidException(String message, Throwable cause) {
		super(message, cause);
	}

	public DateNotValidException(Throwable cause) {
		super(cause);
	}

	protected DateNotValidException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
