/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.service;

import com.neptuo.os.core.data.dao.AccessTypeDAO;
import com.neptuo.os.core.data.model.AccessType;
import com.neptuo.data.DataStorageException;
import com.neptuo.service.HttpException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.result.CollectionResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
@ServiceClass(url="/core/access-types")
public class AccessTypeService {

    @ServiceMethod
    public CollectionResult<AccessType> handleRequest(AccessTypeDAO dao, @RequestInput("accessTypes") List<AccessType> types) throws DataStorageException, HttpException {
        List<AccessType> result = new ArrayList<AccessType>();

        for(AccessType type : types) {
            if(type.getCategory() != null) {
                result.addAll(dao.findCategory(type.getCategory()));
            } else if(type.getId() != null) {
                result.add(dao.find(type.getId()));
            }
        }

        return new CollectionResult<AccessType>("accessTypes", result);
    }
}
