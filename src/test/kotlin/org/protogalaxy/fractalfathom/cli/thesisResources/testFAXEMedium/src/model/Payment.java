package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.model;

//&begin [Payment]
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
//&end [Payment]