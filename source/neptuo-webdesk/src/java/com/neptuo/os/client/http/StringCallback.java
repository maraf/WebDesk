/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

/**
 *
 * @author Mara
 */
public interface StringCallback {

    public void onSucces(String content);

    public void onClientError(Throwable e);

    public void onServerError(ExceptionType exceptionType);
}
