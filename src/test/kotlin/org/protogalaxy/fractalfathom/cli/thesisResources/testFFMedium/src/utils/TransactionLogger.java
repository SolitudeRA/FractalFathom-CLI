package org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.utils;

import org.protogalaxy.fractalfathom.*;
import org.protogalaxy.fractalfathom.cli.thesisResources.testFFMedium.src.model.Payment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@FractalFathomFeature(name = "TransactionLogger", type = FeatureType.FUNCTIONAL)
@FractalFathomMapping(type = MappingType.CONCEPT, toConcept = "utils")
public class TransactionLogger {
    private List<String> logs = new ArrayList<>();

    public void logTransaction(Payment payment) {
        logs.add("Transaction ID: " + payment.getTransactionId() +
                ", Amount: " + payment.getAmount() +
                ", Currency: " + payment.getCurrency() +
                ", Method: " + payment.getPaymentMethod());
    }

    public void saveLogsToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (String log : logs) {
                writer.write(log + "\n");
            }
            System.out.println("Transaction logs saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving logs to file: " + e.getMessage());
        }
    }
}