package org.be.airqualitymonitoring.error;

public class MeasurementOutOfRangeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6656413332619996748L;
	
	public MeasurementOutOfRangeException() {
		super();
	}

	public MeasurementOutOfRangeException(String message) {
		super(message);
	}

	public MeasurementOutOfRangeException(String message, Throwable cause) {
		super(message, cause);
	}

	public MeasurementOutOfRangeException(Throwable cause) {
		super(cause);
	}

	protected MeasurementOutOfRangeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
