package se.nackademin.stringify.exception;

public class ConnectionLimitException extends RuntimeException {

    public ConnectionLimitException(String message) {
        super(message);
    }
}
