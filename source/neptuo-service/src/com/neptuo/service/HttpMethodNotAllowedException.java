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
public class HttpMethodNotAllowedException extends HttpException {

    public HttpMethodNotAllowedException() {
        super("");
        httpCode = ResponseHelper.Codes.METHOD_NOT_ALLOWED;
    }

    public HttpMethodNotAllowedException(String message) {
        super(message);
        httpCode = ResponseHelper.Codes.METHOD_NOT_ALLOWED;
    }
}
