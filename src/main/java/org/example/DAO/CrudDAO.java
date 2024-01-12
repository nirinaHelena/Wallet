package org.example.DAO;

import java.util.List;

public interface CrudDAO<T> {
    List<T> findAll();
    T save(T toSave);
}
