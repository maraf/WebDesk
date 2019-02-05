/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.annotation.ServiceClass;
import com.neptuo.service.util.ReflectionHelper;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import org.reflections.Reflections;

/**
 *
 * @author Mara
 */
public class ServiceMap extends HashMap<String, ServiceInfo> {
    private static final Logger log = Logger.getLogger(ServiceMap.class.getName());

    public void initialize(ServletContext context) {
        Reflections reflections = ReflectionHelper.getReflections(context);
        
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ServiceClass.class);
        for (Class c : annotated) {
            ServiceClass sc = (ServiceClass) c.getAnnotation(ServiceClass.class);
            log.info("Binding service class " + c.getName() + " to url " + sc.url());

            ServiceInfo info = new ServiceInfo(sc.url(), c);
            put(info.getUrl(), info);
        }
    }
}
