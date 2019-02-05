/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.service;

import com.neptuo.service.util.ReflectionHelper;
import com.neptuo.service.io.Deserializer;
import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.io.AutoDeserializer;
import com.neptuo.service.io.AutoDeserializerItem;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Mara
 */
public final class ParamsInitializer {

    private Object[] params;
    private MethodInfo method;
    private Deserializer deserializer;
    private HttpServletRequest request;

    private MethodParamsProviders providers;

    public ParamsInitializer(MethodParamsProviders providers, InputStream input, MethodInfo method, Deserializer deserializer) throws HttpException, Exception {
        Map<Integer, AutoDeserializerItem> deserializers = new HashMap<Integer, AutoDeserializerItem>();
        this.params = new Object[method.getMethod().getParameterTypes().length];
        this.providers = providers;
        this.method = method;
        this.deserializer = deserializer;

        int index = 0;
        for (Class c : method.getMethod().getParameterTypes()) {
            //Try ParamsProviders
            ParamsProviderResult result = providers.runProvide(c, method, index);

            //If param is handled, use result value
            if (result != null && result.isHandled()) {
                params[index] = result.getResult();
            } else {
                //Try RequestInput
                RequestInput annotation = ReflectionHelper.getParamRequestInputAnnotation(method.getMethod(), index);
                if (annotation != null) {
                    if (Collection.class.isAssignableFrom(c)) {
                        deserializers.put(index, new AutoDeserializerItem(annotation, c, ReflectionHelper.getGenericParameterType(method.getMethod(), index)));
                    } else {
                        deserializers.put(index, new AutoDeserializerItem(annotation, null, c));
                    }
                } else {
                    //Set to null, not supported param
                    params[index] = null;
                }
            }
            index++;
        }
        if (!deserializers.isEmpty()) {
            AutoDeserializer.factory(deserializer, input, deserializers.values().toArray(new AutoDeserializerItem[0])).deserialize();
            for (Integer key : deserializers.keySet()) {
                AutoDeserializerItem item = deserializers.get(key);
                params[key] = item.getCollection() != null ? item.getCollection() : item.getItem();
            }
        }
    }

    public Object[] getParams() {
        if (params.length == 0) {
            return null;
        }
        return params;
    }
}
