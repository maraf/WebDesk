/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.service;

import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.model.User;
import com.neptuo.data.DataStorageException;
import com.neptuo.os.core.AlreadyLoggedInUserCantBeDeleted;
import com.neptuo.os.core.CurrentUserCantBeDeletedException;
import com.neptuo.os.core.CurrentUserCantBeDisabledException;
import com.neptuo.os.fsys.data.dao.DriveDAO;
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
@ServiceClass(url = "/core/users")
public class UserService {

    @ServiceMethod
    public CollectionResult<User> getUsers(User current, UserDAO dao) throws DataStorageException, HttpException {
        return new CollectionResult<User>("users", dao.findAll(current));
    }

    @ServiceMethod(name = "current")
    public CollectionResult<User> getCurrentUser(User current) throws DataStorageException, HttpException {
        List<User> list = new ArrayList<User>();
        list.add(current);
        return new CollectionResult<User>("users", list);
    }

    @ServiceMethod(name = "save")
    public CollectionResult<User> createUsers(User current, UserDAO dao, @RequestInput("users") List<User> users) throws DataStorageException, HttpException {
        List<User> entries = new ArrayList<User>();
        for (User user : users) {
            entries.add(dao.save(user, current));
        }
        return new CollectionResult<User>("users", entries);
    }

    @ServiceMethod(name = "delete")
    public void deleteUsers(User current, UserDAO dao, DriveDAO drives, @RequestInput("users") List<User> users) throws DataStorageException, HttpException {
        for (User u : users) {
            dao.delete(u, current);
        }
    }
}
