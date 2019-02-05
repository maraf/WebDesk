/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.data.dao;

import com.neptuo.os.core.TooShortPasswordException;
import com.neptuo.os.core.UsernameUsedException;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.QueryBuilder;
import com.neptuo.data.QueryComparator;
import com.neptuo.data.QueryOrder;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.os.core.AlreadyLoggedInUserCantBeDeleted;
import com.neptuo.os.core.CurrentUserCantBeDeletedException;
import com.neptuo.os.core.CurrentUserCantBeDisabledException;
import com.neptuo.os.core.data.model.IdentityBase;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.data.model.UserLog;
import com.neptuo.os.core.util.HashHelper;
import com.neptuo.os.core.util.PublicHelper;
import com.neptuo.os.fsys.data.dao.DriveDAO;
import com.neptuo.service.HttpException;
import com.neptuo.service.HttpUnauthorizedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

/**
 *
 * @author Mara
 */
public class UserDAO extends BaseDAO {
    private static final Logger log = Logger.getLogger(UserDAO.class.getName());

    public UserLog login(String username, String password, String domain) throws DataStorageException, HttpException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(User.class, "u")
                .where("username", QueryComparator.EQ)
                .and()
                .where("passwordHash", QueryComparator.EQ);

        User user = (User) getResultOrNull(getDs().createQuery(qb).setParameter("username", username).setParameter("passwordHash", HashHelper.SHA1(username + password)));

        if(user != null) {
            UserLog userLog = new UserLog(user, HashHelper.SHA1(PublicHelper.random()));
            getDs().persist(userLog);
            return userLog;
        } else {
            throw new HttpUnauthorizedException();
        }
    }

    public void logout(String authToken) throws DataStorageException {
        QueryBuilder qb = getDs().factory()
                .select()
                .from(UserLog.class, "ul")
                .where("authToken", QueryComparator.EQ);

        List<UserLog> logs = getDs().createQuery(qb)
                .setParameter("authToken", authToken)
                .getResultList();

        for(UserLog userLog : logs) {
            userLog.setLogoutTime(new Date());
            getDs().merge(userLog);
        }
    }

    public User find(Long id) throws DataStorageException {
        return getDs().find(User.class, id);
    }

    public User findAdmin() throws DataStorageException {
        //TODO: Do much better!!!
        return find(3L);
    }

    public User findByToken(String token) throws DataStorageException {
        Date now = new Date();
        Date before = new Date(now.getTime() - 30 * 60 * 1000);

        QueryBuilder qb = getDs().factory()
                .select()
                .from(UserLog.class, "ul")
                .where("authToken", QueryComparator.EQ)
                .and()
                .where("logoutTime", QueryComparator.ISNULL)
                .and()
                .where("lastActivity", QueryComparator.GT);

        UserLog userLog = (UserLog) getResultOrNull(getDs().createQuery(qb).setParameter("authToken", token).setParameter("lastActivity", before));
        if(userLog != null) {
            userLog.setLastActivity(now);
            getDs().merge(userLog);
            return userLog.getUser();
        }
        return null;
    }

    public List<User> find(String username, User current) throws DataStorageException {
        List<User> users = new ArrayList<User>();
        QueryBuilder qb = getDs().factory()
                .select("u", true)
                .from(User.class, "u")
                .join("u", "roles", "r")
                .where("", "r", QueryComparator.IN)
                .and()
                .where("username", QueryComparator.LIKE)
                .order("u", "id", QueryOrder.ASC);

        Query q = getDs().createQuery(qb);

        q.setParameter("r", createDAO(UserRoleDAO.class).findManageable(current));
        q.setParameter("username", username);
        users = (List<User>) q.getResultList();

        users.addAll((List<User>) getDs()
                .createQuery(getDs().factory().select("u").from(User.class, "u").where("u", "roles.size", QueryComparator.EQ, 0).and().where("username", QueryComparator.LIKE).order("u", "id", QueryOrder.ASC))
                .setParameter("username", username)
                .getResultList());

        return users;
    }

    public List<User> findAll(User current) throws DataStorageException {
        List<User> users = new ArrayList<User>();
        QueryBuilder qb = getDs().factory()
                .select("u", true)
                .from(User.class, "u")
                .join("u", "roles", "r")
                .where("", "r", QueryComparator.IN)
                .order("u", "id", QueryOrder.ASC);

        Query q = getDs().createQuery(qb);

        q.setParameter("r", createDAO(UserRoleDAO.class).findManageable(current));
        users = (List<User>) q.getResultList();

        users.addAll((List<User>) getDs()
                .createQuery(getDs().factory().select("u").from(User.class, "u").where("u", "roles.size", QueryComparator.EQ, 0).order("u", "id", QueryOrder.ASC))
                .getResultList());

        return users;
    }

    public boolean isUsernameUsed(String username) throws DataStorageException {
        try {
            User u = (User) getDs()
                .createQuery(getDs().factory().select().from(User.class, "u").where("username", QueryComparator.EQ))
                .setParameter("username", username)
                .getSingleResult();

            return true;
        } catch(PersistenceException e) {
            return false;
        }
    }

    public List<IdentityBase> findManageable(User current) throws DataStorageException {
        List<IdentityBase> manageable = new ArrayList<IdentityBase>();
        manageable.addAll(createDAO(UserRoleDAO.class).findManageable(current));
        manageable.add(current);
        return manageable;
    }

    public User save(User model, User current) throws DataStorageException {
        if (model == current && !model.getEnabled()) {
            throw new CurrentUserCantBeDisabledException();
        }
        if(model.getId() == null) {
            if (isUsernameUsed(model.getUsername())) {
                throw new UsernameUsedException();
            } else {
                if (model.getPasswordHash() == null || "".equals(model.getPasswordHash()) || model.getPasswordHash().length() < 4) {
                    throw new TooShortPasswordException("Password is too short");
                }
                model.setPasswordHash(HashHelper.SHA1(model.getUsername() + model.getPasswordHash()));
                model.setCreated(new Date());
                getDs().persist(model);
                return model;
            }
        } else {
            User db = find(model.getId());
            if (!db.getUsername().equals(model.getUsername()) && isUsernameUsed(model.getUsername())) {
                throw new UsernameUsedException();
            } else {
                db.setUsername(model.getUsername());
                db.setName(model.getName());
                db.setSurname(model.getSurname());
                db.setEnabled(model.getEnabled());
                if(model.getPasswordHash() != null) {
                    if ("".equals(model.getPasswordHash()) || model.getPasswordHash().length() < 4) {
                        throw new TooShortPasswordException("Password is too short");
                    }
                    db.setPasswordHash(HashHelper.SHA1(model.getUsername() + model.getPasswordHash()));
                }
                return getDs().merge(db);
            }
        }
    }

    public void delete(User model, User current) throws DataStorageException {
        User dbUser = find(model.getId());
        if (dbUser == current) {
            throw new CurrentUserCantBeDeletedException();
        }
        if (createDAO(DriveDAO.class).findUserHome(current) != null) {
            throw new AlreadyLoggedInUserCantBeDeleted();
        }
        if (dbUser != null) {
            getDs().remove(model);
        }
    }
}
