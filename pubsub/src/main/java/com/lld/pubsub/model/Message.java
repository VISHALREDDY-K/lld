package com.lld.pubsub.model;

import java.time.Instant;
import java.util.UUID;

public class Message {
    private final String id;
    private final String payload;
    private final Instant createdAt;

    public Message(String payload) {
        this(UUID.randomUUID().toString(), payload, Instant.now());
    }

    public Message(String id, String payload, Instant createdAt) {
        this.id = id;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", payload='" + payload + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
