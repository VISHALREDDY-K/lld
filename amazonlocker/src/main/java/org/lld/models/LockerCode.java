package org.lld.models;

public class LockerCode {
    private String code;
    private long expirationTime;

    public LockerCode(String code, long expirationTime) {
        this.code = code;
        this.expirationTime = expirationTime;
    }

    public String getCode() {
        return code;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }
}
