/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.data.jpa;

import com.neptuo.data.DataStorage;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryBuilder;
import com.neptuo.data.SingleCache;
import com.neptuo.data.SystemValueException;
import com.neptuo.data.model.AbstractEntity;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

/**
 *
 * @author Mara
 */
public class JpaDataStorage implements DataStorage {

    private EntityManager em;

    private static Map<Class, SingleCache> cache = new HashMap<Class, SingleCache>();

    public EntityManager getEm() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void persist(Object o) throws DataStorageException {
        em.persist(o);
    }

    @Override
    public <T> T merge(T t) throws DataStorageException {
        return em.merge(t);
    }

    @Override
    public void remove(Object o) throws DataStorageException {
        AbstractEntity ae = (AbstractEntity) o;
        if (ae.getIsSystemValue()) {
            throw new SystemValueException();
        }
        em.remove(o);
    }

    @Override
    public <T> T find(Class<T> type, Object o) throws DataStorageException {
        return em.find(type, o);
    }

    @Override
    public void flush() throws DataStorageException {
        em.flush();
    }

    @Override
    public void setFlushMode(FlushModeType fmt) throws DataStorageException {
        em.setFlushMode(fmt);
    }

    @Override
    public FlushModeType getFlushMode() throws DataStorageException {
        return em.getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lmt) throws DataStorageException {
        em.lock(o, lmt);
    }

    @Override
    public void refresh(Object o) throws DataStorageException {
        em.refresh(o);
    }

    @Override
    public void clear() throws DataStorageException {
        em.clear();
    }

    @Override
    public boolean contains(Object o) throws DataStorageException {
        return em.contains(o);
    }

    @Override
    public Query createQuery(QueryBuilder builder) throws DataStorageException {
        return em.createQuery(builder.getResult());
    }

    @Override
    public void joinTransaction() throws DataStorageException {
        em.joinTransaction();
    }

    @Override
    public Object getDelegate() throws DataStorageException {
        return em.getDelegate();
    }

    @Override
    public void close() throws DataStorageException {
        em.close();
    }

    @Override
    public boolean isOpen() throws DataStorageException {
        return em.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() throws DataStorageException {
        return em.getTransaction();
    }

    @Override
    public QueryBuilder factory() {
        return new JpaQueryBuilder();
    }

    @Override
    public SingleCache getCache(Class clazz) {
        if(cache.containsKey(clazz)) {
            return cache.get(clazz);
        } else {
            SingleCache sc = new JpaSingleCache();
            cache.put(clazz, sc);
            return sc;
        }
    }
}
