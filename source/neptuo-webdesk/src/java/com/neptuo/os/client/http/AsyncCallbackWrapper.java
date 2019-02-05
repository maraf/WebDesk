/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http;

import com.neptuo.os.client.http.service.BaseService;
import com.extjs.gxt.ui.client.data.ModelType;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public class AsyncCallbackWrapper<T extends BaseModelData> implements RequestCallback {
    private AsyncCallback<T> callback;
    private Class<T> type;
    private ModelType modelType;
    private LoadResultCallback<T> resulCallback;

    public AsyncCallbackWrapper(AsyncCallback<T> callback, Class<T> type) {
        this.callback = callback;
        this.type = type;
        this.modelType = BaseService.getModelType(type);
    }

    public AsyncCallbackWrapper(AsyncCallback<T> callback, Class<T> type, ModelType modelType, LoadResultCallback<T> resulCallback) {
        this.callback = callback;
        this.type = type;
        this.modelType = modelType;
        this.resulCallback = resulCallback;
    }

    @Override
    public void onSuccess(Request request, Response response) {
        if(callback instanceof AbstractAsyncCallback) {
            AbstractAsyncCallback aac = (AbstractAsyncCallback) callback;
            aac.setRequest(request);
            aac.setResponse(response);
        }

        if (response.getHeader("Content-type").contains("application/xml")) {
            callback.onSuccess(BaseService.parse(response.getText(), type, modelType, resulCallback));
        } else {
            callback.onServerError(new ExceptionType("java.lang.Exception", "Generic server error", Constants.exceptions().code500(), 500));
        }
    }

    @Override
    public void onClientError(Request request, Throwable exception) {
        callback.onClientError(exception);
    }

    @Override
    public void onServerError(ExceptionType exceptionType) {
        callback.onServerError(exceptionType);
    }

}
