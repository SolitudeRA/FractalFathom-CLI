package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.service;

import org.protogalaxy.fractalfathom.*;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.User;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.Payment;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.utils.Validator;

@FractalFathomFeature(name = "PaymentProcessing", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "payment")
public class PaymentService {
    public boolean processPayment(User user, Payment payment) {
        if (!Validator.validate("ValidatePayment", payment)) {
            System.out.println("Validation failed for payment: " + payment.getAmount());
            return false;
        }
        if (!authorizePayment(user, payment)) {
            System.out.println("Authorization failed for payment: " + payment.getAmount());
            return false;
        }
        System.out.println("Payment processed successfully for: " + user.getName());
        return true;
    }

    private boolean authorizePayment(User user, Payment payment) {
        String paymentMethod = payment.getPaymentMethod();
        switch (paymentMethod.toLowerCase()) {
            case "credit card":
                System.out.println("Authorizing Credit Card...");
                return checkCreditLimit(user, payment);
            case "paypal":
                System.out.println("Authorizing PayPal...");
                return true;
            case "bitcoin":
                System.out.println("Authorizing Bitcoin...");
                return validateBitcoinPayment(payment);
            default:
                System.out.println("Unsupported payment method: " + paymentMethod);
                return false;
        }
    }

    private boolean checkCreditLimit(User user, Payment payment) {
        double limit = 1000.0;
        if (payment.getAmount() > limit) {
            System.out.println("Payment exceeds credit limit for: " + user.getName());
            return false;
        }
        return true;
    }

    private boolean validateBitcoinPayment(Payment payment) {
        return payment.getAmount() > 0.0001;
    }

    public void refundPayment(Payment payment) {
        System.out.println("Refunding payment: Transaction ID " + payment.getTransactionId());
    }
}