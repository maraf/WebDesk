/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.data.dao;

import com.neptuo.data.dao.AbstractDAO;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.QueryOrder;
import com.neptuo.os.core.data.model.AccessType;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author Mara
 */
public class AccessTypeDAO extends AbstractDAO<AccessType> {

    @Override
    public AccessType find(Long id) throws DataStorageException {
        AccessType at = getDs().find(AccessType.class, id);
        getDs().getCache(AccessTypeDAO.class).insert(at.getName(), at);
        return at;
    }

    public AccessType find(String name) throws DataStorageException {
        Query q = getDs()
                .createQuery(getDs().factory().select().from(AccessType.class, "t").where("name", QueryComparator.EQ).order("id", QueryOrder.ASC))
                .setParameter("name", name);
                
        return (AccessType) getDs().getCache(AccessTypeDAO.class).has(name, q);
    }

    public List<AccessType> findCategory(String category) throws DataStorageException {
        Query q = getDs()
                .createQuery(getDs().factory().select("t").from(AccessType.class, "t").where("t", "category", QueryComparator.EQ).order("t", "id", QueryOrder.ASC))
                .setParameter("category", category);
        return (List<AccessType>) q.getResultList();
    }

    @Deprecated
    @Override
    public AccessType save(AccessType model) throws DataStorageException {
        throw new UnsupportedOperationException("Not supported yet.");
//        getDs().persist(getDs().merge(model));
    }

    @Deprecated
    @Override
    public AccessType merge(AccessType model) throws DataStorageException {
        throw new UnsupportedOperationException("Not supported yet.");
//        return getDs().merge(model);
    }

    @Deprecated
    @Override
    public void delete(AccessType model) throws DataStorageException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
