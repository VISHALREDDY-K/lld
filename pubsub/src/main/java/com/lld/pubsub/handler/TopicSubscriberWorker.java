package com.lld.pubsub.handler;

import com.lld.pubsub.model.Message;
import com.lld.pubsub.model.Topic;
import com.lld.pubsub.model.TopicSubscriber;

public class TopicSubscriberWorker implements Runnable {
    private final Topic topic;
    private final TopicSubscriber topicSubscriber;
    private final Thread thread;
    private volatile boolean running = true;

    public TopicSubscriberWorker(Topic topic, TopicSubscriber topicSubscriber) {
        this.topic = topic;
        this.topicSubscriber = topicSubscriber;
        this.thread = new Thread(this, "Worker-" + topic.getName() + "-" + topicSubscriber.getSubscriber().getId());
        this.thread.setDaemon(true);
        this.thread.start();
    }

    @Override
    public void run() {
        while (running) {
            Message messageToProcess = null;
            int curOffset = -1;

            synchronized (topicSubscriber) {
                while (running && topicSubscriber.getOffset().get() >= topic.getMessages().size()) {
                    try {
                        topicSubscriber.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                if (!running) {
                    return;
                }

                curOffset = topicSubscriber.getOffset().get();
                if (curOffset >= 0 && curOffset < topic.getMessages().size()) {
                    messageToProcess = topic.getMessages().get(curOffset);
                }
            }

            if (messageToProcess != null) {
                try {
                    topicSubscriber.getSubscriber().onMessage(messageToProcess);
                } catch (Exception ex) {
                    System.err.println("[" + Thread.currentThread().getName() + "] Error processing message: " + ex.getMessage());
                }

                // Advance offset safely if it wasn't modified or reset concurrently
                topicSubscriber.getOffset().compareAndSet(curOffset, curOffset + 1);
            }
        }
    }

    public void wakeUp() {
        synchronized (topicSubscriber) {
            topicSubscriber.notify();
        }
    }

    public void stop() {
        running = false;
        synchronized (topicSubscriber) {
            topicSubscriber.notify();
        }
        thread.interrupt();
    }
}
