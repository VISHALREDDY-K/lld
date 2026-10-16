package com.lld.entities;

import java.time.LocalDateTime;

public class ActivityLog {
    private final String description;
    private final LocalDateTime time;

    public ActivityLog(String description, LocalDateTime time) {
        this.description = description;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
