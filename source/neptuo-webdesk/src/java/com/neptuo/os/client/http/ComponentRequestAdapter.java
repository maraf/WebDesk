/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;

/**
 *
 * @author Mara
 */
public class ComponentRequestAdapter implements RequestCallback {
    private El component;

    public ComponentRequestAdapter(El component) {
        this.component = component;
    }
    
    public ComponentRequestAdapter(Component component) {
        this.component = component.el();
    }

    @Override
    public void onSuccess(Request request, Response response) {
        component.unmask();
    }

    @Override
    public void onClientError(Request request, Throwable exception) {
        component.mask(exception.getMessage());
    }

    @Override
    public void onServerError(ExceptionType exceptionType) {
        component.mask(exceptionType.getLocalizedMessage());
    }

}
