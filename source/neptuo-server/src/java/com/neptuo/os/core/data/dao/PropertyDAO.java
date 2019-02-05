/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.data.dao;

import com.neptuo.os.core.data.model.Property;
import com.neptuo.os.core.data.model.User;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryBuilder;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.dao.AbstractDAO;
import java.util.List;

/**
 *
 * @author Mara
 */
public class PropertyDAO extends AbstractDAO<Property> {

    @Override
    public Property find(Long id) throws DataStorageException {
        return getDs().find(Property.class, id);
    }

    public Property find(String className, String key, User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Property.class, "p")
                .where("className", QueryComparator.EQ)
                .and()
                .where("key", QueryComparator.EQ)
                .and()
                .where("user", QueryComparator.EQ);

        return (Property) getDs().createQuery(qb)
                .setParameter("className", className)
                .setParameter("key", key)
                .setParameter("user", current)
                .getSingleResult();
    }

    public Property find(String className, List<String> keys, User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Property.class, "p")
                .where("className", QueryComparator.EQ)
                .and()
                .where("key", QueryComparator.IN)
                .and()
                .where("user", QueryComparator.EQ);

        return (Property) getDs().createQuery(qb)
                .setParameter("className", className)
                .setParameter("key", keys)
                .setParameter("user", current)
                .getResultList();
    }

    public List<Property> find(String className, User current) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Property.class, "p")
                .where("className", QueryComparator.EQ)
                .and()
                .where("user", QueryComparator.EQ);

        return (List<Property>) getDs().createQuery(qb)
                .setParameter("className", className)
                .setParameter("user", current)
                .getResultList();
    }

    public List<Property> findSystem() throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(Property.class, "p")
                .where("isSystemValue", QueryComparator.EQ, true);

        return (List<Property>) getDs().createQuery(qb).getResultList();
    }

    @Override
    public Property save(Property model) throws DataStorageException {
        getDs().persist(model);
        return model;
    }

    @Override
    public Property merge(Property model) throws DataStorageException {
        return getDs().merge(model);
    }

    @Override
    public void delete(Property model) throws DataStorageException {
        getDs().remove(model);
    }
}
