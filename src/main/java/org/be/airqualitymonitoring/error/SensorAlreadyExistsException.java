package org.be.airqualitymonitoring.error;

public class SensorAlreadyExistsException extends Exception {

	 private static final long serialVersionUID = 1L;

	    public SensorAlreadyExistsException() {
	        super();
	    }

	    public SensorAlreadyExistsException(String message) {
	        super(message);
	    }

	    public SensorAlreadyExistsException(String message, Throwable cause) {
	        super(message, cause);
	    }

	    public SensorAlreadyExistsException(Throwable cause) {
	        super(cause);
	    }

	    protected SensorAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
	            boolean writableStackTrace) {
	        super(message, cause, enableSuppression, writableStackTrace);
	    }

}
