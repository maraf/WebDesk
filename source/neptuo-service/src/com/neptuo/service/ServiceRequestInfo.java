/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

import com.neptuo.service.MethodInfo;
import com.neptuo.service.ServiceInfo;

/**
 *
 * @author Mara
 */
public class ServiceRequestInfo {

    private ServiceInfo service;
    private MethodInfo method;

    public ServiceRequestInfo(ServiceInfo service, MethodInfo method) {
        this.service = service;
        this.method = method;
    }

    public MethodInfo getMethod() {
        return method;
    }

    public void setMethod(MethodInfo method) {
        this.method = method;
    }

    public ServiceInfo getService() {
        return service;
    }

    public void setService(ServiceInfo service) {
        this.service = service;
    }
}
