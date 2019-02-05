/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.app.usermanager;

import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public class UserRolePermission extends BaseModelData {

    public UserRolePermission() {
        modelType.setRoot("userRolePermissions");
        modelType.addField("id");
        modelType.addField("targetId");
        modelType.addField("identityId");
        modelType.addField("identityDisplayName");
        modelType.addField("accessTypeId");
        modelType.addField("accessTypeDisplayName");
    }

    public UserRolePermission(int identityId, int targetId, int accessTypeId) {
        setIdentityId(identityId);
        setTargetId(targetId);
        setAccessTypeId(accessTypeId);
    }

    public void setId(int id) {
        set("id", id);
    }

    public Integer getId() {
        return get("id");
    }

    public void setTargetId(int id) {
        set("targetId", id);
    }

    public int getTargetId() {
        return (Integer) get("targetId");
    }

    public void setIdentityId(int id) {
        set("identityId", id);
    }

    public int getIdentityId() {
        return (Integer) get("identityId");
    }

    public String getIdentityDisplayName() {
        return get("identityDisplayName");
    }

    public void setAccessTypeId(int id) {
        set("accessTypeId", id);
    }

    public int getAccessTypeId() {
        return (Integer) get("accessTypeId");
    }

    public String getAccessTypeDisplayName() {
        return get("accessTypeDisplayName");
    }
}
