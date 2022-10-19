package org.be.airqualitymonitoring.error;

public class PollutantAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5890074844958904091L;
	
    public PollutantAlreadyExistsException() {
        super();
    }

    public PollutantAlreadyExistsException(String message) {
        super(message);
    }

    public PollutantAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PollutantAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected PollutantAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
