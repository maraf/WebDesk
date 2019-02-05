/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http.service;

import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.databus.DataEventType;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.data.model.User;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.http.ServiceRequestBuilder;

/**
 *
 * @author Mara
 */
public class LoginService {

    public static void login(String username, String password, String domain, AsyncCallback<User> callback) {
        BaseModelData bmd = new BaseModelData();
        bmd.getModelType().setRecordName("login");
        bmd.getModelType().addField("username");
        bmd.getModelType().addField("password");
        bmd.getModelType().addField("domain");

        bmd.set("username", username);
        bmd.set("password", password);
        bmd.set("domain", domain);

        ServiceRequestBuilder.factory(User.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.CurrentUser, DataEventType.REPLACE)
                .setModelType(bmd.getModelType())
                .setData(bmd)
                .setMethod(ServiceMethods.Core.Login.Login)
                .toRequestSent();
    }

    public static void isLogged(AsyncCallback<User> callback) {
        ServiceRequestBuilder.factory(User.class)
                .setCallback(callback)
                .setDataEvent(DataEvents.CurrentUser, DataEventType.REPLACE)
                .setMethod(ServiceMethods.Core.Login.IsLogged)
                .toRequestSent();
    }

    public static void logout(RequestCallback callback) {
        ServiceRequestBuilder.factory()
                .setCallback(callback)
                .setMethod(ServiceMethods.Core.Logout.Logout)
                .toRequestSent();
    }
}
