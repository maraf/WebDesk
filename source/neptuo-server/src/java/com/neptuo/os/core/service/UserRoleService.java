/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.service;

import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.dao.UserRoleDAO;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.data.model.UserRole;
import com.neptuo.data.DataStorageException;
import com.neptuo.service.HttpException;
import com.neptuo.service.HttpNotAcceptableException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.result.CollectionResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Mara
 */
@ServiceClass(url = "/core/user-roles")
public class UserRoleService {

    public static final String USER_ONLY = "USER_ONLY";
    public static final String USER_MANAGEABLE = "USER_MANAGEABLE";

    @ServiceMethod
    public CollectionResult<UserRole> getUserRoles(User current, UserDAO users, UserRoleDAO dao, @RequestInput("type") String type) throws DataStorageException, HttpException {
        Collection<UserRole> roles = null;
        if (USER_ONLY.equals(type)) {
            roles = current.getRoles();
        } else if (USER_MANAGEABLE.equals(type)) {
            roles = dao.findManageable(current);
        } else {
            throw new HttpNotAcceptableException();
        }
        return new CollectionResult<UserRole>("userRoles", roles);
    }

    @ServiceMethod(name = "save")
    public CollectionResult<UserRole> saveUserRoles(User current, UserRoleDAO dao, @RequestInput("userRoles") List<UserRole> roles) throws DataStorageException, HttpException {
        List<UserRole> result = new ArrayList<UserRole>();
        for (UserRole role : roles) {
            if (role.getId() != null) {
                UserRole dbRole = dao.find(role.getId());
                if (dbRole != null) {
                    dbRole.setName(role.getName());
                    if (role.getParentId() != null) {
                        dbRole.setParent(dao.find(role.getParentId()));
                    }
                    result.add(dao.save(role, current));
                }
            } else {
                UserRole dbRole = new UserRole(role.getName());
                if (role.getParent() != null) {
                    dbRole.setParent(dao.find(role.getParent().getId()));
                }
                result.add(dao.save(dbRole, current));
            }
        }
        return new CollectionResult<UserRole>("userRoles", result);
    }

    @ServiceMethod(name = "delete")
    public void deleteUserRoles(User current, UserRoleDAO dao, @RequestInput("userRoles") List<UserRole> roles) throws DataStorageException, HttpException {
        for (UserRole role : roles) {
            UserRole dbRole = dao.find(role.getId());
            if (dbRole != null) {
                dao.delete(dbRole, current);
            }
        }
    }

    @ServiceMethod(name = "getUserRoles")
    public CollectionResult<UserRole> getUserRolesForUser(User current, UserRoleDAO dao, UserDAO users, @RequestInput("userId") Long userId) throws DataStorageException, HttpException {
        User user = users.find(userId);
        return new CollectionResult<UserRole>("userRoles", user.getRoles());
    }

    @ServiceMethod(name = "addUserToRole")
    public void addUserToRole(User current, UserDAO users, UserRoleDAO roles, @RequestInput("userInRoles") List<UserInRoleItem> usersInRoles) throws DataStorageException, HttpException {
        for (UserInRoleItem item : usersInRoles) {
            User u = users.find(item.getUserId());
            UserRole r = roles.find(item.getRoleId());
            if (u != null && r != null) {
                u.getRoles().add(r);
                users.save(u, current);
            }
        }
    }

    @ServiceMethod(name = "removeUserFormRole")
    public void removeUserFromRole(User current, UserDAO users, UserRoleDAO roles, @RequestInput("userInRoles") List<UserInRoleItem> usersInRoles) throws DataStorageException, HttpException {
        for (UserInRoleItem item : usersInRoles) {
            User u = users.find(item.getUserId());
            UserRole r = roles.find(item.getRoleId());
            if (u != null && r != null) {
                u.getRoles().remove(r);
                users.save(u, current);
            }
        }
    }
}
