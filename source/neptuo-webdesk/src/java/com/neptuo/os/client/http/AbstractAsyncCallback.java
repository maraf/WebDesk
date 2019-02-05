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
public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {
    private Request request;
    private Response response;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
