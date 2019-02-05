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
public class Permission extends BaseModelData {
    public Permission() {
        propMap.put("id", DataType.INT);
        propMap.put("targetId", DataType.INT);
        propMap.put("typeId", DataType.INT);
        propMap.put("identityId", DataType.INT);

        modelType.setRoot("permissions");
        modelType.setRecordName("permission");
        modelType.addField("id");
        modelType.addField("targetId");
        modelType.addField("typeId");
        modelType.addField("typeName");
        modelType.addField("identityId");
        modelType.addField("identityName");
        modelType.addField("identityType");
    }

    public Permission(int id) {
        this();
        setId(id);
    }

    public Permission(int targetId, int typeId, int identityId) {
        this();
        setTargetId(targetId);
        setTypeId(typeId);
        setIdentityId(identityId);
    }

    public int getId() {
        return (Integer) get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public int getTypeId() {
        return (Integer) get("typeId");
    }

    public void setTypeId(int id) {
        set("typeId", id);
    }

    public int getTargetId() {
        return (Integer) get("targetId");
    }

    public void setTargetId(int id) {
        set("targetId", id);
    }

    public int getIdentityId() {
        return (Integer) get("identityId");
    }

    public void setIdentityId(int id) {
        set("identityId", id);
    }

    public String getTypeName() {
        return get("typeName");
    }

    public void setTypeName(String name) {
        set("typeName", name);
    }

    public String getIdentityName() {
        return get("identityName");
    }

    public void setIdentityName(String name) {
        set("identityName", name);
    }

    public String getIdentityType() {
        return get("identityType");
    }

    public void setIdentityType(String type) {
        set("identityType", type);
    }
}
