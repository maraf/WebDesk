package com.neptuo.os.fsys.data.model;
// Generated Apr 24, 2010 12:14:52 PM by Hibernate Tools 3.2.4.GA

import com.neptuo.data.model.AbstractEntity;
import com.neptuo.service.io.annotation.Deserializable;
import com.neptuo.service.io.annotation.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fsys_filesystemitem")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FileSystemItem extends AbstractEntity implements java.io.Serializable {
    private static final long serialVersionUID = -6827476176403181448L;
    
    protected Long id;
    protected Directory parent;
    protected String name;
    protected Date created;
    protected Date modified;
    protected String publicId;
    protected boolean isPublic;
    protected Set<FileSystemPermission> permissions = new HashSet<FileSystemPermission>();

    public FileSystemItem() {
    }

    public FileSystemItem(Long id, Date created) {
        this.id = id;
        this.created = created;
    }

    public FileSystemItem(Long id, String name, Date created, Date modified, Set<FileSystemPermission> permissions) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.modified = modified;
        this.permissions = permissions;
    }

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    @Override
    @Serializable(name="id",primary=true)
    public Long getId() {
        return this.id;
    }

    @Deserializable(name="id")
    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Directory getParent() {
        return parent;
    }

    public void setParent(Directory parent) {
        this.parent = parent;
    }

    @Column(name = "name", length = 200, nullable = false)
    @Length(max = 200)
    @NotNull
    @Serializable(name="name")
    public String getName() {
        return this.name;
    }

    @Deserializable(name="name")
    public void setName(String name) {
        this.name = name;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false, length = 0)
    @NotNull
    @Serializable(name="created")
    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified", length = 0)
    @Serializable(name="modified")
    public Date getModified() {
        return this.modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    @Column(name="is_public")
    @Serializable(name="isPublic")
    public boolean getIsPublic() {
        return isPublic;
    }

    @Deserializable(name="isPublic")
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Column(name="public_id")
    @Serializable(name="publicId")
    public String getPublicId() {
        return publicId;
    }

    @Deserializable(name="publicId")
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "target", targetEntity=FileSystemPermission.class)
    public Set<FileSystemPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<FileSystemPermission> permissions) {
        this.permissions = permissions;
    }

    //Serialization ------------------------------------------------------------

    @Transient
    @Serializable(name="parentId")
    public Long getParentId() {
        if (parent == null) {
            return null;
        }
        return parent.getId();
    }

    @Deserializable(name="parentId")
    public void setParentId(Long id) {
        if(parent == null) {
            parent = new Directory();
        }
        parent.setId(id);
    }
    
    private String path;

    @Transient
    @Serializable(name="path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Transient
    @Serializable(name="type")
    public String getType() {
        return this instanceof File ? ((File) this).getType() : "folder";
    }
}
