package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.service;

import org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.model.Payment;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.model.User;

//&begin [NotificationService]
public class NotificationService {
    //&begin [sendNotification]
    public void sendNotification(String notificationType, User user, Payment payment) {
        switch (notificationType) {
            case "PaymentSuccessNotification":
                System.out.println("Payment succeeded for: " + user.getName() +
                        ", Amount: " + (payment != null ? payment.getAmount() : "N/A"));
                break;
            case "PaymentFailureNotification":
                System.out.println("Payment failed for: " + user.getName() +
                        ", Amount: " + (payment != null ? payment.getAmount() : "N/A"));
                break;
            case "GeneralNotification":
                System.out.println("General notification sent to: " + user.getName());
                break;
            default:
                System.out.println("Unknown notification type: " + notificationType);
        }
    }
    //&end [sendNotification]

    //&begin [sendLocalizedNotification]
    public void sendLocalizedNotification(String language, String message, User user) {
        if ("es".equalsIgnoreCase(language)) {
            System.out.println("Notificación para " + user.getName() + ": " + message);
        } else {
            System.out.println("Notification for " + user.getName() + ": " + message);
        }
    }
    //&end [sendLocalizedNotification]
}
//&end [NotificationService]