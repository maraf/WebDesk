/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.service;

import com.neptuo.data.DataStorageException;
import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.dao.UserRoleDAO;
import com.neptuo.os.core.data.model.IdentityBase;
import com.neptuo.os.core.data.model.User;
import com.neptuo.service.ServiceException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.result.CollectionResult;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Mara
 */
@ServiceClass(url="/core/identity-lookup")
public class IdentityLookupService {

    @ServiceMethod
    public CollectionResult<IdentityBase> doLookup(User current, UserDAO users, UserRoleDAO roles, @RequestInput("identityLookup") List<IdentityItem> identities) throws DataStorageException, ServiceException {
        Set<IdentityBase> result = new HashSet<IdentityBase>();
        for(IdentityItem item : identities) {
            result.addAll(users.find(item.getName(), current));
            result.addAll(roles.find(item.getName(), current));
        }

        return new CollectionResult<IdentityBase>("identities", "identity", result);
    }
}
