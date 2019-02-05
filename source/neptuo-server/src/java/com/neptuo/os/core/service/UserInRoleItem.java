/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.service;

import com.neptuo.service.io.annotation.Deserializable;

/**
 *
 * @author Mara
 */
@Deserializable(name="userInRole")
public class UserInRoleItem {

    private Long userId;
    private Long roleId;

    public Long getRoleId() {
        return roleId;
    }

    @Deserializable(name="roleId")
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    @Deserializable(name="userId")
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
