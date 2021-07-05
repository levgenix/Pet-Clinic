package com.vet24.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ReadWriteDaoImpl<K extends Serializable, T> extends ReadOnlyDaoImpl<K, T> {

    public void persist(T entity) {
        manager.persist(entity);
    }

    public void persistAll(List<T> entities) {
        int count = 0;
        List<T> temp = null;
        int tempIndex = 0;

        while (count < entities.size()) {
            if (temp == null) {
                manager.persist(entities.get(count));
            } else {
                manager.persist(temp.get(tempIndex));
                tempIndex++;
            }
            count++;
            if (count % 100 == 0) {
                temp = new ArrayList<>(entities.subList(count, entities.size()));
                tempIndex = 0;
                manager.flush();
                manager.clear();
            }
        }
    }

    public T update(T entity) {
        return manager.merge(entity);
    }

    public List<T> updateAll (List<T> entities) {
        return entities.stream().map(elem -> manager.merge(elem)).collect(Collectors.toList());
    }

    public void delete(T entity) {
        manager.remove(manager.contains(entity) ? entity : manager.merge(entity));
    }

    public void deleteAll(List<T> entities) {
        entities.forEach(this::delete);
    }
}
