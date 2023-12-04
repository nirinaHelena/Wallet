package org.example.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Expenses extends Transaction  {
    private Categories categories;
    private Account beneficiary;

    //constructor without date and hour as parameter
    public Expenses(Account account, Double amount, Device device, String label, String note, PaiementMethod paiementMethod, PaiementStatus paiementStatus, String location, Categories categories, Account beneficiary) {
        super(account, amount, device, label, note, paiementMethod, paiementStatus, location);
        this.categories = categories;
        this.beneficiary = beneficiary;
    }
}
