package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Currency {
    private UUID currencyId;
    private String currencyName;
    private String currencyCode;

    // constructor without id
    public Currency(String currencyName, String currencyCode) {
        this.currencyName = currencyName;
        this.currencyCode = currencyCode;
    }
}
