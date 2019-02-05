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
public class Identity extends BaseModelData {
    public Identity() {
        propMap.put("id", DataType.INT);

        modelType.setRoot("identities");
        modelType.setRecordName("identity");
        modelType.addField("id");
        modelType.addField("username");
        modelType.addField("surname");
        modelType.addField("name");
        modelType.addField("displayName");
        modelType.addField("type");
    }

    public void setId(int id) {
        set("id", id);
    }

    public int getId() {
        return (Integer) get("id");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setDisplayName(String name) {
        set("displayName", name);
    }

    public String getDisplayName() {
        return get("displayName");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public String getUsername() {
        return get("username");
    }

    public void setSurname(String surname) {
        set("surname", surname);
    }

    public String getSurname() {
        return get("surname");
    }

    public void setType(String type) {
        set("type", type);
    }

    public String getType() {
        return get("type");
    }
}
