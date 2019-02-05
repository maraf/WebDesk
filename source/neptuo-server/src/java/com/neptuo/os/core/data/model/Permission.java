package com.neptuo.os.core.data.model;
// Generated Apr 24, 2010 12:14:52 PM by Hibernate Tools 3.2.4.GA

import com.neptuo.data.model.AbstractEntity;
import com.neptuo.service.io.annotation.Deserializable;
import com.neptuo.service.io.annotation.Serializable;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "core_permission")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="permtype", discriminatorType=DiscriminatorType.STRING)
public abstract class Permission extends AbstractEntity implements java.io.Serializable {
    private static final long serialVersionUID = 2654497596787943816L;
    
    private Long id;
    private AccessType type;
    private IdentityBase identity;

    public Permission() {
    }

    public Permission(AccessType type, IdentityBase identity) {
        this.type = type;
        this.identity = identity;
    }

    @Id
    @GeneratedValue
    @Override
    @Serializable(name="id")
    public Long getId() {
        return id;
    }

    @Deserializable(name="id")
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public IdentityBase getIdentity() {
        return identity;
    }

    public void setIdentity(IdentityBase identity) {
        this.identity = identity;
    }

    @ManyToOne
    public AccessType getType() {
        return type;
    }

    public void setType(AccessType type) {
        this.type = type;
    }

    //Serialization ------------------------------------------------------------

    @Transient
    @Serializable(name="identityId")
    public Long getIdentityId() {
        return identity.getId();
    }

    @Deserializable(name="identityId")
    public void setIdentityId(Long id) {
        if(identity == null) {
            identity = new User();
        }
        identity.setId(id);
    }

    @Transient
    @Serializable(name="identityName")
    public String getIdentityName() {
        return identity.getDisplayName();
    }

    @Transient
    @Serializable(name="identityType")
    public String getIdentityType() {
        return identity.getType();
    }

    @Transient
    @Serializable(name="typeId")
    public Long getTypeId() {
        return type.getId();
    }

    @Deserializable(name="typeId")
    public void setTypeId(Long id) {
        if(type == null) {
            type = new AccessType();
        }
        type.setId(id);
    }

    @Transient
    @Serializable(name="typeName")
    public String getTypeName() {
        return type.getName();
    }
}
