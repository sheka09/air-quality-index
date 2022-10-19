package org.be.airqualitymonitoring.error;

public class MeasurementNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7361103710946113451L;
	
	public MeasurementNotFoundException() {
		super();
	}

	public MeasurementNotFoundException(String message) {
		super(message);
	}

	public MeasurementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public MeasurementNotFoundException(Throwable cause) {
		super(cause);
	}

	protected MeasurementNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
