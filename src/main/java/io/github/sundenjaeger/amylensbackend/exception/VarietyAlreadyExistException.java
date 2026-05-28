package io.github.sundenjaeger.amylensbackend.exception;

public class VarietyAlreadyExistException extends RuntimeException {
    public VarietyAlreadyExistException(String message) {
        super(message);
    }

    public VarietyAlreadyExistException(String message, Throwable ex) {
        super(message, ex);
    }
}
