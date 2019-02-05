/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.data;

import com.neptuo.os.client.data.ClipboardData;
import com.neptuo.os.client.data.model.FsysItem;

/**
 *
 * @author Mara
 */
public class FileSystemClipboardData extends ClipboardData {
    private FsysItem data;
    private FileSystemClipboardEventType eventType;

    public FileSystemClipboardData(FsysItem data, FileSystemClipboardEventType eventType) {
        this.data = data;
        this.eventType = eventType;
    }

    public FsysItem getData() {
        return data;
    }

    public void setData(FsysItem data) {
        this.data = data;
    }

    public FileSystemClipboardEventType getEventType() {
        return eventType;
    }

    public void setEventType(FileSystemClipboardEventType eventType) {
        this.eventType = eventType;
    }
}
