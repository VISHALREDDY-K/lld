package org.lld.strategy;

import org.lld.models.Package;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void sendNotification(Package pkg) {
        System.out.println("Notification sent for package: " + pkg.getId());
    }
}