/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

import java.util.List;

/**
 *
 * @author Mara
 */
public interface AsyncCallback<T> {

    public void onSuccess(List<T> objects);

    public void onClientError(Throwable e);

    public void onServerError(ExceptionType exceptionType);
}
