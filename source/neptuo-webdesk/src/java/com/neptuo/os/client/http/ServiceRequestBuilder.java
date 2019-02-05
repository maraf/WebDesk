/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http;

import com.extjs.gxt.ui.client.data.ModelType;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.XmlWriter;
import com.neptuo.os.client.data.databus.DataEvent;
import com.neptuo.os.client.data.databus.DataEventType;
import com.neptuo.os.client.http.service.BaseService;
import com.neptuo.os.client.http.service.ServiceMethod;
import com.neptuo.os.client.http.service.ServiceUrl;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class ServiceRequestBuilder<T extends BaseModelData> {

    private ServiceMethod method;
    private ModelType modelType;
    private Class<T> type;
    private List<T> data;
    private AsyncCallback<T> callback;
    private RequestCallback plainCallback;
    private DataEvent event;
    private DataEventType eventType;
    private Object eventSource;

    //TODO: Remove!!!
    private XmlWriter writer;

    public static ServiceRequestBuilder factory() {
        return new ServiceRequestBuilder();
    }

    public static <T extends BaseModelData> ServiceRequestBuilder factory(Class<T> type) {
        ServiceRequestBuilder<T> builder = new ServiceRequestBuilder();
        return builder.setType(type);
    }

    public ServiceRequestBuilder<T> setMethod(ServiceMethod method) {
        this.method = method;
        return this;
    }

    public ServiceRequestBuilder<T> setModelType(ModelType modelType) {
        this.modelType = modelType;
        return this;
    }

    public ServiceRequestBuilder<T> setType(Class<T> type) {
        this.type = type;
        return this;
    }

    public ServiceRequestBuilder<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

    public ServiceRequestBuilder<T> setData(T data) {
        this.data = new ArrayList<T>();
        this.data.add(data);
        return this;
    }

    public ServiceRequestBuilder<T> setCallback(AsyncCallback<T> callback) {
        this.callback = callback;
        return this;
    }

    public ServiceRequestBuilder<T> setCallback(RequestCallback callback) {
        this.plainCallback = callback;
        return this;
    }

    public ServiceRequestBuilder<T> setDataEvent(DataEvent event, DataEventType eventType, Object eventSource) {
        setDataEvent(event);
        setDataEventType(eventType);
        setDataEventSource(eventSource);
        return this;
    }

    public ServiceRequestBuilder<T> setDataEvent(DataEvent event, DataEventType eventType) {
        setDataEvent(event);
        setDataEventType(eventType);
        return this;
    }

    public ServiceRequestBuilder<T> setDataEvent(DataEvent event) {
        this.event = event;
        return this;
    }

    public ServiceRequestBuilder<T> setDataEventType(DataEventType eventType) {
        this.eventType = eventType;
        return this;
    }

    public ServiceRequestBuilder<T> setDataEventSource(Object eventSource) {
        this.eventSource = eventSource;
        return this;
    }

    @Deprecated
    public ServiceRequestBuilder<T> setXmlWriter(XmlWriter writer) {
        this.writer = writer;
        return this;
    }

    public Request toRequestSent() {
        if(modelType == null) {
            if(data != null && data.size() == 1) {
                modelType = data.get(0).getModelType();
            } else if(type != null) {
                modelType = BaseService.getModelType(type);
            } else if (data != null && data.size() > 0) {
                modelType = data.get(0).getModelType();
            }
        }
        
        if(writer == null && data != null && modelType != null) {
            writer = XmlWriter.write(modelType, data);
        }
        
        String content = writer != null ? writer.toXml() : "";

        ServiceUrl url = method.getService();
        
        RequestCallback finalCallback = null;
        if (callback != null) {
            finalCallback = new ServiceRequestCallback(callback) {

                @Override
                public void onSuccess(Request request, Response response) {
                    if (callback instanceof AbstractAsyncCallback) {
                        AbstractAsyncCallback aac = (AbstractAsyncCallback) callback;
                        aac.setRequest(request);
                        aac.setResponse(response);
                    }
                    
                    if (response.getHeader("Content-type").contains("application/xml")) {
                        List<T> items = BaseService.parse(response.getText(), type);
                        if (event != null && eventType != null) {
                            NeptuoRoot.getDataBus().fireEvent(event, eventType, eventSource, items);
                        }
                        callback.onSuccess(items);
                    } else {
                        callback.onServerError(new ExceptionType("java.lang.Exception", "Generic server error", Constants.exceptions().code500(), 500));
                    }

                }
            };
        } else {
            finalCallback = new RequestCallback() {

                @Override
                public void onSuccess(Request request, Response response) {
                    if (event != null && eventType != null) {
                        NeptuoRoot.getDataBus().fireEvent(event, eventType, eventSource, data);
                    }
                    if (plainCallback != null) {
                        plainCallback.onSuccess(request, response);
                    }
                }

                @Override
                public void onClientError(Request request, Throwable exception) {
                    if (plainCallback != null) {
                        plainCallback.onClientError(request, exception);
                    }
                }

                @Override
                public void onServerError(ExceptionType exceptionType) {
                    if (plainCallback != null) {
                        plainCallback.onServerError(exceptionType);
                    }
                }
            };
        }

        return RequestFactory.send(method != null ? method.getName() : null, url.toAbsolute(), content, finalCallback);
    }
}
