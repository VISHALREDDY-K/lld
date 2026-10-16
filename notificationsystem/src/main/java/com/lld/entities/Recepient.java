package com.lld.entities;

import java.util.Optional;

public class Recepient {
    private final String userId;
    private final Optional<String> email;
    private final Optional<String> phoneNumber;

    public Recepient(String userId, String email, String phoneNumber) {
        this.userId = userId;
        this.email = Optional.ofNullable(email);
        this.phoneNumber = Optional.ofNullable(phoneNumber);
    }

    public String getUserId() {
        return userId;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }
}
