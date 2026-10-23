package com.lld.model;

import java.util.concurrent.CopyOnWriteArrayList;

import com.lld.subscriber.Subscriber;

public class Topic {
    private final String name;
    private final CopyOnWriteArrayList<Subscriber> subscribers;
    private volatile boolean active;

    public Topic(String name) {
        this.name = name;
        this.subscribers = new CopyOnWriteArrayList<>();
        this.active = true;
    }

    public void subscribe(Subscriber subscriber) {
        if (subscriber == null) throw new IllegalArgumentException("Subscriber cannot be null");
        if (!active) throw new IllegalStateException("Topic is not active");
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        if (subscriber == null) throw new IllegalArgumentException("Subscriber cannot be null");
        subscribers.remove(subscriber);
    }

    public void publish(Message message) {
        if (message == null) throw new IllegalArgumentException("Message cannot be null");
        if (!active) throw new IllegalStateException("Topic is not active");
        for (Subscriber subscriber : subscribers) {
            subscriber.onMessage(message);
        }
    }

    public void deactivate() {
        this.active = false;
        for (Subscriber subscriber : subscribers) {
            subscriber.shutdown();
        }
        subscribers.clear();
    }

    public String getName() { return name; }
}
