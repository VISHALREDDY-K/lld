package com.lld.pubsub.model;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Topic {
    private final String id;
    private final String name;
    private final List<Message> messages;
    private final List<TopicSubscriber> subscribers;

    public Topic(String name) {
        this(UUID.randomUUID().toString(), name);
    }

    public Topic(String id, String name) {
        this.id = id;
        this.name = name;
        this.messages = new CopyOnWriteArrayList<>();
        this.subscribers = new CopyOnWriteArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<TopicSubscriber> getSubscribers() {
        return subscribers;
    }

    public synchronized void addMessage(Message message) {
        messages.add(message);
    }

    public synchronized void addSubscriber(TopicSubscriber subscriber) {
        subscribers.add(subscriber);
    }
}
