package org.example.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Account {
    private UUID accountId;
    private String accountName;
    private Amount amount;
    private List<Transaction> transactionList;
    private Currency currency;
    private String accountType;

    // amount value initial: 0.0
    Amount amountInitial= new Amount(0.0);
    // constructor without accountId and amount initial= 0.0

    public Account(String accountName, Currency currency, String accountType) {
        this.accountName = accountName;
        this.amount = amountInitial;
        this.currency = currency;
        this.accountType = accountType;
    }
}
