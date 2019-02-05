/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.util;

import com.neptuo.os.fsys.data.model.FileSystemItem;

/**
 *
 * @author Mara
 */
public class ResolveResult {
    private FileSystemItem result;
    private boolean isFolder;
    private boolean isFile;

    public ResolveResult(FileSystemItem result, boolean isFolder, boolean isFile) {
        this.result = result;
        this.isFolder = isFolder;
        this.isFile = isFile;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public FileSystemItem getResult() {
        return result;
    }
}
