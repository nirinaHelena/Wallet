package org.example.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Transaction {
    private UUID transactionId;
    private String transactionLabel;
    private double amount;
    private LocalDateTime dateTime;
    private String transactionType;
    // type: debit or credit

    // constructor without id

    public Transaction(String transactionLabel, double amount,
                       LocalDateTime dateTime, String transactionType) {
        this.transactionLabel = transactionLabel;
        this.amount = amount;
        this.dateTime = dateTime;
        this.transactionType = transactionType;
    }
}
