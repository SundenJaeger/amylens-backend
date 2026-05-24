// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.exception;

public class UnauthorizedDeviceException extends RuntimeException {
    public UnauthorizedDeviceException(String message) {
        super(message);
    }

    public UnauthorizedDeviceException(String message, Throwable ex) {
        super(message, ex);
    }
}
