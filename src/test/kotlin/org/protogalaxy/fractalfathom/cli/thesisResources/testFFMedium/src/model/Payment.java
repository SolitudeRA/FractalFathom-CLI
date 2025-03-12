package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model;

import org.protogalaxy.fractalfathom.*;

@FractalFathomFeature(name = "PaymentData", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "payment")
public class Payment {
    private double amount;
    private String currency;
    private String paymentMethod;
    private String transactionId;

    public Payment(double amount, String currency, String paymentMethod) {
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}