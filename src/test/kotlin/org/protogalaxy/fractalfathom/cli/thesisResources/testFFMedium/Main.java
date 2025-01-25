package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium;

import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.model.User;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.model.Payment;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.service.PaymentService;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.service.NotificationService;

public class Main {
    public static void main(String[] args) {
        User user = new User(1, "Alice", "alice@example.com");
        Payment payment = new Payment(200.0, "USD", "Credit Card");
        PaymentService paymentService = new PaymentService();
        NotificationService notificationService = new NotificationService();

        if (paymentService.processPayment(user, payment)) {
            notificationService.sendPaymentSuccessNotification(user, payment);
        } else {
            notificationService.sendPaymentFailureNotification(user, payment);
        }
    }
}
