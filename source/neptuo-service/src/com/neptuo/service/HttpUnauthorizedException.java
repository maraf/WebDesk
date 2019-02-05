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
public class HttpUnauthorizedException extends HttpException {

    public HttpUnauthorizedException() {
        super("");
        httpCode = ResponseHelper.Codes.UNAUTHORIZED;
    }

    public HttpUnauthorizedException(String message) {
        super(message);
        httpCode = ResponseHelper.Codes.UNAUTHORIZED;
    }
}
