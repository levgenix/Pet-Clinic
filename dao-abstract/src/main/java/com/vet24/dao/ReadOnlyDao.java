package com.vet24.dao;

import java.io.Serializable;
import java.util.List;

public interface ReadOnlyDao<K extends Serializable, T> {

    T getByKey (K key);

    boolean isExistByKey (K key);

    List<T> getAll ();
}
