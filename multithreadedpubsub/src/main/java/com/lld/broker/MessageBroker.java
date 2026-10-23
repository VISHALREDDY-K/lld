package com.lld.broker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.lld.model.Message;
import com.lld.model.Topic;
import com.lld.subscriber.AsyncSubscriber;
import com.lld.subscriber.Subscriber;

public class MessageBroker {
    private final ConcurrentHashMap<String, Topic> topics;

    public MessageBroker() {
        this.topics = new ConcurrentHashMap<>();
    }

    public Topic getOrCreateTopic(String topicName) {
        return topics.computeIfAbsent(topicName, Topic::new);
    }

    public void deleteTopic(String topicName) {
        Topic topic = topics.remove(topicName);
        if (topic != null) {
            topic.deactivate();
        }
    }

    public Subscriber subscribe(String topicName, String subscriberId, Consumer<Message> handler) {
        Topic topic = getOrCreateTopic(topicName);
        Subscriber subscriber = new AsyncSubscriber(subscriberId, handler);
        topic.subscribe(subscriber);
        return subscriber;
    }

    public void unsubscribe(String topicName, Subscriber subscriber) {
        Topic topic = topics.get(topicName);
        if (topic != null) {
            topic.unsubscribe(subscriber);
        }
        subscriber.shutdown();
    }

    public void publish(String topicName, Message message) {
        Topic topic = topics.get(topicName);
        if (topic == null) {
            throw new IllegalArgumentException("Topic not found: " + topicName);
        }
        topic.publish(message);
    }
}
