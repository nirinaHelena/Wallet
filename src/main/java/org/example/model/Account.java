package org.example.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    private int id;
    private String name;
    private String email;
    private Double balance;

    public Account(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Account(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
