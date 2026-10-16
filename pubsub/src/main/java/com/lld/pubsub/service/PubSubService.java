package com.lld.pubsub.service;

import com.lld.pubsub.consumer.ISubscriber;
import com.lld.pubsub.handler.TopicSubscriberWorker;
import com.lld.pubsub.model.Message;
import com.lld.pubsub.model.Topic;
import com.lld.pubsub.model.TopicSubscriber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PubSubService {
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();
    // topicId -> (subscriberId -> TopicSubscriberWorker)
    private final Map<String, Map<String, TopicSubscriberWorker>> topicWorkers = new ConcurrentHashMap<>();

    public Topic createTopic(String topicName) {
        if (topicName == null || topicName.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be null or empty");
        }
        Topic topic = new Topic(topicName);
        topics.put(topic.getId(), topic);
        topicWorkers.put(topic.getId(), new ConcurrentHashMap<>());
        return topic;
    }

    public Topic getTopic(String topicId) {
        return topics.get(topicId);
    }

    public void subscribe(Topic topic, ISubscriber subscriber) {
        if (topic == null || subscriber == null) {
            throw new IllegalArgumentException("Topic and subscriber cannot be null");
        }

        TopicSubscriber topicSubscriber = new TopicSubscriber(subscriber);
        topic.addSubscriber(topicSubscriber);

        TopicSubscriberWorker worker = new TopicSubscriberWorker(topic, topicSubscriber);
        topicWorkers.computeIfAbsent(topic.getId(), k -> new ConcurrentHashMap<>())
                .put(subscriber.getId(), worker);

        // If there are already messages in the topic, wake up worker immediately to process from offset 0
        if (!topic.getMessages().isEmpty()) {
            worker.wakeUp();
        }
    }

    public void publish(Topic topic, Message message) {
        if (topic == null || message == null) {
            throw new IllegalArgumentException("Topic and message cannot be null");
        }

        topic.addMessage(message);

        Map<String, TopicSubscriberWorker> workers = topicWorkers.get(topic.getId());
        if (workers != null) {
            for (TopicSubscriberWorker worker : workers.values()) {
                worker.wakeUp();
            }
        }
    }

    public void resetOffset(Topic topic, ISubscriber subscriber, int newOffset) {
        if (topic == null || subscriber == null) {
            throw new IllegalArgumentException("Topic and subscriber cannot be null");
        }

        TopicSubscriber targetSubscriber = null;
        for (TopicSubscriber ts : topic.getSubscribers()) {
            if (ts.getSubscriber().getId().equals(subscriber.getId())) {
                targetSubscriber = ts;
                break;
            }
        }

        if (targetSubscriber == null) {
            throw new IllegalArgumentException("Subscriber " + subscriber.getId() + " is not subscribed to topic " + topic.getName());
        }

        if (newOffset < 0 || newOffset > topic.getMessages().size()) {
            throw new IllegalArgumentException("Invalid offset: " + newOffset + ". Topic message count: " + topic.getMessages().size());
        }

        targetSubscriber.setOffset(newOffset);
        System.out.printf("[System] Reset offset for Subscriber '%s' on Topic '%s' to %d%n",
                subscriber.getId(), topic.getName(), newOffset);

        Map<String, TopicSubscriberWorker> workers = topicWorkers.get(topic.getId());
        if (workers != null) {
            TopicSubscriberWorker worker = workers.get(subscriber.getId());
            if (worker != null) {
                worker.wakeUp();
            }
        }
    }

    public void shutdown() {
        for (Map<String, TopicSubscriberWorker> workers : topicWorkers.values()) {
            for (TopicSubscriberWorker worker : workers.values()) {
                worker.stop();
            }
        }
    }
}
