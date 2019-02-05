/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data.dao;

import com.neptuo.data.DataStorageException;
import com.neptuo.data.model.AbstractEntity;

/**
 *
 * @author Mara
 */
public abstract class AbstractDAO<E extends AbstractEntity> extends BaseDAO {
    public abstract E find(Long id) throws DataStorageException;

    public abstract E save(E model) throws DataStorageException;

    public abstract E merge(E model) throws DataStorageException;

    public abstract void delete(E model) throws DataStorageException;
}
