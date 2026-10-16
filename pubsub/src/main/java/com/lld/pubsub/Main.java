package com.lld.pubsub;

import com.lld.pubsub.consumer.ConsoleSubscriber;
import com.lld.pubsub.consumer.ISubscriber;
import com.lld.pubsub.model.Message;
import com.lld.pubsub.model.Topic;
import com.lld.pubsub.producer.Publisher;
import com.lld.pubsub.service.PubSubService;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("==================================================");
        System.out.println("   DISTRIBUTED IN-MEMORY PUB-SUB SYSTEM (LLD)    ");
        System.out.println("==================================================\n");

        PubSubService pubSubService = new PubSubService();

        // 1. Create Topics
        Topic orderTopic = pubSubService.createTopic("order-events");
        Topic paymentTopic = pubSubService.createTopic("payment-events");

        // 2. Create Subscribers (Demonstrating Fast vs Slow Consumer concurrency)
        ISubscriber fastOrderConsumer = new ConsoleSubscriber("FastOrderService", 0);
        ISubscriber slowAnalyticsConsumer = new ConsoleSubscriber("SlowAnalyticsService", 300);
        ISubscriber paymentAuditor = new ConsoleSubscriber("PaymentAuditor", 50);

        // 3. Subscribe consumers to topics
        pubSubService.subscribe(orderTopic, fastOrderConsumer);
        pubSubService.subscribe(orderTopic, slowAnalyticsConsumer);
        pubSubService.subscribe(paymentTopic, paymentAuditor);

        // 4. Create Publishers
        Publisher orderPublisher = new Publisher("OrderGateway", pubSubService);
        Publisher paymentPublisher = new Publisher("PaymentGateway", pubSubService);

        // 5. Publish initial messages
        System.out.println("\n--- Step 1: Publishing Initial Messages ---");
        orderPublisher.publish(orderTopic, new Message("Order #101: Placed"));
        orderPublisher.publish(orderTopic, new Message("Order #102: Placed"));
        orderPublisher.publish(orderTopic, new Message("Order #103: Placed"));

        paymentPublisher.publish(paymentTopic, new Message("Payment #501: Verified"));
        paymentPublisher.publish(paymentTopic, new Message("Payment #502: Verified"));

        // Allow subscribers to process
        Thread.sleep(1200);

        // 6. Demonstrate Replay / Reset Offset for a single subscriber
        System.out.println("\n--- Step 2: Resetting Offset for FastOrderService (Replay from 0) ---");
        pubSubService.resetOffset(orderTopic, fastOrderConsumer, 0);

        // Allow replay processing
        Thread.sleep(600);

        // 7. Publish more messages while system is active
        System.out.println("\n--- Step 3: Publishing New Messages ---");
        orderPublisher.publish(orderTopic, new Message("Order #104: Placed"));

        Thread.sleep(800);

        // 8. Graceful Shutdown
        System.out.println("\n--- Step 4: Graceful Shutdown ---");
        pubSubService.shutdown();
        System.out.println("PubSub service successfully shut down. All worker threads terminated.");
    }
}
