/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.annotation.ServiceMethod;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 *
 * @author Mara
 */
public class MethodMap extends HashMap<String, MethodInfo> {

    private static final Logger log = Logger.getLogger(MethodMap.class.getName());

    public void initialize(Class clazz) {
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(ServiceMethod.class)) {
                ServiceMethod sm = method.getAnnotation(ServiceMethod.class);
                log.info("Binding service method " + method.getName() + " to " + sm.name());

                MethodInfo info = new MethodInfo(sm.name(), method, sm);
                put(info.getName(), info);
            }
        }
    }
}
