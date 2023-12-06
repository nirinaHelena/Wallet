package org.example.model;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Device implements Serializable {
    private int id;
    private String device;
    private String country;

    public Device(String device, String country) {
        this.device = device;
        this.country = country;
    }
}