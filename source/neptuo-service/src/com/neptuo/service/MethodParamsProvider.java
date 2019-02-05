/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

import com.neptuo.service.ServiceException;
import com.neptuo.service.MethodInfo;
import com.neptuo.service.ParamsProviderResult;
import com.neptuo.service.io.Deserializer;
import com.neptuo.service.io.Serializer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
public interface MethodParamsProvider {

    public void init();

    public void destroy();

    public void beforeRequest(HttpServletRequest request, HttpServletResponse response, Serializer serializer, Deserializer deserializer);

    public void afterRequest(HttpServletRequest request, HttpServletResponse response);

    public ParamsProviderResult provide(Class clazz, MethodInfo method, int index) throws ServiceException;
}
