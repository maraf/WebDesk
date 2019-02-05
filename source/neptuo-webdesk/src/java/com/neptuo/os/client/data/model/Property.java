/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.model;

import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public class Property extends BaseModelData {
    public Property() {
        modelType.setRoot("properties");
        modelType.setRecordName("property");
        modelType.addField("className");
        modelType.addField("key");
        modelType.addField("value");
    }

    public Property(String className, String key, String value) {
        this();
        set("className", className);
        set("key", key);
        set("value", value);
    }
    
    public void setClassName(String className) {        
        set("className", className);
    }
    
    public String getClassName() {
        return get("className");
    }

    public void setKey(String key) {
        set("key", key);
    }

    public String getKey() {
        return get("key");
    }

    public void setValue(String Value) {
        set("value", Value);
    }

    public String getValue() {
        return get("value");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Property)) return false;

        Property p = (Property) obj;

        if(p.getKey().equals(getKey()) && p.getClassName().equals(getClassName())) {
            return true;
        } else {
            return false;
        }
    }
}
