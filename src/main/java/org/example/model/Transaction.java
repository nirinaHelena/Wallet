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
    private UUID account_id;
    private String transactionLabel;
    private double amount;
    private LocalDateTime dateTime;
    private String transactionType;
    // type: debit or credit

    // constructor without id
    public Transaction(UUID account_id, String transactionLabel, double amount,
                       LocalDateTime dateTime, String transactionType) {
        this.account_id= account_id;
        this.transactionLabel = transactionLabel;
        this.amount = amount;
        this.dateTime = dateTime;
        this.transactionType = transactionType;
    }

    // constructor whithout id, account_id, datetime
    public Transaction( String transactionLabel, double amount, String transactionType) {
        this.transactionLabel = transactionLabel;
        this.amount = amount;
        this.transactionType = transactionType;
    }
}
