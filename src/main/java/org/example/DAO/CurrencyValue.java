package org.example.DAO;

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
    private UUID currencyId;
    private UUID currencyIdSource;
    private UUID currencyIdDestination;
    private double Value;
    private LocalDateTime effectDate;
}
