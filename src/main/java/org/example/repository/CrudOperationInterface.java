package org.example.repository;

import java.util.List;

public interface CrudOperationInterface<T> {
    List<T> findAll();
    List<T> saveAll(List<T> toSave);
    T save(T toSave);
}
