package org.be.airqualitymonitoring.error;

public class InsufficientDataForAqiCalculation extends Exception {

	private static final long serialVersionUID = 1L;

    public InsufficientDataForAqiCalculation() {
        super();
    }

    public InsufficientDataForAqiCalculation(String message) {
        super(message);
    }

    public InsufficientDataForAqiCalculation(String message, Throwable cause) {
        super(message, cause);
    }

    public InsufficientDataForAqiCalculation(Throwable cause) {
        super(cause);
    }

    protected InsufficientDataForAqiCalculation(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
