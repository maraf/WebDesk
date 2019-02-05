/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http.service;

import com.neptuo.os.client.data.model.AccessType;
import com.neptuo.os.client.data.databus.DataEventType;
import com.neptuo.os.client.data.databus.DataEvents;
import com.neptuo.os.client.http.AsyncCallback;
import com.neptuo.os.client.http.ServiceRequestBuilder;

/**
 *
 * @author Mara
 */
public class AccessTypeService {

    public static void get(String category, final AsyncCallback<AccessType> callback) {
        ServiceRequestBuilder.factory(AccessType.class)
                .setCallback(callback)
                .setMethod(ServiceMethods.Core.AccessType.Get)
                .setDataEvent(DataEvents.AccessTypes)
                .setDataEventSource(category)
                .setDataEventType(DataEventType.REPLACE)
                .setData(new AccessType(category))
                .toRequestSent();
    }
}