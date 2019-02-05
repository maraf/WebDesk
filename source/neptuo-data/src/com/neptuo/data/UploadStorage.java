/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data;

import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Mara
 */
public interface UploadStorage {

    public void add(String key, FileItem item);

    public void remove(String key);

    public FileItem get(String key);
}
