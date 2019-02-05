/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.core.data.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="core_userlog")
public class UserLog implements Serializable {
    private Long id;
    private User user;
    private Date loginTime;
    private Date lastActivity;
    private Date logoutTime;
    private String authToken;

    public UserLog() {
    }

    public UserLog(User user, String authToken) {
        this.user = user;
        this.authToken = authToken;
        this.loginTime = new Date();
        this.lastActivity = new Date();
    }

    public UserLog(User user, Date loginTime, String authToken) {
        this.user = user;
        this.loginTime = loginTime;
        this.authToken = authToken;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }

    @ManyToOne
    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NotNull
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
