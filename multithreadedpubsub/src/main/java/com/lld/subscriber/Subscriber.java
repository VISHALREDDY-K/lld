package com.lld.subscriber;

import com.lld.model.Message;

public interface Subscriber {
    String getId();
    void onMessage(Message message);
    void shutdown();
}
