/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.service;

import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.service.annotation.AuthToken;
import com.neptuo.data.DataStorageException;
import com.neptuo.service.HttpException;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;

/**
 *
 * @author Mara
 */
@ServiceClass(url="/core/logout")
public class LogoutService {

    @ServiceMethod
    public void doLogout(UserDAO dao, @AuthToken String token) throws DataStorageException, HttpException {
        // Logout
        dao.logout(token);
    }
}
