/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

import com.neptuo.service.io.annotation.Serializable;

/**
 *
 * @author Mara
 */
@Serializable(name="exception")
public class ServiceException extends Exception {
    
    public ServiceException(Throwable cause) {
    }

    public ServiceException(String message, Throwable cause) {
    }

    public ServiceException(String message) {
    }

    public ServiceException() {
    }

    public int getErrorCode() {
        return 100;
    }

    @Serializable(name="errorCode")
    public int getErrorCodeOverride() {
        return getErrorCode();
    }

    @Override
    @Serializable(name="errorMessage")
    public String getMessage() {
        return super.getMessage();
    }

    @Serializable(name="className")
    public String getClassName() {
        return getClass().getName();
    }
}
