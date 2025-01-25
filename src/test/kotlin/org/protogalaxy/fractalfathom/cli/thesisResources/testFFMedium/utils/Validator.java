package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.utils;

import org.protogalaxy.fractalfathom.*;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.model.Payment;

@FractalFathomFeature(name = "Validation", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "utils")
public class Validator {
    public static boolean validatePayment(Payment payment) {
        if (payment.getAmount() <= 0) {
            System.out.println("Invalid payment amount: " + payment.getAmount());
            return false;
        }
        if (payment.getCurrency() == null || payment.getCurrency().isEmpty()) {
            System.out.println("Invalid currency: " + payment.getCurrency());
            return false;
        }
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            System.out.println("Invalid payment method: " + payment.getPaymentMethod());
            return false;
        }
        return true;
    }
}