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
@Deserializable(name="login")
public class LoginItem {
    private String username;
    private String password;
    private String domain;

    public String getDomain() {
        return domain;
    }

    @Deserializable(name="domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    @Deserializable(name="password")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Deserializable(name="username")
    public void setUsername(String username) {
        this.username = username;
    }
}
