/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

/**
 *
 * @author Mara
 */
public class RequestAdapter implements RequestCallback {

    @Override
    public void onSuccess(Request request, Response response) {
    }

    @Override
    public void onClientError(Request request, Throwable exception) {
    }

    @Override
    public void onServerError(ExceptionType exceptionType) {
    }
}
