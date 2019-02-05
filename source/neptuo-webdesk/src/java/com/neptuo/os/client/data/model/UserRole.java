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
public class UserRole extends BaseModelData {
    public UserRole() {
        propMap.put("id", DataType.INT);
        propMap.put("parentId", DataType.INT);

        modelType.setRoot("userRoles");
        modelType.setRecordName("role");
        modelType.addField("id");
        modelType.addField("name");
        modelType.addField("parentId");
    }

    public UserRole(int id) {
        this();
        set("id", id);
    }

    public UserRole(int id, String name, int parentId) {
        this();
        set("id", id);
        set("name", name);
        set("parentId", parentId);
    }

    public void setId(int id) {
        set("id", id);
    }

    public Integer getId() {
        return get("id");
    }

    public void setParentId(int parentId) {
        set("parentId", parentId);
    }

    public Integer getParentId() {
        return get("parentId");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return (String) get("name");
    }
}
