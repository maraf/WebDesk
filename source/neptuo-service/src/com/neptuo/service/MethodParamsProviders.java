/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.annotation.ParamsProvider;
import com.neptuo.service.io.Deserializer;
import com.neptuo.service.io.Serializer;
import com.neptuo.service.util.ReflectionHelper;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.reflections.Reflections;

/**
 *
 * @author Mara
 */
public class MethodParamsProviders {

    private static final Logger log = Logger.getLogger(MethodParamsProviders.class.getName());
    private List<MethodParamsProviderMethods> providers;

    public void initialize(ServletContext context) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
        providers = new ArrayList<MethodParamsProviderMethods>();

        Reflections reflections = ReflectionHelper.getReflections(context);

        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ParamsProvider.class);
        for (Class clazz : annotated) {
            log.info("Annotated MethodParamsProvider class: " + clazz.getName());
            MethodParamsProviderMethods provider = new MethodParamsProviderMethods(clazz);
            provider.initialize();
            providers.add(provider);
        }

        //TODO: Call init!!
        runInit();
    }

    public void destroy() {
        try {
            runDestroy();
        } catch (IllegalAccessException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    public void runInit() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (MethodParamsProviderMethods mppm : providers) {
            mppm.runInit();
        }
    }

    public void runDestroy() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (MethodParamsProviderMethods mppm : providers) {
            mppm.runDestroy();
        }
    }

    public void runBefore(ServletContext context, HttpServletRequest request, HttpServletResponse response, Serializer serializer, Deserializer deserializer) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (MethodParamsProviderMethods mppm : providers) {
            mppm.runBefore(context, request, response, serializer, deserializer);
        }
    }

    public void runAfter(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (MethodParamsProviderMethods mppm : providers) {
            mppm.runAfter(context, request, response);
        }
    }

    public ParamsProviderResult runProvide(Class clazz, MethodInfo method, int index) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (MethodParamsProviderMethods mppm : providers) {
            ParamsProviderResult result = mppm.runProvide(clazz, method, index);
            if (result.isHandled()) {
                return result;
            }
        }
        return null;
    }

    public boolean runAuthorize(String roleName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        for (MethodParamsProviderMethods mppm : providers) {
            if(mppm.runAuthorize(roleName)) {
                return true;
            }
        }
        return true;
    }
}
