/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.util.ReflectionHelper;
import com.neptuo.service.annotation.HttpHeader;
import com.neptuo.service.annotation.HttpParam;
import com.neptuo.service.annotation.ParamsProvider;
import com.neptuo.service.annotation.ProviderMethod;
import com.neptuo.service.annotation.ServerResource;
import com.neptuo.service.io.Deserializer;
import com.neptuo.service.io.Serializer;
import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
@ParamsProvider
public class HttpParamsProvider {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext context;
    private Serializer serializer;
    private Deserializer deserializer;

    @ProviderMethod(ProviderMethodType.BEFORE)
    public void beforeRequest(ServletContext context, HttpServletRequest request, HttpServletResponse response, Serializer serializer, Deserializer deserializer) {
        this.request = request;
        this.response = response;
        this.context = context;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @ProviderMethod(ProviderMethodType.AFTER)
    public void afterRequest(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
        this.request = null;
        this.response = null;
        this.context = null;
        this.serializer = null;
        this.deserializer = null;
    }

    @ProviderMethod(ProviderMethodType.PROVIDE)
    public ParamsProviderResult provide(Class clazz, MethodInfo method, int index) throws ServiceException {
        Object result = null;
        boolean handled = false;

        if (clazz == Serializer.class) {
            result = getSerializer();
            handled = true;
        } else if (clazz == Deserializer.class) {
            result = getDeserializer();
            handled = true;
        } else if (clazz == HttpServletRequest.class) {
            result = getRequest();
            handled = true;
        } else if (clazz == HttpServletResponse.class) {
            result = getResponse();
            handled = true;
        } else {
            if (!handled) {
                HttpHeader header = ReflectionHelper.getParamAnnotation(method.getMethod(), index, HttpHeader.class);
                if (header != null && clazz == String.class) {
                    result = getHeader(header.value());
                    handled = true;
                }
            }

            if (!handled) {
                HttpParam param = ReflectionHelper.getParamAnnotation(method.getMethod(), index, HttpParam.class);
                if (param != null && clazz == String.class) {
                    result = getParam(param.value());
                    handled = true;
                }
            }

            if(!handled) {
                ServerResource res = ReflectionHelper.getParamAnnotation(method.getMethod(), index, ServerResource.class);
                if(res != null && clazz == InputStream.class) {
                    result = context.getResourceAsStream(res.value());
                    handled = true;
                }
            }
        }

        if (handled) {
            return new ParamsProviderResult(result);
        } else {
            return new ParamsProviderResult();
        }
    }

    public String getHeader(String name) {
        return request.getHeader(name);
    }

    public String getParam(String name) {
        return request.getParameter(name);
    }

    private HttpServletRequest getRequest() {
        return request;
    }

    private HttpServletResponse getResponse() {
        return response;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public Deserializer getDeserializer() {
        return deserializer;
    }
}
