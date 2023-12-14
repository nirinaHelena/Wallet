package org.example.DAO;

import org.example.model.Amount;

import java.util.List;
import java.util.UUID;

public interface DAOInterface<T> {
    List<T> findAll();
    T save(T toSave);

    // save an account with only name currency and type
    // TODO: change currency to int when insert into database
    Amount save(Amount toSave, UUID accountId, int currencyId);

    List<T> saveAll(List<T> toSave);
}
