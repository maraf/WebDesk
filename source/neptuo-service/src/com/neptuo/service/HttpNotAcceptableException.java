/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

import com.neptuo.service.util.ResponseHelper;

/**
 *
 * @author Mara
 */
public class HttpNotAcceptableException extends HttpException {

    public HttpNotAcceptableException() {
        super("");
        httpCode = ResponseHelper.Codes.NOT_ACCEPTABLE;
    }

    public HttpNotAcceptableException(String message) {
        super(message);
        httpCode = ResponseHelper.Codes.NOT_ACCEPTABLE;
    }
}
