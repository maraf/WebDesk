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
public class FileCopyMoveItem extends CopyMoveItemBase {
    private Long fileId;

    public Long getFileId() {
        return fileId;
    }

    @Deserializable(name="fileId")
    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
}
