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
public class HttpNotFoundException extends HttpException {

    public HttpNotFoundException() {
        super("");
        httpCode = ResponseHelper.Codes.NOT_FOUND;
    }

    public HttpNotFoundException(String message) {
        super(message);
        httpCode = ResponseHelper.Codes.NOT_FOUND;
    }
}