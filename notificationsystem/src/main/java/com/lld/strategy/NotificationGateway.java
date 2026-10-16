package com.lld.strategy;

import com.lld.entities.Notification;
import com.lld.exceptions.DeliveryException;

public interface NotificationGateway {
    void send(Notification notification) throws DeliveryException;
}
