package org.example.ExplicationMahefa;

import org.example.ExplicationMahefa.crudOP;
import org.example.model.Account;

import java.util.List;

public class AccountCrudOP implements crudOP<Account> {
    public List<Account> findAll(){
        return Account.class.getField(("id"));
    }
}
