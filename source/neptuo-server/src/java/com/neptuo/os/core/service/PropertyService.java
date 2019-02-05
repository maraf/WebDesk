/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.service;

import com.neptuo.os.core.data.dao.PropertyDAO;
import com.neptuo.os.core.data.model.Property;
import com.neptuo.os.core.data.model.User;
import com.neptuo.data.DataStorageException;
import com.neptuo.service.HttpException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.result.CollectionResult;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author Mara
 */
@ServiceClass(url = "/core/properties")
public class PropertyService {

    @ServiceMethod
    public CollectionResult<Property> getProperties(User current, @RequestInput("properties") List<Property> props, PropertyDAO dao) throws DataStorageException, HttpException {
        List<Property> properties = new ArrayList<Property>();
        for (Property p : props) {
            try {
                if (p.getClassName() != null && p.getKey() != null) {
                    Property res = dao.find(p.getClassName(), p.getKey(), current);
                    if (res != null) {
                        properties.add(res);
                    }
                } else if (p.getClassName() != null) {
                    properties.addAll(dao.find(p.getClassName(), current));
                }
            } catch (NoResultException e) {
                continue;
            }
        }
        return new CollectionResult<Property>("properties", properties);
    }

    @ServiceMethod(name = "getSystem")
    public CollectionResult<Property> getSystemProperties(PropertyDAO dao) throws DataStorageException, HttpException {
        return new CollectionResult<Property>("properties", dao.findSystem());
    }

    @ServiceMethod(name = "set")
    public void setProperties(User current, PropertyDAO dao, @RequestInput("properties") List<Property> props) throws DataStorageException, HttpException {
        for (Property p : props) {
            try {
                Property en = dao.find(p.getClassName(), p.getKey(), current);
                en.setValue(p.getValue());
                dao.merge(en);
            } catch (NoResultException e) {
                p.setUser(current);
                dao.save(p);
            }
        }
    }

    @ServiceMethod(name = "delete")
    public void deleteProperties(User current, PropertyDAO dao, @RequestInput("properties") List<Property> props) throws DataStorageException, HttpException {
        for (Property p : props) {
            try {
                Property en = dao.find(p.getClassName(), p.getKey(), current);
                dao.delete(en);
            } catch (NoResultException e) {
                continue;
            }
        }
    }
}
