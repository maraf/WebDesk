/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.test.dao.core;

import com.neptuo.data.*;
import com.neptuo.os.core.data.model.*;
import com.neptuo.os.core.data.dao.*;
import com.neptuo.os.test.dao.TestDAO;
import com.neptuo.service.*;
import org.junit.Test;
import static org.junit.Assert.* ;

/**
 *
 * @author Mara
 */
public class TestUserDAO extends TestDAO {

    @Test
    public void testFindAdmin() throws DataStorageException {
        UserDAO dao = createDAO(UserDAO.class);
        User admin = dao.findAdmin();
        
        assertTrue(admin != null && "admin".equals(admin.getUsername()));
    }

    @Test
    public void testLogin() throws DataStorageException, HttpException {
        UserDAO dao = createDAO(UserDAO.class);
        User admin = dao.findAdmin();

        UserLog log = dao.login("admin", "admin", "localhost");

        assertTrue(log.getUser().equals(admin));
    }
}
