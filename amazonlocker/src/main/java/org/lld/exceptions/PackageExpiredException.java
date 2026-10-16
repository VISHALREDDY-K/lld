package org.lld.exceptions;

public class PackageExpiredException extends RuntimeException {
    public PackageExpiredException(String message) {
        super(message);
    }
}