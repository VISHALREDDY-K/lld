package com.lld.pubsub.model;

import com.lld.pubsub.consumer.ISubscriber;
import java.util.concurrent.atomic.AtomicInteger;

public class TopicSubscriber {
    private final ISubscriber subscriber;
    private final AtomicInteger offset;

    public TopicSubscriber(ISubscriber subscriber) {
        this(subscriber, 0);
    }

    public TopicSubscriber(ISubscriber subscriber, int initialOffset) {
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(initialOffset);
    }

    public ISubscriber getSubscriber() {
        return subscriber;
    }

    public AtomicInteger getOffset() {
        return offset;
    }

    public void setOffset(int newOffset) {
        this.offset.set(newOffset);
    }
}
