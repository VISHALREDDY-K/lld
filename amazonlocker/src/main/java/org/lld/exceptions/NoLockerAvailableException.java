package org.lld.exceptions;

public class NoLockerAvailableException extends RuntimeException {
    public NoLockerAvailableException(String message) {
        super(message);
    }
}
