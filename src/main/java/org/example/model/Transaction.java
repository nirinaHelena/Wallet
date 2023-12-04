package org.example.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class Transaction {
    private Account account;
    private Double amount;
    private Device device;
    private String label;
    private LocalDate date;
    private LocalDateTime hour;
    private String note;
    private PaiementMethod paiementMethod;
    private PaiementStatus paiementStatus;
    private String location;


    public Transaction(Account account, Double amount, Device device,
                       String label, String note,
                       PaiementMethod paiementMethod,
                       PaiementStatus paiementStatus, String location) {
        this.account = account;
        this.amount = amount;
        this.device = device;
        this.label = label;
        this.date = LocalDate.now();
        this.hour = LocalDateTime.now();
        this.note = note;
        this.paiementMethod = paiementMethod;
        this.paiementStatus = paiementStatus;
        this.location = location;
    }
}
