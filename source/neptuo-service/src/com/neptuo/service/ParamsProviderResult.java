/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service;

/**
 *
 * @author Mara
 */
public class ParamsProviderResult {
    private Object result;
    private boolean handled;

    public ParamsProviderResult() {
        this.handled = false;
    }

    public ParamsProviderResult(Object result) {
        this.result = result;
        this.handled = true;
    }

    public boolean isHandled() {
        return handled;
    }

    public Object getResult() {
        return result;
    }
}
