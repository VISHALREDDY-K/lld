package com.lld.decorator;

import com.lld.entities.Notification;
import com.lld.exceptions.DeliveryException;
import com.lld.strategy.NotificationGateway;

public class RetryableGatewayDecorator implements NotificationGateway {

    private final NotificationGateway notificationGateway;
    private final int maxRetries;
    private final long retryDelayMillis;

    public RetryableGatewayDecorator(NotificationGateway notificationGateway, int maxRetries,
            long retryDelayMillis) {
        this.notificationGateway = notificationGateway;
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public void send(Notification notification) throws DeliveryException {

        int attempts = 0;
        while (attempts < maxRetries) {
            try {
                notificationGateway.send(notification);
                return;
            } catch (DeliveryException e) {
                attempts++;
                if (attempts >= maxRetries) {
                    throw e;
                }
                try {
                    Thread.sleep(retryDelayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new DeliveryException("Retry interrupted", ie);
                }
            }
        }
    }

}
