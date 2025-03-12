package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src;

import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.User;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.Payment;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.service.PaymentService;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.service.NotificationService;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.utils.TransactionLogger;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.utils.UserAuthentication;

public class Main {
    public static void main(String[] args) {
        UserAuthentication auth = new UserAuthentication();
        TransactionLogger transactionLogger = new TransactionLogger();

        User user = auth.login(1, "password123");
        if (user == null) {
            System.out.println("Authentication failed. Exiting program.");
            return;
        }

        Payment validPayment = new Payment(200.0, "USD", "Credit Card");
        validPayment.setTransactionId("TXN12345");

        PaymentService paymentService = new PaymentService();
        NotificationService notificationService = new NotificationService();

        if (paymentService.processPayment(user, validPayment)) {
            notificationService.sendNotification("PaymentSuccessNotification", user, validPayment);
            transactionLogger.logTransaction(validPayment);
        } else {
            notificationService.sendNotification("PaymentFailureNotification", user, validPayment);
            paymentService.refundPayment(validPayment);
        }

        transactionLogger.saveLogsToFile("transaction_logs.txt");
    }
}