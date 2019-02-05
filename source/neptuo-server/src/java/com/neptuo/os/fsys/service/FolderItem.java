/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.neptuo.os.fsys.service;

import com.neptuo.service.io.annotation.Deserializable;

/**
 *
 * @author Mara
 */
@Deserializable(name="folder")
public class FolderItem {

    private Long id;
    private String path;

    public Long getId() {
        return id;
    }

    @Deserializable(name="id")
    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    @Deserializable(name="path")
    public void setPath(String path) {
        this.path = path;
    }
}
