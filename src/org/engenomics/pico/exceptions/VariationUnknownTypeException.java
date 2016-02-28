package org.engenomics.pico.exceptions;

public class VariationUnknownTypeException extends Exception {

    public VariationUnknownTypeException() {
    }

    public VariationUnknownTypeException(String message) {
        super(message);
    }

    public VariationUnknownTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public VariationUnknownTypeException(Throwable cause) {
        super(cause);
    }

    public VariationUnknownTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
