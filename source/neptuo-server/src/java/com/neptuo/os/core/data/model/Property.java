/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.core.data.model;

import com.neptuo.data.model.AbstractEntity;
import com.neptuo.service.io.annotation.Deserializable;
import com.neptuo.service.io.annotation.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Mara
 */
@Entity
@Table(name = "core_property")
@Serializable(name = "property")
@Deserializable(name = "property")
public class Property extends AbstractEntity implements java.io.Serializable {

    private Long id;
    private User user;
    private String className;
    private String key;
    private String value;

    @Id
    @GeneratedValue
    @Override
    @Serializable(name = "id", primary = true)
    public Long getId() {
        return id;
    }

    @Deserializable(name = "id")
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Serializable(name = "className")
    public String getClassName() {
        return className;
    }

    @Deserializable(name = "className")
    public void setClassName(String className) {
        this.className = className;
    }

    @Column(name = "key_col")
    @Serializable(name = "key")
    public String getKey() {
        return key;
    }

    @Deserializable(name = "key")
    public void setKey(String key) {
        this.key = key;
    }

    @Serializable(name = "value")
    public String getValue() {
        return value;
    }

    @Deserializable(name = "value")
    public void setValue(String value) {
        this.value = value;
    }
}
