/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

import com.neptuo.service.annotation.ServiceMethod;
import java.lang.reflect.Method;

/**
 *
 * @author Mara
 */
public class MethodInfo {

    private String name;
    private Method method;
    private ServiceMethod annotation;

    public MethodInfo() {
    }

    public MethodInfo(String name, Method method, ServiceMethod annotation) {
        this.name = name;
        this.method = method;
        this.annotation = annotation;
    }

    public ServiceMethod getAnnotation() {
        return annotation;
    }

    public void setAnnotation(ServiceMethod annotation) {
        this.annotation = annotation;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
