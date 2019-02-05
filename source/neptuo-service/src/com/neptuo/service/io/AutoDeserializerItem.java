/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.service.io;

import com.neptuo.service.annotation.RequestInput;
import com.neptuo.service.io.annotation.Deserializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Mara
 */
public class AutoDeserializerItem {
    private RequestInput requestInput;
    private Class<Collection> collectionClass;
    private Class itemClass;

    private Deserializable itemClassDeserializable;
    private Map<String, Method> itemMethodInfo;

    private Collection collection;
    private Object item;

    public AutoDeserializerItem(RequestInput requestInput, Class<Collection> collectionClass, Class itemClass) {
        this.requestInput = requestInput;
        this.collectionClass = collectionClass;
        this.itemClass = itemClass;
    }

    public Class<Collection> getCollectionClass() {
        return collectionClass;
    }

    public void setCollectionClass(Class<Collection> collectionClass) {
        this.collectionClass = collectionClass;
    }

    public Class getItemClass() {
        return itemClass;
    }

    public void setItemClass(Class itemClass) {
        this.itemClass = itemClass;
    }

    public RequestInput getRequestInput() {
        return requestInput;
    }

    public void setRequestInput(RequestInput requestInput) {
        this.requestInput = requestInput;
    }

    

    public Deserializable getItemClassDeserializable() {
        return itemClassDeserializable;
    }

    public void setItemClassDeserializable(Deserializable itemClassDeserializable) {
        this.itemClassDeserializable = itemClassDeserializable;
    }

    public Map<String, Method> getItemMethodInfo() {
        return itemMethodInfo;
    }

    public void setItemMethodInfo(Map<String, Method> itemMethodInfo) {
        this.itemMethodInfo = itemMethodInfo;
    }
    
    

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }
}
