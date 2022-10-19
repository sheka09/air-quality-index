package org.be.airqualitymonitoring.error;

public class MeasurementAlreadyExistsException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6527583009541514982L;
	
    public MeasurementAlreadyExistsException() {
        super();
    }

    public MeasurementAlreadyExistsException(String message) {
        super(message);
    }

    public MeasurementAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeasurementAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected MeasurementAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
