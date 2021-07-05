package com.vet24.dao;

import java.io.Serializable;
import java.util.List;

public interface ReadWriteDao<K extends Serializable, T> extends ReadOnlyDao<K, T> {

    void persist (T entity);

    void persistAll (List<T> entities);

    T update (T entity);

    List<T> updateAll (List<T> entities);

    void delete (T entity);

    void deleteAll (List<T> entities);
}
