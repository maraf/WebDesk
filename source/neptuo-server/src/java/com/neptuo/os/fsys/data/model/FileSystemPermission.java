package com.neptuo.os.fsys.data.model;

import com.neptuo.os.core.data.model.AccessType;
import com.neptuo.os.core.data.model.IdentityBase;
import com.neptuo.os.core.data.model.Permission;
import com.neptuo.service.io.annotation.Deserializable;
import com.neptuo.service.io.annotation.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("fsysperm")
@Serializable(name="permission")
@Deserializable(name="permission")
public class FileSystemPermission extends Permission implements java.io.Serializable {

    private FileSystemItem target;

    public FileSystemPermission() {
    }

    public FileSystemPermission(AccessType type, IdentityBase identity, FileSystemItem target) {
        setType(type);
        setIdentity(identity);
        setTarget(target);
    }

    @ManyToOne
    public FileSystemItem getTarget() {
        return target;
    }

    public void setTarget(FileSystemItem target) {
        this.target = target;
    }

    //Serialization ------------------------------------------------------------

    @Transient
    @Serializable(name="targetId")
    public Long getTargetId() {
        return target.getId();
    }

    @Deserializable(name="targetId")
    public void setTargetId(Long id) {
        if (target == null) {
            target = new Directory();
        }
        target.setId(id);
    }
}
