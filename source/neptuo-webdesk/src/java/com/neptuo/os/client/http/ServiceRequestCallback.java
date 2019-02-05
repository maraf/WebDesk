/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

import com.google.gwt.http.client.Request;

/**
 *
 * @author Mara
 */
public abstract class ServiceRequestCallback implements RequestCallback {
    private AsyncCallback callback;

    public ServiceRequestCallback(AsyncCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onClientError(Request request, Throwable exception) {
        callback.onClientError(exception);
    }

    @Override
    public void onServerError(ExceptionType exceptionType) {
        callback.onServerError(exceptionType);
    }
}
