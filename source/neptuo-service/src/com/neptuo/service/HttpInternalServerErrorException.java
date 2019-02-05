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
public class HttpInternalServerErrorException extends HttpException {

    public HttpInternalServerErrorException() {
        super("");
        httpCode = ResponseHelper.Codes.INTERNAL_SERVER_ERROR;
    }

    public HttpInternalServerErrorException(String message) {
        super(message);
        httpCode = ResponseHelper.Codes.INTERNAL_SERVER_ERROR;
    }
}