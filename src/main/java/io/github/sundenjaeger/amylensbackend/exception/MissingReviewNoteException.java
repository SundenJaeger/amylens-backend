// SPDX-License-Identifier: AGPL-3.0-or-later

package io.github.sundenjaeger.amylensbackend.exception;

public class MissingReviewNoteException extends RuntimeException {
    public MissingReviewNoteException(String message) {
        super(message);
    }

    public MissingReviewNoteException(String message, Throwable ex) {
        super(message, ex);
    }
}
