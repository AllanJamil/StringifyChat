package se.nackademin.stringify.exception;

public class InvalidKeyException extends Exception {


    public InvalidKeyException() {
        super("Invalid key: Incorrect argument cannot be handled");
    }
}
