/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

/**
 *
 * @author Mara
 */
public interface DataStorage {

    public void persist(Object o) throws DataStorageException;

    public <T extends Object> T merge(T t) throws DataStorageException;

    public void remove(Object o) throws DataStorageException;

    public <T extends Object> T find(Class<T> type, Object o) throws DataStorageException;

    public void flush() throws DataStorageException;

    public void setFlushMode(FlushModeType fmt) throws DataStorageException;

    public FlushModeType getFlushMode() throws DataStorageException;

    public void lock(Object o, LockModeType lmt) throws DataStorageException;

    public void refresh(Object o) throws DataStorageException;

    public void clear() throws DataStorageException;

    public boolean contains(Object o) throws DataStorageException;

    public Query createQuery(QueryBuilder builder) throws DataStorageException;

    public void joinTransaction() throws DataStorageException;

    public Object getDelegate() throws DataStorageException;

    public void close() throws DataStorageException;

    public boolean isOpen() throws DataStorageException;

    public EntityTransaction getTransaction() throws DataStorageException;

    public QueryBuilder factory();

    public SingleCache getCache(Class clazz);
}
