/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.MethodParamsProviderMethods;
import com.neptuo.service.MethodInfo;
import com.neptuo.service.ParamsProviderResult;
import com.neptuo.service.util.ReflectionHelper;
import com.neptuo.service.annotation.ProviderMethod;
import com.neptuo.service.io.Deserializer;
import com.neptuo.service.io.Serializer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Mara
 */
public class MethodParamsProviderMethods {
    private static final Logger log = Logger.getLogger(MethodParamsProviderMethods.class.getName());

    private Class clazz;
    private Object instance;
    private List<Method> init;
    private List<Method> destroy;
    private List<Method> before;
    private List<Method> after;
    private Method authorize;
    private Method provide;

    public MethodParamsProviderMethods(Class clazz) {
        this.clazz = clazz;
    }

    public void initialize() throws InstantiationException, IllegalAccessException {
        instance = clazz.newInstance();

        init = new ArrayList<Method>();
        destroy = new ArrayList<Method>();
        before = new ArrayList<Method>();
        after = new ArrayList<Method>();

        for (Method m : clazz.getMethods()) {
            ProviderMethod pp = ReflectionHelper.getAnnotation(m, ProviderMethod.class);
            if (pp != null) {
                switch (pp.value()) {
                    case INIT:
                        init.add(m);
                        log.info("Provider method found, type: INIT, name: " + m.getName());
                        break;
                    case DESTROY:
                        destroy.add(m);
                        log.info("Provider method found, type: DESTROY, name: " + m.getName());
                        break;
                    case BEFORE:
                        before.add(m);
                        log.info("Provider method found, type: BEFORE, name: " + m.getName());
                        break;
                    case AFTER:
                        after.add(m);
                        log.info("Provider method found, type: AFTER, name: " + m.getName());
                        break;
                    case PROVIDE:
                        provide = m;
                        log.info("Provider method found, type: PROVIDE, name: " + m.getName());
                        break;
                    case AUTHORIZE:
                        authorize = m;
                        log.info("Provider method found, type: AUTHORIZE, name: " + m.getName());
                        break;
                }
            }
        }

        if(provide == null) {
            throw new IllegalStateException("Provider must have method 'provide'");
        }
    }

    public void runInit() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Method m : init) {
            m.invoke(instance);
        }
    }

    public void runDestroy() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Method m : destroy) {
            m.invoke(instance);
        }
    }

    public void runBefore(ServletContext context, HttpServletRequest request, HttpServletResponse response, Serializer serializer, Deserializer deserializer) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Method m : before) {
            m.invoke(instance, context, request, response, serializer, deserializer);
        }
    }

    public void runAfter(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (Method m : after) {
            m.invoke(instance, context, request, response);
        }
    }

    public ParamsProviderResult runProvide(Class clazz, MethodInfo method, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ParamsProviderResult result = (ParamsProviderResult) provide.invoke(instance, clazz, method, index);
        return result;
    }

    public boolean runAuthorize(String roleName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (authorize != null) {
            return (Boolean) authorize.invoke(instance, roleName);
        } else {
            return true;
        }
    }
}
