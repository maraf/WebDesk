/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http;

import com.neptuo.os.client.http.service.BaseService;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.neptuo.os.client.Constants;
import com.neptuo.os.client.NeptuoRoot;
import java.util.List;
import java.util.MissingResourceException;

/**
 *
 * @author Mara
 */
public class RequestBuilder extends com.google.gwt.http.client.RequestBuilder {

    public RequestBuilder(Method httpMethod, String url) {
        super(httpMethod, url);
    }

    public Request sendRequest(String requestData, final RequestCallback callback) {
        Request newRequest = null;
        try {
            newRequest = super.sendRequest(requestData, new com.google.gwt.http.client.RequestCallback() {

                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (NeptuoRoot.isInitialized()) {
                        if (response.getStatusCode() == 401) {
                            NeptuoRoot.showLoginScreen();
                        } else if(response.getStatusCode() == 200) {
                            if (NeptuoRoot.getCurrentWorkspace() != null) {
                                NeptuoRoot.getCurrentWorkspace().getMainMenu().resetTimeout();
                            }
                            callback.onSuccess(request, response);
                        } else {
                            if (response.getHeader("Content-type").contains("application/xml") && response.getText() != null && response.getText().length() > 0) {
                                ExceptionType ex = parseExceptionType(response.getText());
                                if (NeptuoRoot.getCurrentWorkspace() != null && NeptuoRoot.getCurrentWorkspace().getLogger() != null) {
                                    NeptuoRoot.getCurrentWorkspace().getLogger().log(ex);
                                }
                                callback.onServerError(ex);
                            } else {
                                callback.onServerError(new ExceptionType("java.lang.Exception", "Generic server error", Constants.exceptions().code500(), 500));
                            }
                        }
                    } else {
                        callback.onSuccess(request, response);
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    if (NeptuoRoot.getCurrentWorkspace() != null && NeptuoRoot.getCurrentWorkspace().getLogger() != null) {
                        NeptuoRoot.getCurrentWorkspace().getLogger().log("Error getting request response", exception);
                    }
                    callback.onClientError(request, exception);
                }
            });
            return newRequest;
        } catch (RequestException exception) {
            if (NeptuoRoot.getCurrentWorkspace() != null && NeptuoRoot.getCurrentWorkspace().getLogger() != null) {
                NeptuoRoot.getCurrentWorkspace().getLogger().log("Error getting request response", exception);
            }
            callback.onClientError(newRequest, exception);
            return null;
        }
    }

    protected ExceptionType parseExceptionType(String response) {
        List<ExceptionType> list = BaseService.parse(response, ExceptionType.class, new LoadResultCallback<ExceptionType>() {

            @Override
            public void onItem(ExceptionType item) {
                try {
                    item.setLocalizedMessage(Constants.exceptions().getString("code" + item.getCode()));
                } catch (MissingResourceException e) {
                    item.setLocalizedMessage(Constants.webdesk().errorRetrievingdata());
                }
            }
        });

        if(list.isEmpty()) {
            return new ExceptionType("java.lang.Exception", "Generic server error", Constants.exceptions().code500(), 500);
        } else {
            return list.get(0);
        }
    }
}
