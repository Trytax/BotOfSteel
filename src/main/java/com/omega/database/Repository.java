package com.omega.database;

public interface Repository<T> {

    T findById(Object id);

    void save(T entity);

    void delete(T entity);

    Class<T> getType();
}
