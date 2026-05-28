package io.github.sundenjaeger.amylensbackend.exception;

public class DeviceAlreadyExistException extends RuntimeException {
    public DeviceAlreadyExistException(String message) {
        super(message);
    }

    public DeviceAlreadyExistException(String message, Throwable ex) {
        super(message,ex);
    }
}
