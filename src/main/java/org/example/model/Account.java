package org.example.model;

import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Account {
    private int accountId;
    private String accountName;
    private Amount amount;
    private List<Transaction> transactionList;
    private int currency;
    private String accountType;

    // constructor without accountId and amount initial= 0.0

    public Account(String accountName, int currency, String accountType) {
        this.accountName = accountName;
        this.currency = currency;
        this.accountType = accountType;
    }

    // constructor without amount and transaction list
    public Account(int accountId, String accountName, int currency, String accountType) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.currency = currency;
        this.accountType = accountType;
    }
}
