/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.service;

import com.neptuo.os.core.data.dao.AccessTypeDAO;
import com.neptuo.os.core.data.dao.PropertyDAO;
import com.neptuo.os.core.data.dao.UserDAO;
import com.neptuo.os.core.data.dao.UserRoleDAO;
import com.neptuo.os.core.data.model.User;
import com.neptuo.os.core.data.model.UserRole;
import com.neptuo.os.core.service.annotation.AuthToken;
import com.neptuo.os.core.util.PersistenceHelper;
import com.neptuo.data.DataStorage;
import com.neptuo.data.DataStorageException;
import com.neptuo.data.UploadStorage;
import com.neptuo.data.dao.BaseDAO;
import com.neptuo.data.jpa.JpaDataStorage;
import com.neptuo.data.upload.UploadStorageImpl;
import com.neptuo.service.HttpUnauthorizedException;
import com.neptuo.service.MethodInfo;
import com.neptuo.service.ParamsProviderResult;
import com.neptuo.service.ProviderMethodType;
import com.neptuo.service.annotation.ParamsProvider;
import com.neptuo.service.annotation.ProviderMethod;
import com.neptuo.service.io.Deserializer;
import com.neptuo.service.io.Serializer;
import com.neptuo.service.util.ReflectionHelper;
import com.neptuo.os.core.util.RequestHelper;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
@ParamsProvider
public class CoreParamsProvider {
    public static final String AUTH_HEADER_NAME = "AuthToken";

    protected UploadStorage uploads;
    protected EntityManager manager;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected ServletContext context;
    protected MethodInfo method;

    protected JpaDataStorage ds;

    @ProviderMethod(ProviderMethodType.INIT)
    public void init() {
        uploads = new UploadStorageImpl();
    }

    @ProviderMethod(ProviderMethodType.BEFORE)
    public void beforeRequest(ServletContext context, HttpServletRequest request, HttpServletResponse response, Serializer serializer, Deserializer deserializer) {
        this.request = request;
        this.response = response;
        this.context = context;
    }

    @ProviderMethod(ProviderMethodType.AFTER)
    public void afterRequest(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
        //If there is AuthToken in request, than copy that one to response
        if (request.getHeader(CoreParamsProvider.AUTH_HEADER_NAME) != null) {
            response.setHeader(CoreParamsProvider.AUTH_HEADER_NAME, request.getHeader(CoreParamsProvider.AUTH_HEADER_NAME));
        }

        //Clear for next request
        this.request = null;
        this.response = null;
        this.context = null;

        this.ds = null;
    }

    @ProviderMethod(ProviderMethodType.AUTHORIZE)
    public boolean authorize(String roleName) throws DataStorageException, HttpUnauthorizedException {
        for(UserRole role : getCurrentUser().getRoles()) {
            if(role.getName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }

    @ProviderMethod(ProviderMethodType.PROVIDE)
    public ParamsProviderResult provide(Class clazz, MethodInfo method, int index) throws DataStorageException, HttpUnauthorizedException {
        this.method = method;

        Object result = null;
        boolean handled = false;

        if (clazz == User.class) {
            result = getCurrentUser();
            handled = true;
        } else if (clazz == DataStorage.class) {
            result = getDataStorage();
            handled = true;
        } else if (clazz == UploadStorage.class) {
            result = getUploadStorage();
            handled = true;
        } else if (clazz == BaseDAO.class) {
            result = getBaseDAO();
            handled = true;
        } else if (clazz == AccessTypeDAO.class) {
            result = getAccessTypeDAO();
            handled = true;
        } else if (clazz == UserDAO.class) {
            result = getUserDAO();
            handled = true;
        } else if (clazz == UserRoleDAO.class) {
            result = getUserRoleDAO();
            handled = true;
        } else if (clazz == PropertyDAO.class) {
            result = getPropertyDAO();
            handled = true;
        } else {
            if(ReflectionHelper.getParamAnnotation(method.getMethod(), index, AuthToken.class) != null) {
                result = getAuthToken();
                handled = true;
            }
        }

        if (handled) {
            return new ParamsProviderResult(result);
        } else {
            return new ParamsProviderResult();
        }
    }

    public String getAuthToken() {
        return RequestHelper.getAuthToken(request);
    }

    public User getCurrentUser() throws HttpUnauthorizedException, DataStorageException {
        User u = getUserDAO().findByToken(getAuthToken());
        if (u == null) {
            throw new HttpUnauthorizedException();
        } else {
            return u;
        }
    }

    public DataStorage getDataStorage() {
        if (ds == null) {
            manager = PersistenceHelper.lookupDefault();
            ds = new JpaDataStorage();
            ds.setEntityManager(manager);
        }
        return ds;
    }

    public UploadStorage getUploadStorage() {
        return uploads;
    }

    public BaseDAO getBaseDAO() {
        BaseDAO dao = new BaseDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }

    public AccessTypeDAO getAccessTypeDAO() {
        AccessTypeDAO dao = new AccessTypeDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }

    public UserDAO getUserDAO() {
        UserDAO dao = new UserDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }

    public UserRoleDAO getUserRoleDAO() {
        UserRoleDAO dao = new UserRoleDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }

    protected PropertyDAO getPropertyDAO() {
        PropertyDAO dao = new PropertyDAO();
        dao.setDataStorage(getDataStorage());
        return dao;
    }
}
