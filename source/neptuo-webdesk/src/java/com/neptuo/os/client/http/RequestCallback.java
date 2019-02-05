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
public interface RequestCallback {

    public void onSuccess(Request request, Response response);

    public void onClientError(Request request, Throwable exception);

    public void onServerError(ExceptionType exceptionType);
}
