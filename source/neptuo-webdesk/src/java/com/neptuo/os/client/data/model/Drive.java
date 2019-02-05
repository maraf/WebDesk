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
public class Drive extends BaseModelData {

    public Drive() {
        modelType.setRoot("drives");
        modelType.setRecordName("drive");
        modelType.addField("id");
        modelType.addField("folderId");
        modelType.addField("name");
        modelType.addField("label");
        modelType.addField("isSystem");
        modelType.addField("url");
        modelType.addField("path");

        propMap.put("id", DataType.INT);
        propMap.put("folderId", DataType.INT);
        propMap.put("isSystem", DataType.BOOLEAN);
    }

    public Drive(int id) {
        this();
        setId(id);
    }

    public Drive(String name, String label, int folderId) {
        this();
        setName(name);
        setLabel(label);
        setFolderId(folderId);
    }

    public Drive(int id, String name, String label, int folderId) {
        this();
        setId(id);
        setName(name);
        setLabel(label);
        setFolderId(folderId);
    }

    public void setId(int id) {
        set("id", id);
    }

    public Integer getId() {
        return get("id");
    }

    public void setFolderId(int folderId) {
        set("folderId", folderId);
    }

    public Integer getFolderId() {
        return get("folderId");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    public String getLabel() {
        return get("label");
    }

    public Boolean getIsSystem() {
        return get("isSystem");
    }

    public void setIsSystem(boolean isSystem) {
        set("isSystem", isSystem);
    }

    public void setUrl(String url) {
        set("url", url);
    }

    public String getUrl() {
        return get("url");
    }

    public void setPath(String path) {
        set("path", path);
    }

    public String getPath() {
        return get("path");
    }
}
