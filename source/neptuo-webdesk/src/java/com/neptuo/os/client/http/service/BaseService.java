/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.http.service;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.XmlReader;
import com.google.gwt.core.client.GWT;
import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.BaseTreeModelData;
import com.neptuo.os.client.data.ObjectFactory;
import com.neptuo.os.client.http.LoadResultCallback;
import com.neptuo.os.client.http.LoadResultCallbackTree;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mara
 */
public class BaseService {

    // Parse response to list of models
    
    public static <T extends BaseModelData> ModelType getModelType(final Class type) {
        return ((T) ObjectFactory.create(type)).getModelType();
    }

    public static <T extends BaseModelData> List<T> parse(String response, Class<T> type) {
        ModelType modelType = getModelType(type);
        return parse(response, type, modelType, null);
    }

    public static <T extends BaseModelData> List<T> parse(String response, Class<T> type, ModelType modelType) {
        return parse(response, type, modelType, null);
    }

    public static <T extends BaseModelData> List<T> parse(String response, final Class<T> type, LoadResultCallback<T> callback) {
        ModelType modelType = ((T) ObjectFactory.create(type)).getModelType();
        return parse(response, type, modelType, callback);
    }

    public static <T extends BaseModelData> List<T> parse(String response, final Class<T> type, ModelType modelType, LoadResultCallback<T> callback) {
        List<T> result = new ArrayList<T>();

        XmlReader<ListLoadResult<com.extjs.gxt.ui.client.data.BaseModelData>> reader = new XmlReader<ListLoadResult<com.extjs.gxt.ui.client.data.BaseModelData>>(modelType);
        for (com.extjs.gxt.ui.client.data.BaseModelData item : (List<com.extjs.gxt.ui.client.data.BaseModelData>) reader.read(new Object(), response)) {
            T target = (T) ObjectFactory.create(type);
            for (int i = 0; i < modelType.getFieldCount(); i++) {
                target.set(modelType.getField(i).getName(), item.get(modelType.getField(i).getName()));
            }
            if(callback != null) {
                callback.onItem(target);
            }
            result.add(target);
        }
        return result;
    }



    // Parse response to list of tree models

    public static <T extends BaseTreeModelData> List<T> parseTree(String response, Class<T> type) {
        ModelType modelType = ((T) ObjectFactory.create(type)).getModelType();
        return parseTree(response, type, modelType, null);
    }

    public static <T extends BaseTreeModelData> List<T> parseTree(String response, Class<T> type, ModelType modelType) {
        return parseTree(response, type, modelType, null);
    }

    public static <T extends BaseTreeModelData> List<T> parseTree(String response, Class<T> type, LoadResultCallbackTree<T> callback) {
        ModelType modelType = ((T) ObjectFactory.create(type)).getModelType();
        return parseTree(response, type, modelType, callback);
    }

    public static <T extends BaseTreeModelData> List<T> parseTree(String response, Class<T> type, ModelType modelType, LoadResultCallbackTree<T> callback) {
        List<T> result = new ArrayList<T>();

        XmlReader<ListLoadResult<com.extjs.gxt.ui.client.data.BaseModelData>> reader = new XmlReader<ListLoadResult<com.extjs.gxt.ui.client.data.BaseModelData>>(modelType);
        for (com.extjs.gxt.ui.client.data.BaseModelData item : (List<com.extjs.gxt.ui.client.data.BaseModelData>) reader.read(new Object(), response)) {
            T target = (T) ObjectFactory.create(type);
            for (int i = 0; i < modelType.getFieldCount(); i++) {
                target.set(modelType.getField(i).getName(), item.get(modelType.getField(i).getName()));
            }
            if(callback != null) {
                callback.onItem(target);
            }
            result.add(target);
        }
        return result;
    }
}
