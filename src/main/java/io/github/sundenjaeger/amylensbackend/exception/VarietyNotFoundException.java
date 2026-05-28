package io.github.sundenjaeger.amylensbackend.exception;

public class VarietyNotFoundException extends RuntimeException {
    public VarietyNotFoundException(String message) {
        super(message);
    }

    public VarietyNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
