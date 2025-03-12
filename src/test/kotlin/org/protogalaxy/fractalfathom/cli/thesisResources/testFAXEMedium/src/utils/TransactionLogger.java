package org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.utils;

import org.protogalaxy.fractalfathom.cli.thesisResources.testFAXEMedium.src.model.Payment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//&begin [TransactionLogger]
public class TransactionLogger {
    private List<String> logs = new ArrayList<>();

    //&begin [logTransaction]
    public void logTransaction(Payment payment) {
        logs.add("Transaction ID: " + payment.getTransactionId() +
                ", Amount: " + payment.getAmount() +
                ", Currency: " + payment.getCurrency() +
                ", Method: " + payment.getPaymentMethod());
    }
    //&end [logTransaction]

    //&begin [saveLogsToFile]
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
    //&end [saveLogsToFile]
}
//&end [TransactionLogger]