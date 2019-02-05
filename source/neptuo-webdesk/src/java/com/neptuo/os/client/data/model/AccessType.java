/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data.model;

import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.DataType;

/**
 *
 * @author Mara
 */
public class AccessType extends BaseModelData {

    public AccessType() {
        modelType.setRoot("accessTypes");
        modelType.setRecordName("accessType");
        modelType.addField("id");
        modelType.addField("name");
        modelType.addField("category");
        
        propMap.put("id", DataType.INT);
    }

    public AccessType(String category) {
        this();
        setCategory(category);
    }

    public AccessType(Integer id, String name) {
        this();
        setId(id);
        setName(name);
    }

    public void setId(int id) {
        set("id", id);
    }

    public Integer getId() {
        return get("id");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setCategory(String category) {
        set("category", category);
    }

    public String getCategory() {
        return get("category");
    }
}
