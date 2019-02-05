/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.data.model;

import com.neptuo.os.client.data.BaseModelData;

/**
 *
 * @author Mara
 */
public class UploadItem extends BaseModelData {
    public static int STATUS_WAITING = 1;
    public static int STATUS_UPLOADING = 2;
    public static int STATUS_UPLOADED = 3;
    public static int STATUS_SAVING = 4;
    public static int STATUS_SAVED = 5;

    public UploadItem() {
        
    }

    public UploadItem(String name, String status) {
        setName(name);
        setStatus(status);
    }

    public UploadItem(String key, String name, String status, int statusCode) {
        setKey(key);
        setName(name);
        setStatus(status);
        setStatusCode(statusCode);
    }

    public void setKey(String key) {
        set("key", key);
    }

    public String getKey() {
        return get("key");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setStatus(String status) {
        set("status", status);
    }

    public String getStatus() {
        return get("status");
    }

    public void setStatusCode(int code) {
        set("statusCode", code);
    }

    public int getStatusCode() {
        return (Integer) get("statusCode");
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof UploadItem))
            return false;

        UploadItem other = (UploadItem) obj;
        return other.getKey().equals(getKey());
    }
}
