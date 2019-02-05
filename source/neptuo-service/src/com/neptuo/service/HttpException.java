/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

import com.neptuo.service.ServiceException;

/**
 *
 * @author Mara
 */
public class HttpException extends ServiceException {

    protected int httpCode;

    public HttpException(String message) {
        super(message);
    }
    
    protected void setHttpCode(int code) {
        httpCode = code;
    }

    /**
     * @return returns http status code.
     */
    public int getHttpCode() {
        return httpCode;
    }

    @Override
    public int getErrorCode() {
        return httpCode;
    }
}
