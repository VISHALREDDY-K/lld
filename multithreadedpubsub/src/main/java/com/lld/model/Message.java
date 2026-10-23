package com.lld.model;

import java.util.UUID;
import com.lld.enums.MessagePriority;

public class Message {
    private final String id;
    private final Object content;
    private final MessagePriority priority;
    private final long timestamp;
    private final String topic;

    public Message(Object content, MessagePriority priority, long timestamp, String topic) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.priority = priority;
        this.timestamp = timestamp;
        this.topic = topic;
    }

    public String getId() { return id; }
    public Object getContent() { return content; }
    public MessagePriority getPriority() { return priority; }
    public long getTimestamp() { return timestamp; }
    public String getTopic() { return topic; }

    @Override
    public String toString() {
        return "Message [id=" + id + ", content=" + content + ", priority=" + priority
                + ", timestamp=" + timestamp + ", topic=" + topic + "]";
    }
}
