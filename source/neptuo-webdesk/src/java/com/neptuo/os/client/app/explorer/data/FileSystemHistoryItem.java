/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.client.app.explorer.data;

/**
 *
 * @author Mara
 */
public class FileSystemHistoryItem {
    private int folderId;
    private String folderPath;

    public FileSystemHistoryItem() {
    }

    public FileSystemHistoryItem(int folderId, String folderPath) {
        this.folderId = folderId;
        this.folderPath = folderPath;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof FileSystemHistoryItem))
            return false;

        FileSystemHistoryItem other = (FileSystemHistoryItem) obj;
        return other.getFolderId() == getFolderId();
    }
}
