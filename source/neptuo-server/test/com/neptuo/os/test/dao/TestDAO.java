/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.test.dao;

import com.neptuo.data.dao.BaseDAO;
import com.neptuo.data.jpa.JpaDataStorage;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author Mara
 */
public class TestDAO {
    private static Map<Class, BaseDAO> daos = new HashMap<Class, BaseDAO>();

    public <T extends BaseDAO> T createDAO(Class<T> daoClass) {
        try {
            JpaDataStorage ds = new JpaDataStorage();
            EntityManager em = Persistence.createEntityManagerFactory("NeptuoOSDefaultPU").createEntityManager();
            ds.setEntityManager(em);

            T dao = null;
            if(daos.containsKey(daoClass)) {
                dao = (T) daos.get(daoClass);
            } else {
                dao = daoClass.newInstance();
                daos.put(daoClass, dao);
            }
            dao.setDataStorage(ds);
            return dao;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
