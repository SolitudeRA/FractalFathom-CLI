package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.service;

import org.protogalaxy.fractalfathom.*;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.User;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.Payment;

@FractalFathomFeature(name = "NotificationHandling", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "notification")
public class NotificationService {
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

    public void sendLocalizedNotification(String language, String message, User user) {
        if ("es".equalsIgnoreCase(language)) {
            System.out.println("Notificación para " + user.getName() + ": " + message);
        } else {
            System.out.println("Notification for " + user.getName() + ": " + message);
        }
    }
}