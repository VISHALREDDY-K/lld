package com.lld;

import com.lld.entities.Notification;
import com.lld.entities.Recepient;
import com.lld.enums.NotificationType;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Starting Notification System Test ===");

        NotifiationService notificationService = NotifiationService.getNotificationService(2);

        // Case 1: Successful SMS delivery
        System.out.println("\n--- Test 1: Successful SMS Delivery ---");
        Recepient validRecipient = new Recepient("user_101", "user101@example.com", "+1-555-0199");
        Notification successNotification = new Notification.Builder(validRecipient, NotificationType.SMS)
                .message("Your verification code is: 482910")
                .subject("Verification Code")
                .build();

        notificationService.send(successNotification);

        // Case 2: Delivery failure with retries (recipient missing phone number)
        System.out.println("\n--- Test 2: Failure with Retries (Missing Phone Number) ---");
        Recepient invalidRecipient = new Recepient("user_102", "user102@example.com", null);
        Notification failureNotification = new Notification.Builder(invalidRecipient, NotificationType.SMS)
                .message("This message should fail and trigger retries.")
                .subject("Alert")
                .build();

        notificationService.send(failureNotification);

        // Gracefully shutdown the executor and wait for tasks to finish
        System.out.println("\nWaiting for all asynchronous tasks to complete...");
        notificationService.shutdown();
        System.out.println("=== Notification System Test Completed ===");
    }
}