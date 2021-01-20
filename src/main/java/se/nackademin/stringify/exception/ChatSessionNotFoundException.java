package se.nackademin.stringify.exception;

public class ChatSessionNotFoundException extends RuntimeException {

    public ChatSessionNotFoundException(String message) {
        super(message);
    }
}
