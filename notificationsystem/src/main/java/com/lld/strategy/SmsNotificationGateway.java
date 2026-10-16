package com.lld.strategy;

import com.lld.entities.Notification;
import com.lld.exceptions.DeliveryException;

public class SmsNotificationGateway implements NotificationGateway {

    @Override
    public void send(Notification notification) throws DeliveryException {
        String phone = notification.getRecipient().getPhoneNumber()
                .orElseThrow(() -> new DeliveryException("Phone number not found"));
        System.out.println("Sending SMS to: " + phone);
        System.out.println("Message: " + notification.getMessage());
    }

}
