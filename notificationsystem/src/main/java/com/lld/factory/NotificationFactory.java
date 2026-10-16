package com.lld.factory;

import java.util.concurrent.ConcurrentHashMap;

import com.lld.enums.NotificationType;
import com.lld.strategy.NotificationGateway;
import com.lld.strategy.SmsNotificationGateway;

public class NotificationFactory {

    private static ConcurrentHashMap<NotificationType, NotificationGateway> notificationGatewayMap = new ConcurrentHashMap<>();

    public static NotificationGateway createGateway(NotificationType type) {
        return notificationGatewayMap.computeIfAbsent(type, t -> buildGateway(t));
    }

    private static NotificationGateway buildGateway(NotificationType type) {
        switch (type) {
            case SMS:
                return new SmsNotificationGateway();
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }

}
