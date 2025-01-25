package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.service;

import org.protogalaxy.fractalfathom.*;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.model.User;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.model.Payment;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.utils.Validator;

@FractalFathomFeature(name = "PaymentProcessing", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "payment")
public class PaymentService {

    public boolean processPayment(User user, Payment payment) {
        if (!Validator.validatePayment(payment)) return false;
        System.out.println("Processing payment for: " + user.getName() + ", Amount: " + payment.getAmount());
        return true;
    }
}