package com.vet24.service;

import com.vet24.dao.ReadWriteDao;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class ReadWriteServiceImpl<K extends Serializable, T> extends ReadOnlyServiceImpl<K, T> {

    private final ReadWriteDao<K, T> readWriteDao;

    protected ReadWriteServiceImpl(ReadWriteDao<K, T> readWriteDao) {
        super(readWriteDao);
        this.readWriteDao = readWriteDao;
    }

    @Transactional
    public void persist(T entity) {
        readWriteDao.persist(entity);
    }

    @Transactional
    public void persistAll(List<T> entities) {
        readWriteDao.persistAll(entities);
    }

    @Transactional
    public T update(T entity) {
        return readWriteDao.update(entity);
    }

    @Transactional
    public List<T> updateAll(List<T> entities) {
        return readWriteDao.updateAll(entities);
    }

    @Transactional
    public void delete(T entity) {
        readWriteDao.delete(entity);
    }

    @Transactional
    public void deleteAll(List<T> entities) {
        readWriteDao.deleteAll(entities);
    }
}
