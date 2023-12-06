package org.example.DAO;

import java.util.List;

public interface DAOInterface<T> {
    List<T> findAll();
    T save(T toSave);
    List<T> saveAll(List<T> toSave);
}
