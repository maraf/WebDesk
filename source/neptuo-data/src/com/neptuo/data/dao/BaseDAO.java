/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data.dao;

import com.neptuo.data.DataStorage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;

/**
 *
 * @author Mara
 */
public class BaseDAO {
    private Map<Class, BaseDAO> daos = new HashMap<Class, BaseDAO>();

    protected DataStorage dataStorage;

    protected DataStorage getDs() {
        return dataStorage;
    }

    public void setDataStorage(DataStorage ds) {
        dataStorage = ds;
    }

    public <T extends BaseDAO> T createDAO(Class<T> daoClass) {
        try {
            T dao = null;
            if(daos.containsKey(daoClass)) {
                dao = (T) daos.get(daoClass);
            } else {
                dao = daoClass.newInstance();
                daos.put(daoClass, dao);
            }
            dao.setDataStorage(dataStorage);
            return dao;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected Object getResultOrNull(Query q) {
        List result = q.getResultList();
        if(result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
}
