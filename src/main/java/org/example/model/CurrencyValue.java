package org.example.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CurrencyValue {
    private int currencyId;
    private int currencyIdSource;
    private int currencyIdDestination;
    private double Value;
    private LocalDateTime effectDate;
}
