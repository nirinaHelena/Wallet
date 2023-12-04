package org.example.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Revenue extends Transaction{
    private Categories categories;
    private Account payer;

    //constructor without parameter date and hour
    public Revenue(Account account, Double amount, Device device,
                   String label, String note,
                   PaiementMethod paiementMethod, PaiementStatus paiementStatus,
                   String location, Categories categories, Account payer) {
        super(account, amount, device, label, note, paiementMethod, paiementStatus, location);
        this.categories = categories;
        this.payer = payer;
    }
}
