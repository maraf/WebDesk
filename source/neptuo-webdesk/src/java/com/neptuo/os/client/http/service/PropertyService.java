/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http.service;

import com.google.gwt.user.client.Timer;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.model.Property;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ExceptionType;
import com.neptuo.os.client.http.ServiceRequestBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class PropertyService {
    private static List<Property> properties = new ArrayList<Property>();

    public static void get(List<Property> props, final AsyncCallback<Property> callback) {
        final List<Property> requireLoad = new ArrayList<Property>();
        final List<Property> local = new ArrayList<Property>();

        for(Property p1 : props) {
            boolean used = false;
            for(Property p2 : properties) {
                if(p1.equals(p2)) {
                    used = true;
                    local.add(p2);
                }
            }
            if (!used) {
                requireLoad.add(p1);
            }
        }

        if (requireLoad.isEmpty()) {
            Timer t = new Timer() {

                @Override
                public void run() {
                    callback.onSuccess(local);
                }
            };
            t.schedule(10);
        } else {
            AsyncCallback<Property> thisCallback = new AsyncCallback() {

                @Override
                public void onSuccess(List objects) {
                    properties.addAll(objects);
                    local.addAll(objects);
                    callback.onSuccess(local);
                }

                @Override
                public void onClientError(Throwable e) {
                    NeptuoRoot.getCurrentWorkspace().getLogger().log("Error login properties", e);
                }

                @Override
                public void onServerError(ExceptionType exceptionType) {
                    NeptuoRoot.getCurrentWorkspace().getLogger().log(exceptionType);
                }
            };
            ServiceRequestBuilder.factory(Property.class)
                    .setCallback(thisCallback)
                    .setMethod(ServiceMethods.Core.Property.Get)
                    .setData(requireLoad)
                    .toRequestSent();
        }
    }

    public static void set(List<Property> properties) {
        ServiceRequestBuilder.factory(Property.class)
                .setData(properties)
                .setMethod(ServiceMethods.Core.Property.Set)
                .toRequestSent();

        for(Property p1 : properties) {
            boolean used = false;
            for(Property p2 : properties) {
                if(p1.equals(p2)) {
                    p2.setValue(p1.getValue());
                    used = true;
                }
            }
            if(!used) {
                properties.add(p1);
            }
        }
    }

    public static void clear() {
        properties.clear();
    }
}
