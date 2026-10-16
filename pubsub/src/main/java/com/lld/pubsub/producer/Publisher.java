package com.lld.pubsub.producer;

import com.lld.pubsub.model.Message;
import com.lld.pubsub.model.Topic;
import com.lld.pubsub.service.PubSubService;

public class Publisher {
    private final String name;
    private final PubSubService pubSubService;

    public Publisher(String name, PubSubService pubSubService) {
        this.name = name;
        this.pubSubService = pubSubService;
    }

    public String getName() {
        return name;
    }

    public void publish(Topic topic, Message message) {
        System.out.printf("[Publisher: %s] Publishing to topic '%s': %s%n",
                name, topic.getName(), message.getPayload());
        pubSubService.publish(topic, message);
    }
}
