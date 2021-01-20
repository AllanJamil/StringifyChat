package se.nackademin.stringify.exception;

public class InvalidKeyException extends RuntimeException {


    public InvalidKeyException() {
        super("Invalid key: Incorrect argument cannot be handled");
    }
}
