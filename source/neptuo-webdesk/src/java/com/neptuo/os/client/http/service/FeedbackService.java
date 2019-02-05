/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http.service;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.neptuo.os.client.data.model.Feedback;
import com.neptuo.os.client.http.RequestCallback;
import com.neptuo.os.client.http.ServiceRequestBuilder;

/**
 *
 * @author Mara
 */
public class FeedbackService {

    public static void send(Feedback feedback, RequestCallback callback) {
        ServiceRequestBuilder.factory(Feedback.class)
                .setCallback(callback)
                .setMethod(ServiceMethods.Core.Feedback.Send)
                .setData(feedback)
                .toRequestSent();
    }
}
