/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

import com.neptuo.service.MethodMap;

/**
 *
 * @author Mara
 */
public class ServiceInfo {

    private String url;
    private Class clazz;
    private MethodMap methods;

    public ServiceInfo() {
    }

    public ServiceInfo(String url, Class service) {
        this.url = url;
        this.clazz = service;
        this.methods = new MethodMap();
        this.methods.initialize(service);
    }

    public ServiceInfo(String url, Class service, MethodMap methods) {
        this.url = url;
        this.clazz = service;
        this.methods = methods;
    }

    public MethodMap getMethods() {
        return methods;
    }

    public void setMethods(MethodMap methods) {
        this.methods = methods;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
