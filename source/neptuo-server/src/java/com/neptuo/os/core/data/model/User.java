package com.neptuo.os.core.data.model;

import com.neptuo.service.io.annotation.Deserializable;
import com.neptuo.service.io.JsonSerializer;
import com.neptuo.service.io.annotation.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "core_user")
@Serializable(name = "user")
@Deserializable(name = "user")
public class User extends IdentityBase implements java.io.Serializable {

    private static final long serialVersionUID = -7331545886118192924L;
    private Date created;
    private boolean enabled;
    private String name;
    private String surname;
    private String username;
    private String passwordHash;
    private Set<UserRole> roles;// = new HashSet<UserRole>();

    public User() {
        super();
    }

    public User(String username, String passwordHash) {
        super();
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public User(String username, String passwordHash, String name, String surname, Date created, boolean enabled, Set<UserRole> roles) {
        this.created = created;
        this.enabled = enabled;
        this.name = name;
        this.surname = surname;
        this.roles = roles;

        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", length = 0)
    @Serializable(name = "created", options = {JsonSerializer.ESCAPE_VALUE_KEY})
    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Column(name = "enabled")
    @Serializable(name = "enabled")
    public boolean getEnabled() {
        return this.enabled;
    }

    @Deserializable(name = "enabled")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "name", nullable = false, length = 50)
    @NotNull
    @Length(min = 3, max = 50)
    @Serializable(name = "name")
    public String getName() {
        return this.name;
    }

    @Deserializable(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "surname", nullable = false, length = 50)
    @NotNull
    @Length(min = 3, max = 50)
    @Serializable(name = "surname")
    public String getSurname() {
        return this.surname;
    }

    @Deserializable(name = "surname")
    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Column(name = "password", nullable = false, length = 50)
    @NotNull
    @Length(min = 3, max = 50)
    public String getPasswordHash() {
        return passwordHash;
    }

    @Deserializable(name = "passwordHash")
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Column(name = "username", nullable = false, length = 50)
    @NotNull
    @Length(min = 3, max = 50)
    @Serializable(name = "username")
    public String getUsername() {
        return username;
    }

    @Deserializable(name = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @ManyToMany()
    @JoinTable(name = "core_userinrole", joinColumns =
    @JoinColumn(name = "user_id"), inverseJoinColumns =
    @JoinColumn(name = "userrole_id"))
    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    @Override
    @Transient
    @Serializable(name = "displayName")
    public String getDisplayName() {
        return getUsername() + ": " + getName() + " " + getSurname();
    }
}
