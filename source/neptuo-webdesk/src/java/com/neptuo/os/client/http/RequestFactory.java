/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.http;

import com.google.gwt.http.client.Request;
import com.google.gwt.i18n.client.LocaleInfo;
import com.neptuo.os.client.NeptuoRoot;
import com.neptuo.os.client.data.KeyValuePair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class RequestFactory {
    public static final String AUTH_HEADER_NAME = "AuthToken";

    private static Map<String, String> defaultHeaders;

    public static Request send(String method, String url, String params, Map<String, String> headers, RequestCallback callback) {
        params = addDefaultXml(params);

        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
        for(String key : headers.keySet()) {
            builder.setHeader(key, headers.get(key));
        }
        if (method != null) {
            builder.setHeader("Method", method);
        }
        return builder.sendRequest(params, callback);
    }

    public static Request send(String method, String url, String params, RequestCallback callback) {
        if (defaultHeaders == null) {
            defaultHeaders = new HashMap<String, String>();
            defaultHeaders.put("Accept", "application/xml");
            defaultHeaders.put("Accept-Language", LocaleInfo.getCurrentLocale().getLocaleName() + ",en;q=0.5");
            defaultHeaders.put("Content-Type", "application/xml; charset=utf-8");
        }
        defaultHeaders.put("Content-Length", String.valueOf(params.length()));
        if (NeptuoRoot.getAuthToken() != null) {
            defaultHeaders.put(AUTH_HEADER_NAME, NeptuoRoot.getAuthToken());
        } else {
            defaultHeaders.remove(AUTH_HEADER_NAME);
        }
        return send(method, url, params, defaultHeaders, callback);
    }

    public static Request send(String url, String params, RequestCallback callback) {
        return send(null, url, params, callback);
    }

    public static RequestBuilder get(RequestBuilder.Method method, String url) {
        return new RequestBuilder(method, url);
    }

    private static String addDefaultXml(String params) {
        return "<neptuo-os>" + params + "</neptuo-os>";
    }
}
