package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.service;

import org.protogalaxy.fractalfathom.*;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.model.User;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.model.Payment;

@FractalFathomFeature(name = "NotificationHandling", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "notification")
public class NotificationService {
    public void sendPaymentSuccessNotification(User user, Payment payment) {
        System.out.println("Payment succeeded for: " + user.getName() + ", Amount: " + payment.getAmount());
    }

    public void sendPaymentFailureNotification(User user, Payment payment) {
        System.out.println("Payment failed for: " + user.getName() + ", Amount: " + payment.getAmount());
    }
}