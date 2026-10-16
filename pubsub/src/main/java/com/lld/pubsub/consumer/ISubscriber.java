package com.lld.pubsub.consumer;

import com.lld.pubsub.model.Message;

public interface ISubscriber {
    String getId();

    void onMessage(Message message) throws Exception;
}
