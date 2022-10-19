package org.be.airqualitymonitoring.error;

public class SensorNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6518561770787550143L;
	
	public SensorNotFoundException() {
		super();
	}

	public SensorNotFoundException(String message) {
		super(message);
	}

	public SensorNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SensorNotFoundException(Throwable cause) {
		super(cause);
	}

	protected SensorNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
