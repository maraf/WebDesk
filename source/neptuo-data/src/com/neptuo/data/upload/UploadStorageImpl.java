/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.data.upload;

import com.neptuo.data.UploadStorage;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author Mara
 */
public class UploadStorageImpl implements UploadStorage {
    private Map<String, FileItem> uploads = new HashMap<String, FileItem>();

    @Override
    public void add(String key, FileItem item) {
        uploads.put(key, item);
    }

    @Override
    public void remove(String key) {
        uploads.remove(key);
    }

    @Override
    public FileItem get(String key) {
        return uploads.get(key);
    }

}
