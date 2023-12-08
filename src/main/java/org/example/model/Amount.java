package org.example.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Amount {
    private double amount;
    private LocalDateTime dateTime;

    public Amount(double amount) {
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
    }
}
