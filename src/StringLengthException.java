class StringLengthException extends RuntimeException {
    public StringLengthException() {
        super();
    }

    public StringLengthException(String message) {
        super(message);
    }

    public StringLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    public StringLengthException(Throwable cause) {
        super(cause);
    }

    public StringLengthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
