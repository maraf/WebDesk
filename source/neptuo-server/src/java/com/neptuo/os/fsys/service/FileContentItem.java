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
@Deserializable(name="file")
public class FileContentItem {

    private String publicId;
    private String content;

    public String getContent() {
        return content;
    }

    @Deserializable(name="content")
    public void setContent(String content) {
        this.content = content;
    }

    public String getPublicId() {
        return publicId;
    }

    @Deserializable(name="publicId")
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }
}
