/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data.model;

import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.DataType;
import java.util.Date;

/**
 *
 * @author Mara
 */
public class User extends BaseModelData {
    public User() {
        propMap.put("id", DataType.INT);
        propMap.put("enabled", DataType.BOOLEAN);
        
        modelType.setRoot("users");
        modelType.setRecordName("user");
        modelType.addField("id");
        modelType.addField("name");
        modelType.addField("surname");
        modelType.addField("username");
        modelType.addField("passwordHash");
        modelType.addField("created");
        modelType.addField("enabled");
    }

    public User(Integer id) {
        this();
        setId(id);
    }

    public Date getCreated() {
        return get("created");
    }

    public void setCreated(Date created) {
        set("created", created);
    }

    public Boolean getEnabled() {
        return get("enabled");
    }

    public void setEnabled(boolean enabled) {
        set("enabled", enabled);
    }

    public Integer getId() {
        return get("id");
    }

    public void setId(int id) {
        set("id", id);
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getPassword() {
        return get("passwordHash");
    }

    public void setPassword(String password) {
        set("passwordHash", password);
    }

    public String getSurname() {
        return get("surname");
    }

    public void setSurname(String surname) {
        set("surname", surname);
    }

    public String getUsername() {
        return get("username");
    }

    public void setUsername(String username) {
        set("username", username);
    }
}
