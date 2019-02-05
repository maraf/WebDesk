/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http.service;

/**
 *
 * @author Mara
 */
public class ServiceMethod {
    private String name;
    private ServiceUrl service;

    public ServiceMethod(String name, ServiceUrl service) {
        this.name = name;
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public ServiceUrl getService() {
        return service;
    }
}
