package com.lld;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lld.decorator.RetryableGatewayDecorator;
import com.lld.entities.Notification;
import com.lld.enums.NotificationType;
import com.lld.exceptions.DeliveryException;
import com.lld.strategy.NotificationGateway;
import com.lld.strategy.SmsNotificationGateway;

public class NotifiationService {
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final int DEFAULT_RETRY_DELAY_MILLIS = 1000;
    private final ExecutorService executorService;
    private final ConcurrentHashMap<NotificationType, NotificationGateway> gatewayMap = new ConcurrentHashMap<>();
    private static volatile NotifiationService instance;

    private NotifiationService(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public static NotifiationService getNotificationService(int poolSize) {
        if (instance == null) {
            synchronized (NotifiationService.class) {
                if (instance == null) {
                    instance = new NotifiationService(poolSize);
                }
            }
        }
        return instance;
    }

    private NotificationGateway buildGateway(NotificationType type) {
        switch (type) {
            case SMS:
                return new RetryableGatewayDecorator(new SmsNotificationGateway(), DEFAULT_MAX_RETRIES,
                        DEFAULT_RETRY_DELAY_MILLIS);
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }

    public void send(Notification notification) {
        executorService.submit(() -> {
            try {
                gatewayMap.computeIfAbsent(notification.getType(), type -> buildGateway(type)).send(notification);
            } catch (DeliveryException e) {
                System.out.println("Failed to send notification: " + e.getMessage());
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
