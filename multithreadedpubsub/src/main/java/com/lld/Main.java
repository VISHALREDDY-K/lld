package com.lld;

import com.lld.broker.MessageBroker;
import com.lld.enums.MessagePriority;
import com.lld.model.Message;
import com.lld.subscriber.Subscriber;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MessageBroker broker = new MessageBroker();

        System.out.println("=== Multithreaded Pub-Sub Demo ===\n");

        // Subscribe two subscribers to "news" topic
        Subscriber sub1 = broker.subscribe("news", "sub1",
                msg -> System.out.println("[sub1] Received: " + msg.getContent() + " | Priority: " + msg.getPriority()));

        Subscriber sub2 = broker.subscribe("news", "sub2",
                msg -> System.out.println("[sub2] Received: " + msg.getContent() + " | Priority: " + msg.getPriority()));

        // Subscribe one subscriber to "sports" topic
        Subscriber sub3 = broker.subscribe("sports", "sub3",
                msg -> System.out.println("[sub3-sports] Received: " + msg.getContent()));

        // Publish messages to "news"
        broker.publish("news", new Message("Breaking: Java 21 released!", MessagePriority.HIGH, System.currentTimeMillis(), "news"));
        broker.publish("news", new Message("Market update: Stocks up", MessagePriority.MEDIUM, System.currentTimeMillis(), "news"));

        // Publish to "sports"
        broker.publish("sports", new Message("Cricket: India wins!", MessagePriority.LOW, System.currentTimeMillis(), "sports"));

        // Let async subscribers process messages
        Thread.sleep(500);

        // Unsubscribe sub1 from news
        System.out.println("\n[sub1 unsubscribed from news]");
        broker.unsubscribe("news", sub1);

        // Only sub2 should receive this
        broker.publish("news", new Message("Tech: AI advances continue", MessagePriority.HIGH, System.currentTimeMillis(), "news"));
        Thread.sleep(500);

        // Delete the sports topic — shuts down sub3
        System.out.println("\n[sports topic deleted]");
        broker.deleteTopic("sports");

        System.out.println("\n=== Demo Complete ===");
    }
}