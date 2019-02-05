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
public class UserInRole extends BaseModelData {

    public UserInRole(int userId, int roleId) {
        modelType.setRoot("userInRoles");
        modelType.setRecordName("userInRole");
        modelType.addField("userId");
        modelType.addField("roleId");

        propMap.put("userId", DataType.INT);
        propMap.put("roleId", DataType.INT);

        setUserId(userId);
        setRoleId(roleId);
    }

    public void setUserId(int userId) {
        set("userId", userId);
    }

    public Integer getUserId() {
        return get("userId");
    }

    public void setRoleId(int roleId) {
        set("roleId", roleId);
    }

    public Integer getRoleId() {
        return get("roleId");
    }
}
