package com.lld.pubsub.consumer;

import com.lld.pubsub.model.Message;

public class ConsoleSubscriber implements ISubscriber {
    private final String id;
    private final long sleepMillis;

    public ConsoleSubscriber(String id) {
        this(id, 0);
    }

    public ConsoleSubscriber(String id, long sleepMillis) {
        this.id = id;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void onMessage(Message message) throws Exception {
        if (sleepMillis > 0) {
            Thread.sleep(sleepMillis);
        }
        System.out.printf("[%s] [Consumer: %s] Processed: %s (Payload: %s)%n",
                Thread.currentThread().getName(), id, message.getId(), message.getPayload());
    }
}
