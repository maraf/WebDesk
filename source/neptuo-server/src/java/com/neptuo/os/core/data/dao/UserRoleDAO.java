/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.data.dao;

import com.neptuo.os.core.RoleNameUsedException;
import com.neptuo.os.core.data.model.IdentityBase;
import com.neptuo.data.dao.AbstractDAO;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.os.core.UserCantManageRole;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.data.model.UserRole;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author Mara
 */
public class UserRoleDAO extends BaseDAO {

    public UserRole find(Long id) throws DataStorageException {
        return getDs().find(UserRole.class, id);
    }

    public List<UserRole> findManageable(User current) throws DataStorageException {
        List<UserRole> roles = new ArrayList<UserRole>();
        roles.addAll(current.getRoles());
        for(UserRole r : current.getRoles()) {
            for(UserRole r1 : findChildren(r)) {
                if(!roles.contains(r1)) {
                    roles.add(r1);
                }
            }
        }
        roles.add(findEveryone());

        return roles;
    }

    public List<UserRole> find(String name, User current) throws DataStorageException {
        List<UserRole> result = new ArrayList<UserRole>();
        for(UserRole role : findManageable(current)) {
            if(role.getName().contains(name)) {
                result.add(role);
            }
        }
        return result;
    }

    protected UserRole findByName(String name) throws DataStorageException {
        return (UserRole) getDs()
                .createQuery(getDs().factory().select("r").from(UserRole.class, "r").where("r", "name", QueryComparator.EQ))
                .setParameter("name", name)
                .getSingleResult();
    }

    protected List<UserRole> findChildren(UserRole parent) throws DataStorageException {
        List<UserRole> roles = new ArrayList<UserRole>();
        for(UserRole role : parent.getChidren()) {
            roles.add(role);
            roles.addAll(findChildren(role));
        }
        return roles;
    }

    public UserRole findAdmins() throws DataStorageException {
        return findByName("admins");
    }

    public UserRole findEveryone() throws DataStorageException {
        return findByName("everyone");
    }

    public boolean canUserManage(User current, UserRole target) throws DataStorageException {
        List<UserRole> manageable = findManageable(current);
        for(UserRole role : manageable) {
            if(role.equals(target)) {
                return true;
            }
        }
        return false;
    }

    public boolean isRoleNameUsed(String name) throws DataStorageException {
        try {
            UserRole r = (UserRole) getDs()
                    .createQuery(getDs().factory().select().from(UserRole.class, "r").where("name", QueryComparator.EQ))
                    .setParameter("name", name)
                    .getSingleResult();

            return true;
        } catch(NoResultException e) {
            return false;
        }
    }

    public boolean canUserAssignIdentity(IdentityBase identity, User current) throws DataStorageException {
        List<UserRole> roles = findManageable(current);
        for (UserRole role : roles) {
            if (identity instanceof UserRole && role.equals(identity)) {
                return true;
            } else if (identity instanceof User && ((User) identity).getRoles().contains(role)) {
                return true;
            }
        }

        return false;
    }

    public UserRole save(UserRole model, User current) throws DataStorageException {
        if(model.getId() == null) {
            if(isRoleNameUsed(model.getName())) {
                throw new RoleNameUsedException();
            } else if(canUserManage(current, model.getParent())) {
                getDs().persist(model);
                return model;
            } else {
                throw new UserCantManageRole();
            }
        } else {
            model = getDs().merge(model);
            return model;
        }
    }

    public void delete(UserRole model, User current) throws DataStorageException {
        if(canUserManage(current, model)) {
            getDs().remove(model);
        } else {
            throw new UserCantManageRole();
        }
    }

}
