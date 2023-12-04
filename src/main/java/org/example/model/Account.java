package org.example.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Account {
    private Integer id;
    private String name;
    private String email;

    public Account(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
