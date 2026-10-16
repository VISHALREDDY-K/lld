package com.lld.entities;

import java.util.UUID;

import com.lld.enums.NotificationType;

public class Notification {
    private final String id;
    private final NotificationType type;
    private final Recepient recipient;
    private final String message;
    private final String subject;

    public Notification(NotificationType type, Recepient recipient, String message, String subject) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.recipient = recipient;
        this.message = message;
        this.subject = subject;
    }

    public String getId() {
        return id;
    }
    public NotificationType getType() {
        return type;
    }
    public Recepient getRecipient() {
        return recipient;
    }
    public String getMessage() {
        return message;
    }
    public String getSubject() {
        return subject;
    }

    public static class Builder {
        private NotificationType type;
        private Recepient recipient;
        private String message;
        private String subject;       
        
        public Builder(Recepient recipient, NotificationType type) {
            this.recipient = recipient;
            this.type = type;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Notification build() {
            return new Notification(type, recipient, message, subject);
        }
    }
    
}