package com.neptuo.os.core.service;

import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.data.model.UserLog;
import com.neptuo.data.DataStorageException;
import com.neptuo.os.core.util.ResponseHelper;
import com.neptuo.os.fsys.data.dao.DriveDAO;
import com.neptuo.service.HttpException;
import com.neptuo.service.ServiceException;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.annotation.ServiceMethod;
import com.neptuo.service.result.CollectionResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
@ServiceClass(url = "/core/login")
public class LoginService {
    
    @ServiceMethod
    public CollectionResult<User> doLogin(UserDAO dao, DriveDAO drives, @RequestInput("login") LoginItem item, HttpServletResponse response) throws DataStorageException, HttpException {
        UserLog log = dao.login(item.getUsername(), item.getPassword(), item.getDomain());
        ResponseHelper.setAuthToken(response, log.getAuthToken());

        if(drives.findUserHome(log.getUser()) == null) {
            drives.createUserHome(log.getUser());
        }

        List<User> list = new ArrayList<User>();
        list.add(log.getUser());
        return new CollectionResult<User>("users", list);
    }

    @ServiceMethod(name = "isLogged")
    public CollectionResult<User> checkLogged(User current) throws ServiceException, IOException {
        List<User> list = new ArrayList<User>();
        list.add(current);
        return new CollectionResult<User>("users", list);
    }
}
