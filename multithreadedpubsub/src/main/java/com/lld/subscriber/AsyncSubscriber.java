package com.lld.subscriber;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.lld.enums.SubscriberStatus;
import com.lld.model.Message;

public class AsyncSubscriber implements Subscriber {
    private final String id;
    private final BlockingQueue<Message> messageQueue;
    private final Thread pollingThread;
    private volatile SubscriberStatus status;
    private final Consumer<Message> messageHandler;

    public AsyncSubscriber(String id, Consumer<Message> messageHandler) {
        this.id = id;
        this.messageQueue = new LinkedBlockingQueue<>();
        this.pollingThread = new Thread(this::pollLoop, "AsyncSubscriber-" + id);
        this.status = SubscriberStatus.ACTIVE;
        this.messageHandler = messageHandler;
        this.pollingThread.setDaemon(true);
        this.pollingThread.start();
    }

    @Override
    public String getId() { return id; }

    @Override
    public void onMessage(Message message) {
        if (status == SubscriberStatus.TERMINATED) {
            throw new IllegalStateException("Subscriber is terminated");
        }
        try {
            messageQueue.offer(message, 100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void shutdown() {
        status = SubscriberStatus.TERMINATED;
        pollingThread.interrupt();
    }

    private void pollLoop() {
        while (status != SubscriberStatus.TERMINATED) {
            try {
                Message message = messageQueue.poll(100, TimeUnit.MILLISECONDS);
                if (message != null) {
                    try {
                        messageHandler.accept(message);
                    } catch (Exception e) {
                        System.out.println("Error processing message for subscriber " + id + ": " + e.getMessage());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
