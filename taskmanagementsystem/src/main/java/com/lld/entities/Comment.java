package com.lld.entities;

import java.util.UUID;

public class Comment {
    private final String id;
    private final String text;
    private final User user;
    private final long createdAt;

    private Comment(String text, User user, long createdAt) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.user = user;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
