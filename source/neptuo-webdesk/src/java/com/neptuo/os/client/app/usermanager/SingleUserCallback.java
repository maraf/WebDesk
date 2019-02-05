/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.usermanager;

import com.neptuo.os.client.data.model.User;
import com.neptuo.os.client.http.ExceptionType;

/**
 *
 * @author Mara
 */
public interface SingleUserCallback {

    public void onResponse(User user);

    public void onClientError(Throwable exception);

    public void onServerError(ExceptionType exceptionType);
}
