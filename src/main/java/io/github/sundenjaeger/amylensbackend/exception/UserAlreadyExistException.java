package io.github.sundenjaeger.amylensbackend.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }

    public UserAlreadyExistException(String message, Throwable ex) {
        super(message, ex);
    }
}
