package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.utils;

import org.protogalaxy.fractalfathom.*;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.Payment;

@FractalFathomFeature(name = "Validation", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "utils")
public class Validator {
    public static boolean validate(String validationType, Payment payment) {
        switch (validationType) {
            case "ValidatePayment":
                if (payment.getAmount() <= 0) {
                    System.out.println("Invalid payment amount.");
                    return false;
                }
                if (payment.getCurrency() == null || payment.getCurrency().isEmpty()) {
                    System.out.println("Invalid currency.");
                    return false;
                }
                if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
                    System.out.println("Invalid payment method.");
                    return false;
                }
                return true;
            default:
                System.out.println("Unknown validation type: " + validationType);
                return false;
        }
    }

    public static boolean validateEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}