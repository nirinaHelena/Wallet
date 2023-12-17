package org.example.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Transaction {
    private int transactionId;
    private int accountId;
    private String transactionLabel;
    private double amount;
    private LocalDateTime dateTime;
    private String transactionType;
    // type: debit or credit
    private Category category;


    public Transaction( int accountId, String transactionLabel, double amount,
                        LocalDateTime dateTime, String transactionType) {
        this.accountId = accountId;
        this.transactionLabel = transactionLabel;
        this.amount = amount;
        this.dateTime = dateTime;
        this.transactionType = transactionType;
    }
}
