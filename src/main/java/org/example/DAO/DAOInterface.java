package org.example.DAO;

import org.example.model.Amount;

import java.util.List;
import java.util.UUID;

public interface DAOInterface<T> {
    List<T> findAll();
    T save(T toSave);

    List<T> saveAll(List<T> toSave);
}
