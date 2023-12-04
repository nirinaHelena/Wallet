package org.example.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transfer extends Transaction{
    private Account toAccount;
    private Account beneficiary;

    //constructor without parameter date and hour
    public Transfer(Account account, Double amount, Devise device,
                    String label, String note,
                    PaiementMethod paiementMethod,
                    PaiementStatus paiementStatus, String location,
                    Account toAccount, Account beneficiary) {
        super(account, amount, device, label, note, paiementMethod, paiementStatus, location);
        this.toAccount = toAccount;
        this.beneficiary = beneficiary;
    }
}
