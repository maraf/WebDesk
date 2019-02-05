/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptuo.os.client.data.model;

import com.neptuo.os.client.data.BaseModelData;
import com.neptuo.os.client.data.DataType;
import java.util.Date;

/**
 *
 * @author Mara
 */
public class FsysItem extends BaseModelData {

    public FsysItem() {
        modelType.setRoot("folderItems");
        modelType.setRecordName("folderItem");
        modelType.addField("id");
        modelType.addField("parentId");
        modelType.addField("name");
        modelType.addField("created");
        modelType.addField("modified");
        modelType.addField("isPublic");
        modelType.addField("publicId");
        modelType.addField("ownerDisplayName");
        modelType.addField("ownerId");
        modelType.addField("type");
        modelType.addField("size");
        modelType.addField("path");
        modelType.addField("uploadKey");
        modelType.addField("content");

        propMap.put("id", DataType.INT);
        propMap.put("parentId", DataType.INT);
        propMap.put("created", DataType.DATE);
        propMap.put("modified", DataType.DATE);
        propMap.put("size", DataType.INT);
        propMap.put("isPublic", DataType.BOOLEAN);
    }

    public FsysItem(Integer id) {
        this();
        setId(id);
    }

    public FsysItem(Integer id, String name) {
        this();
        setId(id);
        setName(name);
    }


    public FsysItem(Integer id, String name, Boolean isPublic) {
        this();
        setId(id);
        setName(name);
        setIsPublic(isPublic);
    }

    public FsysItem(Integer id, String name, String key) {
        this();
        setId(id);
        setName(name);
        setKey(key);
    }

    public FsysItem(Integer id, String name, Integer parentId) {
        this();
        setId(id);
        setName(name);
        setParentId(parentId);
    }

    public FsysItem(Integer id, String name, Integer parentId, String key) {
        this();
        setId(id);
        setName(name);
        setParentId(parentId);
        setKey(key);
    }

    public void setId(Integer id) {
        set("id", id);
    }

    public Integer getId() {
        return get("id");
    }

    public void setParentId(Integer parentId) {
        set("parentId", parentId);
    }

    public Integer getParentId() {
        return get("parentId");
    }

    public void setOwnerId(Integer ownerId) {
        set("ownerId", ownerId);
    }

    public Integer getOwnerId() {
        return get("ownerId");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setOwnerDisplayName(String ownerDisplayName) {
        set("ownerDisplayName", ownerDisplayName);
    }

    public String getOwnerDisplayName() {
        return get("ownerDisplayName");
    }

    public void setType(String type) {
        set("type", type);
    }

    public String getType() {
        return get("type");
    }

    public void setKey(String key) {
        set("uploadKey", key);
    }

    public String getKey() {
        return get("uploadKey");
    }

    public void setCreated(Date created) {
        set("created", created);
    }

    public Date getCreated() {
        return get("created");
    }

    public void setModified(Date modified) {
        set("modified", modified);
    }

    public Date getModified() {
        return get("modified");
    }

    public void setIsPublic(Boolean isPublic) {
        set("isPublic", isPublic);
    }

    public Boolean getIsPublic() {
        return get("isPublic");
    }

    public void setPublicId(String publicId) {
        set("publicId", publicId);
    }

    public String getPublicId() {
        return get("publicId");
    }

    public void setSize(int size) {
        set("size", size);
    }

    public Integer getSize() {
        return get("size", 0);
    }

    public void setPath(String path) {
        set("path", path);
    }

    public String getPath() {
        return get("path");
    }

    public void setContent(String content) {
        set("content", content);
    }

    public String getContent() {
        return get("content");
    }
}
