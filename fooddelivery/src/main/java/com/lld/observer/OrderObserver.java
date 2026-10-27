package com.lld.observer;

import com.lld.model.Order;

public interface OrderObserver {
    void onUpdate(Order order);
}
